package br.com.company.pontoeletronico.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.company.pontoeletronico.entities.Usuario;
import br.com.company.pontoeletronico.models.UsuarioModel;
import br.com.company.pontoeletronico.repository.UsuarioRepository;

@SpringBootTest
public class UsuarioServiceTest {

  @Mock
  private UsuarioRepository usuarioRepository;

  @InjectMocks
  private UsuarioService usuarioService;

  @Test
  public void shouldReturnAllUsers() {
    List<Usuario> usuarios = new ArrayList<>();
    usuarios.add(getMockedUserEntity());

    when(usuarioRepository.findAll()).thenReturn(usuarios);

    List<UsuarioModel> resultUsuariosModel = usuarioService.findAll();

    List<UsuarioModel> expectedUsuariosModel = new ArrayList<>();
    expectedUsuariosModel.add(getMockedUserModel());

    assertEquals(resultUsuariosModel, expectedUsuariosModel);
  }

  @Test
  public void shouldReturnUserById() throws Exception {
    Optional<Usuario> usuario = Optional.of(getMockedUserEntity());

    when(usuarioRepository.findById((long) 1)).thenReturn(usuario);

    UsuarioModel resultUsuarioModel = usuarioService.findById((long) 1);

    assertEquals(resultUsuarioModel, getMockedUserModel());
  }

  @Test
  public void shouldNotReturnAnUser() {
    when(usuarioRepository.findById((long) 1)).thenReturn(Optional.ofNullable(null));

    Exception exception = assertThrows(Exception.class, () -> {
      usuarioService.findById((long) 1);
    });

    assertEquals(exception.getMessage(), "Usuário não encontrado");
  }

  @Test
  public void shouldCreateAnUser() {
    Usuario usuario = getMockedUserEntity();
    UsuarioModel usuarioModel = getMockedUserModel();

    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

    UsuarioModel resultUsuarioModel = usuarioService.create(usuarioModel);

    assertEquals(resultUsuarioModel, usuarioModel);
  }

  @Test
  public void shouldUpdateAnUser() throws Exception {
    Usuario usuario = getMockedUserEntity();
    UsuarioModel usuarioModel = getMockedUserModel();

    when(usuarioRepository.findById((long) 1)).thenReturn(Optional.of(usuario));
    when(usuarioRepository.save(usuario)).thenReturn(usuario);

    UsuarioModel resultUsuarioModel = usuarioService.update((long) 1, usuarioModel);

    assertEquals(resultUsuarioModel, usuarioModel);
  }

  @Test
  public void shouldNotUpdateAnUser() throws Exception {
    UsuarioModel usuarioModel = getMockedUserModel();

    when(usuarioRepository.findById((long) 1)).thenReturn(Optional.ofNullable(null));

    Exception exception = assertThrows(Exception.class, () -> {
      usuarioService.update((long) 1, usuarioModel);
    });

    assertEquals(exception.getMessage(), "Usuário não encontrado");
  }

  private Usuario getMockedUserEntity() {
    return new Usuario((long) 1, "Américo Neto", "19100000000", "email@email.com", null);
  }

  private UsuarioModel getMockedUserModel() {
    return new UsuarioModel((long) 1, "Américo Neto", "19100000000", "email@email.com", null);
  }
}