package com.gerenciador.financeiro.patterns.strategy;

import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class UltimosSeisMesesStrategy implements PeriodoDashboardStrategy {

    @Override
    public YearMonth obterInicio(YearMonth mesAtual) {
        return mesAtual.minusMonths(5);
    }
}
