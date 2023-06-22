package com.atique.balanceservice.infrustructure.exceptionresolver.exception;

import com.atique.balanceservice.infrustructure.exceptionresolver.enums.ErrorCode;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 12:43 AM
 */

public class BaseException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    private Exception exception;

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
        this.exception = exception;
        this.message = exception.getMessage();
    }

    public BaseException(ErrorCode errorCode, Exception exception) {
        super(exception);
        this.exception = exception;
        this.errorCode = errorCode;
        this.message = exception.getMessage();
    }

    public BaseException(ErrorCode errorCode, String message, Exception exception) {
        super(exception);
        this.exception = exception;
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

    public Exception getException() {
        return exception;
    }
}
