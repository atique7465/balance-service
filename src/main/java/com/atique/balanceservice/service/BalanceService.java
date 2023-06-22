package com.atique.balanceservice.service;

import com.atique.balanceservice.controller.model.BalanceSummary;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 10:58 PM
 */

public interface BalanceService {

    /**
     * Get transaction-history from ext-history-service, calculate and return the summary of Balance list sorted by months.
     *
     * @param accNo Customer Account no.
     * @return {@link BalanceSummary}
     */
    BalanceSummary getSummaryByAccNo(String accNo);
}
