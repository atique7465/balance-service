package com.atique.balanceservice.infrustructure.logging;

import io.micrometer.core.instrument.util.IOUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 6:29 PM
 */

@Component
@WebFilter(filterName = "LoggingFilter", urlPatterns = "/*")
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!LoggingProperties.enabled) return;

        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        Map<String, String> headers = getReqHeadersToPrint(cachedHttpServletRequest);
        String reqPayload = getRequestBodyString(headers, cachedHttpServletRequest);
        RequestResponseLogger.log(true, cachedHttpServletRequest.getMethod(), cachedHttpServletRequest.getRequestURI(), cachedHttpServletRequest.getProtocol(), headers, reqPayload);

        filterChain.doFilter(cachedHttpServletRequest, responseWrapper);

        headers = getResHeadersToPrint(responseWrapper);
        String resPayload = getResBodyString(headers, responseWrapper.getContentAsByteArray());
        RequestResponseLogger.log(false, cachedHttpServletRequest.getMethod(), cachedHttpServletRequest.getRequestURI(), cachedHttpServletRequest.getProtocol(), headers, resPayload);

        responseWrapper.copyBodyToResponse();
    }

    private Map<String, String> getReqHeadersToPrint(CachedHttpServletRequest request) {

        Map<String, String> map = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            map.put(name, value);
        }

        return map;
    }

    private Map<String, String> getResHeadersToPrint(ContentCachingResponseWrapper responseWrapper) {

        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = responseWrapper.getHeaderNames();
        for (String name : headerNames) {
            String value = responseWrapper.getHeader(name);
            map.put(name, value);
        }

        return map;
    }

    private String getRequestBodyString(Map<String, String> headers, CachedHttpServletRequest cachedHttpServletRequest) {

        String contentType = headers.get(HttpHeaders.CONTENT_TYPE.toLowerCase());

        if (StringUtils.hasLength(contentType) && LoggingProperties.printableContent.contains(contentType)) {
            String result = IOUtils.toString(cachedHttpServletRequest.getInputStream(), StandardCharsets.UTF_8);
            return result.replaceAll("\\s", "");
        }

        return null;
    }

    private String getResBodyString(Map<String, String> headers, byte[] contentAsByteArray) {

        String contentType = headers.get(HttpHeaders.CONTENT_TYPE.toLowerCase());

        if (StringUtils.hasLength(contentType) && LoggingProperties.printableContent.contains(contentType)) {
            String result = new String(contentAsByteArray, StandardCharsets.UTF_8);
            return result.replaceAll("\\s", "");
        }

        return null;
    }
}
