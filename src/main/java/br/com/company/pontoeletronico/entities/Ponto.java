package br.com.company.pontoeletronico.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.company.pontoeletronico.models.TipoBatida;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor @AllArgsConstructor
public class Ponto {

  @Id @GeneratedValue 
  private Long id;

  @Temporal(TemporalType.DATE)
  @Getter
  @OrderBy
  Date dataBatida;

  @Temporal(TemporalType.TIME)
  @Getter
  @OrderBy
  Date horaBatida;

  @Getter @Setter 
  @Enumerated(EnumType.STRING) 
  private TipoBatida tipoBatida;

  @ManyToOne 
  @Getter @Setter 
  private Usuario usuario;

  @PrePersist
  protected void onCreate() {
    dataBatida = new Date();
    horaBatida = new Date();
  }
}