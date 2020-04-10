package br.com.company.pontoeletronico.models;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class UsuarioModel {

  @JsonProperty(value = "id")
  private Long id;

  @NotNull
  @JsonProperty(value = "nome")
  private String nome;

  @NotNull
  @JsonProperty(value = "cpf")
  private String cpf;

  @Email 
  @JsonProperty(value = "email")
  private String email;

  @JsonProperty(value = "dataCadastro")
  private Date dataCadastro;
}