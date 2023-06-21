package com.atique.balanceservice.model;

import io.swagger.v3.oas.annotations.Parameter;
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
public class Balance {

    @Parameter(name = "month", description = "Represent the balance month", required = true, example = "05-2023", schema = @Schema(implementation = String.class, minLength = 7, maxLength = 7, pattern = "MM-yyyy"))
    private String month;

    @Parameter(name = "monthlyBalance", description = "Amount of money in an account at the end of a specific month", required = true, example = "1000.54", schema = @Schema(implementation = BigDecimal.class, pattern = "Up to 2 Decimal Point"))
    private BigDecimal monthlyBalance;

    @Parameter(name = "cumulativeBalance", description = "Total balance in an account at any given point in time", required = true, example = "2000.54", schema = @Schema(implementation = BigDecimal.class, pattern = "Up to 2 Decimal Point"))
    private BigDecimal cumulativeBalance;
}
