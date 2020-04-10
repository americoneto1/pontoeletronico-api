# API de Ponto Eletrônico

Esse repositório contém um projeto de estudo exemplicando um API construída em SpringBoot com Maven.

## Iniciando o servidor local

Para rodar a aplicação localmente, execute o seguinte comando no terminal:

```
mvn spring-boot:run
```

## Executando os testes unitários

Para executar os testes unitários do projeto, execute o seguinte comando no terminal:

```
mvn clean test
```

## Configuração de banco de dados

As configurações de banco de dados, como host, usuário e senha estão no arquivo `application.properties`

```
# Datasource
spring.datasource.url=jdbc:mysql://localhost:3306/pontoeletronico
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

## Documentação dos endpoints da API

### Gestão de usuários

#### GET - /usuario

Lista de todos os usuários cadastradas na base de dados

```
[
  {
    "id": 1,
    "nome": "João Batista",
    "cpf": "19100000000",
    "email": "joao.batista@company.com",
    "dataCadastro": "2020-04-10T21:31:43.000+0000"
  },
  {
    "id": 2,
    "nome": "Mateus Augusto",
    "cpf": "12345678909",
    "email": "mateus.augusto@company.com",
    "dataCadastro": "2020-04-10T10:31:43.000+0000"
  }
]
```

#### GET - /usuario/{id}

Detalhes do usuário com o identificador passado como parâmetro

```
{
  "id": 1,
  "nome": "João Batista",
  "cpf": "19100000000",
  "email": "joao.batista@company.com",
  "dataCadastro": "2020-04-10T21:31:43.000+0000"
}
```

#### POST - /usuario

Criação de um novo usuário com os dados passados no corpo da requisição

```
{
  "nome": "Pedro Lima",
  "cpf": "12345678989",
  "email": "pedro.lima@company.com"
}
```

#### PUT - /usuario/{id}

Atualização do cadastro do usuário com o identificador passado como parâmetro

```
/usuario/3
{
  "nome": "Pedro Lima Pereira",
  "cpf": "12345678989",
  "email": "pedro.lima@company.com"
}
```

### Registro de ponto

#### GET - /ponto

Listagem de todas as marcações de ponto de um determinado usuário, juntamente com a quantidade de horas trabalhadas por dia

*O identificador do usuário é obtido atráves do Header da requisição (idUsuario)*

*Para marcações ímpares de ponto, a quantidade de horas trabalhadas é calculada até o final do dia corrente. Sendo que para dias anteriores o cálculo é feito até o final do dia (23h59m59s)*

```
[
  {
    "data": "10/04/2020",
    "horasTrabalhadas": "08:00",
    "marcacoes": [
      {
        "hora": "09:00:00",
        "tipo": "ENTRADA"
      },
      {
        "hora": "12:00:00",
        "tipo": "SAIDA"
      },
      {
        "hora": "13:00:00",
        "tipo": "ENTRADA"
      },
      {
        "hora": "18:00:00",
        "tipo": "SAIDA"
      }
    ]
  }
]
```

#### POST - /ponto

Registro de batida de ponto de um determinado usuário (Entrada ou Saída)

*O identificador do usuário é obtido atráves do Header da requisição (idUsuario)*