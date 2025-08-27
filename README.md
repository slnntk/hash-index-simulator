# Hash Index Simulator

A Java application that implements and simulates a static hash index with a graphical user interface. This educational tool demonstrates hash indexing concepts, collision resolution, bucket overflow handling, and performance comparisons between index searches and table scans.

## Features

### Core Functionality
- **Static Hash Index Implementation**: Complete implementation of a hash index with collision and overflow resolution
- **Multiple Hash Functions**: Support for Simple Modulo, DJB2, and FNV-1a hash functions
- **Data Structures**: Page-based data storage with configurable page sizes and bucket-based indexing
- **Performance Analysis**: Detailed statistics and performance comparisons

### GUI Interface
- **Interactive Controls**: Configure page size, bucket capacity, and hash function
- **Data Visualization**: View first and last pages, bucket information, and statistics
- **Search Operations**: Perform both index-based searches and table scans
- **Real-time Statistics**: Monitor collision rates, overflow rates, and performance metrics

### Statistics Tracked
- Collision rate (%)
- Overflow rate (%)
- Cost estimation (disk access count - page reads)
- Time comparison between index search and table scan
- Total records, pages, and buckets

## Requirements

- Java 17 or higher
- Maven 3.6+ (for building from source)

## Usage

### Running the Application

#### Option 1: Run the GUI Application
```bash
java -jar target/hash-index-simulator-1.0.0.jar
```

#### Option 2: Build and Run from Source
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.hashindex.HashIndexSimulatorApp"
```

#### Option 3: CLI Testing (for validation)
```bash
mvn exec:java -Dexec.mainClass="com.hashindex.util.HashIndexCLI"
```

### Building from Source

1. Clone the repository
2. Ensure Java 17+ is installed
3. Build the project:
```bash
mvn clean package
```

### Using the GUI

1. **Load Data**: 
   - Set desired page size (default: 100)
   - Click "Load Data" to load the English words dataset
   - View first and last pages in the display panels

2. **Configure Index**:
   - Set bucket capacity (default: 5)
   - Select hash function (Simple Modulo, DJB2, or FNV-1a)
   - Click "Construct Index" to build the hash index

3. **Search Operations**:
   - Enter a search key in the search field
   - Click "Search with Index" for hash index search
   - Click "Table Scan" for sequential search
   - Compare performance results

4. **View Statistics**:
   - Monitor collision and overflow rates
   - Compare search times and disk access counts
   - Analyze performance differences

## Implementation Details

### Data Structures

- **Page**: Represents physical data storage with configurable capacity
- **Bucket**: Maps search keys to page addresses with overflow handling
- **BucketEntry**: Key-page mapping records
- **IndexStatistics**: Comprehensive performance tracking

### Hash Functions

1. **Simple Modulo**: Uses Java's hashCode() with modulo operation
2. **DJB2**: Popular string hashing algorithm (hash * 33 + char)
3. **FNV-1a**: Fast hash function with good distribution

### Collision Resolution

- **Separate Chaining**: Each bucket maintains a list of entries
- **Overflow Buckets**: Additional buckets created when primary buckets exceed capacity

## Dataset

The application uses a dataset of 466,550 English words from the [dwyl/english-words](https://github.com/dwyl/english-words) repository.

## Architecture

```
com.hashindex/
├── model/          # Data structures (Page, Bucket, Statistics)
├── service/        # Core logic (HashIndexService, HashFunction)
├── gui/            # Swing-based user interface
└── util/           # Utilities and CLI tools
```

## Testing

Run the test suite:
```bash
mvn test
```

The project includes comprehensive unit tests for:
- Data loading and page creation
- Index construction and search operations
- Hash function implementations
- Performance measurement

## Educational Objectives

This simulator demonstrates:
- Hash index construction and operation
- Collision handling strategies
- Bucket overflow management
- Performance analysis of different search methods
- Impact of hash function choice on distribution
- Trade-offs between index size and performance

## License

This project is intended for educational purposes.