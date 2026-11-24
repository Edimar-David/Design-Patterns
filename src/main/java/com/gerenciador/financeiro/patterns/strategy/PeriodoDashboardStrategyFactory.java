package com.gerenciador.financeiro.patterns.strategy;

import org.springframework.stereotype.Component;

@Component
public class PeriodoDashboardStrategyFactory {

    private final UltimosSeisMesesStrategy seisMeses;
    private final UltimosDozeMesesStrategy dozeMeses;
    private final AnoAtualStrategy anoAtual;

    public PeriodoDashboardStrategyFactory(
            UltimosSeisMesesStrategy seisMeses,
            UltimosDozeMesesStrategy dozeMeses,
            AnoAtualStrategy anoAtual
    ) {
        this.seisMeses = seisMeses;
        this.dozeMeses = dozeMeses;
        this.anoAtual = anoAtual;
    }

    public PeriodoDashboardStrategy obterEstrategia(
            PeriodoDashboard periodo
    ) {

        return switch (periodo) {

            case ULTIMOS_SEIS_MESES -> seisMeses;

            case ULTIMOS_DOZE_MESES -> dozeMeses;

            case ANO_ATUAL -> anoAtual;
        };
    }
}
