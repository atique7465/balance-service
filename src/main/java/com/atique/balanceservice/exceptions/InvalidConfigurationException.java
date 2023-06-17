package com.atique.balanceservice.exceptions;

import com.atique.balanceservice.enums.ErrorCode;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 11:16 PM
 */

public class InvalidConfigurationException extends BaseException {

    public InvalidConfigurationException() {
        super(ErrorCode.INVALID_CONFIGURATION);
    }

    public InvalidConfigurationException(String message) {
        super(ErrorCode.INVALID_CONFIGURATION, message);
    }
}
