package com.atique.balanceservice.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 12:08 AM
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistory {
    private List<Transaction> content;
}
