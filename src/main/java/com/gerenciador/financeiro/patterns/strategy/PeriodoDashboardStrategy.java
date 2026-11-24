package com.gerenciador.financeiro.patterns.strategy;

import java.time.YearMonth;

public interface PeriodoDashboardStrategy {

    YearMonth obterInicio(YearMonth mesAtual);
}

