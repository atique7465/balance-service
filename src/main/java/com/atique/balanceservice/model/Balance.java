package com.atique.balanceservice.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 10:54 PM
 */

@Builder
@Data
public class Balance {
    private BigDecimal monthlyBalance;
    private BigDecimal cumulativeBalance;
}
