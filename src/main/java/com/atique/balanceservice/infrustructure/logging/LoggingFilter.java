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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
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
@WebFilter(filterName = "RequestResponseLoggingFilter", urlPatterns = "/*")
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    private final LoggingProperties props;
    private final RequestResponseLogger requestResponseLogger;

    public LoggingFilter(LoggingProperties props, RequestResponseLogger requestResponseLogger) {
        this.props = props;
        this.requestResponseLogger = requestResponseLogger;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!props.isEnabled()) return;

        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String body = getRequestBodyString(cachedHttpServletRequest);
        Map<String, String> headers = getReqHeadersToPrint(cachedHttpServletRequest);
        requestResponseLogger.log(true, cachedHttpServletRequest.getMethod(), cachedHttpServletRequest.getRequestURI(), cachedHttpServletRequest.getProtocol(), headers, body);

        filterChain.doFilter(cachedHttpServletRequest, responseWrapper);

        body = getResBodyString(responseWrapper.getContentAsByteArray());
        headers = getResHeadersToPrint(responseWrapper);
        requestResponseLogger.log(false, cachedHttpServletRequest.getMethod(), cachedHttpServletRequest.getRequestURI(), cachedHttpServletRequest.getProtocol(), headers, body);

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

    private String getRequestBodyString(CachedHttpServletRequest cachedHttpServletRequest) {
        String result = IOUtils.toString(cachedHttpServletRequest.getInputStream(), StandardCharsets.UTF_8);
        return result.replaceAll("\\s", "");
    }

    private String getResBodyString(byte[] contentAsByteArray) {
        String result = new String(contentAsByteArray, StandardCharsets.UTF_8);
        return result.replaceAll("\\s", "");
    }
}
