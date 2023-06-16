package com.atique.balanceservice.exceptions;

import com.atique.balanceservice.util.ErrorCode;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 12:43 AM
 */

public class BaseException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public BaseException() {
    }

    public BaseException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public BaseException(Exception exception) {
        super(exception);
        this.message = exception.getMessage();
    }

    public BaseException(ErrorCode errorCode, Exception exception) {
        super(exception);
        this.errorCode = errorCode;
        this.message = exception.getMessage();
    }

    public BaseException(ErrorCode errorCode, String message, Exception exception) {
        super(exception);
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
