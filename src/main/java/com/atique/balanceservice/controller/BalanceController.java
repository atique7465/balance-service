package com.atique.balanceservice.controller;

import com.atique.balanceservice.annotations.ValidAccountNo;
import com.atique.balanceservice.model.BalanceSummary;
import com.atique.balanceservice.service.BalanceService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = "/{accNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    BalanceSummary getBalance(@PathVariable(value = "accNo") @ValidAccountNo String accNo) {
        return balanceService.getSummaryByAccNo(accNo);
    }
}
