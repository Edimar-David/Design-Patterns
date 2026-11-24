package com.gerenciador.financeiro.service;

import com.gerenciador.financeiro.dto.TransacaoRequest;
import com.gerenciador.financeiro.dto.TransacaoResponse;
import com.gerenciador.financeiro.entity.Transacao;
import com.gerenciador.financeiro.exception.RecursoNaoEncontradoException;
import com.gerenciador.financeiro.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransacaoService {

    private static final String CATEGORIA_PADRAO = "Sem categoria";

    private final TransacaoRepository repository;

    public TransacaoService(TransacaoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponse> listar() {
        return repository.findAllByOrderByDataDescIdDesc().stream()
                .map(TransacaoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TransacaoResponse buscarPorId(Long id) {
        return TransacaoResponse.from(obterEntidade(id));
    }

    @Transactional
    public TransacaoResponse criar(TransacaoRequest request) {
        Transacao transacao = new Transacao();
        aplicarDados(transacao, request);
        return TransacaoResponse.from(repository.save(transacao));
    }

    @Transactional
    public TransacaoResponse atualizar(Long id, TransacaoRequest request) {
        Transacao transacao = obterEntidade(id);
        aplicarDados(transacao, request);
        return TransacaoResponse.from(repository.save(transacao));
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(obterEntidade(id));
    }

    private Transacao obterEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Transação " + id + " não encontrada."));
    }

    /** Regras de normalização pertencem ao backend, não à interface. */
    private void aplicarDados(Transacao transacao, TransacaoRequest request) {
        transacao.setDescricao(request.descricao().trim());
        transacao.setValor(request.valor());
        transacao.setTipo(request.tipo());
        transacao.setData(request.data());
        transacao.setCategoria(normalizarCategoria(request.categoria()));
    }

    private String normalizarCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            return CATEGORIA_PADRAO;
        }
        return categoria.trim();
    }
}
