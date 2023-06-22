package com.atique.balanceservice.infrustructure.http;

import com.atique.balanceservice.exceptionresolvers.exceptions.InvalidConfigurationException;
import com.atique.balanceservice.exceptionresolvers.enums.ComponentCode;
import lombok.Data;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.net.Host;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 2:53 PM
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "http.route")
public class HttpRouteProperties {

    Map<String, Route> map = new HashMap<>();

    @Data
    public static class Route {
        /**
         * http or https
         *
         * @see org.apache.hc.core5.http.URIScheme
         */
        private String scheme;

        /**
         * IP or DNS name
         */
        private String host;

        /**
         * port – the port number. -1 indicates the scheme default port.
         */
        private Integer port;

        /**
         * Set Context path or keep empty
         */
        private String contextPath;

        /**
         * Determine the maximum http connection to pool for this route
         */
        private Integer maxConnection;
    }

    /**
     * @param componentCode {@link ComponentCode}
     * @return Configured Base URL for a component from property resource
     */
    public String getBasePath(ComponentCode componentCode) {

        Route route = map.get(componentCode.getName());
        if (route == null)
            throw new InvalidConfigurationException("Route not configured for " + componentCode.getName() + " in property source");

        return new StringBuilder(new HttpHost(route.scheme, new Host(route.getHost(), route.getPort())).toURI()).append(route.contextPath).toString();
    }
}
