package com.gerenciador.financeiro.patterns.strategy;

import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class UltimosDozeMesesStrategy implements PeriodoDashboardStrategy {

    @Override
    public YearMonth obterInicio(YearMonth mesAtual) {
        return mesAtual.minusMonths(11);
    }
}

