package com.atique.balanceservice.service;

import com.atique.balanceservice.dao.TransactionHistoryDao;
import com.atique.balanceservice.dao.config.ExternalTxnHistoryConfig;
import com.atique.balanceservice.dao.enums.TransactionType;
import com.atique.balanceservice.controller.model.Balance;
import com.atique.balanceservice.controller.model.BalanceSummary;
import com.atique.balanceservice.dao.model.Transaction;
import com.atique.balanceservice.dao.model.TransactionHistory;
import com.atique.balanceservice.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 11:00 PM
 */

@Service
public class BalanceServiceImpl implements BalanceService {

    private final TransactionHistoryDao transactionHistoryDao;
    private final ExternalTxnHistoryConfig externalTxnHistoryConfig;

    public BalanceServiceImpl(TransactionHistoryDao transactionHistoryDao, ExternalTxnHistoryConfig externalTxnHistoryConfig) {
        this.transactionHistoryDao = transactionHistoryDao;
        this.externalTxnHistoryConfig = externalTxnHistoryConfig;
    }

    @Override
    public BalanceSummary getSummaryByAccNo(String accNo) {

        BalanceSummary summary = BalanceSummary.builder().content(new ArrayList<>()).build();

        if (!StringUtils.hasLength(accNo)) return summary;

        TransactionHistory transactionHistory = transactionHistoryDao.getHistory(accNo);

        return getBalanceSummeryFromTransactionHistory(summary, transactionHistory);
    }

    /**
     * Sort the transaction history by approvalDateTime in ascending order, calculate and populate the summary.
     *
     * @param summary {@link BalanceSummary}
     * @param history {@link TransactionHistory}
     * @return updated {@link BalanceSummary}
     */
    private BalanceSummary getBalanceSummeryFromTransactionHistory(BalanceSummary summary, TransactionHistory history) {

        if (history == null || history.getContent() == null || history.getContent().isEmpty()) return summary;

        //Sort the history order by Transaction ApprovalDateTime in ASC
        history.getContent().sort(Comparator.comparing(t -> DateUtils.convertToDate(t.getApprovalDateTime(), externalTxnHistoryConfig.getTxnDateTimeFormat())));

        BigDecimal cumulativeBalance = BigDecimal.ZERO;
        BigDecimal monthlyBalance = BigDecimal.ZERO;
        String month = getMonth(history.getContent().get(0).getApprovalDateTime());

        for (Transaction t : history.getContent()) {

            String currentMonth = getMonth(t.getApprovalDateTime());

            if (!currentMonth.equals(month)) {
                summary.getContent().add(getBalance(month, monthlyBalance, cumulativeBalance));
                monthlyBalance = BigDecimal.ZERO;
                month = currentMonth;
            }

            if (t.getTransactionType() == TransactionType.CREDIT) {
                monthlyBalance = monthlyBalance.add(t.getAmount());
                cumulativeBalance = cumulativeBalance.add(t.getAmount());
            } else {
                monthlyBalance = monthlyBalance.subtract(t.getAmount());
                cumulativeBalance = cumulativeBalance.subtract(t.getAmount());
            }
        }

        //Append Last month balance
        summary.getContent().add(getBalance(month, monthlyBalance, cumulativeBalance));

        return summary;
    }

    private static Balance getBalance(String month, BigDecimal monthlyBalance, BigDecimal cumulativeBalance) {
        return Balance.builder()
                .month(month)
                .monthlyBalance(monthlyBalance.setScale(2, RoundingMode.DOWN))
                .cumulativeBalance(cumulativeBalance.setScale(2, RoundingMode.DOWN))
                .build();
    }

    private String getMonth(String dateStr) {
        return DateUtils.convertDateStrFormat(dateStr, externalTxnHistoryConfig.getTxnDateTimeFormat(), externalTxnHistoryConfig.getMonthFormat());
    }
}
