package br.com.company.pontoeletronico.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.company.pontoeletronico.models.UsuarioModel;
import br.com.company.pontoeletronico.services.UsuarioService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController()
@RequestMapping("/usuario")
public class UsuarioController {

  @Autowired
  UsuarioService usuarioService;

  @GetMapping()
  public ResponseEntity<List<UsuarioModel>> findAll() {
    List<UsuarioModel> usuarios = usuarioService.findAll();
    return ResponseEntity.ok().body(usuarios);
  }

  @GetMapping(path = {"{id}"})
  public ResponseEntity<UsuarioModel> findById(@PathVariable("id") long id) {
    try {
      return ResponseEntity.ok(usuarioService.findById(id));
    } catch(Exception ex) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping()
  public ResponseEntity<UsuarioModel> create(@RequestBody UsuarioModel usuario) {
    UsuarioModel newUsuario = usuarioService.create(usuario);
    return ResponseEntity.ok().body(newUsuario);
  }

  @PutMapping(path = {"{id}"})
  public ResponseEntity<UsuarioModel> update(@PathVariable(value = "id") long id, @RequestBody UsuarioModel newUsuario) {
    try {
      return ResponseEntity.ok().body(usuarioService.update(id, newUsuario));
    } catch(Exception ex) {
      return ResponseEntity.notFound().build();
    }
  }
}