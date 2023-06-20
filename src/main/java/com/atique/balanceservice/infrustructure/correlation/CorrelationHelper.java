package com.atique.balanceservice.infrustructure.correlation;

import com.atique.balanceservice.enums.ComponentCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author atiQue
 * @since 20'Jun 2023 at 11:16 PM
 */

public class CorrelationHelper {

    private static final String CORRELATION_DATE_FORMAT = "yyMMddHHmmss";
    private static final String CORRELATION_SEPARATOR = "-";
    private static final int CORRELATION_HEX_STRING_LEN = 7;

    /**
     * Storing all the incoming correlation headers RequestContext
     *
     * @param request HttpServletRequest
     */
    public static void prepareRequestContext(HttpServletRequest request) {

        for (CorrelationHeader header : CorrelationHeader.values()) {
            String value = request.getHeader(header.getValue());
            if (StringUtils.hasLength(value)) {
                RequestContext.put(header.getValue(), value);
            }
        }

        //If this is the first service then generate CORRELATION_ID
        String correlationID = RequestContext.get(CorrelationHeader.CORRELATION_ID_HEADER.getValue());
        if (!StringUtils.hasLength(correlationID)) {
            RequestContext.put(CorrelationHeader.CORRELATION_ID_HEADER.getValue(), generateCorrelationId());
        }
    }

    private static String generateCorrelationId() {
        StringBuilder correlationIdStringBuilder = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat(CORRELATION_DATE_FORMAT);
        String dateStr = sdf.format(new Date());
        return correlationIdStringBuilder.append(dateStr).append(CORRELATION_SEPARATOR).append(getRandomHexString()).toString();
    }

    private static String getRandomHexString() {
        String randomHex;
        Random random = new Random();
        randomHex = Integer.toHexString(random.nextInt());
        return randomHex.substring(0, CORRELATION_HEX_STRING_LEN);
    }


    /**
     * Updating correlation headers in response to propagate to upper-stream service
     *
     * @param response HttpServletResponse
     */
    public static void updateHeaders(HttpServletResponse response) {
        for (CorrelationHeader header : CorrelationHeader.values()) {
            String value = RequestContext.get(header.getValue());
            if (StringUtils.hasLength(value)) {
                response.setHeader(header.getValue(), value);
            }
        }
    }

    /**
     * Appending correlation headers in request to propagate to down-stream service
     *
     * @param request HttpRequest
     */
    public static void appendHeaders(HttpRequest request) {

        for (CorrelationHeader header : CorrelationHeader.values()) {
            String value = RequestContext.get(header.getValue());
            if (StringUtils.hasLength(value)) {
                request.getHeaders().set(header.getValue(), value);
            }
        }

        request.getHeaders().set(CorrelationHeader.CALLER_SERVICE_HEADER.getValue(), ComponentCode.BALANCE_SERVICE.getName());
    }

    /**
     * Update RequestContext using the response headers from down-stream service
     *
     * @param response ClientHttpResponse
     */
    public static void updateRequestContext(ClientHttpResponse response) {

        for (CorrelationHeader header : CorrelationHeader.values()) {
            String value = response.getHeaders().getFirst(header.getValue());
            if (StringUtils.hasLength(value)) {
                RequestContext.put(header.getValue(), value);
            }
        }
    }
}
