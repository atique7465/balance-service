package com.atique.balanceservice.infrustructure.gateway;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author atiQue
 * @since 18'Jun 2023 at 2:02 AM
 */

@Service
public class ApiGateWayImpl implements ApiGateWay {

    private final RestTemplate restTemplate;

    public ApiGateWayImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> T GET(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    @Override
    public <T> T POST(String url, Object request, Class<T> responseType) {
        return restTemplate.postForObject(url, request, responseType);
    }
}
