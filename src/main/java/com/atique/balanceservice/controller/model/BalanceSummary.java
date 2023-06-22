package com.atique.balanceservice.controller.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 10:57 PM
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Balance list of sorted months")
public class BalanceSummary {
    @Schema(name = "content", description = "List of balance", allOf = Balance.class)
    List<Balance> content;
}
