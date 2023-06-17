package com.atique.balanceservice.infrustructure.gateway;

import org.springframework.lang.Nullable;

/**
 * @author atiQue
 * @since 18'Jun 2023 at 1:49 AM
 */

public interface ApiGateWay {

    <T> T GET(String url, Class<T> responseType);

    <T> T POST(String url, @Nullable Object request, Class<T> responseType);
}
