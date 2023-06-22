package com.atique.balanceservice.infrustructure.exceptionresolver.exception;

import com.atique.balanceservice.infrustructure.exceptionresolver.enums.ErrorCode;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 11:16 PM
 */

public class InvalidDataException extends BaseException {

    public InvalidDataException() {
        super(ErrorCode.INVALID_DATA_GIVEN);
    }

    public InvalidDataException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidDataException(ErrorCode errorCode, Exception exception) {
        super(errorCode, exception);
    }

    public InvalidDataException(String message, Exception exception) {
        super(ErrorCode.INVALID_DATA_GIVEN, message, exception);
    }
}
