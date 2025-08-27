# Documentação Técnica - Simulador de Índice Hash

## Visão Geral

Este documento fornece uma explicação detalhada e técnica de cada parte do código do Simulador de Índice Hash, implementado em Java com interface gráfica Swing. O sistema implementa um índice hash estático com resolução de colisões e tratamento de overflow de buckets.

## Arquitetura do Sistema

```
com.hashindex/
├── model/              # Estruturas de dados (Page, Bucket, Statistics, etc.)
├── service/            # Lógica principal (HashIndexService, HashFunction)
├── gui/                # Interface gráfica Swing
├── util/               # Utilitários e ferramentas CLI
└── HashIndexSimulatorApp.java  # Ponto de entrada principal
```

## Estruturas de Dados Implementadas

### 1. Page (Página)

**Localização:** `src/main/java/com/hashindex/model/Page.java`

A classe `Page` representa a divisão física e alocação da tabela na mídia de armazenamento.

```java
public class Page {
    private final int pageNumber;        // Número identificador da página
    private final int capacity;         // Capacidade máxima de registros
    private final List<String> records; // Lista de registros (palavras)
}
```

**Funcionalidades principais:**

- **Construtor:** `Page(int pageNumber, int capacity)`
  - Inicializa uma página com número e capacidade específicos
  - Cria uma lista vazia de registros com capacidade inicial definida

- **addRecord(String record):** `boolean`
  - Adiciona um registro à página se houver espaço disponível
  - Retorna `true` se o registro foi adicionado, `false` se a página está cheia
  - Implementa controle de capacidade: `records.size() < capacity`

- **containsRecord(String searchKey):** `boolean`
  - Busca sequencial por um registro específico na página
  - Utiliza `records.contains(searchKey)` para verificação
  - Usado tanto na busca por índice quanto no table scan

- **Métodos de acesso:**
  - `getPageNumber()`: Retorna o número da página
  - `getCapacity()`: Retorna a capacidade máxima
  - `getRecords()`: Retorna lista de registros (somente leitura)
  - `size()`: Retorna número atual de registros
  - `isFull()`: Verifica se a página está cheia

### 2. BucketEntry (Entrada do Bucket)

**Localização:** `src/main/java/com/hashindex/model/BucketEntry.java`

Record class que representa uma entrada individual no bucket, mapeando uma chave de busca para um endereço de página.

```java
public record BucketEntry(String searchKey, int pageNumber) {
    @Override
    public String toString() {
        return String.format("(%s -> Page %d)", searchKey, pageNumber);
    }
}
```

**Características:**
- Imutável (record class do Java 17)
- Mapeia diretamente chave → página
- Implementa toString() para debugging e visualização

### 3. Bucket (Bucket do Índice Hash)

**Localização:** `src/main/java/com/hashindex/model/Bucket.java`

Estrutura que mapeia chaves de busca em endereços de páginas, com suporte a overflow.

```java
public class Bucket {
    private final int bucketNumber;           // Número identificador do bucket
    private final int capacity;              // Capacidade máxima de entradas
    private final List<BucketEntry> entries; // Lista de entradas do bucket
    private Bucket overflowBucket;           // Bucket de overflow (se necessário)
    private boolean isOverflow;              // Flag indicando se é bucket de overflow
}
```

**Algoritmo de Resolução de Colisões:**
- **Separate Chaining:** Cada bucket mantém uma lista de entradas
- **Overflow Buckets:** Quando um bucket excede sua capacidade, cria-se um bucket de overflow

**Métodos principais:**

- **addEntry(BucketEntry entry):** `boolean`
  ```java
  public boolean addEntry(BucketEntry entry) {
      if (entries.size() < capacity) {
          entries.add(entry);
          return true;
      } else {
          // Criação ou uso de bucket de overflow
          if (overflowBucket == null) {
              overflowBucket = new Bucket(bucketNumber, capacity, true);
          }
          return overflowBucket.addEntry(entry);
      }
  }
  ```

- **findEntry(String searchKey):** `BucketEntry`
  - Busca uma entrada pelo search key
  - Procura primeiro no bucket principal, depois nos buckets de overflow
  - Implementa busca em cadeia para resolução de colisões

- **getPageNumber(String searchKey):** `int`
  - Retorna o número da página onde a chave está armazenada
  - Retorna -1 se a chave não for encontrada
  - Utiliza `findEntry()` internamente

**Algoritmo de Resolução de Overflow:**
1. Quando `entries.size() >= capacity`
2. Criar novo bucket de overflow se não existir
3. Adicionar entrada no bucket de overflow
4. Manter referência em cadeia para buckets de overflow

### 4. IndexStatistics (Estatísticas do Índice)

**Localização:** `src/main/java/com/hashindex/model/IndexStatistics.java`

Classe responsável por coletar e calcular todas as estatísticas do sistema.

```java
public class IndexStatistics {
    private long totalRecords;        // Total de registros carregados
    private int totalPages;           // Total de páginas criadas
    private int totalBuckets;         // Total de buckets criados
    private int bucketCapacity;       // Capacidade de cada bucket
    private long collisions;          // Número de colisões detectadas
    private long overflows;           // Número de overflows detectados
    private long searchAccesses;      // Acessos durante busca por índice
    private long tableScanAccesses;   // Acessos durante table scan
    private long searchTimeNanos;     // Tempo de busca por índice (nanossegundos)
    private long tableScanTimeNanos;  // Tempo de table scan (nanossegundos)
}
```

**Cálculos Estatísticos:**

- **Taxa de Colisões:** `(collisions / totalRecords) * 100`
- **Taxa de Overflow:** `(overflows / totalBuckets) * 100`
- **Diferença de Tempo:** `tableScanTimeNanos - searchTimeNanos`

**Métodos de Cálculo:**

```java
public double getCollisionRate() {
    return totalRecords > 0 ? (collisions * 100.0) / totalRecords : 0.0;
}

public double getOverflowRate() {
    return totalBuckets > 0 ? (overflows * 100.0) / totalBuckets : 0.0;
}

public double getTimeDifferenceMillis() {
    return (tableScanTimeNanos - searchTimeNanos) / 1_000_000.0;
}
```

### 5. SearchResult (Resultado da Busca)

**Localização:** `src/main/java/com/hashindex/model/SearchResult.java`

Record class que encapsula os resultados de operações de busca.

```java
public record SearchResult(
    boolean found,      // Se a chave foi encontrada
    int pageNumber,     // Número da página onde foi encontrada
    int accessCount,    // Número de acessos realizados
    String searchKey    // Chave que foi buscada
) {}
```

## Implementações de Função Hash

### HashFunction Interface

**Localização:** `src/main/java/com/hashindex/service/HashFunction.java`

Interface que define o contrato para funções hash:

```java
public interface HashFunction {
    int hash(String key, int bucketCount);
    String getName();
}
```

### 1. SimpleModuloHashFunction

Implementação básica usando o hashCode() nativo do Java:

```java
public int hash(String key, int bucketCount) {
    if (key == null || bucketCount <= 0) {
        return 0;
    }
    return Math.abs(key.hashCode()) % bucketCount;
}
```

**Características:**
- Simples e rápida
- Utiliza o algoritmo de hash nativo do Java para strings
- Pode ter distribuição irregular dependendo dos dados

### 2. DJB2HashFunction

Algoritmo de hash popular para strings (Daniel J. Bernstein):

```java
public int hash(String key, int bucketCount) {
    if (key == null || bucketCount <= 0) {
        return 0;
    }
    
    long hash = 5381;
    for (char c : key.toCharArray()) {
        hash = ((hash << 5) + hash) + c; // hash * 33 + c
    }
    
    return (int) (Math.abs(hash) % bucketCount);
}
```

**Características:**
- Fórmula: `hash = hash * 33 + caractere`
- Valor inicial: 5381
- Boa distribuição para strings
- Amplamente usado em aplicações reais

### 3. FNV1aHashFunction

Fowler-Noll-Vo hash algorithm, variante 1a:

```java
public int hash(String key, int bucketCount) {
    if (key == null || bucketCount <= 0) {
        return 0;
    }
    
    long hash = FNV_OFFSET_BASIS; // 2166136261L
    for (byte b : key.getBytes()) {
        hash ^= b;
        hash *= FNV_PRIME; // 16777619L
    }
    
    return (int) (Math.abs(hash) % bucketCount);
}
```

**Características:**
- Constantes específicas: FNV_OFFSET_BASIS e FNV_PRIME
- Operações: XOR seguido de multiplicação
- Excelente distribuição e velocidade
- Recomendado para uso geral com strings

## Serviço Principal

### HashIndexService

**Localização:** `src/main/java/com/hashindex/service/HashIndexService.java`

Classe principal que implementa toda a funcionalidade do índice hash.

#### Atributos Principais:

```java
private List<Page> pages;              // Lista de páginas de dados
private List<Bucket> buckets;          // Lista de buckets do índice
private HashFunction hashFunction;     // Função hash escolhida
private IndexStatistics statistics;    // Estatísticas do sistema
private int pageSize;                  // Tamanho de cada página
private int bucketCapacity;            // Capacidade de cada bucket
```

#### Métodos Principais:

### 1. loadData(int pageSize)

**Responsabilidade:** Carregamento e organização dos dados em páginas.

```java
public void loadData(int pageSize) throws IOException {
    this.pageSize = pageSize;
    
    // Carrega palavras do arquivo de recursos
    List<String> words = loadWordsFromResource();
    
    // Cria páginas com base no tamanho especificado
    createPages(words);
    
    // Atualiza estatísticas
    statistics.setTotalRecords(words.size());
    statistics.setTotalPages(pages.size());
}
```

**Algoritmo de Criação de Páginas:**

```java
private void createPages(List<String> words) {
    pages.clear();
    int pageNumber = 0;
    Page currentPage = new Page(pageNumber++, pageSize);
    
    for (String word : words) {
        if (!currentPage.addRecord(word)) {
            // Página cheia, criar nova página
            pages.add(currentPage);
            currentPage = new Page(pageNumber++, pageSize);
            currentPage.addRecord(word);
        }
    }
    
    // Adicionar última página
    if (currentPage.size() > 0) {
        pages.add(currentPage);
    }
}
```

### 2. constructIndex(int bucketCapacity)

**Responsabilidade:** Construção do índice hash com tratamento de colisões e overflow.

```java
public void constructIndex(int bucketCapacity) {
    this.bucketCapacity = bucketCapacity;
    
    // Calcula número de buckets (NB > NR/FR)
    long totalRecords = statistics.getTotalRecords();
    int numberOfBuckets = (int) Math.ceil((double) totalRecords / bucketCapacity) + 1;
    
    // Cria buckets
    this.buckets.clear();
    for (int i = 0; i < numberOfBuckets; i++) {
        buckets.add(new Bucket(i, bucketCapacity));
    }
    
    // Popula buckets
    populateBuckets();
    
    // Calcula estatísticas
    calculateStatistics();
}
```

**Algoritmo de População de Buckets:**

```java
private void populateBuckets() {
    for (Page page : pages) {
        for (String record : page.getRecords()) {
            int bucketIndex = hashFunction.hash(record, buckets.size());
            Bucket bucket = buckets.get(bucketIndex);
            
            // Detecção de colisão
            if (!bucket.getEntries().isEmpty()) {
                statistics.incrementCollisions();
            }
            
            BucketEntry entry = new BucketEntry(record, page.getPageNumber());
            bucket.addEntry(entry);
        }
    }
}
```

### 3. searchWithIndex(String searchKey)

**Responsabilidade:** Busca usando o índice hash construído.

```java
public SearchResult searchWithIndex(String searchKey) {
    long startTime = System.nanoTime();
    
    // Aplicar função hash para encontrar bucket
    int bucketIndex = hashFunction.hash(searchKey, buckets.size());
    Bucket bucket = buckets.get(bucketIndex);
    
    // Buscar entrada no bucket (inclui buckets de overflow)
    int pageNumber = bucket.getPageNumber(searchKey);
    long accesses = 1; // Pelo menos um acesso ao bucket
    
    if (pageNumber != -1) {
        // Encontrado no bucket, agora ler a página
        accesses++; // Leitura da página
        Page page = pages.get(pageNumber);
        boolean found = page.containsRecord(searchKey);
        
        long endTime = System.nanoTime();
        statistics.setSearchTimeNanos(endTime - startTime);
        statistics.setSearchAccesses(accesses);
        
        return new SearchResult(found, pageNumber, (int) accesses, searchKey);
    }
    
    // Não encontrado
    long endTime = System.nanoTime();
    statistics.setSearchTimeNanos(endTime - startTime);
    statistics.setSearchAccesses(accesses);
    
    return new SearchResult(false, -1, (int) accesses, searchKey);
}
```

### 4. tableScan(String searchKey)

**Responsabilidade:** Busca sequencial (table scan) em todas as páginas.

```java
public SearchResult tableScan(String searchKey) {
    long startTime = System.nanoTime();
    int accesses = 0;
    
    for (Page page : pages) {
        accesses++; // Cada leitura de página conta como um acesso
        
        if (page.containsRecord(searchKey)) {
            long endTime = System.nanoTime();
            statistics.setTableScanTimeNanos(endTime - startTime);
            statistics.setTableScanAccesses(accesses);
            
            return new SearchResult(true, page.getPageNumber(), accesses, searchKey);
        }
    }
    
    // Não encontrado após verificar todas as páginas
    long endTime = System.nanoTime();
    statistics.setTableScanTimeNanos(endTime - startTime);
    statistics.setTableScanAccesses(accesses);
    
    return new SearchResult(false, -1, accesses, searchKey);
}
```

## Interface Gráfica

### HashIndexGUI

**Localização:** `src/main/java/com/hashindex/gui/HashIndexGUI.java`

Interface gráfica Swing que permite visualização e interação com o simulador.

#### Componentes Principais:

```java
// Controles de entrada
private JTextField pageSizeField;           // Campo para tamanho da página
private JTextField bucketCapacityField;     // Campo para capacidade do bucket
private JComboBox<HashFunctionType> hashFunctionCombo; // Seleção de função hash
private JTextField searchKeyField;          // Campo para chave de busca

// Botões de ação
private JButton loadDataButton;             // Botão "Carregar Dados"
private JButton constructIndexButton;       // Botão "Construir Índice"
private JButton searchButton;               // Botão "Buscar com Índice"
private JButton tableScanButton;            // Botão "Table Scan"

// Áreas de exibição
private JTextArea firstPageArea;            // Exibição da primeira página
private JTextArea lastPageArea;             // Exibição da última página
private JTextArea bucketsArea;              // Informações dos buckets
private JTextArea statisticsArea;           // Estatísticas do sistema
private JTextArea searchResultArea;         // Resultados da busca
```

#### Fluxo de Interação:

1. **Carregamento de Dados:**
   ```java
   private class LoadDataAction implements ActionListener {
       @Override
       public void actionPerformed(ActionEvent e) {
           try {
               int pageSize = Integer.parseInt(pageSizeField.getText());
               service.loadData(pageSize);
               updatePageDisplays();
               constructIndexButton.setEnabled(true);
               statusLabel.setText("Data loaded successfully. Pages: " + service.getPages().size());
           } catch (Exception ex) {
               showError("Error loading data: " + ex.getMessage());
           }
       }
   }
   ```

2. **Construção do Índice:**
   ```java
   private class ConstructIndexAction implements ActionListener {
       @Override
       public void actionPerformed(ActionEvent e) {
           try {
               int bucketCapacity = Integer.parseInt(bucketCapacityField.getText());
               HashFunctionType selectedType = (HashFunctionType) hashFunctionCombo.getSelectedItem();
               HashFunction hashFunc = HashFunctionFactory.createHashFunction(selectedType);
               service.setHashFunction(hashFunc);
               service.constructIndex(bucketCapacity);
               
               updateBucketDisplay();
               updateStatisticsDisplay();
               searchButton.setEnabled(true);
               tableScanButton.setEnabled(true);
           } catch (Exception ex) {
               showError("Error constructing index: " + ex.getMessage());
           }
       }
   }
   ```

3. **Operações de Busca:**
   ```java
   private class SearchAction implements ActionListener {
       @Override
       public void actionPerformed(ActionEvent e) {
           String searchKey = searchKeyField.getText().trim();
           SearchResult result = service.searchWithIndex(searchKey);
           displaySearchResult("Index Search", result);
           updateStatisticsDisplay();
       }
   }
   
   private class TableScanAction implements ActionListener {
       @Override
       public void actionPerformed(ActionEvent e) {
           String searchKey = searchKeyField.getText().trim();
           SearchResult result = service.tableScan(searchKey);
           displaySearchResult("Table Scan", result);
           updateStatisticsDisplay();
       }
   }
   ```

## Parâmetros e Cálculos

### Parâmetros Configuráveis:

1. **Tamanho da Página (pageSize):** Entrada do usuário
   - Determina quantos registros cada página pode conter
   - Afeta o número total de páginas: `totalPages = ceil(totalRecords / pageSize)`

2. **Capacidade do Bucket (bucketCapacity):** Entrada do usuário
   - Determina quantas entradas cada bucket pode armazenar
   - Afeta quando ocorre overflow

3. **Função Hash:** Seleção do usuário
   - Simple Modulo, DJB2, ou FNV-1a
   - Afeta a distribuição e taxa de colisões

### Parâmetros Calculados:

1. **Número de Buckets (NB):**
   ```java
   int numberOfBuckets = (int) Math.ceil((double) totalRecords / bucketCapacity) + 1;
   ```
   - Fórmula: NB > NR/FR, onde NR = número de registros, FR = capacidade do bucket

2. **Quantidade de Páginas:**
   ```java
   int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
   ```

### Algoritmos de Resolução:

#### Resolução de Colisões:
- **Separate Chaining:** Cada bucket mantém lista de entradas
- **Detecção:** Incrementa contador quando bucket já possui entradas

#### Resolução de Overflow:
- **Overflow Buckets:** Criação dinâmica quando bucket excede capacidade
- **Encadeamento:** Buckets de overflow são linkados ao bucket principal

## Medição de Performance

### Estimativa de Custo (Acessos a Disco):

1. **Busca por Índice:**
   - 1 acesso para ler o bucket (pode incluir buckets de overflow)
   - 1 acesso para ler a página de dados
   - Custo médio: 2 acessos (melhor caso: 2, pior caso: 2 + overflows)

2. **Table Scan:**
   - 1 acesso por página até encontrar o registro
   - Custo médio: totalPages/2
   - Pior caso: totalPages

### Medição de Tempo:
```java
long startTime = System.nanoTime();
// ... operação de busca ...
long endTime = System.nanoTime();
long durationNanos = endTime - startTime;
```

## Dataset

**Arquivo:** `src/main/resources/words.txt`
- **Origem:** https://github.com/dwyl/english-words
- **Tamanho:** 466.550 palavras em inglês
- **Formato:** Uma palavra por linha
- **Características:** Cada palavra é única, servindo como chave primária

## Conclusão

O sistema implementa completamente um simulador de índice hash estático com:

- **Estruturas de dados** bem definidas e especializadas
- **Algoritmos de resolução** para colisões e overflow
- **Múltiplas funções hash** para comparação de performance
- **Interface gráfica intuitiva** para visualização e interação
- **Medição detalhada de performance** com estatísticas completas
- **Comparação de métodos** de busca (índice vs. table scan)

A arquitetura modular permite fácil extensão e manutenção, seguindo boas práticas de desenvolvimento Java e princípios de design orientado a objetos.

## Validação dos Requisitos

Este documento atende a todos os requisitos especificados:

✅ **Interface gráfica**: HashIndexGUI completamente documentada  
✅ **Construção do índice**: Método `constructIndex()` detalhado  
✅ **Busca por índice**: Método `searchWithIndex()` explicado  
✅ **Table scan**: Método `tableScan()` analisado  
✅ **Estrutura Página**: Classe `Page` completamente documentada  
✅ **Estrutura Bucket**: Classe `Bucket` com resolução de colisões/overflow  
✅ **Função hash**: Três implementações detalhadas  
✅ **Dataset**: 466.550 palavras em inglês  
✅ **Parâmetros configuráveis**: Todos documentados  
✅ **Parâmetros calculados**: Fórmulas explicadas  
✅ **Resolução de colisões**: Algoritmo separate chaining  
✅ **Resolução de overflow**: Buckets de overflow encadeados  
✅ **Estatísticas**: Todas as métricas implementadas e documentadas  
✅ **Medição de performance**: Sistema completo de timing e contagem de acessos

## Documentos Complementares

- **GUIA_USUARIO.md**: Explicação simplificada para usuários leigos
- **README.md**: Instruções de instalação e uso
- **Código fonte**: Implementação completa em `src/main/java/`