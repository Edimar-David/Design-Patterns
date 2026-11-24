package com.gerenciador.financeiro.exception;

import java.time.Instant;
import java.util.Map;

public record ApiErro(
        Instant timestamp,
        int status,
        String mensagem,
        Map<String, String> detalhes
) {
}
