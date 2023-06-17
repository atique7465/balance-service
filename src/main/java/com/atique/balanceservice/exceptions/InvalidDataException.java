package com.atique.balanceservice.exceptions;

import com.atique.balanceservice.enums.ErrorCode;

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
}
