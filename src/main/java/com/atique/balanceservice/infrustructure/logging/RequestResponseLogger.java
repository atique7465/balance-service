package com.atique.balanceservice.infrustructure.logging;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public static void log(boolean isRequest, String method, String uri, String protocol, Map<String, String> headers, String payLoad) {

        if (!LoggingProperties.enabled) return;

        // Log Method, URL, Protocol
        StringBuilder msg = new StringBuilder(isRequest ? REQ : RES).append(" ")
                .append(method).append(" ")
                .append(uri).append(" ");

        if (StringUtils.hasLength(protocol)) msg.append(protocol).append(" ");

        // Log Headers
        if (LoggingProperties.printHeader) {
            msg.append("[Headers]: ").append(headers).append(" ");
        }

        // Log payLoad
        if (LoggingProperties.printPayload && StringUtils.hasLength(payLoad) && payLoad.length() <= LoggingProperties.maxPayloadSize) {
            msg.append("[Payload]: ").append(payLoad).append(" ");
        }

        logger.info(msg.toString());
    }
}
