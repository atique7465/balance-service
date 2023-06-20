package com.atique.balanceservice.infrustructure.correlation;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 6:59 PM
 */

public enum CorrelationHeader {

    CORRELATION_ID_HEADER("x-bs-correlation-id"),
    CALLER_SERVICE_HEADER("x-bs-caller-service"),
    ACCEPT_LANGUAGE_HEADER("x-bs-accept-language"),
    USER_AGENT_HEADER("x-bs-user-agent"),
    ;

    private final String value;

    CorrelationHeader(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
