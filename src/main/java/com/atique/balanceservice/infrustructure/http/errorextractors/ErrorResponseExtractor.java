package com.atique.balanceservice.infrustructure.http.errorextractors;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author atiQue
 * @since 19'Jun 2023 at 12:41 AM
 */

public interface ErrorResponseExtractor<T> {
    T extract(String errorResponseStr) throws JsonProcessingException;
}
