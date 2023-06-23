package com.atique.balanceservice.infrustructure.logging;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author atiQue
 * @since 18'Jun 2023 at 12:47 AM
 */

public class LoggerApiGatewayInterceptor implements ClientHttpRequestInterceptor {

    private final LoggingProperties props;

    public LoggerApiGatewayInterceptor(LoggingProperties props) {
        this.props = props;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        long start = System.nanoTime();

        //Log request
        RequestResponseLogger.log(props, request, body);

        ClientHttpResponse response = execution.execute(request, body);

        long processingTime = (System.nanoTime() - start) / 1000000;

        //Log response
        RequestResponseLogger.log(props, request, response, processingTime);

        return response;
    }
}
