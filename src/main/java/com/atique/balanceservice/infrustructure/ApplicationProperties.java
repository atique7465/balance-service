package com.atique.balanceservice.infrustructure;

import com.atique.balanceservice.infrustructure.exceptionresolver.enums.ComponentCode;
import com.atique.balanceservice.infrustructure.http.HttpRouteProperties;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 4:33 PM
 */

@Getter
@Configuration
public class ApplicationProperties {

    /**
     *  </p>@Value annotated properties can be set here
     * <br>
     * <p>@Value("${app.config.something}") private String something;
     */

    private final String thsBasePath;

    private final HttpRouteProperties routeProperties;

    public ApplicationProperties(HttpRouteProperties routeProperties) {
        this.routeProperties = routeProperties;
        thsBasePath = routeProperties.getBasePath(ComponentCode.TRANSACTION_HISTORY_SERVICE);
    }
}
