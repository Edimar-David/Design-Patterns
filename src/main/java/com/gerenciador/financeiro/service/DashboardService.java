
package com.gerenciador.financeiro.service;

import com.gerenciador.financeiro.dto.DashboardResponse;
import com.gerenciador.financeiro.entity.Transacao;
import com.gerenciador.financeiro.patterns.decorator.DespesasPorCategoriaDecorator;
import com.gerenciador.financeiro.patterns.decorator.EvolucaoMensalDecorator;
import com.gerenciador.financeiro.patterns.decorator.RelatorioFinanceiro;
import com.gerenciador.financeiro.patterns.decorator.RelatorioFinanceiroBasico;
import com.gerenciador.financeiro.patterns.singleton.ConfiguracaoDashboard;
import com.gerenciador.financeiro.patterns.strategy.PeriodoDashboard;
import com.gerenciador.financeiro.patterns.strategy.PeriodoDashboardStrategy;
import com.gerenciador.financeiro.patterns.strategy.PeriodoDashboardStrategyFactory;
import com.gerenciador.financeiro.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Service
public class DashboardService {

    private final TransacaoRepository repository;
    private final PeriodoDashboardStrategyFactory strategyFactory;

    private final ConfiguracaoDashboard configuracao =
            ConfiguracaoDashboard.getInstance();

    public DashboardService(
            TransacaoRepository repository,
            PeriodoDashboardStrategyFactory strategyFactory
    ) {
        this.repository = repository;
        this.strategyFactory = strategyFactory;
    }

    @Transactional(readOnly = true)
    public DashboardResponse obterDashboard(PeriodoDashboard periodo) {

        List<Transacao> transacoes = repository.findAll();

        PeriodoDashboardStrategy estrategia =
                strategyFactory.obterEstrategia(periodo);

        YearMonth mesAtual = YearMonth.now();

        YearMonth inicioPeriodo =
                estrategia.obterInicio(mesAtual);

        YearMonth fimPeriodo = mesAtual;

        RelatorioFinanceiro relatorio =
                new RelatorioFinanceiroBasico(transacoes, configuracao);

        relatorio = new EvolucaoMensalDecorator(
                relatorio,
                transacoes,
                inicioPeriodo,
                fimPeriodo
        );

        relatorio = new DespesasPorCategoriaDecorator(
                relatorio,
                transacoes,
                configuracao,
                inicioPeriodo,
                fimPeriodo
        );

        return relatorio.gerar();
    }
}
