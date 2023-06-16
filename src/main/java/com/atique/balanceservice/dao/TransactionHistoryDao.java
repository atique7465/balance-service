package com.atique.balanceservice.dao;

import com.atique.balanceservice.model.TransactionHistory;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 12:05 AM
 */

public interface TransactionHistoryDao {
    TransactionHistory getHistory(String accNo);
}
