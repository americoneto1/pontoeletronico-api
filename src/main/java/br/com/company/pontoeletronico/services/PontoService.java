package br.com.company.pontoeletronico.services;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.company.pontoeletronico.entities.Ponto;
import br.com.company.pontoeletronico.entities.Usuario;
import br.com.company.pontoeletronico.models.MarcacaoModel;
import br.com.company.pontoeletronico.models.PontoModel;
import br.com.company.pontoeletronico.models.TipoBatida;
import br.com.company.pontoeletronico.repository.PontoRepository;
import br.com.company.pontoeletronico.repository.UsuarioRepository;

@Service
public class PontoService {
  @Autowired
  PontoRepository pontoRepository;

  @Autowired
  UsuarioRepository usuarioRepository;

  public List<PontoModel> findAll(Long idUsuario) {
    Map<Date, List<Ponto>> resultMap = pontoRepository.findByUsuarioId(idUsuario).stream()
        .collect(Collectors.groupingBy(Ponto::getDataBatida));

    List<PontoModel> result = new ArrayList<PontoModel>();

    for (Map.Entry<Date, List<Ponto>> entry : resultMap.entrySet()) {
      PontoModel ponto = new PontoModel();
      ponto.setData(entry.getKey());
      ponto.setMarcacoes(entry.getValue().stream().map(item -> {
        MarcacaoModel marcacao = new MarcacaoModel();
        marcacao.setHora(item.getHoraBatida());
        marcacao.setTipoBatida(item.getTipoBatida());
        return marcacao;
      }).collect(Collectors.toList()));

      String horasTrabalhadas = getHorasTrabalhadas(ponto.getData(), ponto.getMarcacoes());
      ponto.setHorasTrabalhadas(horasTrabalhadas);

      result.add(ponto);
    }

    return result;
  }

  private String getHorasTrabalhadas(Date data, List<MarcacaoModel> marcacoes) {
    long segundosTrabalhados = 0;

    for (int i = 0; i < marcacoes.size(); i++) {
      MarcacaoModel item = marcacoes.get(i);
      if (item.getTipoBatida().equals(TipoBatida.ENTRADA)) {
        try {
          MarcacaoModel marcacaoSaida = marcacoes.get(i + 1);
          if (marcacaoSaida.getTipoBatida().equals(TipoBatida.SAIDA)) {
            long duration = marcacaoSaida.getHora().getTime() - item.getHora().getTime();
            segundosTrabalhados += TimeUnit.MILLISECONDS.toSeconds(duration);
          }
        } catch (IndexOutOfBoundsException ex) {
          // Cenário de marcação de Entrada sem Saída
          long duration = getEndDate(data).getTime() - item.getHora().getTime();
          segundosTrabalhados += TimeUnit.MILLISECONDS.toSeconds(duration);
        }
      }
    }

    return LocalTime.ofSecondOfDay(segundosTrabalhados).toString();
  }

  private Date getEndDate(Date data) {
    try {
      Calendar calendar = Calendar.getInstance();

      if (!isToday(data)) {
        return new SimpleDateFormat("HH:mm:ss").parse("23:59:59");
      } else {
        return new SimpleDateFormat("HH:mm:ss").parse(
          calendar.get(Calendar.HOUR_OF_DAY) + ":" + 
          calendar.get(Calendar.MINUTE) + ":" + 
          calendar.get(Calendar.SECOND)
        );
      }
    } catch (ParseException e) {
      return new Date();
    }
  }

  private Boolean isToday(Date date) {
    Date paramDate = getZeroTimeDate(date);
    Date actualDate = getZeroTimeDate(new Date());
    return paramDate.equals(actualDate);
  }

  private Date getZeroTimeDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }

  public void register(Long idUsuario) throws Exception {
    Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
    if (!usuario.isPresent()) {
      throw new Exception("Usuário não encontrado");
    }
    Ponto ponto = new Ponto();
    ponto.setUsuario(usuario.get());

    List<Ponto> marcacoes = pontoRepository.findByDataBatida(new Date());

    if (marcacoes.size() == 0) {      
      ponto.setTipoBatida(TipoBatida.ENTRADA);
    } else {
      if (marcacoes.get(marcacoes.size() - 1).getTipoBatida().equals(TipoBatida.ENTRADA)) {
        ponto.setTipoBatida(TipoBatida.SAIDA);
      } else {
        ponto.setTipoBatida(TipoBatida.ENTRADA);
      }
    }
    pontoRepository.save(ponto);
  }
}