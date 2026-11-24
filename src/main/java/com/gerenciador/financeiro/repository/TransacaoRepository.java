package com.gerenciador.financeiro.repository;

import com.gerenciador.financeiro.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findAllByOrderByDataDescIdDesc();
}
