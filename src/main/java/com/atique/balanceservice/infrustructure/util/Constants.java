package com.atique.balanceservice.infrustructure.util;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 11:50 PM
 */

public interface Constants {

    interface AccountNo {
        String REGEX = "^\\d+$";
        Integer MIN_LENGTH = 10;
        Integer MAX_LENGTH = 10;
    }

    interface ApiGateWays {
        String COMMON_API_GATEWAY = "common-api-gateway";
    }

    interface CircuitBreakerDao {
        String TXN_HISTORY_DAO = "transaction-history-dao";
    }
}
