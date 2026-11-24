package com.gerenciador.financeiro.patterns.decorator;

import com.gerenciador.financeiro.dto.DashboardResponse;

public abstract class RelatorioDecorator implements RelatorioFinanceiro {

    protected final RelatorioFinanceiro relatorio;

    public RelatorioDecorator(RelatorioFinanceiro relatorio) {
        this.relatorio = relatorio;
    }

    @Override
    public DashboardResponse gerar() {
        return relatorio.gerar();
    }
}