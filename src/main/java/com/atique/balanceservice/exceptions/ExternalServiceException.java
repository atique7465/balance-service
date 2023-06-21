package com.atique.balanceservice.exceptions;

import com.atique.balanceservice.exceptionresolvers.ErrorResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * @author atiQue
 * @since 19'Jun 2023 at 12:35 AM
 */

@Data
@EqualsAndHashCode(callSuper=false)
public class ExternalServiceException extends RuntimeException {
    private HttpStatus status;
    private ErrorResponse errorResponse;
}
