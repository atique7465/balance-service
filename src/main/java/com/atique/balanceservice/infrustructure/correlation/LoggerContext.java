package com.atique.balanceservice.infrustructure.correlation;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author atiQue
 * @since 20'Jun 2023 at 11:01 PM
 */

public class LoggerContext {

    private static final Map<CorrelationLoggingField, CorrelationHeader> CONTEXT_MAP = new HashMap<>();

    public static void populate() {
        for (CorrelationLoggingField loggingField : CorrelationLoggingField.values()) {
            CorrelationHeader header = CONTEXT_MAP.get(loggingField);
            if (header != null) {
                put(loggingField.getValue(), RequestContext.get(header.getValue()));
            }
        }
    }

    public static void put(String key, String value) {
        if (!StringUtils.hasLength(key) || !StringUtils.hasLength(value)) return;
        MDC.put(key, value);
    }

    /**
     * Clearing LoggerContext MDC as leaving this service
     */
    public static void clear() {
        MDC.clear();
    }

    static {
        CONTEXT_MAP.put(CorrelationLoggingField.CORRELATION_ID, CorrelationHeader.CORRELATION_ID_HEADER);
        CONTEXT_MAP.put(CorrelationLoggingField.CALLER_SERVICE, CorrelationHeader.CALLER_SERVICE_HEADER);
    }
}
