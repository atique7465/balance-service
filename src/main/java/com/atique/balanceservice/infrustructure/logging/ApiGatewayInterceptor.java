package com.atique.balanceservice.infrustructure.logging;

import com.atique.balanceservice.enums.CommonHeader;
import com.atique.balanceservice.enums.ComponentCode;
import com.atique.balanceservice.exceptions.InvalidDataException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author atiQue
 * @since 18'Jun 2023 at 12:47 AM
 */

public class ApiGatewayInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        request.getHeaders().add(CommonHeader.CORRELATION_ID_HEADER.getValue(), "CID0001");
        request.getHeaders().add(CommonHeader.CALLER_COMPONENT_HEADER.getValue(), ComponentCode.BALANCE_SERVICE.getName());

        //Log request
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        RequestResponseLogger.log(true, request.getMethod().name(), request.getURI().toURL().toString(), null, headers, getReqPayload(headers, body));

        ClientHttpResponse response = execution.execute(request, body);

        //Log response
        headers = response.getHeaders().toSingleValueMap();
        RequestResponseLogger.log(false, request.getMethod().name(), request.getURI().toURL().toString(), null, headers, getResPayload(headers, response.getBody()));

        return response;
    }

    String getReqPayload(Map<String, String> headers, byte[] payload) {

        String contentType = headers.get(HttpHeaders.CONTENT_TYPE.toLowerCase());

        if (StringUtils.hasLength(contentType) && LoggingProperties.printableContent.contains(contentType)) {
            String payloadStr = new String(payload, StandardCharsets.UTF_8);
            return payloadStr.replaceAll("\\s", "");
        }

        return null;
    }

    String getResPayload(Map<String, String> headers, InputStream inputStream) {

        String contentType = headers.get(HttpHeaders.CONTENT_TYPE.toLowerCase());

        if (StringUtils.hasLength(contentType) && LoggingProperties.printableContent.contains(contentType)) {
            try {
                String payloadStr = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                return payloadStr.replaceAll("\\s", "");
            } catch (IOException e) {
                throw new InvalidDataException("External response data parse error", e);
            }
        }

        return null;
    }
}
