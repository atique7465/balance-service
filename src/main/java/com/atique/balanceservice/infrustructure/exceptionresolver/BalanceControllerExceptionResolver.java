package com.atique.balanceservice.infrustructure.exceptionresolver;

import com.atique.balanceservice.controller.BalanceController;
import com.atique.balanceservice.infrustructure.exceptionresolver.enums.FeatureCode;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 10:09 PM
 */

@ControllerAdvice(assignableTypes = BalanceController.class)
@Order(value = 1)
public class BalanceControllerExceptionResolver extends BaseExceptionResolver {

    @Override
    public FeatureCode getFeatureCode() {
        return FeatureCode.BALANCE_SUMMARY;
    }
}
