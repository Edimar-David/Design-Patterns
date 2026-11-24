# Decorator — Dashboard Financeiro

## 1. Sobre o padrão Decorator

O **Decorator** é um padrão de projeto estrutural (Structural Design Pattern) que permite adicionar novas funcionalidades a um objeto dinamicamente, sem modificar sua classe original.

Seu principal objetivo é estender o comportamento de objetos por meio de composição, permitindo que diferentes funcionalidades sejam adicionadas de maneira flexível e independente.

Diferentemente da herança tradicional, o Decorator permite combinar diferentes comportamentos envolvendo um objeto com outros objetos decoradores, mantendo a mesma interface.

## 2. Aplicação no projeto

Neste projeto, o padrão Decorator foi implementado para organizar a geração dos relatórios do dashboard financeiro, permitindo adicionar diferentes informações ao relatório básico sem concentrar toda a lógica em uma única classe.

A implementação está localizada no pacote:

```text
patterns/decorator/
```

A estrutura possui as seguintes classes:

| Classe | Responsabilidade |
|---|---|
| `RelatorioFinanceiro` | Define o contrato dos relatórios financeiros. |
| `RelatorioFinanceiroBasico` | Gera os indicadores financeiros principais. |
| `RelatorioDecorator` | Classe abstrata base para os decorators. |
| `EvolucaoMensalDecorator` | Adiciona os dados de evolução mensal. |
| `DespesasPorCategoriaDecorator` | Adiciona as despesas agrupadas por categoria. |

O relatório financeiro foi dividido em três responsabilidades principais:

- **Relatório básico:** calcula receitas, despesas, saldo e quantidade de transações.
- **Evolução mensal:** calcula os valores de receitas e despesas dos últimos meses.
- **Despesas por categoria:** agrupa as despesas por categoria e retorna as categorias com maiores valores.

Dessa forma, cada classe possui uma responsabilidade específica, facilitando a manutenção e a possibilidade de adicionar novas funcionalidades futuramente.

## 3. Estrutura da implementação

A implementação começa com a interface `RelatorioFinanceiro`, que define o método responsável pela geração do relatório.

**RelatorioFinanceiro.java**

```java
public interface RelatorioFinanceiro {

    DashboardResponse gerar();
}
```

Essa interface garante que tanto o relatório básico quanto os decorators utilizem o mesmo contrato.

### Relatório básico

A classe `RelatorioFinanceiroBasico` implementa a interface e representa a estrutura inicial do relatório.

Ela recebe uma lista de transações e a configuração do dashboard, sendo responsável por calcular os indicadores financeiros principais.

```java
public class RelatorioFinanceiroBasico implements RelatorioFinanceiro {

    private final List<Transacao> transacoes;
    private final ConfiguracaoDashboard configuracao;

    @Override
    public DashboardResponse gerar() {

        BigDecimal totalReceitas = BigDecimal.ZERO;
        BigDecimal totalDespesas = BigDecimal.ZERO;

        for (Transacao transacao : transacoes) {

            if (transacao.getTipo() == TipoTransacao.RECEITA) {
                totalReceitas = totalReceitas.add(transacao.getValor());

            } else if (transacao.getTipo() == TipoTransacao.DESPESA) {
                totalDespesas = totalDespesas.add(transacao.getValor());
            }
        }

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new DashboardResponse(
                totalReceitas,
                totalDespesas,
                saldo,
                transacoes.size(),
                configuracao.getMoeda(),
                List.of(),
                List.of()
        );
    }
}
```

As listas de evolução mensal e despesas por categoria começam vazias. Elas serão preenchidas posteriormente pelos decorators.

### Decorator base

A classe abstrata `RelatorioDecorator` implementa a mesma interface do relatório original.

Sua responsabilidade é receber um relatório existente e delegar a execução do método `gerar()` para ele.

```java
public abstract class RelatorioDecorator implements RelatorioFinanceiro {

    protected final RelatorioFinanceiro relatorio;

    public RelatorioDecorator(RelatorioFinanceiro relatorio) {
        this.relatorio = relatorio;
    }

    @Override
    public DashboardResponse gerar() {
        return relatorio.gerar();
    }
}
```

Essa classe é fundamental para o funcionamento do padrão, pois permite que os decorators sejam encadeados e trabalhem sobre um relatório já existente.

### Principais elementos

- **Interface RelatorioFinanceiro:** define o contrato comum utilizado por todos os relatórios.
- **RelatorioFinanceiroBasico:** representa o relatório inicial, responsável pelos indicadores financeiros.
- **RelatorioDecorator:** mantém uma referência ao relatório que será decorado e permite estender seu comportamento.
- **Construtor do Decorator:** recebe outro objeto que implementa `RelatorioFinanceiro`.
- **Método gerar():** permite executar o relatório anterior antes de adicionar novas funcionalidades.

## 4. Decorators implementados

Neste projeto foram criados dois decorators, cada um responsável por adicionar uma funcionalidade específica ao dashboard.

### EvolucaoMensalDecorator

O `EvolucaoMensalDecorator` adiciona ao relatório os dados de evolução mensal de receitas e despesas.

Ele utiliza o período definido na classe `ConfiguracaoDashboard`, implementada anteriormente com o padrão Singleton.

Seu funcionamento consiste em:

1. Recuperar o relatório anterior utilizando `super.gerar()`.
2. Consultar a quantidade de meses configurada.
3. Criar uma estrutura contendo os meses do período.
4. Percorrer as transações e identificar o mês de cada registro.
5. Agrupar os valores de receitas e despesas.
6. Construir a lista de evolução mensal.
7. Retornar um novo `DashboardResponse`, preservando os indicadores do relatório anterior.

O agrupamento é realizado utilizando `YearMonth` e `LinkedHashMap`, mantendo a organização cronológica dos meses.

A classe também utiliza uma estrutura interna chamada `ResumoMensal`, responsável por armazenar os totais de receitas e despesas de cada mês.

### DespesasPorCategoriaDecorator

O `DespesasPorCategoriaDecorator` adiciona ao relatório os dados de despesas agrupadas por categoria.

Ele também utiliza a configuração Singleton para consultar o período de análise e o limite máximo de categorias que devem ser exibidas.

Seu funcionamento consiste em:

1. Recuperar o relatório anterior utilizando `super.gerar()`.
2. Consultar o limite de categorias configurado.
3. Determinar o intervalo de meses considerado.
4. Percorrer as transações, considerando apenas despesas.
5. Agrupar os valores utilizando a categoria como chave.
6. Ordenar as categorias pelo valor total, em ordem decrescente.
7. Aplicar o limite configurado.
8. Retornar um novo `DashboardResponse`, preservando os dados calculados anteriormente.

O agrupamento utiliza `HashMap` e o método `merge()`, permitindo somar os valores de transações pertencentes à mesma categoria.

A ordenação é realizada utilizando Streams, com `sorted()` e `reversed()`, enquanto o método `limit()` controla a quantidade de categorias retornadas.

## 5. Integração com o DashboardService

O padrão Decorator está integrado diretamente ao `DashboardService`, responsável por coordenar a geração dos dados apresentados no dashboard.

A implementação utiliza a composição dos objetos para construir o relatório completo.

**DashboardService.java**

```java
@Transactional(readOnly = true)
public DashboardResponse obterDashboard() {

    List<Transacao> transacoes = repository.findAll();

    RelatorioFinanceiro relatorio =
            new RelatorioFinanceiroBasico(transacoes, configuracao);

    relatorio = new EvolucaoMensalDecorator(
            relatorio,
            transacoes,
            configuracao
    );

    relatorio = new DespesasPorCategoriaDecorator(
            relatorio,
            transacoes,
            configuracao
    );

    return relatorio.gerar();
}
```

O processo começa criando uma instância de `RelatorioFinanceiroBasico`, que representa o relatório original.

Em seguida, o objeto é envolvido pelo `EvolucaoMensalDecorator`, adicionando os dados de evolução mensal.

Por último, o resultado é envolvido pelo `DespesasPorCategoriaDecorator`, que acrescenta as informações de despesas por categoria.

Ao final, o método `gerar()` executa toda a cadeia de decorators e retorna o relatório financeiro completo.

### Funcionalidades beneficiadas

**Indicadores financeiros:**

O relatório básico calcula o total de receitas, despesas, saldo e quantidade de transações.

**Evolução mensal:**

O decorator de evolução mensal adiciona os valores de receitas e despesas de cada mês, permitindo alimentar o gráfico de evolução financeira no frontend.

**Despesas por categoria:**

O decorator de categorias agrupa as despesas, ordena os resultados pelos maiores valores e retorna somente a quantidade de categorias definida na configuração do dashboard.

## 6. Fluxo de funcionamento

O funcionamento do padrão no projeto segue a seguinte sequência:

```text
DashboardController
        |
        v
DashboardService
        |
        v
Busca as transações
        |
        v
RelatorioFinanceiroBasico
        |
        v
EvolucaoMensalDecorator
        |
        v
DespesasPorCategoriaDecorator
        |
        v
Executa gerar()
        |
        v
DashboardResponse
        |
        v
Frontend
```

A composição dos objetos funciona da seguinte maneira:

```text
DespesasPorCategoriaDecorator
              |
              v
    EvolucaoMensalDecorator
              |
              v
   RelatorioFinanceiroBasico
```

Cada decorator recebe um relatório anterior, executa seu método `gerar()` e acrescenta uma nova funcionalidade ao resultado.

O resultado final reúne todos os indicadores e informações necessários para alimentar o dashboard financeiro.

Uma característica importante dessa implementação é que os decorators são instanciados a cada chamada do dashboard, enquanto a configuração Singleton é compartilhada durante a execução da aplicação.

Isso permite montar o relatório de acordo com as transações consultadas em cada requisição, reutilizando as configurações centralizadas.

## 7. Integração com o Singleton

Os padrões Decorator e Singleton trabalham em conjunto na geração do dashboard financeiro.

O Singleton, implementado na classe `ConfiguracaoDashboard`, centraliza as configurações utilizadas pelos relatórios.

Já os decorators utilizam essas configurações para determinar como as informações devem ser calculadas e apresentadas.

Exemplos de utilização:

```java
int periodo = configuracao.getPeriodoGraficoMeses();

int limite = configuracao.getLimiteCategorias();

String moeda = configuracao.getMoeda();
```

A integração acontece da seguinte maneira:

- **Singleton:** disponibiliza as configurações compartilhadas.
- **RelatorioFinanceiroBasico:** utiliza a configuração de moeda.
- **EvolucaoMensalDecorator:** utiliza o período configurado para gerar a evolução mensal.
- **DespesasPorCategoriaDecorator:** utiliza o período e o limite de categorias.

Dessa forma, os dois padrões possuem responsabilidades diferentes, mas complementares dentro da arquitetura do sistema.

## 8. Vantagens da implementação

- Permite adicionar funcionalidades sem modificar a classe original do relatório.
- Separa as responsabilidades de cada funcionalidade do dashboard.
- Facilita a manutenção e evolução do código.
- Permite combinar diferentes decorators por meio de composição.
- Evita concentrar toda a lógica de geração do relatório em uma única classe.
- Mantém uma interface comum para todos os componentes.
- Permite adicionar novos tipos de informações ao dashboard com novos decorators.
- Trabalha em conjunto com o Singleton para reutilizar as configurações centralizadas.

## 9. Conclusão

A implementação do padrão Decorator no Dashboard Financeiro permite adicionar funcionalidades ao relatório financeiro de maneira organizada e flexível.

Sua utilização possibilitou separar o cálculo dos indicadores principais da geração dos dados de evolução mensal e despesas por categoria.

A integração com o `DashboardService` demonstra a aplicação prática do padrão, utilizando composição de objetos para construir um relatório completo sem modificar diretamente a implementação original.

Além disso, o uso conjunto com o padrão Singleton demonstra como diferentes padrões de projeto podem trabalhar em conjunto para melhorar a organização e a estrutura da aplicação.

Dessa forma, o projeto aplica um padrão estrutural de maneira explícita, contribuindo para a compreensão dos conceitos de composição, extensão de comportamento e separação de responsabilidades.