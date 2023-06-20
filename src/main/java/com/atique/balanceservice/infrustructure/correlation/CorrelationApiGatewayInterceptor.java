package com.atique.balanceservice.infrustructure.correlation;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author atiQue
 * @since 18'Jun 2023 at 12:47 AM
 */

public class CorrelationApiGatewayInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        CorrelationHelper.appendHeaders(request);

        ClientHttpResponse response = execution.execute(request, body);

        CorrelationHelper.updateRequestContext(response);

        return response;
    }
}
