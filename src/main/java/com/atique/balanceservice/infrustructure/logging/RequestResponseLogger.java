package com.atique.balanceservice.infrustructure.logging;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 9:10 PM
 */

@Service
public class RequestResponseLogger {

    private static final String REQ = "[REQ]";
    private static final String RES = "[RES]";

    private static final Logger logger = Logger.getLogger(RequestResponseLogger.class.getName());

    public static void log(CachedHttpServletRequest cachedHttpServletRequest) {

        if (!LoggingProperties.enabled) return;

        String payLoad = "";
        if (LoggingProperties.printPayload && LoggingProperties.printableContent.contains(cachedHttpServletRequest.getContentType())) {
            payLoad = LoggerHelper.getPayloadToPrint(cachedHttpServletRequest);
        }

        log(
                REQ,
                cachedHttpServletRequest.getMethod(),
                cachedHttpServletRequest.getRequestURI(),
                null,
                cachedHttpServletRequest.getProtocol(),
                null,
                LoggingProperties.printHeader ? LoggerHelper.getHeadersToPrint(cachedHttpServletRequest) : null,
                payLoad
        );
    }

    public static void log(CachedHttpServletRequest cachedHttpServletRequest, ContentCachingResponseWrapper responseWrapper, long processingTime) {

        if (!LoggingProperties.enabled) return;

        String payLoad = "";
        if (LoggingProperties.printPayload && StringUtils.hasLength(responseWrapper.getContentType()) && LoggingProperties.printableContent.contains(responseWrapper.getContentType())) {
            payLoad = LoggerHelper.getPayloadToPrint(responseWrapper.getContentAsByteArray());
        }

        RequestResponseLogger.log(
                RES,
                cachedHttpServletRequest.getMethod(),
                cachedHttpServletRequest.getRequestURI(),
                String.valueOf(responseWrapper.getStatus()),
                null,
                String.valueOf(processingTime),
                null,
                payLoad
        );
    }

    public static void log(HttpRequest request, byte[] body) {

        if (!LoggingProperties.enabled) return;

        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        String contentType = LoggerHelper.getContentType(headers);

        String payLoad = "";
        if (LoggingProperties.printPayload && StringUtils.hasLength(contentType) && LoggingProperties.printableContent.contains(contentType)) {
            payLoad = LoggerHelper.getPayloadToPrint(body);
        }

        log(
                REQ,
                request.getMethod().name(),
                request.getURI().toString(),
                null,
                null,
                null,
                LoggingProperties.printHeader ? headers : null,
                payLoad
        );
    }

    public static void log(HttpRequest request, ClientHttpResponse response, long processingTime) throws IOException {

        if (!LoggingProperties.enabled) return;

        Map<String, String> headers = response.getHeaders().toSingleValueMap();
        String contentType = LoggerHelper.getContentType(headers);

        String payLoad = "";
        if (LoggingProperties.printPayload && StringUtils.hasLength(contentType) && LoggingProperties.printableContent.contains(contentType)) {
            payLoad = LoggerHelper.getPayloadToPrint(response);
        }

        RequestResponseLogger.log(
                RES,
                request.getMethod().name(),
                request.getURI().toString(),
                response.getStatusCode().toString(),
                null,
                String.valueOf(processingTime),
                LoggingProperties.printHeader ? headers : null,
                payLoad
        );
    }


    private static void log(String prefix, String method, String uri, String httpStatus, String protocol, String processingTime, Map<String, String> headers, String payLoad) {

        // Log Method, URL
        StringBuilder msg = new StringBuilder(prefix).append(" ").append(method).append(" ").append(uri).append(" ");

        //Log Http Status
        if (StringUtils.hasLength(httpStatus)) {
            msg.append(httpStatus).append(" ");
        }

        //Log Protocol
        if (StringUtils.hasLength(protocol)) {
            msg.append(protocol).append(" ");
        }

        //Log Processing Time in millis
        if (StringUtils.hasLength(processingTime)) {
            msg.append(processingTime).append(" ms ");
        }

        // Log Headers
        if (headers != null) {
            msg.append("[Headers]: ").append(headers).append(" ");
        }

        // Log payLoad
        if (StringUtils.hasLength(payLoad) && payLoad.length() <= LoggingProperties.maxPayloadSize) {
            msg.append("[Payload]: ").append(payLoad);
        }

        logger.info(msg.toString());
    }
}
