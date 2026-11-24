package com.gerenciador.financeiro.dto;

import com.gerenciador.financeiro.entity.TipoTransacao;
import com.gerenciador.financeiro.entity.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        TipoTransacao tipo,
        LocalDate data,
        String categoria
) {

    public static TransacaoResponse from(Transacao transacao) {
        return new TransacaoResponse(
                transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getTipo(),
                transacao.getData(),
                transacao.getCategoria()
        );
    }
}
