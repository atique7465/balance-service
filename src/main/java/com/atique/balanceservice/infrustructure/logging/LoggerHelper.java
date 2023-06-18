package com.atique.balanceservice.infrustructure.logging;

import com.atique.balanceservice.exceptions.InvalidDataException;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author atiQue
 * @since 18'Jun 2023 at 9:45 PM
 */

@Service
public class LoggerHelper {

    public static Map<String, String> getHeadersToPrint(CachedHttpServletRequest request) {

        Map<String, String> map = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            map.put(name, value);
        }

        return map;
    }

    public static Map<String, String> getHeadersToPrint(ContentCachingResponseWrapper responseWrapper) {

        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = responseWrapper.getHeaderNames();
        for (String name : headerNames) {
            String value = responseWrapper.getHeader(name);
            map.put(name, value);
        }

        return map;
    }

    public static String getContentType(Map<String, String> headers) {

        String contentType = headers.get(HttpHeaders.CONTENT_TYPE);

        if (!StringUtils.hasLength(contentType)) {
            contentType = headers.get(HttpHeaders.CONTENT_TYPE.toLowerCase());
        }

        return contentType;
    }

    public static String getPayloadToPrint(CachedHttpServletRequest cachedHttpServletRequest) {
        String result = IOUtils.toString(cachedHttpServletRequest.getInputStream(), StandardCharsets.UTF_8);
        return result.replaceAll("\\s", "");
    }

    public static String getPayloadToPrint(byte[] payload) {
        String payloadStr = new String(payload, StandardCharsets.UTF_8);
        return payloadStr.replaceAll("\\s", "");
    }

    public static String getPayloadToPrint(ClientHttpResponse response) {
        try {
            String payloadStr = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
            return payloadStr.replaceAll("\\s", "");
        } catch (IOException e) {
            throw new InvalidDataException("External response data parse error", e);
        }
    }
}
