package com.atique.balanceservice.service;

import com.atique.balanceservice.model.BalanceSummary;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 10:58 PM
 */

public interface BalanceService {
    BalanceSummary getSummaryByAccNo(String accNo);
}
