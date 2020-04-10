package br.com.company.pontoeletronico.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.company.pontoeletronico.entities.Ponto;
import br.com.company.pontoeletronico.entities.Usuario;
import br.com.company.pontoeletronico.models.MarcacaoModel;
import br.com.company.pontoeletronico.models.PontoModel;
import br.com.company.pontoeletronico.models.TipoBatida;
import br.com.company.pontoeletronico.repository.PontoRepository;
import br.com.company.pontoeletronico.repository.UsuarioRepository;

@SpringBootTest
public class PontoServiceTest {

  @Mock
  private PontoRepository pontoRepository;

  @Mock
  private UsuarioRepository usuarioRepository;

  @InjectMocks
  private PontoService pontoService;

  @Test
  public void shouldReturnAllAppointmentsByUser() {
    Usuario usuario = new Usuario();
    usuario.setId((long) 1);

    List<Ponto> ponto = new ArrayList<>();
    ponto.add(new Ponto((long) 1, getDate("2020-04-01"), getTime("09:00:00"), TipoBatida.ENTRADA, usuario));
    ponto.add(new Ponto((long) 2, getDate("2020-04-01"), getTime("12:00:00"), TipoBatida.SAIDA, usuario));
    ponto.add(new Ponto((long) 3, getDate("2020-04-01"), getTime("13:00:00"), TipoBatida.ENTRADA, usuario));
    ponto.add(new Ponto((long) 4, getDate("2020-04-01"), getTime("18:00:00"), TipoBatida.SAIDA, usuario));

    when(pontoRepository.findByUsuarioId((long) 1)).thenReturn(ponto);

    List<PontoModel> pontoModelList = pontoService.findAll((long) 1);

    List<PontoModel> expectedPontoModelList = new ArrayList<>();
    List<MarcacaoModel> marcacoes = new ArrayList<>();
    marcacoes.add(new MarcacaoModel(getTime("09:00:00"), TipoBatida.ENTRADA));
    marcacoes.add(new MarcacaoModel(getTime("12:00:00"), TipoBatida.SAIDA));
    marcacoes.add(new MarcacaoModel(getTime("13:00:00"), TipoBatida.ENTRADA));
    marcacoes.add(new MarcacaoModel(getTime("18:00:00"), TipoBatida.SAIDA));

    expectedPontoModelList.add(new PontoModel(getDate("2020-04-01"), "08:00", marcacoes));

    assertEquals(expectedPontoModelList, pontoModelList);
  }

  @Test
  public void shouldReturnAllAppointmentsOddByUser() {
    Usuario usuario = new Usuario();
    usuario.setId((long) 1);

    List<Ponto> ponto = new ArrayList<>();
    ponto.add(new Ponto((long) 1, getDate("2020-04-01"), getTime("09:00:00"), TipoBatida.ENTRADA, usuario));

    when(pontoRepository.findByUsuarioId((long) 1)).thenReturn(ponto);

    List<PontoModel> pontoModelList = pontoService.findAll((long) 1);

    List<PontoModel> expectedPontoModelList = new ArrayList<>();
    List<MarcacaoModel> marcacoes = new ArrayList<>();
    marcacoes.add(new MarcacaoModel(getTime("09:00:00"), TipoBatida.ENTRADA));

    expectedPontoModelList.add(new PontoModel(getDate("2020-04-01"), "14:59:59", marcacoes));

    assertEquals(expectedPontoModelList, pontoModelList);
  }

  @Test
  public void shouldRegisterEntryAppointment() throws Exception {
    Optional<Usuario> usuario = Optional.of(getMockedUserEntity());
    Ponto ponto = new Ponto();
    ponto.setUsuario(usuario.get());
    ponto.setTipoBatida(TipoBatida.ENTRADA);

    when(usuarioRepository.findById((long) 1)).thenReturn(usuario);
    when(pontoRepository.findByDataBatida(getDate("2020-04-01"))).thenReturn(new ArrayList<>());

    assertDoesNotThrow(() -> pontoService.register((long) 1));
  }

  @Test
  public void shouldRegisterExitAppointment() throws Exception {
    Optional<Usuario> usuario = Optional.of(getMockedUserEntity());
    Ponto ponto = new Ponto();
    ponto.setUsuario(usuario.get());
    ponto.setTipoBatida(TipoBatida.ENTRADA);

    List<Ponto> pontos = new ArrayList<>();
    pontos.add(new Ponto((long) 1, getDate("2020-04-01"), getTime("09:00:00"), TipoBatida.ENTRADA, usuario.get()));

    when(usuarioRepository.findById((long) 1)).thenReturn(usuario);
    when(pontoRepository.findByDataBatida(getDate("2020-04-01"))).thenReturn(pontos);

    assertDoesNotThrow(() -> pontoService.register((long) 1));
  }

  @Test
  public void shouldRegisterAnotherEntryAppointment() throws Exception {
    Optional<Usuario> usuario = Optional.of(getMockedUserEntity());
    Ponto ponto = new Ponto();
    ponto.setUsuario(usuario.get());
    ponto.setTipoBatida(TipoBatida.ENTRADA);

    List<Ponto> pontos = new ArrayList<>();
    pontos.add(new Ponto((long) 1, getDate("2020-04-01"), getTime("09:00:00"), TipoBatida.ENTRADA, usuario.get()));
    pontos.add(new Ponto((long) 1, getDate("2020-04-01"), getTime("12:00:00"), TipoBatida.SAIDA, usuario.get()));

    when(usuarioRepository.findById((long) 1)).thenReturn(usuario);
    when(pontoRepository.findByDataBatida(getDate("2020-04-01"))).thenReturn(pontos);

    assertDoesNotThrow(() -> pontoService.register((long) 1));
  }

  @Test
  public void shouldNotRegisterAnAppointment() throws Exception {
    when(usuarioRepository.findById((long) 1)).thenReturn(Optional.ofNullable(null));
    
    Exception exception = assertThrows(Exception.class, () -> {
      pontoService.register((long) 1);
    });

    assertEquals(exception.getMessage(), "Usuário não encontrado");
  }

  private Date getDate(String date) {
    try {
      return new SimpleDateFormat("yyyyy-MM-dd").parse(date);
    } catch (ParseException e) {
      return new Date();
    }
  }

  private Date getTime(String date) {
    try {
      return new SimpleDateFormat("HH:mm:ss").parse(date);
    } catch (ParseException e) {
      return new Date();
    }
  }

  private Usuario getMockedUserEntity() {
    return new Usuario((long) 1, "Américo Neto", "19100000000", "email@email.com", null);
  }

}