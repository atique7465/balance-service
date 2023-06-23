package com.atique.balanceservice.infrustructure.logging;

import com.atique.balanceservice.infrustructure.correlation.CorrelationHeader;
import com.atique.balanceservice.infrustructure.correlation.RequestContext;
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

    public static void log(LoggingProperties props, CachedHttpServletRequest cachedHttpServletRequest) {

        if (!props.isEnabled()) return;

        String payLoad = "";
        if (props.isPrintPayload() && props.getPrintableContent().contains(cachedHttpServletRequest.getContentType())) {
            payLoad = LoggerHelper.getPayloadToPrint(cachedHttpServletRequest);
            if (payLoad.length() > props.getMaxPayloadSize()) {
                payLoad = "";
            }
        }
        log(
                RequestContext.get(CorrelationHeader.CALLER_SERVICE_HEADER.getValue()),
                REQ,
                cachedHttpServletRequest.getMethod(),
                cachedHttpServletRequest.getRequestURI(),
                null,
                cachedHttpServletRequest.getProtocol(),
                null,
                props.isPrintHeader() ? LoggerHelper.getHeadersToPrint(cachedHttpServletRequest) : null,
                payLoad
        );
    }

    public static void log(LoggingProperties props, CachedHttpServletRequest cachedHttpServletRequest, ContentCachingResponseWrapper responseWrapper, long processingTime) {

        if (!props.isEnabled()) return;

        String payLoad = "";
        if (props.isPrintPayload() && StringUtils.hasLength(responseWrapper.getContentType()) && props.getPrintableContent().contains(responseWrapper.getContentType())) {
            payLoad = LoggerHelper.getPayloadToPrint(responseWrapper.getContentAsByteArray());
            if (payLoad.length() > props.getMaxPayloadSize()) {
                payLoad = "";
            }
        }

        log(
                null,
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

    public static void log(LoggingProperties props, HttpRequest request, byte[] body) {

        if (!props.isEnabled()) return;

        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        String contentType = LoggerHelper.getContentType(headers);

        String payLoad = "";
        if (props.isPrintPayload() && StringUtils.hasLength(contentType) && props.getPrintableContent().contains(contentType)) {
            payLoad = LoggerHelper.getPayloadToPrint(body);
            if (payLoad.length() > props.getMaxPayloadSize()) {
                payLoad = "";
            }
        }

        log(
                null,
                REQ,
                request.getMethod().name(),
                request.getURI().toString(),
                null,
                null,
                null,
                props.isPrintHeader() ? headers : null,
                payLoad
        );
    }

    public static void log(LoggingProperties props, HttpRequest request, ClientHttpResponse response, long processingTime) throws IOException {

        if (!props.isEnabled()) return;

        Map<String, String> headers = response.getHeaders().toSingleValueMap();
        String contentType = LoggerHelper.getContentType(headers);

        String payLoad = "";
        if (props.isPrintPayload() && StringUtils.hasLength(contentType) && props.getPrintableContent().contains(contentType)) {
            payLoad = LoggerHelper.getPayloadToPrint(response);
            if (payLoad.length() > props.getMaxPayloadSize()) {
                payLoad = "";
            }
        }

        log(
                null,
                RES,
                request.getMethod().name(),
                request.getURI().toString(),
                response.getStatusCode().toString(),
                null,
                String.valueOf(processingTime),
                props.isPrintHeader() ? headers : null,
                payLoad
        );
    }


    private static void log(String fromService, String prefix, String method, String uri, String httpStatus, String protocol, String processingTime, Map<String, String> headers, String payLoad) {

        StringBuilder msg = new StringBuilder();

        if (StringUtils.hasLength(fromService)) {
            msg.append("[FROM:").append(fromService).append("] ");
        }

        // Log Method, URL
        msg.append(prefix).append(" ").append(method).append(" ").append(uri).append(" ");

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
        if (StringUtils.hasLength(payLoad)) {
            msg.append("[Payload]: ").append(payLoad);
        }

        logger.info(msg.toString());
    }
}
