package com.gerenciador.financeiro.patterns.decorator;

import com.gerenciador.financeiro.dto.DashboardResponse;

public interface RelatorioFinanceiro {

    DashboardResponse gerar();
}