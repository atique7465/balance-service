package com.atique.balanceservice.infrustructure.http;

import com.atique.balanceservice.infrustructure.exceptionresolver.exception.ExternalServiceErrorResponseParseException;
import com.atique.balanceservice.infrustructure.exceptionresolver.exception.ExternalServiceException;
import com.atique.balanceservice.infrustructure.exceptionresolver.model.ErrorResponse;
import com.atique.balanceservice.infrustructure.http.errorextractors.ErrorResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * <b>CommonErrorHandler</b> is used in this service to handle any error response from external service. <br>
 * <p>
 * This handler converts any http 4xx | 5xx status external service error response to generic {@link ErrorResponse} and
 * throws {@link ExternalServiceException}
 * <p>
 * If external response content can't be read throws {@link ExternalServiceErrorResponseParseException}
 * <p>
 * For all other http status throws {@link ExternalServiceErrorResponseParseException}
 * <p>
 * <b>This handler does not interrupt</b> {@link org.springframework.web.client.ResourceAccessException}
 * <p>
 *
 * @author atiQue
 * @since 17'Jun 2023 at 5:07 PM
 */
@Slf4j
public class CommonErrorHandler extends DefaultResponseErrorHandler {

    private final ErrorResponseExtractor<? extends ErrorResponse> errorResponseExtractor;

    public CommonErrorHandler(ErrorResponseExtractor<? extends ErrorResponse> errorResponseExtractor) {
        this.errorResponseExtractor = errorResponseExtractor;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        return statusCode.is4xxClientError() || statusCode.is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        handleError(response, statusCode);
    }

    @Override
    protected void handleError(ClientHttpResponse response, HttpStatusCode statusCode) throws IOException {

        if (statusCode.is4xxClientError() || statusCode.is5xxServerError()) {
            try {
                String responseBodyAsString = getResponseBodyAsString(response);
                log.error("Error response from external service: {}", responseBodyAsString.replaceAll("\\s", ""));
                ErrorResponse errorResponse = errorResponseExtractor.extract(responseBodyAsString);
                ExternalServiceException exception = new ExternalServiceException();
                exception.setStatus((HttpStatus) statusCode);
                exception.setErrorResponse(errorResponse);
                throw exception;
            } catch (IOException ex) {
                throw new ExternalServiceErrorResponseParseException();
            }
        } else {
            throw new ExternalServiceErrorResponseParseException();
        }
    }

    private String getResponseBodyAsString(ClientHttpResponse response) {
        byte[] responseBodyBytes = getResponseBody(response);
        Charset charset = getCharset(response);
        if (charset == null) charset = Charset.defaultCharset();
        return new String(responseBodyBytes, charset);
    }

}
