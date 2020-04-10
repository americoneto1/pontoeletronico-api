package br.com.company.pontoeletronico.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor @AllArgsConstructor
public class Usuario {

  @Id @GeneratedValue 
  @Getter @Setter
  private Long id;

  @Getter @Setter
  private String nome;

  @Getter @Setter 
  private String cpf;

  @Getter @Setter
  private String email;

  @Getter 
  private Date dataCadastro;

  @PrePersist
  protected void onCreate() {
    dataCadastro = new Date();
  }
}