package com.atique.balanceservice.enums;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 9:56 PM
 */

public enum ErrorCode {

    UNKNOWN_ERROR("999", "Error"),
    INVALID_CONFIGURATION("001", "Invalid configuration"),
    INVALID_REQUEST("002", "Invalid request"),
    INVALID_DATA_GIVEN("003", "Invalid data given"),
    INVALID_DATE_FORMAT("003", "Invalid date format"),
    ;

    private final String id;
    private final String message;

    ErrorCode(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
