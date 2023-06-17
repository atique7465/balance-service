package com.atique.balanceservice.exceptionresolvers;

import com.atique.balanceservice.exceptions.BaseException;
import com.atique.balanceservice.exceptions.InvalidDataException;
import com.atique.balanceservice.exceptions.InvalidRequestException;
import com.atique.balanceservice.enums.ComponentCode;
import com.atique.balanceservice.enums.ErrorCode;
import com.atique.balanceservice.enums.FeatureCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 9:42 PM
 */

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
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidRequestException.class})
    public ErrorResponse handleBadRequestException(HttpServletRequest request, BaseException baseException) {
        return getErrorResponse(request, baseException);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({InvalidDataException.class})
    @ResponseBody
    public ErrorResponse handleInternalServerException(HttpServletRequest request, BaseException baseException) {
        return getErrorResponse(request, baseException);
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorResponse handleBadRequestException(HttpServletRequest request, MethodArgumentNotValidException exception) {
        return getErrorResponse(request, new BaseException(exception));
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidationException.class})
    public ErrorResponse handleBadRequestException(HttpServletRequest request, ValidationException exception) {
        return getErrorResponse(request, new BaseException(exception));
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ErrorResponse handleGeneralJavaException(HttpServletRequest request, Exception exception) {
        return getErrorResponse(request, new BaseException(exception));
    }

    private ErrorResponse getErrorResponse(HttpServletRequest request, BaseException baseException) {

        String errorId = ErrorCode.UNKNOWN_ERROR.getId();
        String errorMessage = ErrorCode.UNKNOWN_ERROR.getMessage();

        if (baseException.getErrorCode() != null) {
            errorId = baseException.getErrorCode().getId();
            errorMessage = baseException.getErrorCode().getMessage();
        }

        if (StringUtils.hasLength(baseException.getMessage())) {
            errorMessage = new StringBuilder(errorMessage).append(MESSAGE_SEPARATOR).append(baseException.getMessage()).toString();
        }

        String reason = new StringBuilder(getComponentCode().getCode()).append(REASON_SEPARATOR).append(getFeatureCode().getCode()).append(REASON_SEPARATOR).append(errorId).toString();
        String message = new StringBuilder(getFeatureCode().getName()).append(MESSAGE_SEPARATOR).append(errorMessage).toString();

        return ErrorResponse.builder()
                .reason(reason)
                .message(message)
                .build();
    }
}
