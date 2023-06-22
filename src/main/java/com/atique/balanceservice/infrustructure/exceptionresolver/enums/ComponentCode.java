package com.atique.balanceservice.infrustructure.exceptionresolver.enums;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 4:29 PM
 */

public enum ComponentCode {

    BALANCE_SERVICE("BS", "10"),
    TRANSACTION_HISTORY_SERVICE("THS", "11"),
    ;

    private final String name;
    private final String code;

    ComponentCode(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
