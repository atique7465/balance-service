package com.atique.balanceservice.infrustructure.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author atiQue
 * @since 18'Jun 2023 at 6:38 PM
 */

@Slf4j
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
                .slowCallDurationThreshold(Duration.ofMillis(props.getSlowCallDurationThreshold()))
                .permittedNumberOfCallsInHalfOpenState(props.getPermittedNumberOfCallsInHalfOpenState())
                .waitDurationInOpenState(Duration.ofMillis(props.getWaitDurationInOpenState()))
                .slidingWindowType(props.slidingWindowType)
                .recordExceptions(props.getRecordExceptions())
                .ignoreExceptions(props.getIgnoreExceptions())
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);

//        circuitBreakerRegistry.circuitBreaker("transaction-history-service")
//                .getEventPublisher().onStateTransition(event -> log.info("Something : ", event));

        return circuitBreakerRegistry;
    }
}
