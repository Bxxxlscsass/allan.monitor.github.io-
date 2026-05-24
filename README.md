#  Cofrinho Digital com Conversão de Moedas (POO)

Este projeto é um sistema de TI focado em finanças que simula um cofrinho internacional de moedas. Ele foi desenvolvido aplicando os pilares fundamentais da **Programação Orientada a Objetos (POO)** no backend em Java, totalmente integrado a uma interface de usuário dinâmica no frontend via chamadas de API assíncronas.

O sistema permite a inserção e remoção de diferentes tipos de moedas, realizando a conversão automática dos valores para Real (BRL) com base em cotações atualizadas.

## Interface do Sistema (Monitor de Console)
O frontend foi projetado imitando um terminal de gerenciamento de TI:

![Painel do Cofrinho Executando](prints/cofrinho_funcionando.png)
*(Dica: Mova o print da sua pasta 'printapp' para uma pasta chamada 'prints' dentro do projeto para exibi-lo aqui)*

##  Arquitetura de Software & Conceitos de POO Aplicados

O projeto foi construído para demonstrar o uso prático de boas práticas de desenvolvimento:

- **Classe Abstrata (`Moeda`):** Define o molde genérico obrigatório para todas as moedas do sistema, contendo o atributo encapsulado `valor`.
- **Herança (`Real`, `Dolar`, `Euro`, `Libra`):** As classes filhas herdam as propriedades da classe mãe `Moeda`, evitando a duplicação de código.
- **Polimorfismo:** Os métodos `info()` e `converter()` são sobrescritos (`@Override`) em cada classe filha. Isso garante que cada moeda calcule sua própria cotação de hardware/software de forma independente.
- **Sobrescrita do Método `equals()`:** Implementado na classe abstrata para permitir que a estrutura de dados `ArrayList` do Java consiga localizar e remover objetos específicos da memória por valor e tipo.

##   Tecnologias Utilizadas
- **Ambiente Backend:** Java SE (JDK 17 ou superior) utilizando o servidor nativo de alto desempenho `HttpServer`.
- **Ambiente Frontend:** HTML5, CSS3 estruturado em tema escuro moderno e JavaScript assíncrono (Fetch API) transmitindo payloads via strings estruturadas em formato Pipe (`|`).

##   Como Executar o Ecossistema na sua Máquina

### 1. Inicializar o Servidor Java
Abra o prompt de comando ou terminal do seu sistema operacional na pasta do projeto e execute os comandos de compilação e execução:
```bash
javac CofrinhoApp.java
java CofrinhoApp
```
*O terminal exibirá a mensagem: `Servidor do Cofrinho rodando na porta 8083...`*

### 2. Executar a Interface Gráfica Web
Basta navegar até a pasta local do projeto e abrir o arquivo `index.html` dando dois cliques com o mouse para carregá-lo no seu navegador de internet de preferência.

##  Governança de TI: Filtros do Repositório
O projeto conta com um arquivo `.gitignore` configurado para impedir o envio de arquivos de compilação locais gerados pela máquina do desenvolvedor:
- `*.class` (Bloqueio de arquivos binários)
- `.vscode/` (Bloqueio de preferências do editor de código)

