package com.gerenciador.financeiro.dto;

import com.gerenciador.financeiro.entity.TipoTransacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequest(
        @NotBlank(message = "Informe a descrição.")
        @Size(max = 150, message = "A descrição pode ter no máximo 150 caracteres.")
        String descricao,

        @NotNull(message = "Informe o valor.")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
        @Digits(integer = 13, fraction = 2, message = "Use no máximo 2 casas decimais.")
        BigDecimal valor,

        @NotNull(message = "Informe se é receita ou despesa.")
        TipoTransacao tipo,

        @NotNull(message = "Informe a data.")
        LocalDate data,

        @Size(max = 60, message = "A categoria pode ter no máximo 60 caracteres.")
        String categoria
) {
}
