
package com.gerenciador.financeiro.patterns.decorator;

import com.gerenciador.financeiro.dto.DashboardResponse;
import com.gerenciador.financeiro.entity.TipoTransacao;
import com.gerenciador.financeiro.entity.Transacao;
import com.gerenciador.financeiro.patterns.singleton.ConfiguracaoDashboard;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DespesasPorCategoriaDecorator extends RelatorioDecorator {

    private final List<Transacao> transacoes;
    private final ConfiguracaoDashboard configuracao;
    private final YearMonth inicioPeriodo;
    private final YearMonth fimPeriodo;

    public DespesasPorCategoriaDecorator(
            RelatorioFinanceiro relatorio,
            List<Transacao> transacoes,
            ConfiguracaoDashboard configuracao,
            YearMonth inicioPeriodo,
            YearMonth fimPeriodo
    ) {
        super(relatorio);
        this.transacoes = transacoes;
        this.configuracao = configuracao;
        this.inicioPeriodo = inicioPeriodo;
        this.fimPeriodo = fimPeriodo;
    }

    @Override
    public DashboardResponse gerar() {

        DashboardResponse base = super.gerar();

        int limite = configuracao.getLimiteCategorias();

        Map<String, BigDecimal> categorias = new HashMap<>();

        for (Transacao transacao : transacoes) {

            if (transacao.getTipo() != TipoTransacao.DESPESA) {
                continue;
            }

            YearMonth mes = YearMonth.from(transacao.getData());

            if (mes.isBefore(inicioPeriodo) || mes.isAfter(fimPeriodo)) {
                continue;
            }

            categorias.merge(
                    transacao.getCategoria(),
                    transacao.getValor(),
                    BigDecimal::add
            );
        }

        List<DashboardResponse.DespesaPorCategoria> despesasPorCategoria =
                categorias.entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, BigDecimal>comparingByValue()
                                .reversed())
                        .limit(limite)
                        .map(entry ->
                                new DashboardResponse.DespesaPorCategoria(
                                        entry.getKey(),
                                        entry.getValue()
                                )
                        )
                        .toList();

        return new DashboardResponse(
                base.totalReceitas(),
                base.totalDespesas(),
                base.saldo(),
                base.quantidadeTransacoes(),
                base.moeda(),
                base.evolucaoMensal(),
                despesasPorCategoria
        );
    }
}
