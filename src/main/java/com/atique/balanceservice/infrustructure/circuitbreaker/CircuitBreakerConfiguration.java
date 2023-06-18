package com.atique.balanceservice.infrustructure.circuitbreaker;

import com.atique.balanceservice.exceptions.BaseException;
import com.atique.balanceservice.exceptions.ExternalServiceException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResourceAccessException;

import java.time.Duration;

/**
 * @author atiQue
 * @since 18'Jun 2023 at 6:38 PM
 */

@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public CircuitBreakerRegistry getCircuitBreakerRegistry() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .slowCallRateThreshold(50)
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .permittedNumberOfCallsInHalfOpenState(3)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .recordExceptions(ResourceAccessException.class)
                .ignoreExceptions(BaseException.class, ExternalServiceException.class)
                .build();

        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }
}
