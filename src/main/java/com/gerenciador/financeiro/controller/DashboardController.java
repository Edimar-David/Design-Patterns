
package com.gerenciador.financeiro.controller;

import com.gerenciador.financeiro.dto.DashboardResponse;
import com.gerenciador.financeiro.patterns.strategy.PeriodoDashboard;
import com.gerenciador.financeiro.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping
    public DashboardResponse obterDashboard(
            @RequestParam(
                    defaultValue = "ULTIMOS_SEIS_MESES"
            ) PeriodoDashboard periodo
    ) {
        return service.obterDashboard(periodo);
    }
}
