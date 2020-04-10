package br.com.company.pontoeletronico.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.company.pontoeletronico.models.UsuarioModel;
import br.com.company.pontoeletronico.services.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsuarioControllerTest {

  @MockBean
  private UsuarioService usuarioService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void shouldReturnAllUsers() {
    List<UsuarioModel> usuarios = new ArrayList<>();
    usuarios.add(getMockedUser());

    when(usuarioService.findAll()).thenReturn(usuarios);

    ParameterizedTypeReference<List<UsuarioModel>> responseType = new ParameterizedTypeReference<List<UsuarioModel>>(){};
    ResponseEntity<List<UsuarioModel>> usuarioResponse = restTemplate.exchange("/usuario", HttpMethod.GET, null, responseType);

    assertEquals(usuarioResponse.getStatusCode(), HttpStatus.OK);
    assertEquals(usuarioResponse.getBody(), usuarios);
  }

  @Test
  public void shouldReturnAnUser() throws Exception {
    UsuarioModel usuario = getMockedUser();

    when(usuarioService.findById(1)).thenReturn(usuario);

    ResponseEntity<UsuarioModel> usuarioResponse = restTemplate.getForEntity("/usuario/1", UsuarioModel.class);

    assertEquals(usuarioResponse.getStatusCode(), HttpStatus.OK);
    assertEquals(usuarioResponse.getBody(), usuario);
  }

  @Test
  public void shouldNotReturnAnUser() throws Exception {
    when(usuarioService.findById(1)).thenThrow(new Exception("Usuário não encontrado"));

    ResponseEntity<UsuarioModel> usuarioResponse = restTemplate.getForEntity("/usuario/1", UsuarioModel.class);

    assertEquals(usuarioResponse.getStatusCode(), HttpStatus.NOT_FOUND);
  }

  @Test
  public void shouldCreateAnUser() {
    UsuarioModel usuario = getMockedUser();

    when(usuarioService.create(usuario)).thenReturn(usuario);

    ResponseEntity<UsuarioModel> usuarioResponse = restTemplate.postForEntity("/usuario", usuario, UsuarioModel.class);

    assertEquals(usuarioResponse.getStatusCode(), HttpStatus.OK);
  }

  @Test
  public void shouldUpdateAnUser() throws Exception {
    UsuarioModel usuario = getMockedUser();

    HttpEntity<UsuarioModel> requestEntity = new HttpEntity<UsuarioModel>(usuario);

    when(usuarioService.update((long) 1, usuario)).thenReturn(usuario);

    ResponseEntity<UsuarioModel> usuarioResponse = restTemplate.exchange("/usuario/1", HttpMethod.PUT, requestEntity, UsuarioModel.class);

    assertEquals(usuarioResponse.getStatusCode(), HttpStatus.OK);
  }

  @Test
  public void shouldNotUpdateAnUser() throws Exception {
    UsuarioModel usuario = getMockedUser();

    HttpEntity<UsuarioModel> requestEntity = new HttpEntity<UsuarioModel>(usuario);

    when(usuarioService.update((long) 1, usuario)).thenThrow(new Exception("Usuário não encontrado"));

    ResponseEntity<UsuarioModel> usuarioResponse = restTemplate.exchange("/usuario/1", HttpMethod.PUT, requestEntity, UsuarioModel.class);

    assertEquals(usuarioResponse.getStatusCode(), HttpStatus.NOT_FOUND);
  }

  private UsuarioModel getMockedUser() {
    return new UsuarioModel((long) 1, "Américo Neto", "19100000000", "email@email.com", Calendar.getInstance().getTime());
  }

}