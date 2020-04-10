package br.com.company.pontoeletronico.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.company.pontoeletronico.models.PontoModel;
import br.com.company.pontoeletronico.services.PontoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/ponto")
public class PontoController {
  @Autowired
  PontoService pontoService;

  @GetMapping()
  public ResponseEntity<List<PontoModel>> findAll(@RequestHeader Long idUsuario) {
    List<PontoModel> pontos = pontoService.findAll(idUsuario);
    return ResponseEntity.ok().body(pontos);
  }

  @PostMapping()
  public ResponseEntity<String> create(@RequestHeader Long idUsuario) {
    try {
      pontoService.register(idUsuario);
      return ResponseEntity.noContent().build();
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }    
  }
}