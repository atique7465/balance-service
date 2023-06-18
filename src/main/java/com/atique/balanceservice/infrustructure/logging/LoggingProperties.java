package com.atique.balanceservice.infrustructure.logging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 6:46 PM
 */

@Configuration
@ConfigurationProperties(prefix = "app.http.log")
public class LoggingProperties {

    public static final boolean enabled = true;

    public static final boolean printHeader = true;

    public static final boolean printPayload = true;

    public static final Integer maxPayloadSize = 2000;

    public static final List<String> printableContent = new ArrayList<>(List.of(MediaType.APPLICATION_JSON_VALUE));
}
