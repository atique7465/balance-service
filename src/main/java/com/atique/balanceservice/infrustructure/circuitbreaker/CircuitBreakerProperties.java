package com.atique.balanceservice.infrustructure.circuitbreaker;

import com.atique.balanceservice.exceptions.BaseException;
import com.atique.balanceservice.exceptions.ExternalServiceException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResourceAccessException;

/**
 * @author atiQue
 * @since 19'Jun 2023 at 11:36 PM
 */

@Getter
@Configuration
@ConfigurationProperties(prefix = "app.circuit.breaker")
public class CircuitBreakerProperties {

    /**
     * Configures the size of the sliding window which is used to record the outcome of calls
     * when the CircuitBreaker is closed. {@code slidingWindowSize} configures the size of the
     * sliding window.
     * <p>
     * The {@code slidingWindowSize} must be greater than 0.
     * <p>
     * Default: 50
     */
    public final int slidingWindowSize = 50;

    /**
     * Configures the minimum number of calls which are required (per sliding window period)
     * before the CircuitBreaker can calculate the error rate. For example, if {@code
     * minimumNumberOfCalls} is 10, then at least 10 calls must be recorded, before the failure
     * rate can be calculated. If only 9 calls have been recorded, the CircuitBreaker will not
     * transition to open, even if all 9 calls have failed.
     * <p>
     * Default: 50
     */
    public final int minimumNumberOfCalls = 50;

    /**
     * Configures the failure rate threshold in percentage. If the failure rate is equal to or
     * greater than the threshold, the CircuitBreaker transitions to open and starts
     * short-circuiting calls.
     * <p>
     * The threshold must be greater than 0 and not greater than 100.
     * <p>
     * Default: 50 %.
     */
    public final int failureRateThreshold = 50;

    /**
     * Configures a threshold in percentage. The CircuitBreaker considers a call as slow when
     * the call duration is greater than duration. When the
     * percentage of slow calls is equal to or greater than the threshold, the CircuitBreaker
     * transitions to open and starts short-circuiting calls.
     *
     * <p>
     * The threshold must be greater than 0 and not greater than 100.
     * <p>
     * Default value is 100
     */
    public final int slowCallRateThreshold = 50;

    /**
     * Configures the duration threshold above which calls are considered as slow and increase
     * the slow calls' percentage.
     * <p>
     * Default: 5000 millis or 5 sec.
     */
    public final int slowCallDurationThreshold = 5 * 1000;

    /**
     * Configures an interval function with a fixed wait duration which controls how long the
     * CircuitBreaker should stay open, before it switches to half open.
     * <p>
     * Default: 10000 millis or 10 sec.
     */
    public final int waitDurationInOpenState = 10 * 1000;

    /**
     * Configures the number of permitted calls when the CircuitBreaker is half open.
     * <p>
     * The size must be greater than 0.
     * <p>
     * Default: 10.
     */
    public final int permittedNumberOfCallsInHalfOpenState = 10;

    /**
     * Configures the type of the sliding window which is used to record the outcome of calls
     * when the CircuitBreaker is closed. Sliding window can either be count-based or
     * time-based.
     * <p>
     * Default: COUNT_BASED.
     */
    public final CircuitBreakerConfig.SlidingWindowType slidingWindowType = CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

    /**
     * Configures a list of error classes that are recorded as a failure and thus increase the
     * failure rate. Any exception matching or inheriting from one of the list should count as a
     * failure, unless ignored.
     * <p>
     * Default: {@link org.springframework.web.client.ResourceAccessException}
     */
    public final Class<? extends Throwable>[] recordExceptions = new Class[]{ResourceAccessException.class};

    /**
     * Configures a list of error classes that are ignored and thus neither count as a failure
     * nor success. Any exception matching or inheriting from one of the list will not count as
     * a failure nor success.
     * <p>
     * Default: {@link com.atique.balanceservice.exceptions.BaseException}, {@link com.atique.balanceservice.exceptions.ExternalServiceException}
     */
    public final Class<? extends Throwable>[] ignoreExceptions = new Class[]{BaseException.class, ExternalServiceException.class};
}
