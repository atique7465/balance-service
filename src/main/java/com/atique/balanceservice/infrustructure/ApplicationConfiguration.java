package com.atique.balanceservice.infrustructure;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 11:40 PM
 */

@Configuration
public class ApplicationConfiguration {

    @Bean
    public RestTemplate getRestTemplate() {
        return creteRestTemplate();
    }

    @Bean
    public PoolingHttpClientConnectionManager gePoolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(20);
        connectionManager.setMaxTotal(200);
        return connectionManager;
    }

    @Bean
    public RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.of(100, TimeUnit.MILLISECONDS)) //todo: take from property
                .setResponseTimeout(Timeout.of(100, TimeUnit.MILLISECONDS)) //todo: take from property
                .build();
    }

    @Bean
    public CloseableHttpClient getHttpClient() {
        return HttpClientBuilder.create()
                .setConnectionManager(gePoolingHttpClientConnectionManager())
//                .setRetryStrategy() //todo: configure if retry needed
                .setDefaultRequestConfig(getRequestConfig())
                .build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory getRequestFactory() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(getHttpClient());
        return requestFactory;
    }

    private RestTemplate creteRestTemplate() {

        RestTemplateBuilder builder = new RestTemplateBuilder()
                .requestFactory(() -> new BufferingClientHttpRequestFactory(getRequestFactory()));
//                .additionalInterceptors(new ArrayList<>()); //todo: need to add interceptors for correlation & logging

        return builder.build();
    }

}
