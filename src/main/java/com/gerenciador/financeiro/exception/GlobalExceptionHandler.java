package com.gerenciador.financeiro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiErro> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        return resposta(HttpStatus.NOT_FOUND, ex.getMessage(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErro> tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> detalhes = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(erro -> detalhes.putIfAbsent(erro.getField(), erro.getDefaultMessage()));
        return resposta(HttpStatus.BAD_REQUEST, "Dados inválidos.", detalhes);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErro> tratarCorpoIlegivel(HttpMessageNotReadableException ex) {
        return resposta(HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido. Verifique os formatos de tipo, valor e data.", Map.of());
    }

    private ResponseEntity<ApiErro> resposta(HttpStatus status, String mensagem, Map<String, String> detalhes) {
        return ResponseEntity.status(status)
                .body(new ApiErro(Instant.now(), status.value(), mensagem, detalhes));
    }
}
