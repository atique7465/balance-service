package com.atique.balanceservice;

import com.atique.balanceservice.controller.BalanceController;
import com.atique.balanceservice.controller.model.Balance;
import com.atique.balanceservice.controller.model.BalanceSummary;
import com.atique.balanceservice.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author atiQue
 * @since 22'Jun 2023 at 1:23 AM
 */

public class BalanceControllerTest {

    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private BalanceController balanceController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(balanceController).build();
    }

    @Test
    void testSuccessGetBalance() throws Exception {
        // Mock the behavior of the service method
        String accNo = "1234567890";
        BalanceSummary expectedSummary = getBalanceSummary();
        when(balanceService.getSummaryByAccNo(accNo)).thenReturn(expectedSummary);

        // Perform the GET request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/balance/{accNo}", accNo)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert the response status code
        int statusCode = result.getResponse().getStatus();
        assertEquals(200, statusCode);

        // Assert the response body
        String responseBody = result.getResponse().getContentAsString();
        assertEquals(getSummaryStr(), responseBody);
    }

    public BalanceSummary getBalanceSummary() {

        List<Balance> content = new ArrayList<>(
                Arrays.asList(
                        Balance.builder().month("04-2022").monthlyBalance(new BigDecimal("100")).cumulativeBalance(new BigDecimal("100")).build(),
                        Balance.builder().month("08-2022").monthlyBalance(new BigDecimal("1000")).cumulativeBalance(new BigDecimal("1100")).build(),
                        Balance.builder().month("05-2023").monthlyBalance(new BigDecimal("640")).cumulativeBalance(new BigDecimal("1740")).build(),
                        Balance.builder().month("05-2023").monthlyBalance(new BigDecimal("1040")).cumulativeBalance(new BigDecimal("2780")).build(),
                        Balance.builder().month("07-2023").monthlyBalance(BigDecimal.ZERO).cumulativeBalance(new BigDecimal("2780")).build()
                )
        );

        return BalanceSummary.builder().content(content).build();
    }

    public String getSummaryStr() {
        return "{\"content\":[{\"month\":\"04-2022\",\"monthlyBalance\":100,\"cumulativeBalance\":100},{\"month\":\"08-2022\",\"monthlyBalance\":1000,\"cumulativeBalance\":1100},{\"month\":\"05-2023\",\"monthlyBalance\":640,\"cumulativeBalance\":1740},{\"month\":\"05-2023\",\"monthlyBalance\":1040,\"cumulativeBalance\":2780},{\"month\":\"07-2023\",\"monthlyBalance\":0,\"cumulativeBalance\":2780}]}";
    }
}
