package com.atique.balanceservice.exceptionresolvers.enums;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 10:11 PM
 */

public enum FeatureCode {

    BASE_FEATURE_CODE("99", "Balance service application"),
    BALANCE_SUMMARY("00", "Balance Summary");

    private final String code;
    private final String name;

    FeatureCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
