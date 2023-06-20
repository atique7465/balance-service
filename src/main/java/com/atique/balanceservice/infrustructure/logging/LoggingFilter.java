package com.atique.balanceservice.infrustructure.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 6:29 PM
 */

@Component
@WebFilter(filterName = "LoggingFilter", urlPatterns = "/*")
@Order(value = 1)
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.nanoTime();

        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        //Log Request
        RequestResponseLogger.log(cachedHttpServletRequest);

        filterChain.doFilter(cachedHttpServletRequest, responseWrapper);

        long processingTime = (System.nanoTime() - startTime) / 1000000;

        //Log Response
        RequestResponseLogger.log(cachedHttpServletRequest, responseWrapper, processingTime);

        responseWrapper.copyBodyToResponse();
    }
}
