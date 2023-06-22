package com.atique.balanceservice.dao;

import com.atique.balanceservice.dao.config.ExternalTxnHistoryConfig;
import com.atique.balanceservice.infrustructure.ApplicationProperties;
import com.atique.balanceservice.infrustructure.gateway.ApiGateWay;
import com.atique.balanceservice.dao.model.TransactionHistory;
import com.atique.balanceservice.util.Constants;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 12:09 AM
 */

@Slf4j
@Repository
public class TransactionHistoryDaoImpl implements TransactionHistoryDao {

    private final ExternalTxnHistoryConfig config;
    private final ApplicationProperties applicationProperties;
    private final ApiGateWay apiGateWay;

    public TransactionHistoryDaoImpl(ExternalTxnHistoryConfig config, ApplicationProperties applicationProperties,
                                     @Qualifier(value = Constants.ApiGateWays.COMMON_API_GATEWAY) ApiGateWay apiGateWay) {
        this.config = config;
        this.applicationProperties = applicationProperties;
        this.apiGateWay = apiGateWay;
    }

    @Override
    @CircuitBreaker(name = Constants.CircuitBreakerDao.TXN_HISTORY_DAO)
    public TransactionHistory getHistory(String accNo) {

        String url = UriComponentsBuilder.fromUriString(applicationProperties.getThsBasePath())
                .path(config.getUrl())
                .path("/")
                .path(accNo).toUriString();

        return apiGateWay.GET(url, TransactionHistory.class);
    }
}
