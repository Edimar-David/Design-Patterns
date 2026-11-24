package com.gerenciador.financeiro.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(

        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal saldo,
        long quantidadeTransacoes,

        String moeda,

        List<EvolucaoMensal> evolucaoMensal,

        List<DespesaPorCategoria> despesasPorCategoria

) {

    public record EvolucaoMensal(
            String mes,
            BigDecimal receitas,
            BigDecimal despesas
    ) {
    }

    public record DespesaPorCategoria(
            String categoria,
            BigDecimal valor
    ) {
    }
}

