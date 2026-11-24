package com.gerenciador.financeiro.patterns.singleton;

public final class ConfiguracaoDashboard {

    private final String moeda = "BRL";
    private final int periodoGraficoMeses = 6;
    private final int limiteCategorias = 5;

    private ConfiguracaoDashboard() {
    }

    private static class InstanciaHolder {
        private static final ConfiguracaoDashboard INSTANCE =
                new ConfiguracaoDashboard();
    }

    public static ConfiguracaoDashboard getInstance() {
        return InstanciaHolder.INSTANCE;
    }

    public String getMoeda() {
        return moeda;
    }

    public int getPeriodoGraficoMeses() {
        return periodoGraficoMeses;
    }

    public int getLimiteCategorias() {
        return limiteCategorias;
    }
}

