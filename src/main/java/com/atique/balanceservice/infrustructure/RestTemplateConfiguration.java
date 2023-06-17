package com.atique.balanceservice.infrustructure;

import com.atique.balanceservice.exceptions.InvalidConfigurationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.socket.LayeredConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 11:40 PM
 */

@Slf4j
@Configuration
public class RestTemplateConfiguration {

    private final ObjectMapper objectMapper;

    private final HttpConnectionPoolProperties poolProps;
    private final HttpRouteProperties routeProps;

    public RestTemplateConfiguration(ObjectMapper objectMapper, HttpConnectionPoolProperties poolProps, HttpRouteProperties routeProps) {
        this.objectMapper = objectMapper;
        this.poolProps = poolProps;
        this.routeProps = routeProps;
    }

    @Bean
    public RestTemplate creteRestTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder()
                .requestFactory(() -> new BufferingClientHttpRequestFactory(getRequestFactory()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .messageConverters(getMappingJackson2HttpMessageConverter())
                .additionalInterceptors(new ArrayList<>()); //todo: need to add interceptors for correlation & logging
        return builder.build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory getRequestFactory() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(getHttpClient());
        return requestFactory;
    }

    @Bean
    public CloseableHttpClient getHttpClient() {

        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec(StandardCookieSpec.STRICT)
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(poolProps.getConnectionRequestTimeout()))
                .setResponseTimeout(Timeout.ofMilliseconds(poolProps.getResponseTimeout()))
                .setConnectionKeepAlive(Timeout.ofMilliseconds(poolProps.getDefaultKeepAliveTime()))
                .build();

        return HttpClients.custom()
                .setConnectionManager(gePoolingHttpClientConnectionManager())
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    private static final URIScheme DEFAULT_SCHEME = URIScheme.HTTP;

    @Bean
    public PoolingHttpClientConnectionManager gePoolingHttpClientConnectionManager() {

        LayeredConnectionSocketFactory sslConnectionSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(SSLContexts.createSystemDefault())
                .setTlsVersions(TLS.V_1_3)
                .build();

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(Timeout.ofMilliseconds(poolProps.getSocketTimeout()))
                .build();

        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setSocketTimeout(Timeout.ofMilliseconds(poolProps.getSocketTimeout()))
                .setConnectTimeout(Timeout.ofMilliseconds(poolProps.getConnectTimeout()))
                .setTimeToLive(Timeout.ofMilliseconds(poolProps.getTimeToLive()))
                .setValidateAfterInactivity(Timeout.ofMilliseconds(poolProps.getValidateAfterInactivity()))
                .build();

        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setDefaultSocketConfig(socketConfig)
                .setDefaultConnectionConfig(connectionConfig)
                .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
                .setConnPoolPolicy(PoolReusePolicy.LIFO)
                .build();

        connectionManager.setMaxTotal(poolProps.getMaxTotalConnection());
        connectionManager.setDefaultMaxPerRoute(poolProps.getMaxConnectionPerRoute());

        routeProps.getMap().forEach((k, v) -> {
            if (v.getMaxConnection() != null && v.getMaxConnection() > 0) {
                if (!StringUtils.hasLength(v.getHost()) || v.getPort() == null) {
                    log.error("[External Route Configuration Error]:[Property Source]: Host or Port configuration is invalid for {}. Found Host: {} & Post: {}", k, v.getHost(), v.getPort());
                    throw new InvalidConfigurationException("Host or Port configuration is invalid for " + k);
                }
                HttpRoute httpRoute = new HttpRoute(new HttpHost(StringUtils.hasLength(v.getScheme()) ? v.getScheme() : DEFAULT_SCHEME.id, v.getHost(), v.getPort()));
                connectionManager.setMaxPerRoute(httpRoute, v.getMaxConnection());
            }
        });

        return connectionManager;
    }

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }
}
