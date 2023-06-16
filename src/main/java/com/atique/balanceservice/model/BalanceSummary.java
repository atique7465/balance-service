package com.atique.balanceservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 10:57 PM
 */

@Data
@Builder
public class BalanceSummary {
    Map<String, Balance> content;
}
