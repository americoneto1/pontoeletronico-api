package br.com.company.pontoeletronico.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.company.pontoeletronico.entities.Ponto;

@Repository
public interface PontoRepository extends JpaRepository<Ponto, Long> {

  List<Ponto> findByDataBatida(Date dataBatida);
  List<Ponto> findByUsuarioId(Long id);
}