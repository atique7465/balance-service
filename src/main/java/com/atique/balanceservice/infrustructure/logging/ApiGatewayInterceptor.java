package com.atique.balanceservice.infrustructure.logging;

import com.atique.balanceservice.enums.CommonHeader;
import com.atique.balanceservice.enums.ComponentCode;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * @author atiQue
 * @since 18'Jun 2023 at 12:47 AM
 */

public class ApiGatewayInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        long start = System.nanoTime();

        request.getHeaders().add(CommonHeader.CORRELATION_ID_HEADER.getValue(), "CID0001");
        request.getHeaders().add(CommonHeader.CALLER_COMPONENT_HEADER.getValue(), ComponentCode.BALANCE_SERVICE.getName());

        //Log request
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        RequestResponseLogger.log(request, body);

        ClientHttpResponse response = execution.execute(request, body);

        long processingTime = (System.nanoTime() - start) / 1000000;

        //Log response
        RequestResponseLogger.log(request, response, processingTime);

        return response;
    }
}
