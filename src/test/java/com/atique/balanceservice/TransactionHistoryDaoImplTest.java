package com.atique.balanceservice;

import com.atique.balanceservice.dao.TransactionHistoryDaoImpl;
import com.atique.balanceservice.dao.config.ExternalTxnHistoryConfig;
import com.atique.balanceservice.dao.enums.TransactionType;
import com.atique.balanceservice.infrustructure.ApplicationProperties;
import com.atique.balanceservice.infrustructure.http.gateway.ApiGateWay;
import com.atique.balanceservice.dao.model.Transaction;
import com.atique.balanceservice.dao.model.TransactionHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * @author atiQue
 * @since 21'Jun 2023 at 11:05 PM
 */

public class TransactionHistoryDaoImplTest {
    @Mock
    private ExternalTxnHistoryConfig config;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    @Qualifier(value = "common-api-gateway")
    private ApiGateWay apiGateWay;

    private TransactionHistoryDaoImpl dao;
    private TransactionHistory expectedHistory;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        dao = new TransactionHistoryDaoImpl(config, applicationProperties, apiGateWay);
        expectedHistory = getHistory();
    }

    @Test
    void testGetHistory_SuccessfulRequest() {
        // Arrange
        String accNo = "1234567890";
        String expectedUrl = "basePath/api/" + accNo;

        when(applicationProperties.getThsBasePath()).thenReturn("basePath");
        when(config.getUrl()).thenReturn("/api");
        when(apiGateWay.GET(expectedUrl, TransactionHistory.class)).thenReturn(expectedHistory);

        // Act
        TransactionHistory actualHistory = dao.getHistory(accNo);

        // Assert
        assertEquals(expectedHistory, actualHistory);
        verify(apiGateWay, times(1)).GET(expectedUrl, TransactionHistory.class);
    }

    @Test
    void testGetHistory_ResourceAccessException() {
        // Arrange
        String accNo = "1234567890";
        String expectedUrl = "basePath/api/" + accNo;

        when(applicationProperties.getThsBasePath()).thenReturn("basePath");
        when(config.getUrl()).thenReturn("/api");
        when(apiGateWay.GET(expectedUrl, TransactionHistory.class)).thenThrow(new ResourceAccessException("Connection refused"));

        // Act and Assert
        assertThrows(ResourceAccessException.class, () -> dao.getHistory(accNo));
    }

    public TransactionHistory getHistory() {

        List<Transaction> content = new ArrayList<>(
                Arrays.asList(
                        Transaction.builder().transactionType(TransactionType.CREDIT).amount(new BigDecimal(1000)).approvalDateTime("01-08-2022 10:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.CREDIT).amount(new BigDecimal(100)).approvalDateTime("01-05-2023 10:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.CREDIT).amount(new BigDecimal(150)).approvalDateTime("01-05-2023 10:45:02").build(),
                        Transaction.builder().transactionType(TransactionType.DEBIT).amount(new BigDecimal(50)).approvalDateTime("02-06-2023 10:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.CREDIT).amount(new BigDecimal(100)).approvalDateTime("03-05-2023 10:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.DEBIT).amount(new BigDecimal(100)).approvalDateTime("04-05-2023 10:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.DEBIT).amount(new BigDecimal(10)).approvalDateTime("05-05-2023 10:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.CREDIT).amount(new BigDecimal(100)).approvalDateTime("06-07-2023 10:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.CREDIT).amount(new BigDecimal(500)).approvalDateTime("07-05-2023 10:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.DEBIT).amount(new BigDecimal(100)).approvalDateTime("31-05-2023 10:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.CREDIT).amount(new BigDecimal(100)).approvalDateTime("01-06-2023 11:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.DEBIT).amount(new BigDecimal(60)).approvalDateTime("01-06-2023 10:46:01").build(),
                        Transaction.builder().transactionType(TransactionType.CREDIT).amount(new BigDecimal(100)).approvalDateTime("01-06-2023 10:45:02").build(),
                        Transaction.builder().transactionType(TransactionType.DEBIT).amount(new BigDecimal(50)).approvalDateTime("30-06-2023 10:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.CREDIT).amount(new BigDecimal(1000)).approvalDateTime("30-06-2023 10:46:01").build(),
                        Transaction.builder().transactionType(TransactionType.DEBIT).amount(new BigDecimal(200)).approvalDateTime("01-07-2023 10:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.CREDIT).amount(new BigDecimal(100)).approvalDateTime("02-07-2023 10:45:01").build(),
                        Transaction.builder().transactionType(TransactionType.CREDIT).amount(new BigDecimal(100)).approvalDateTime("02-04-2022 10:45:01").build()
                )
        );

        return TransactionHistory.builder().content(content).build();
    }
}
