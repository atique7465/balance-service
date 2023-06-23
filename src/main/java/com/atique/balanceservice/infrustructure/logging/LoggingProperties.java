package com.atique.balanceservice.infrustructure.logging;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 6:46 PM
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "app.http.log")
public class LoggingProperties {

    private boolean enabled = true;

    private boolean printHeader = false;

    private boolean printPayload = true;

    private Integer maxPayloadSize = 2000;

    private List<String> printableContent = new ArrayList<>(List.of(MediaType.APPLICATION_JSON_VALUE));
}
