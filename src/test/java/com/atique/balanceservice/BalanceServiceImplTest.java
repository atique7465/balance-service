package com.atique.balanceservice;

import com.atique.balanceservice.dao.TransactionHistoryDao;
import com.atique.balanceservice.dao.config.ExternalTxnHistoryConfig;
import com.atique.balanceservice.dao.enums.TransactionType;
import com.atique.balanceservice.exceptionresolvers.exceptions.InvalidDataException;
import com.atique.balanceservice.controller.model.Balance;
import com.atique.balanceservice.controller.model.BalanceSummary;
import com.atique.balanceservice.dao.model.Transaction;
import com.atique.balanceservice.dao.model.TransactionHistory;
import com.atique.balanceservice.service.BalanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author atiQue
 * @since 22'Jun 2023 at 12:55 AM
 */

public class BalanceServiceImplTest {

    @Mock
    private TransactionHistoryDao transactionHistoryDao;

    @Mock
    private ExternalTxnHistoryConfig externalTxnHistoryConfig;

    private BalanceServiceImpl balanceService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        balanceService = new BalanceServiceImpl(transactionHistoryDao, externalTxnHistoryConfig);
    }

    @Test
    void testGetSummaryByAccNo_WithEmptyAccNo() {
        // Arrange
        String accNo = "";

        // Act
        BalanceSummary summary = balanceService.getSummaryByAccNo(accNo);

        // Assert
        assertNotNull(summary);
        assertEquals(0, summary.getContent().size());
    }

    @Test
    void testGetSummaryByAccNo_WithNullTransactionHistory() {
        // Arrange
        String accNo = "1234567890";

        when(transactionHistoryDao.getHistory(accNo)).thenReturn(null);

        // Act
        BalanceSummary summary = balanceService.getSummaryByAccNo(accNo);

        // Assert
        assertNotNull(summary);
        assertEquals(0, summary.getContent().size());
    }

    @Test
    void testGetSummaryByAccNo_WithProperTransactionHistory() {
        // Arrange
        String accNo = "1234567890";

        when(transactionHistoryDao.getHistory(accNo)).thenReturn(getHistory());
        when(externalTxnHistoryConfig.getTxnDateTimeFormat()).thenReturn("dd-MM-yyyy HH:mm:ss");
        when(externalTxnHistoryConfig.getMonthFormat()).thenReturn("MM-yyyy");

        // Act
        BalanceSummary summary = balanceService.getSummaryByAccNo(accNo);

        // Assert
        assertNotNull(summary);
        assertEquals(5, summary.getContent().size());
        assertEquals("04-2022", summary.getContent().get(0).getMonth());
        assertEquals(new BigDecimal("100"), summary.getContent().get(0).getMonthlyBalance());
        assertEquals(new BigDecimal("100"), summary.getContent().get(0).getCumulativeBalance());

        assertEquals("05-2023", summary.getContent().get(2).getMonth());
        assertEquals(new BigDecimal("640"), summary.getContent().get(2).getMonthlyBalance());
        assertEquals(new BigDecimal("1740"), summary.getContent().get(2).getCumulativeBalance());

        assertEquals("07-2023", summary.getContent().get(4).getMonth());
        assertEquals(BigDecimal.ZERO, summary.getContent().get(4).getMonthlyBalance());
        assertEquals(new BigDecimal("2780"), summary.getContent().get(4).getCumulativeBalance());
    }

    @Test
    void testGetSummaryByAccNo_WithInvalidDateFormat() {
        // Arrange
        String accNo = "1234567890";

        when(transactionHistoryDao.getHistory(accNo)).thenReturn(getHistory());
        when(externalTxnHistoryConfig.getTxnDateTimeFormat()).thenReturn("");
        when(externalTxnHistoryConfig.getMonthFormat()).thenReturn("MM-yyyy");

        // Act
        assertThrows(InvalidDataException.class, () -> balanceService.getSummaryByAccNo(accNo));
    }

    // Helper method to create a mock TransactionHistory object
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

    public BalanceSummary getBalanceSummary() {

        List<Balance> content = new ArrayList<>(
                Arrays.asList(
                        Balance.builder().month("04-2022").monthlyBalance(new BigDecimal("100")).cumulativeBalance(new BigDecimal("100")).build(),
                        Balance.builder().month("08-2022").monthlyBalance(new BigDecimal("1000")).cumulativeBalance(new BigDecimal("1100")).build(),
                        Balance.builder().month("05-2023").monthlyBalance(new BigDecimal("640")).cumulativeBalance(new BigDecimal("1740")).build(),
                        Balance.builder().month("05-2023").monthlyBalance(new BigDecimal("1040")).cumulativeBalance(new BigDecimal("2780")).build(),
                        Balance.builder().month("07-2023").monthlyBalance(BigDecimal.ZERO).cumulativeBalance(new BigDecimal("2780")).build()
                )
        );

        return BalanceSummary.builder().content(content).build();
    }
}
