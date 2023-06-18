package com.atique.balanceservice.enums;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 9:56 PM
 */

public enum ErrorCode {

    //Reserved Code Range [900 - 999]
    UNKNOWN_ERROR("999", "Error"),
    SERVICE_UNAVAILABLE("998", "Service unavailable"),
    EXT_SERVICE_ERR_RES_PARSE_ERROR("997", "Can not parse external service error response"),


    //Other Error Code Range [000 - 889]
    INVALID_CONFIGURATION("000", "Invalid configuration"),
    INVALID_REQUEST("001", "Invalid request"),
    INVALID_DATA_GIVEN("002", "Invalid data given"),
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
