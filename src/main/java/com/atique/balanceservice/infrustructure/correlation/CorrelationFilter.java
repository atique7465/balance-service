package com.atique.balanceservice.infrustructure.correlation;

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

import java.io.IOException;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 6:29 PM
 */

@Component
@WebFilter(filterName = "CorrelationFilter", urlPatterns = "/*")
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class CorrelationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        CorrelationHelper.prepareRequestContext(request);
        LoggerContext.populate();

        filterChain.doFilter(request, response);

        CorrelationHelper.updateHeaders(response);
        LoggerContext.clear();
    }
}
