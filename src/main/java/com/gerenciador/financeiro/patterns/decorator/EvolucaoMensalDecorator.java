
package com.gerenciador.financeiro.patterns.decorator;

import com.gerenciador.financeiro.dto.DashboardResponse;
import com.gerenciador.financeiro.entity.TipoTransacao;
import com.gerenciador.financeiro.entity.Transacao;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EvolucaoMensalDecorator extends RelatorioDecorator {

    private final List<Transacao> transacoes;
    private final YearMonth inicioPeriodo;
    private final YearMonth fimPeriodo;

    public EvolucaoMensalDecorator(
            RelatorioFinanceiro relatorio,
            List<Transacao> transacoes,
            YearMonth inicioPeriodo,
            YearMonth fimPeriodo
    ) {
        super(relatorio);
        this.transacoes = transacoes;
        this.inicioPeriodo = inicioPeriodo;
        this.fimPeriodo = fimPeriodo;
    }

    @Override
    public DashboardResponse gerar() {

        DashboardResponse base = super.gerar();

        Map<YearMonth, ResumoMensal> resumo = new LinkedHashMap<>();

        for (
                YearMonth mes = inicioPeriodo;
                !mes.isAfter(fimPeriodo);
                mes = mes.plusMonths(1)
        ) {
            resumo.put(mes, new ResumoMensal());
        }

        for (Transacao transacao : transacoes) {

            YearMonth mes = YearMonth.from(transacao.getData());

            ResumoMensal dados = resumo.get(mes);

            if (dados == null) {
                continue;
            }

            if (transacao.getTipo() == TipoTransacao.RECEITA) {
                dados.receitas = dados.receitas.add(transacao.getValor());

            } else if (transacao.getTipo() == TipoTransacao.DESPESA) {
                dados.despesas = dados.despesas.add(transacao.getValor());
            }
        }

        List<DashboardResponse.EvolucaoMensal> evolucao =
                new ArrayList<>();

        resumo.forEach((mes, dados) ->
                evolucao.add(
                        new DashboardResponse.EvolucaoMensal(
                                mes.toString(),
                                dados.receitas,
                                dados.despesas
                        )
                )
        );

        return new DashboardResponse(
                base.totalReceitas(),
                base.totalDespesas(),
                base.saldo(),
                base.quantidadeTransacoes(),
                base.moeda(),
                evolucao,
                base.despesasPorCategoria()
        );
    }

    private static class ResumoMensal {

        private BigDecimal receitas = BigDecimal.ZERO;
        private BigDecimal despesas = BigDecimal.ZERO;
    }
}
