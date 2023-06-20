package com.atique.balanceservice.infrustructure.correlation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author atiQue
 * @since 20'Jun 2023 at 10:52 PM
 */

@Slf4j
public class RequestContext {

    public static InheritableThreadLocal<Map<String, String>> CONTEXT_MAP = new InheritableThreadLocal<Map<String, String>>() {
        @Override
        protected Map<String, String> initialValue() {
            log.trace("Correlation request context initiated for: {}", Thread.currentThread());
            return new HashMap<>();
        }
    };

    public static void put(String key, String value) {

        if (!StringUtils.hasLength(key) || !StringUtils.hasLength(value)) return;

        CONTEXT_MAP.get().put(key, value);
    }

    public static String get(String key) {

        if (!StringUtils.hasLength(key)) return null;

        return CONTEXT_MAP.get().get(key);
    }
}
