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
public class RequestResponseLoggerImpl implements RequestResponseLogger {

    private static final String REQ = "[REQ]";
    private static final String RES = "[RES]";

    private final LoggingProperties props;

    private static final Logger logger = Logger.getLogger(RequestResponseLoggerImpl.class.getName());

    public RequestResponseLoggerImpl(LoggingProperties props) {
        this.props = props;
    }

    @Override
    public void log(boolean isRequest, String method, String uri, String protocol, Map<String, String> headers, String body) {

        // Log Method, URL, Protocol
        StringBuilder reqStringBuilder = new StringBuilder(isRequest ? REQ : RES).append(" ")
                .append(method).append(" ")
                .append(uri).append(" ")
                .append(protocol).append(" ");

        // Log Headers
        if (props.isPrintReqHeader()) {
            reqStringBuilder.append("[Headers]: ").append(headers).append(" ");
        }

        // Log body
        if (props.isPrintRequestBody() && StringUtils.hasLength(body)) {
            reqStringBuilder.append("[Body]: ").append(body).append(" ");
        }

        logger.info(reqStringBuilder.toString());
    }
}
