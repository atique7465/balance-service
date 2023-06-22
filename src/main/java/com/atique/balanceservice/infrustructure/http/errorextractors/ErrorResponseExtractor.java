package com.atique.balanceservice.infrustructure.http.errorextractors;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author atiQue
 * @since 19'Jun 2023 at 12:41 AM
 */

public interface ErrorResponseExtractor<T> {

    /**
     * Implement this method to convert custom error response from external server to Generic ErrorResponse <br>
     * and set the extractor to create custom RestTemplate configuration
     * <p>
     * <b>Default:</b> Common implementation of this call assumes external service will return generic ErrorResponse on error
     *
     * @param errorResponseStr {@link org.springframework.http.client.ClientHttpResponse} body as string
     * @return {@link com.atique.balanceservice.exceptionresolvers.model.ErrorResponse}
     * @throws JsonProcessingException
     */
    T extract(String errorResponseStr) throws JsonProcessingException;
}
