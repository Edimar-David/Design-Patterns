# Singleton — Dashboard Financeiro

## 1. Sobre o padrão Singleton

O **Singleton** é um padrão de projeto criacional (Creational Design Pattern) que garante que uma classe possua apenas uma instância durante a execução da aplicação, fornecendo um ponto de acesso global a essa instância.

Seu principal objetivo é evitar a criação desnecessária de múltiplos objetos quando existe a necessidade de compartilhar uma mesma configuração ou recurso entre diferentes partes do sistema.

## 2. Aplicação no projeto

Neste projeto, o padrão Singleton foi implementado na classe `ConfiguracaoDashboard`, localizada no pacote:

```text
patterns/singleton/ConfiguracaoDashboard.java
```

A classe é responsável por centralizar as configurações gerais do dashboard financeiro, permitindo que diferentes componentes da aplicação acessem os mesmos valores.

Atualmente, ela armazena:

| Configuração | Valor padrão | Finalidade |
|---|---|---|
| Moeda | BRL | Definir a moeda utilizada pelo dashboard. |
| Período dos gráficos | 6 meses | Determinar o intervalo da evolução mensal. |
| Limite de categorias | 5 | Limitar as categorias exibidas no gráfico de despesas. |

## 3. Estrutura da implementação

A implementação utiliza o padrão **Initialization-on-demand holder idiom**, uma abordagem de Singleton que utiliza uma classe interna para inicializar a instância somente quando ela é solicitada pela primeira vez.

```java
public final class ConfiguracaoDashboard {

    private final String moeda = "BRL";
    private final int periodoGraficoMeses = 6;
    private final int limiteCategorias = 5;

    private ConfiguracaoDashboard() {
    }

    private static class InstanciaHolder {
        private static final ConfiguracaoDashboard INSTANCE =
                new ConfiguracaoDashboard();
    }

    public static ConfiguracaoDashboard getInstance() {
        return InstanciaHolder.INSTANCE;
    }

    public String getMoeda() {
        return moeda;
    }

    public int getPeriodoGraficoMeses() {
        return periodoGraficoMeses;
    }

    public int getLimiteCategorias() {
        return limiteCategorias;
    }
}
```

### Principais elementos

- **Construtor privado:** impede que outras classes criem instâncias diretamente utilizando `new`.
- **Classe interna InstanciaHolder:** mantém a instância única do Singleton.
- **Atributo INSTANCE:** armazena o único objeto da classe.
- **Método getInstance():** disponibiliza acesso à instância compartilhada.
- **Atributos final:** garantem que as configurações não sejam modificadas depois da inicialização.

A inicialização da classe interna é realizada pelo próprio mecanismo de carregamento de classes da JVM, garantindo segurança em ambientes multithread sem a necessidade de utilizar `synchronized`.

## 4. Integração com o DashboardService

O Singleton não foi criado apenas para demonstrar o padrão de projeto. Ele está integrado à lógica de funcionamento do dashboard.

No `DashboardService`, a instância é obtida através do método `getInstance()`:

```java
private final ConfiguracaoDashboard configuracao =
        ConfiguracaoDashboard.getInstance();
```

A partir dessa instância, o serviço consegue consultar as configurações necessárias para gerar os indicadores financeiros e os gráficos.

Exemplos de utilização:

```java
int periodo = configuracao.getPeriodoGraficoMeses();

int limite = configuracao.getLimiteCategorias();

String moeda = configuracao.getMoeda();
```

### Funcionalidades beneficiadas

**Evolução mensal:**

O `DashboardService` utiliza o período definido pelo Singleton para determinar quantos meses serão apresentados no gráfico de receitas e despesas.

**Despesas por categoria:**

O serviço utiliza o limite configurado para determinar quantas categorias com maiores despesas serão retornadas ao frontend.

**Moeda:**

A moeda padrão é disponibilizada junto aos indicadores financeiros para orientar sua apresentação na interface.

## 5. Fluxo de funcionamento

O funcionamento do padrão no projeto segue o seguinte fluxo:

```text
DashboardController
        |
        v
DashboardService
        |
        +--------------------------+
        |                          |
        v                          v
TransacaoRepository       ConfiguracaoDashboard
        |                          |
        v                          v
Busca as transações       Retorna a instância única
        |                          |
        +-------------+------------+
                      |
                      v
             Calcula os indicadores
                      |
                      v
              DashboardResponse
                      |
                      v
                 Frontend
```

O `DashboardService` utiliza os dados do banco de dados juntamente com as configurações fornecidas pelo Singleton para construir a resposta enviada ao frontend.

## 6. Como testar o Singleton

Uma maneira simples de verificar se o padrão está funcionando corretamente é obter duas referências utilizando o método `getInstance()` e comparar os objetos.

```java
public class TesteSingleton {

    public static void main(String[] args) {

        ConfiguracaoDashboard config1 =
                ConfiguracaoDashboard.getInstance();

        ConfiguracaoDashboard config2 =
                ConfiguracaoDashboard.getInstance();

        System.out.println(config1 == config2);
    }
}
```

**Resultado esperado:**

```text
true
```

O resultado `true` demonstra que as duas variáveis apontam para a mesma instância do objeto na memória.

## 7. Singleton e Spring Boot

O Spring Boot utiliza o escopo Singleton como padrão para componentes gerenciados pelo seu container de injeção de dependências, como classes anotadas com `@Service` e `@Repository`.

Entretanto, neste projeto, foi implementado explicitamente o padrão Singleton, utilizando construtor privado, instância estática e método de acesso global.

Essa abordagem permite demonstrar diretamente os fundamentos do padrão GoF, independentemente do gerenciamento de componentes realizado pelo Spring.

## 8. Vantagens da implementação

- Garante uma única instância da classe durante a execução da aplicação.
- Centraliza as configurações do dashboard.
- Evita a duplicação de objetos responsáveis pelas mesmas configurações.
- Permite acesso compartilhado por diferentes componentes.
- Utiliza inicialização preguiçosa (Lazy Initialization).
- Possui segurança em relação à concorrência durante a inicialização.

## 9. Conclusão

A implementação do padrão Singleton no Dashboard Financeiro permite centralizar as configurações gerais do sistema em uma única instância compartilhada.

Sua utilização no `DashboardService` demonstra uma aplicação prática do padrão, facilitando o acesso às configurações utilizadas na geração dos indicadores e gráficos financeiros.

Dessa forma, o projeto aplica um padrão criacional de maneira explícita, contribuindo para a organização e compreensão da arquitetura do sistema.