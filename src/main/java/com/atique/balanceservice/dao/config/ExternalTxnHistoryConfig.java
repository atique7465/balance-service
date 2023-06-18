package com.atique.balanceservice.dao.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author atiQue
 * @since 16'Jun 2023 at 1:19 AM
 */

@Getter
@Configuration
@ConfigurationProperties(prefix = "ext.txn-history")
public class ExternalTxnHistoryConfig {

    private final String txnDateTimeFormat = "dd-MM-yyyy HH:mm:ss";

    private final String monthFormat = "MM-yyyy";

    private final String url = "/api/v1/transaction-history";
}
