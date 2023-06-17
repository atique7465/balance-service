package com.atique.balanceservice.enums;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 6:59 PM
 */

public enum CommonHeader {

    CORRELATION_ID_HEADER("x-bs-correlation-id"),
    CALLER_COMPONENT_HEADER("x-bs-caller"),
    ACCEPT_LANGUAGE_HEADER("x-bs-accept-language"),
    USER_AGENT_HEADER("x-bs-user-agent"),
    ;

    private final String value;

    CommonHeader(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
