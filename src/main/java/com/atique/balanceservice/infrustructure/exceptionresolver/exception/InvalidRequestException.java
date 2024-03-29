package com.atique.balanceservice.infrustructure.exceptionresolver.exception;

import com.atique.balanceservice.infrustructure.exceptionresolver.enums.ErrorCode;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 10:06 PM
 */

public class InvalidRequestException extends BaseException {

    public InvalidRequestException() {
        super(ErrorCode.INVALID_REQUEST);
    }

    public InvalidRequestException(String message) {
        super(ErrorCode.INVALID_REQUEST, message);
    }
}
