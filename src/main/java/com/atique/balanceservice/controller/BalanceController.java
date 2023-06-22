package com.atique.balanceservice.controller;

import com.atique.balanceservice.annotations.ValidAccountNo;
import com.atique.balanceservice.controller.model.BalanceSummary;
import com.atique.balanceservice.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get Balance Summary By Account Number", tags = {"Balance", "Monthly Balance", "Cumulative Balance"})
    @ApiResponses(value = {@ApiResponse(content = {@Content(schema = @Schema(implementation = BalanceSummary.class))})})
    @GetMapping(value = "/{accNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    BalanceSummary getBalance(@Parameter(name = "accNo", description = "Customer Account No. Length[Min:10, Max:10], Pattern: Digit only [^\\d+$]", required = true, example = "0123456789")
                              @PathVariable(value = "accNo")
                              @ValidAccountNo
                              String accNo) {
        return balanceService.getSummaryByAccNo(accNo);
    }
}
