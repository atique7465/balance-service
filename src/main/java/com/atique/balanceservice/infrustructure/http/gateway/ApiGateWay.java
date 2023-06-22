package com.atique.balanceservice.infrustructure.http.gateway;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

/**
 * @author atiQue
 * @since 18'Jun 2023 at 1:49 AM
 */

public interface ApiGateWay {

    <T> T GET(String url, Class<T> responseType);

    <T> T POST(String url, @Nullable Object request, Class<T> responseType);

    <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType);
}
