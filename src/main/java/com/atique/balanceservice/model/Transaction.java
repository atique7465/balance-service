package com.atique.balanceservice.model;

import com.atique.balanceservice.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 12:06 AM
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private TransactionType transactionType;
    private BigDecimal amount;
    private String approvalDateTime;
}
