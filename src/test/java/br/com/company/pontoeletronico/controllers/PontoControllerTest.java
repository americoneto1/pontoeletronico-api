package br.com.company.pontoeletronico.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.company.pontoeletronico.models.MarcacaoModel;
import br.com.company.pontoeletronico.models.PontoModel;
import br.com.company.pontoeletronico.services.PontoService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PontoControllerTest {

  @MockBean
  private PontoService pontoService;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void shouldReturnAllAppointmentsByUser() {
    List<PontoModel> pontos = new ArrayList<>();
    pontos.add(new PontoModel(null, "00:00:00", new ArrayList<MarcacaoModel>()));

    when(pontoService.findAll((long) 1)).thenReturn(pontos);

    HttpHeaders headers = new HttpHeaders();
    headers.add("idUsuario", "1");
    HttpEntity<String> requestEntity = new HttpEntity<String>("{}", headers);

    ParameterizedTypeReference<List<PontoModel>> responseType = new ParameterizedTypeReference<List<PontoModel>>(){};
    ResponseEntity<List<PontoModel>> usuarioResponse = restTemplate.exchange("/ponto", HttpMethod.GET, requestEntity, responseType);

    assertEquals(usuarioResponse.getStatusCode(), HttpStatus.OK);
    assertEquals(usuarioResponse.getBody(), pontos);
  }

  @Test
  public void shouldRegisterAppointment() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("idUsuario", "1");
    HttpEntity<String> requestEntity = new HttpEntity<String>("{}", headers);

    ResponseEntity<String> usuarioResponse = restTemplate.exchange("/ponto", HttpMethod.POST, requestEntity, String.class);

    assertEquals(usuarioResponse.getStatusCode(), HttpStatus.NO_CONTENT);
  }

  @Test
  public void shouldNotRegisterAppointment() throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.add("idUsuario", "1");
    HttpEntity<String> requestEntity = new HttpEntity<String>("{}", headers);

    doThrow(Exception.class).when(pontoService).register((long) 1);

    ResponseEntity<String> usuarioResponse = restTemplate.exchange("/ponto", HttpMethod.POST, requestEntity, String.class);

    assertEquals(usuarioResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
  }

}