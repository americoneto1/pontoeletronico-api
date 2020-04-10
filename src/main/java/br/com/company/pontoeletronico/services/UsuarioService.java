package br.com.company.pontoeletronico.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.company.pontoeletronico.entities.Usuario;
import br.com.company.pontoeletronico.models.UsuarioModel;
import br.com.company.pontoeletronico.repository.UsuarioRepository;

@Service
public class UsuarioService {
  
  @Autowired
  UsuarioRepository usuarioRepository;

  public List<UsuarioModel> findAll() {
    List<Usuario> usuarios = usuarioRepository.findAll();
    return usuarios.stream()
      .map(item -> getModel(item)).collect(Collectors.toList());
  }

  public UsuarioModel findById(long id) throws Exception {
    Optional<Usuario> usuario = usuarioRepository.findById(id);
    if (usuario.isPresent()) {
      return getModel(usuario.get());
    } else {
      throw new Exception("Usuário não encontrado");
    }
  }

  public UsuarioModel create(@Valid UsuarioModel usuario) {
    Usuario newUsuario = usuarioRepository.save(getEntity(usuario));
    return getModel(newUsuario);
  }

  public UsuarioModel update(long id, @Valid UsuarioModel newUsuario) throws Exception {
    Optional<Usuario> oldUsuario = usuarioRepository.findById(id);
    if (oldUsuario.isPresent()) {
      Usuario usuario = oldUsuario.get();
      usuario.setNome(newUsuario.getNome());
      usuario.setCpf(newUsuario.getCpf());
      usuario.setEmail(newUsuario.getEmail());
      return getModel(usuarioRepository.save(usuario));
    }
    else {
      throw new Exception("Usuário não encontrado");
    }
  }

  private UsuarioModel getModel(Usuario item) {
    UsuarioModel usuario = new UsuarioModel();
    usuario.setId(item.getId());
    usuario.setNome(item.getNome());
    usuario.setEmail(item.getEmail());
    usuario.setCpf(item.getCpf());
    usuario.setDataCadastro(item.getDataCadastro());
    return usuario;
  }

  private Usuario getEntity(UsuarioModel item) {
    Usuario usuario = new Usuario();
    usuario.setId(item.getId());
    usuario.setNome(item.getNome());
    usuario.setEmail(item.getEmail());
    usuario.setCpf(item.getCpf());
    return usuario;
  }
}