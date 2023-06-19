package com.atique.balanceservice.infrustructure.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author atiQue
 * @since 18'Jun 2023 at 6:38 PM
 */

@Configuration
public class CircuitBreakerConfiguration {

    private final CircuitBreakerProperties props;

    public CircuitBreakerConfiguration(CircuitBreakerProperties props) {
        this.props = props;
    }


    @Bean
    public CircuitBreakerRegistry getCircuitBreakerRegistry() {

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(props.getSlidingWindowSize())
                .minimumNumberOfCalls(props.getMinimumNumberOfCalls())
                .failureRateThreshold(props.getFailureRateThreshold())
                .slowCallRateThreshold(props.getSlowCallRateThreshold())
                .slowCallDurationThreshold(Duration.ofSeconds(props.getSlowCallDurationThreshold()))
                .permittedNumberOfCallsInHalfOpenState(props.getPermittedNumberOfCallsInHalfOpenState())
                .waitDurationInOpenState(Duration.ofSeconds(props.getWaitDurationInOpenState()))
                .slidingWindowType(props.slidingWindowType)
                .recordExceptions(props.getRecordExceptions())
                .ignoreExceptions(props.getIgnoreExceptions())
                .build();

        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }
}
