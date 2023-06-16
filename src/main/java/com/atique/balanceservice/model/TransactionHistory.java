package com.atique.balanceservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 12:08 AM
 */

@Data
@Builder
public class TransactionHistory {
    private List<Transaction> content;
}
