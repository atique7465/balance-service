package com.atique.balanceservice.enums;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 6:59 PM
 */

public enum CommonHeader {

    CORRELATION_ID_HEADER("X-BS-Correlation-Id"),
    CALLER_ID_HEADER("X-BS-Caller"),
    ACCEPT_LANGUAGE_HEADER("X-BS-Accept-Language"),
    USER_AGENT_HEADER("X-BS-User-Agent"),
    ;

    private final String value;

    CommonHeader(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
