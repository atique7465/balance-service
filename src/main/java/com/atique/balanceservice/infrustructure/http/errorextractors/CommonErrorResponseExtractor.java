package com.atique.balanceservice.infrustructure.http.errorextractors;

import com.atique.balanceservice.exceptionresolvers.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author atiQue
 * @since 19'Jun 2023 at 12:42 AM
 */

public class CommonErrorResponseExtractor implements ErrorResponseExtractor<ErrorResponse> {

    private final ObjectMapper objectMapper;

    public CommonErrorResponseExtractor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ErrorResponse extract(String errorResponseStr) throws JsonProcessingException {
        return objectMapper.readValue(errorResponseStr, ErrorResponse.class);
    }
}
