package com.atique.balanceservice.controller;

import com.atique.balanceservice.annotations.ValidAccountNo;
import com.atique.balanceservice.infrustructure.ApplicationProperties;
import com.atique.balanceservice.infrustructure.gateway.ApiGateWay;
import com.atique.balanceservice.model.BalanceSummary;
import com.atique.balanceservice.model.Request;
import com.atique.balanceservice.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author atiQue
 * @since 14'Jun 2023 at 10:44 PM
 */

@Validated
@RestController
@RequestMapping(value = "/api/v1/balance")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @Operation(summary = "Get Balance Summary By Account Number", tags = {"Balance", "Monthly Balance", "Cumulative Balance"})
    @GetMapping(value = "/{accNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    BalanceSummary getBalance(@Parameter(name = "accNo", description = "Customer Account No", required = true)
                              @PathVariable(value = "accNo") @ValidAccountNo String accNo) {
        return balanceService.getSummaryByAccNo(accNo);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    BalanceSummary getBalance(@RequestBody Request request) {
        return balanceService.getSummaryByAccNo(request.getAccNo());
    }
}
