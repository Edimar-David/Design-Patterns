package com.gerenciador.financeiro.patterns.singleton;

public class TesteSingleton {

    public static void main(String[] args) {

        ConfiguracaoDashboard config1 =
                ConfiguracaoDashboard.getInstance();

        ConfiguracaoDashboard config2 =
                ConfiguracaoDashboard.getInstance();

        System.out.println(config1 == config2);

        System.out.println(config1.getMoeda());
        System.out.println(config1.getPeriodoGraficoMeses());
        System.out.println(config1.getLimiteCategorias());
    }
}
