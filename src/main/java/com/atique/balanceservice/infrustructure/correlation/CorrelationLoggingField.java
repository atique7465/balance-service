package com.atique.balanceservice.infrustructure.correlation;

/**
 * @author atiQue
 * @since 20'Jun 2023 at 11:07 PM
 */

public enum CorrelationLoggingField {

    CORRELATION_ID("correlationId"),
    CALLER_SERVICE("callerService"),
    ;

    private final String value;

    CorrelationLoggingField(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
