package com.atique.balanceservice.infrustructure.logging;

import java.util.Map;

/**
 * @author atiQue
 * @since 17'Jun 2023 at 9:27 PM
 */

public interface RequestResponseLogger {
    void log(boolean isRequest, String method, String uri, String protocol, Map<String, String> headers, String body);
}
