package com.atique.balanceservice.exceptionresolvers;

import com.atique.balanceservice.exceptionresolvers.exceptions.*;
import com.atique.balanceservice.exceptionresolvers.enums.ComponentCode;
import com.atique.balanceservice.exceptionresolvers.enums.ErrorCode;
import com.atique.balanceservice.exceptionresolvers.enums.FeatureCode;
import com.atique.balanceservice.exceptionresolvers.model.ErrorResponse;
import com.atique.balanceservice.exceptions.*;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 9:42 PM
 */

@Slf4j
@ControllerAdvice(basePackages = "com.atique.balanceservice")
public class BaseExceptionResolver {

    private static final String REASON_SEPARATOR = "_";
    private static final String MESSAGE_SEPARATOR = ". ";

    private ComponentCode getComponentCode() {
        return ComponentCode.BALANCE_SERVICE;
    }

    public FeatureCode getFeatureCode() {
        return FeatureCode.BASE_FEATURE_CODE;
    }

    @ResponseBody
    @ExceptionHandler({ExternalServiceException.class})
    public ErrorResponse handleExternalServiceException(ExternalServiceException exception, HttpServletResponse response) {

        /**
         * ExternalServiceException has the actual status code from external service. <br>
         * That status has to propagated to up-stream caller services by setting status code in HttpServletResponse.
         */
        response.setStatus(exception.getStatus().value());

        return exception.getErrorResponse();
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidRequestException.class})
    public ErrorResponse handleBadRequestException(BaseException baseException) {
        return getErrorResponseAndLogException(baseException);
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorResponse handleBadRequestException(MethodArgumentNotValidException exception) {
        return getErrorResponseAndLogException(new BaseException(exception));
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidationException.class})
    public ErrorResponse handleBadRequestException(ValidationException exception) {
        return getErrorResponseAndLogException(new BaseException(exception));
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({
            BaseException.class,
            InvalidDataException.class,
            ExternalServiceErrorResponseParseException.class})
    @ResponseBody
    public ErrorResponse handleInternalServerException(BaseException baseException) {
        return getErrorResponseAndLogException(baseException);
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ErrorResponse handleGeneralJavaException(Exception exception) {

        // Handle external resource exceptions
        if (exception instanceof ResourceAccessException || exception instanceof CallNotPermittedException) {
            return getErrorResponseAndLogResourceExceptions(new BaseException(exception));
        }

        return getErrorResponseAndLogException(new BaseException(exception));
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({HttpMessageConversionException.class})
    public ErrorResponse handleGeneralJavaException(HttpMessageConversionException exception) {
        return getErrorResponseAndLogException(new BaseException(exception));
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ErrorResponse handleGeneralJavaException(HttpMessageNotReadableException exception) {
        return getErrorResponseAndLogException(new BaseException(exception));
    }

    private ErrorResponse getErrorResponseAndLogException(BaseException exception) {

        String errorId = ErrorCode.UNKNOWN_ERROR.getId();
        String errorMessage = ErrorCode.UNKNOWN_ERROR.getMessage();

        if (exception.getErrorCode() != null) {
            errorId = exception.getErrorCode().getId();
            errorMessage = exception.getErrorCode().getMessage();
        }

        if (StringUtils.hasLength(exception.getMessage())) {
            errorMessage = new StringBuilder(errorMessage).append(MESSAGE_SEPARATOR).append(exception.getMessage()).toString();
        }

        String reason = new StringBuilder(getComponentCode().getCode()).append(REASON_SEPARATOR).append(getFeatureCode().getCode()).append(REASON_SEPARATOR).append(errorId).toString();
        String message = new StringBuilder(getFeatureCode().getName()).append(MESSAGE_SEPARATOR).append(errorMessage).toString();

        log.error("[Error Details]:", exception);

        return ErrorResponse.builder()
                .reason(reason)
                .message(message)
                .build();
    }

    private ErrorResponse getErrorResponseAndLogResourceExceptions(BaseException exception) {

        String errorId;
        String errorMessage;

        if (exception.getException() instanceof CallNotPermittedException) {
            errorId = ErrorCode.EXT_SERVICE_CALL_NOT_PERMITTED.getId();
            errorMessage = ErrorCode.EXT_SERVICE_CALL_NOT_PERMITTED.getMessage();

        } else if (exception.getException() instanceof ResourceAccessException) {
            String exceptionMessage = exception.getException().getCause().toString();
            if (exceptionMessage.contains("java.net.SocketTimeoutException: Read timed out")) {
                errorId = ErrorCode.EXT_SERVICE_READ_TIME_OUT.getId();
                errorMessage = ErrorCode.EXT_SERVICE_READ_TIME_OUT.getMessage();
            } else {
                errorId = ErrorCode.EXT_SERVICE_UNAVAILABLE.getId();
                errorMessage = ErrorCode.EXT_SERVICE_UNAVAILABLE.getMessage();
            }

        } else {
            errorId = ErrorCode.UNKNOWN_ERROR.getId();
            errorMessage = ErrorCode.UNKNOWN_ERROR.getMessage();
        }

        if (StringUtils.hasLength(exception.getMessage())) {
            errorMessage = new StringBuilder(errorMessage).append(MESSAGE_SEPARATOR).append(exception.getMessage()).toString();
        }

        String reason = new StringBuilder(getComponentCode().getCode()).append(REASON_SEPARATOR).append(getFeatureCode().getCode()).append(REASON_SEPARATOR).append(errorId).toString();
        String message = new StringBuilder(getFeatureCode().getName()).append(MESSAGE_SEPARATOR).append(errorMessage).toString();

        log.error("[Error Details]:", exception);

        return ErrorResponse.builder()
                .reason(reason)
                .message(message)
                .build();
    }
}
