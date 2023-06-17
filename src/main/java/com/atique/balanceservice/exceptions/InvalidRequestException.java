package com.atique.balanceservice.exceptions;

import com.atique.balanceservice.enums.ErrorCode;

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
