package com.atique.balanceservice.infrustructure;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 1:33 AM
 */

@Getter
@Configuration
@ConfigurationProperties(prefix = "http.pool")
public class HttpConnectionPoolProperties {

    /**
     * Determines the maximum total connections in the connection pool
     * </p>
     * Default: 200
     * </p>
     */
    private final Integer maxTotalConnection = 200;

    /**
     * Determines the maximum total connections/per route in the connection pool
     * </p>
     * Default: 100
     * </p>
     */
    private final Integer maxConnectionPerRoute = 100;

    /**
     * The connection lease request timeout used when requesting a connection from the connection manager.
     * <p>
     * A timeout value of zero is interpreted as an infinite timeout.
     * </p>
     * <p>
     * Default: 5000 millis or 5 sec.
     * </p>
     */
    private final Integer connectionRequestTimeout = 5000;

    /**
     * Determines the timeout until a new connection is fully established.
     * <p>
     * A timeout value of zero is interpreted as an infinite timeout.
     * </p>
     * <p>
     * Default: 5000 millis or 5 sec.
     * </p>
     */
    private final Integer connectTimeout = 5000;

    /**
     * Determines the default of value of connection keep-alive time period when not
     * explicitly communicated by the origin server with a {@code Keep-Alive} response header.
     * <p>Specifies the amount of time that a connection will be kept alive after it has been idle.</p>
     * <p>
     * A negative value is interpreted as an infinite keep-alive period.
     * </p>
     * <p>
     * Default: 30000 millis or 30 sec.
     * </p>
     */
    private final Integer defaultKeepAliveTime = 30000;

    /**
     * Defines the total span of time connections can be kept alive or execute requests.
     * <p>Specifies the amount of time that a connection will be kept alive regardless of whether it is idle or not.</p>
     * <p>
     * Default: 300000 millis or 5 min.
     * </p>
     */
    private final Integer timeToLive = 300000;

    /**
     * Defines period of inactivity after which persistent connections must be re-validated prior to being leased to the consumer.
     * <p>
     * Negative values passed to this method disable connection validation.
     * <p>
     * Default: 60000 millis or 60 sec.
     * </p>
     */
    private final Integer validateAfterInactivity = 60000;

    /**
     * Determines the timeout until arrival of a response from the opposite endpoint.
     * <p>
     * A timeout value of zero is interpreted as an infinite timeout.
     * </p>
     * <p>
     * Please note that response timeout may be unsupported by HTTP transports with message multiplexing.
     * </p>
     * <p>
     * Default: 10000 millis or 10 sec.
     * </p>
     */
    private final Integer responseTimeout = 10000;

    /**
     * Determines the default socket timeout value for blocking I/O operations.
     * <p>
     * Default: 10000 millis or 10 sec.
     * </p>
     * @see java.net.SocketOptions#SO_TIMEOUT
     */
    private final Integer socketTimeout = 10000;
}
