package com.gerenciador.financeiro.patterns.strategy;

import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class AnoAtualStrategy implements PeriodoDashboardStrategy {

    @Override
    public YearMonth obterInicio(YearMonth mesAtual) {
        return YearMonth.of(mesAtual.getYear(), 1);
    }
}

