package com.hashindex.util;

import com.hashindex.service.HashIndexService;
import com.hashindex.model.SearchResult;

/**
 * Command-line interface for testing the hash index functionality.
 */
public class HashIndexCLI {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Hash Index Simulator - CLI Test ===");
            System.out.println();
            
            HashIndexService service = new HashIndexService();
            
            // Test with different page sizes
            System.out.println("Loading data with page size 100...");
            service.loadData(100);
            
            System.out.println("Data loaded successfully!");
            System.out.println("Total records: " + String.format("%,d", service.getStatistics().getTotalRecords()));
            System.out.println("Total pages: " + String.format("%,d", service.getStatistics().getTotalPages()));
            System.out.println();
            
            // Show first and last pages
            System.out.println("First Page Info:");
            System.out.println(service.getFirstPage());
            System.out.println("Sample records: " + service.getFirstPage().getRecords().subList(0, Math.min(5, service.getFirstPage().size())));
            System.out.println();
            
            System.out.println("Last Page Info:");
            System.out.println(service.getLastPage());
            System.out.println("Sample records: " + service.getLastPage().getRecords().subList(0, Math.min(5, service.getLastPage().size())));
            System.out.println();
            
            // Construct index
            System.out.println("Constructing index with bucket capacity 5...");
            service.constructIndex(5);
            
            System.out.println("Index constructed successfully!");
            System.out.println("Total buckets: " + service.getBuckets().size());
            System.out.println("Hash function: " + service.getHashFunction().getName());
            System.out.println();
            
            // Test searches
            String[] testWords = {"hello", "world", "computer", "algorithm", "data"};
            
            for (String word : testWords) {
                System.out.println("=== Testing word: " + word + " ===");
                
                // Search with index
                SearchResult indexResult = service.searchWithIndex(word);
                System.out.println("Index search: " + indexResult);
                
                // Table scan
                SearchResult scanResult = service.tableScan(word);
                System.out.println("Table scan: " + scanResult);
                
                System.out.println("Time difference: " + 
                    String.format("%.2f ms", service.getStatistics().getTimeDifferenceMillis()));
                System.out.println();
            }
            
            // Show statistics
            System.out.println("=== Final Statistics ===");
            System.out.println(service.getStatistics());
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}