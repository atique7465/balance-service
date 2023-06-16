package com.atique.balanceservice.exceptionresolvers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 9:29 PM
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {
    private String reason;
    private String message;
}
