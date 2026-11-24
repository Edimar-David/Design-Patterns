package com.gerenciador.financeiro.patterns.decorator;

import com.gerenciador.financeiro.dto.DashboardResponse;
import com.gerenciador.financeiro.entity.TipoTransacao;
import com.gerenciador.financeiro.entity.Transacao;
import com.gerenciador.financeiro.patterns.singleton.ConfiguracaoDashboard;

import java.math.BigDecimal;
import java.util.List;

public class RelatorioFinanceiroBasico implements RelatorioFinanceiro {

    private final List<Transacao> transacoes;
    private final ConfiguracaoDashboard configuracao;

    public RelatorioFinanceiroBasico(
            List<Transacao> transacoes,
            ConfiguracaoDashboard configuracao
    ) {
        this.transacoes = transacoes;
        this.configuracao = configuracao;
    }

    @Override
    public DashboardResponse gerar() {

        BigDecimal totalReceitas = BigDecimal.ZERO;
        BigDecimal totalDespesas = BigDecimal.ZERO;

        for (Transacao transacao : transacoes) {

            if (transacao.getTipo() == TipoTransacao.RECEITA) {
                totalReceitas = totalReceitas.add(transacao.getValor());

            } else if (transacao.getTipo() == TipoTransacao.DESPESA) {
                totalDespesas = totalDespesas.add(transacao.getValor());
            }
        }

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new DashboardResponse(
                totalReceitas,
                totalDespesas,
                saldo,
                transacoes.size(),
                configuracao.getMoeda(),
                List.of(),
                List.of()
        );
    }
}