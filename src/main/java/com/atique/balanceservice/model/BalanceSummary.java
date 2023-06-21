package com.atique.balanceservice.model;

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
public class BalanceSummary {
    List<Balance> content;
}
