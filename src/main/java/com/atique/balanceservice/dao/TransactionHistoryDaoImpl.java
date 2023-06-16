package com.atique.balanceservice.dao;

import com.atique.balanceservice.enums.TransactionType;
import com.atique.balanceservice.model.Transaction;
import com.atique.balanceservice.model.TransactionHistory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 12:09 AM
 */

@Repository
public class TransactionHistoryDaoImpl implements TransactionHistoryDao {

    @Override
    public TransactionHistory getHistory(String accNo) {

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
