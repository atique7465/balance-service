package com.atique.balanceservice.infrustructure.logging;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 6:46 PM
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "app.http.log")
public class LoggingProperties {

    private final boolean enabled = true;

    private final boolean printReqHeader = false;

    private final boolean printResHeader = false;

    private final boolean printUrl = true;

    private final boolean printRequestBody = true;

    private final boolean printResponseBody = true;

    private final String printableContent = MediaType.APPLICATION_JSON_VALUE;
}
