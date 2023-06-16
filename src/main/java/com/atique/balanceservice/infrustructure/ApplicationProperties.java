package com.atique.balanceservice.infrustructure;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 11:40 PM
 */

@Getter
@Configuration
@ConfigurationProperties(prefix = "app.balance-service")
public class ApplicationProperties {
}
