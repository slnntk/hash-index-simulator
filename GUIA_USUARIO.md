# Guia do Usuário - Simulador de Índice Hash

## O que é o Simulador de Índice Hash?

Este programa é uma ferramenta educacional que simula como os computadores organizam e encontram informações de forma eficiente usando uma técnica chamada "índice hash". Imagine que você tem uma biblioteca gigante com 466.550 livros e precisa encontrar um livro específico rapidamente!

## Por que Índices Hash são Importantes?

### Analogia da Biblioteca

Imagine duas situações:

**Sem Índice (Table Scan):**
- Você entra na biblioteca e procura livro por livro, estante por estante
- No pior caso, o livro que você quer está na última estante
- Isso demora muito tempo!

**Com Índice Hash:**
- Você usa um catálogo especial que calcula matematicamente onde cada livro está
- Em segundos, você sabe exatamente qual estante e prateleira verificar
- Muito mais rápido!

O índice hash faz a mesma coisa com dados no computador.

## Como Funciona o Sistema?

### 1. Dados de Entrada
O programa usa uma lista de **466.550 palavras em inglês**. Cada palavra é como um livro na nossa biblioteca - precisamos ser capazes de encontrá-la rapidamente.

### 2. Organização em Páginas
As palavras são organizadas em "páginas" (como estantes da biblioteca):
- **Tamanho da Página**: Você decide quantas palavras cada página pode guardar
- **Exemplo**: Se você escolher páginas de 100 palavras, terá cerca de 4.665 páginas

### 3. Sistema de Buckets (Gavetas do Catálogo)
O índice hash usa "buckets" (como gavetas de um catálogo):
- Cada bucket aponta para páginas onde as palavras estão armazenadas
- **Capacidade do Bucket**: Quantas "fichas" cada gaveta pode guardar

### 4. Função Hash (Fórmula Mágica)
A função hash é como uma fórmula matemática que:
- Pega uma palavra (ex: "computer")
- Calcula em qual bucket (gaveta) procurar
- Faz isso de forma consistente e rápida

## Interface Gráfica

O programa possui uma interface gráfica dividida em seções organizadas:

### Painel de Controles (Parte Superior)
- **Campo "Page Size"**: Digite o tamanho desejado para cada página
- **Botão "Load Data"**: Carrega os dados e cria as páginas
- **Campo "Bucket Capacity"**: Digite a capacidade de cada bucket
- **Dropdown "Hash Function"**: Escolha entre Simple Modulo, DJB2, ou FNV-1a
- **Botão "Construct Index"**: Constrói o índice hash
- **Campo "Search Key"**: Digite a palavra para buscar
- **Botão "Search with Index"**: Busca usando o índice
- **Botão "Table Scan"**: Busca sequencial

### Painéis de Visualização (Parte Central)
- **First Page**: Mostra o conteúdo da primeira página
- **Last Page**: Mostra o conteúdo da última página  
- **Bucket Information**: Informações sobre os buckets criados
- **Statistics**: Estatísticas detalhadas do sistema
- **Search Results**: Resultados das buscas realizadas

### Barra de Status (Parte Inferior)
Mostra mensagens sobre o estado atual do sistema.

## Exemplo de Execução (CLI)

Quando executado via linha de comando, o programa mostra:

```
=== Hash Index Simulator - CLI Test ===

Loading data with page size 100...
Data loaded successfully!
Total records: 466,550
Total pages: 4,666

First Page Info:
Page{pageNumber=0, capacity=100, recordCount=100}
Sample records: [2, 1080, &c, 10-point, 10th]

Last Page Info:
Page{pageNumber=4665, capacity=100, recordCount=50}
Sample records: [zumbooruk, Zumbrota, Zumstein, Zumwalt, Zungaria]

Constructing index with bucket capacity 5...
Index constructed successfully!
Total buckets: 93311
Hash function: DJB2 Hash

=== Testing word: hello ===
Index search: Found 'hello' on page 1705 (accessed 2 pages)
Table scan: Found 'hello' on page 1705 (accessed 1706 pages)
Time difference: 2.16 ms

=== Testing word: computer ===
Index search: Found 'computer' on page 808 (accessed 2 pages)
Table scan: Found 'computer' on page 808 (accessed 809 pages)
Time difference: 0.85 ms

=== Final Statistics ===
IndexStatistics {
    Total Records: 466,550
    Total Pages: 4,666
    Total Buckets: 93,311
    Bucket Capacity: 5
    Collisions: 373,870 (80.14%)
    Overflows: 37,199 (39.87%)
}
```

Esta saída demonstra:

1. **Carregamento**: 466.550 palavras organizadas em 4.666 páginas de 100 palavras cada
2. **Páginas**: A primeira página contém palavras como "2", "1080", "&c", enquanto a última contém "zumbooruk", "Zumbrota", etc.
3. **Índice**: 93.311 buckets foram criados para organizar as referências
4. **Busca Rápida**: "hello" foi encontrado na página 1705 com apenas 2 acessos
5. **Busca Lenta**: Table scan precisou de 1706 acessos para encontrar "hello"  
6. **Diferença**: Índice foi quase 1000 vezes mais rápido!
7. **Estatísticas**: Alta taxa de colisões (80.14%) mas ainda muito eficiente

### Executando o Programa

#### Opção 1: Interface Gráfica (Recomendada)
```bash
java -jar hash-index-simulator-1.0.0.jar
```

#### Opção 2: Compilar e Executar
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.hashindex.HashIndexSimulatorApp"
```

#### Opção 3: Versão CLI (Para testes)
```bash
mvn exec:java -Dexec.mainClass="com.hashindex.util.HashIndexCLI"
```

## Como Usar o Programa

### Passo 1: Carregar os Dados
1. **Defina o Tamanho da Página**: Digite um número (ex: 100)
   - Páginas menores = mais páginas, mais precisão
   - Páginas maiores = menos páginas, menos precisão
2. **Clique em "Load Data"**
   - O programa carrega as 466.550 palavras
   - Mostra a primeira e última página criadas

### Passo 2: Construir o Índice
1. **Defina a Capacidade do Bucket**: Digite um número (ex: 5)
   - Quantas entradas cada bucket pode ter antes de "transbordar"
2. **Escolha a Função Hash**:
   - **Simple Modulo**: Mais simples, pode ter distribuição irregular
   - **DJB2**: Boa distribuição, amplamente usado
   - **FNV-1a**: Excelente distribuição e velocidade
3. **Clique em "Construct Index"**
   - O programa cria o índice hash
   - Calcula estatísticas importantes

### Passo 3: Fazer Buscas
1. **Digite uma palavra** no campo de busca (ex: "computer", "house", "water")
2. **Busca com Índice**: Clique "Search with Index"
   - Usa o índice hash para encontrar rapidamente
3. **Table Scan**: Clique "Table Scan"  
   - Procura página por página (método lento)
4. **Compare os resultados**: Veja a diferença de velocidade!

## O que Significam as Estatísticas?

### Informações das Páginas
- **Total de Registros**: 466.550 palavras carregadas
- **Total de Páginas**: Quantas páginas foram criadas
- **Primeira/Última Página**: Mostra o conteúdo das páginas extremas

### Informações dos Buckets
- **Total de Buckets**: Quantos buckets foram criados
- **Capacidade do Bucket**: Quantas entradas cada bucket pode ter
- **Função Hash**: Qual fórmula matemática está sendo usada

### Estatísticas de Performance

#### Taxa de Colisões (%)
**O que é**: Quando duas palavras diferentes são direcionadas para o mesmo bucket.

**Exemplo**: 
- A palavra "cat" vai para o bucket 15
- A palavra "dog" também vai para o bucket 15
- Isso é uma colisão!

**Por que acontece**: A função hash não é perfeita - às vezes palavras diferentes geram o mesmo número de bucket.

**O que significa**:
- **0-10%**: Excelente distribuição
- **10-20%**: Boa distribuição  
- **20%+**: Muitas colisões, pode afetar performance

#### Taxa de Overflow (%)
**O que é**: Quando um bucket fica "cheio" e precisa criar buckets adicionais.

**Exemplo**:
- Bucket 15 tem capacidade para 5 entradas
- Chegam 7 palavras para esse bucket
- 2 palavras vão para um "bucket de overflow"

**O que significa**:
- **0-5%**: Sistema bem dimensionado
- **5-15%**: Alguns overflows normais
- **15%+**: Muitos overflows, considere aumentar capacidade dos buckets

#### Estimativa de Custo (Acessos a Disco)
**O que é**: Quantas vezes o programa precisa "ler" informações do disco.

**Busca com Índice**:
- Normalmente 2 acessos (1 para o bucket, 1 para a página)
- Com overflow: pode ser mais

**Table Scan**:
- Pode precisar ler centenas ou milhares de páginas
- Muito mais lento!

#### Diferença de Tempo
Mostra quantos milissegundos o índice economiza comparado ao table scan.

**Exemplo**: "Index search: 0.5ms, Table scan: 250ms, Difference: 249.5ms"
- O índice é 500 vezes mais rápido!

## Problemas e Soluções Implementadas

### Problema 1: Colisões
**Situação**: Várias palavras querem usar o mesmo bucket.
**Solução**: Cada bucket mantém uma lista de todas as palavras que chegaram até ele.

### Problema 2: Overflow de Buckets
**Situação**: Um bucket recebe mais palavras do que sua capacidade.
**Solução**: Criar "buckets extras" ligados ao bucket original.

## Experimentos que Você Pode Fazer

### Teste 1: Tamanho da Página
- Teste com páginas pequenas (50) vs grandes (500)
- Observe como afeta o número total de páginas
- Veja se há diferença na velocidade de busca

### Teste 2: Capacidade do Bucket  
- Teste com buckets pequenos (3) vs grandes (10)
- Observe a taxa de overflow
- Veja como afeta as colisões

### Teste 3: Funções Hash
- Compare Simple Modulo vs DJB2 vs FNV-1a
- Observe qual tem menos colisões
- Veja qual é mais rápida

### Teste 4: Palavras Diferentes
- Teste palavras comuns: "the", "and", "is"
- Teste palavras raras: "antidisestablishmentarianism"
- Observe se há diferença na velocidade

## Conceitos Importantes Explicados Simplesmente

### Função Hash
É como uma máquina calculadora especial:
- **Entrada**: Uma palavra (ex: "hello")
- **Processamento**: Fórmula matemática complexa
- **Saída**: Um número que indica qual bucket usar (ex: bucket 42)

### Colisão
Quando a "máquina calculadora" dá o mesmo resultado para palavras diferentes:
- "hello" → bucket 42
- "world" → bucket 42  
- Agora o bucket 42 tem duas palavras

### Overflow
Quando um bucket fica "lotado":
- Bucket 42 pode guardar 5 palavras
- Chegam 6 palavras para esse bucket
- A 6ª palavra vai para um bucket "extra"

### Table Scan vs Index Search

**Table Scan** (busca sequencial):
```
Página 1: procura "computer"... não achou
Página 2: procura "computer"... não achou  
Página 3: procura "computer"... não achou
...
Página 1,247: procura "computer"... ACHOU!
```

**Index Search** (busca por índice):
```
1. Calcula: "computer" → bucket 67
2. Bucket 67 diz: "computer" está na página 1,247
3. Vai direto na página 1,247... ACHOU!
```

## Por que Isso é Importante na Vida Real?

### Banco de Dados
- Quando você faz login no Facebook, ele não procura seu nome em bilhões de usuários um por um
- Usa índices hash para te encontrar instantaneamente

### Motor de Busca
- Google não lê toda a internet quando você pesquisa algo
- Usa índices para saber onde estão as páginas relevantes

### Sistema Bancário
- Quando você usa o cartão, o banco não verifica todas as contas uma por uma
- Usa índices para encontrar sua conta imediatamente

## Dicas para Melhores Resultados

1. **Tamanho da Página**:
   - Para poucos dados: páginas pequenas (50-100)
   - Para muitos dados: páginas médias (200-500)

2. **Capacidade do Bucket**:
   - Comece com 5-10 entradas por bucket
   - Ajuste baseado na taxa de overflow

3. **Função Hash**:
   - Para aprendizado: comece com Simple Modulo
   - Para melhor performance: use DJB2 ou FNV-1a

4. **Interpretação**:
   - Foque na diferença de tempo entre métodos
   - Observe como parâmetros afetam as estatísticas

## Glossário

- **Hash**: Técnica de transformar dados em números para organização rápida
- **Bucket**: Recipiente que guarda referências para onde os dados estão
- **Página**: Grupo de dados organizados juntos (como uma página de livro)
- **Colisão**: Quando dois dados diferentes vão para o mesmo lugar
- **Overflow**: Quando um recipiente fica cheio demais
- **Table Scan**: Procurar dados um por um, do início ao fim
- **Index Search**: Usar um "mapa" para encontrar dados rapidamente
- **Acesso a Disco**: Cada vez que o programa precisa ler informações armazenadas

## Conclusão

O Simulador de Índice Hash é uma ferramenta poderosa para entender como os computadores organizam e recuperam informações eficientemente. Através de experimentos práticos, você pode ver a diferença dramática entre busca sequencial (lenta) e busca por índice (rápida).

Esta tecnologia está presente em praticamente todos os sistemas que usamos diariamente - desde redes sociais até sistemas bancários. Entender seus princípios básicos ajuda a compreender como a tecnologia moderna consegue lidar com quantidades enormes de dados de forma tão eficiente.

Experimente diferentes configurações, observe os resultados e descubra por si mesmo por que índices hash são uma das ferramentas mais importantes da ciência da computação!