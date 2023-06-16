package com.atique.balanceservice.service;

import com.atique.balanceservice.dao.TransactionHistoryDao;
import com.atique.balanceservice.enums.TransactionType;
import com.atique.balanceservice.infrustructure.ExternalTxnHistoryConfig;
import com.atique.balanceservice.model.Balance;
import com.atique.balanceservice.model.BalanceSummary;
import com.atique.balanceservice.model.Transaction;
import com.atique.balanceservice.model.TransactionHistory;
import com.atique.balanceservice.util.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

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

        BalanceSummary summary = BalanceSummary.builder().content(new TreeMap<>()).build(); //todo: need to sort

        if (!StringUtils.hasLength(accNo)) return summary;

        TransactionHistory transactionHistory = transactionHistoryDao.getHistory(accNo);

        return getBalanceSummeryFromTransactionHistory(summary, transactionHistory);
    }

    private BalanceSummary getBalanceSummeryFromTransactionHistory(BalanceSummary summary, TransactionHistory history) {

        if (history == null || history.getContent() == null || history.getContent().isEmpty()) return summary;

        history.getContent().sort(Comparator.comparing(t -> DateUtils.convertToDate(t.getApprovalDateTime(), externalTxnHistoryConfig.getTxnDateTimeFormat())));

        BigDecimal cumulativeBalance = BigDecimal.ZERO;
        BigDecimal monthlyBalance = BigDecimal.ZERO;
        String month = getMonth(history.getContent().get(0).getApprovalDateTime());

        for (Transaction t : history.getContent()) {

            String currentMonth = getMonth(t.getApprovalDateTime());

            if (!currentMonth.equals(month)) {
                summary.getContent().put(month, Balance.builder().monthlyBalance(monthlyBalance).cumulativeBalance(cumulativeBalance).build());
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

        summary.getContent().put(month, Balance.builder().monthlyBalance(monthlyBalance).cumulativeBalance(cumulativeBalance).build());

        return summary;
    }

    private String getMonth(String dateStr) {
        return DateUtils.convertDateStrFormat(dateStr, externalTxnHistoryConfig.getTxnDateTimeFormat(), externalTxnHistoryConfig.getMonthFormat());
    }
}
