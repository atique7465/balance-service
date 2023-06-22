package com.atique.balanceservice.controller.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 10:54 PM
 */

@Builder
@Data
@Schema(description = "Balance of every month")
public class Balance {

    @Schema(name = "month", description = "Represent the balance month", example = "05-2023", implementation = String.class, minLength = 7, maxLength = 7, pattern = "MM-yyyy")
    private String month;

    @Schema(name = "monthlyBalance", description = "Amount of money in an account at the end of a specific month", example = "1000.54", implementation = BigDecimal.class, pattern = "Up to 2 Decimal Point")
    private BigDecimal monthlyBalance;

    @Schema(name = "cumulativeBalance", description = "Total balance in an account at any given point in time", example = "2000.54", implementation = BigDecimal.class, pattern = "Up to 2 Decimal Point")
    private BigDecimal cumulativeBalance;
}
