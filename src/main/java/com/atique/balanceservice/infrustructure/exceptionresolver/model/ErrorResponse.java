package com.atique.balanceservice.infrustructure.exceptionresolver.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Generic error response of balance service")
public class ErrorResponse {
    @Schema(name = "reason", description = "Represent the reason code", example = "10_00_001", implementation = String.class, minLength = 9, maxLength = 9)
    private String reason;

    @Schema(name = "message", description = "Represent the details against the reason", example = "Balance service. Invalid acc no.", implementation = String.class)
    private String message;
}
