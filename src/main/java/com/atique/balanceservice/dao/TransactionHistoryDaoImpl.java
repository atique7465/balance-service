package com.atique.balanceservice.dao;

import com.atique.balanceservice.dao.config.ExternalTxnHistoryConfig;
import com.atique.balanceservice.enums.ErrorCode;
import com.atique.balanceservice.exceptions.BaseException;
import com.atique.balanceservice.infrustructure.ApplicationProperties;
import com.atique.balanceservice.infrustructure.gateway.ApiGateWay;
import com.atique.balanceservice.model.TransactionHistory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 12:09 AM
 */

@Slf4j
@Repository
public class TransactionHistoryDaoImpl implements TransactionHistoryDao {

    private static final String CIRCUIT_BREAKER_NAME = "transaction-history-service";

    private final ExternalTxnHistoryConfig config;
    private final ApplicationProperties applicationProperties;
    private final ApiGateWay apiGateWay;

    public TransactionHistoryDaoImpl(ExternalTxnHistoryConfig config, ApplicationProperties applicationProperties,
                                     @Qualifier(value = "common-api-gateway") ApiGateWay apiGateWay) {
        this.config = config;
        this.applicationProperties = applicationProperties;
        this.apiGateWay = apiGateWay;
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME)
    public TransactionHistory getHistory(String accNo) {

        String url = UriComponentsBuilder.fromUriString(applicationProperties.getThsBasePath())
                .path(config.getUrl())
                .path("/")
                .path(accNo).toUriString();

        return apiGateWay.GET(url, TransactionHistory.class);
    }
}
