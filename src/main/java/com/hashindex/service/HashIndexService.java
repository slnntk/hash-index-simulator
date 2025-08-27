package com.hashindex.service;

import com.hashindex.model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Main service class that implements the hash index functionality.
 */
public class HashIndexService {
    
    private List<Page> pages;
    private List<Bucket> buckets;
    private HashFunction hashFunction;
    private IndexStatistics statistics;
    private int pageSize;
    private int bucketCapacity;
    
    public HashIndexService() {
        this.pages = new ArrayList<>();
        this.buckets = new ArrayList<>();
        this.hashFunction = HashFunctionFactory.createDefaultHashFunction();
        this.statistics = new IndexStatistics();
        this.pageSize = 100; // Default page size
        this.bucketCapacity = 5; // Default bucket capacity
    }
    
    /**
     * Loads data from the words file and creates pages.
     * 
     * @param pageSize the size of each page
     * @throws IOException if there's an error reading the file
     */
    public void loadData(int pageSize) throws IOException {
        this.pageSize = pageSize;
        this.pages.clear();
        this.statistics.reset();
        
        List<String> words = loadWordsFromResource();
        createPages(words);
        
        statistics.setTotalRecords(words.size());
        statistics.setTotalPages(pages.size());
    }
    
    /**
     * Constructs the hash index by creating buckets and populating them.
     * 
     * @param bucketCapacity the capacity of each bucket
     */
    public void constructIndex(int bucketCapacity) {
        this.bucketCapacity = bucketCapacity;
        
        // Calculate number of buckets (NB > NR/FR)
        long totalRecords = statistics.getTotalRecords();
        int numberOfBuckets = (int) Math.ceil((double) totalRecords / bucketCapacity) + 1;
        
        // Create buckets
        this.buckets.clear();
        for (int i = 0; i < numberOfBuckets; i++) {
            buckets.add(new Bucket(i, bucketCapacity));
        }
        
        // Populate buckets
        populateBuckets();
        
        statistics.setTotalBuckets(numberOfBuckets);
        statistics.setBucketCapacity(bucketCapacity);
        
        // Calculate collision and overflow statistics
        calculateStatistics();
    }
    
    /**
     * Searches for a key using the hash index.
     * 
     * @param searchKey the key to search for
     * @return search result containing the page number and whether found
     */
    public SearchResult searchWithIndex(String searchKey) {
        long startTime = System.nanoTime();
        
        int bucketIndex = hashFunction.hash(searchKey, buckets.size());
        Bucket bucket = buckets.get(bucketIndex);
        
        int pageNumber = bucket.getPageNumber(searchKey);
        long accesses = 1; // At least one bucket access
        
        if (pageNumber != -1) {
            // Found in bucket, now read the page
            accesses++; // Page read
            Page page = pages.get(pageNumber);
            boolean found = page.containsRecord(searchKey);
            
            long endTime = System.nanoTime();
            statistics.setSearchTimeNanos(endTime - startTime);
            statistics.setSearchAccesses(accesses);
            
            return new SearchResult(found, pageNumber, (int) accesses, searchKey);
        }
        
        long endTime = System.nanoTime();
        statistics.setSearchTimeNanos(endTime - startTime);
        statistics.setSearchAccesses(accesses);
        
        return new SearchResult(false, -1, (int) accesses, searchKey);
    }
    
    /**
     * Performs a table scan to find the search key.
     * 
     * @param searchKey the key to search for
     * @return search result containing the page number and whether found
     */
    public SearchResult tableScan(String searchKey) {
        long startTime = System.nanoTime();
        
        int accesses = 0;
        
        for (Page page : pages) {
            accesses++; // Each page read counts as an access
            
            if (page.containsRecord(searchKey)) {
                long endTime = System.nanoTime();
                statistics.setTableScanTimeNanos(endTime - startTime);
                statistics.setTableScanAccesses(accesses);
                
                return new SearchResult(true, page.getPageNumber(), accesses, searchKey);
            }
        }
        
        long endTime = System.nanoTime();
        statistics.setTableScanTimeNanos(endTime - startTime);
        statistics.setTableScanAccesses(accesses);
        
        return new SearchResult(false, -1, accesses, searchKey);
    }
    
    /**
     * Loads words from the resource file.
     */
    private List<String> loadWordsFromResource() throws IOException {
        List<String> words = new ArrayList<>();
        
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("words.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    words.add(line);
                }
            }
        }
        
        return words;
    }
    
    /**
     * Creates pages from the loaded words.
     */
    private void createPages(List<String> words) {
        int pageNumber = 0;
        Page currentPage = new Page(pageNumber, pageSize);
        
        for (String word : words) {
            if (!currentPage.addRecord(word)) {
                // Current page is full, create a new one
                pages.add(currentPage);
                pageNumber++;
                currentPage = new Page(pageNumber, pageSize);
                currentPage.addRecord(word);
            }
        }
        
        // Add the last page if it has records
        if (!currentPage.isEmpty()) {
            pages.add(currentPage);
        }
    }
    
    /**
     * Populates buckets with entries from all pages.
     */
    private void populateBuckets() {
        for (Page page : pages) {
            for (String record : page.getRecords()) {
                int bucketIndex = hashFunction.hash(record, buckets.size());
                Bucket bucket = buckets.get(bucketIndex);
                
                // Check if this causes a collision (bucket already has entries)
                if (!bucket.getEntries().isEmpty()) {
                    statistics.incrementCollisions();
                }
                
                BucketEntry entry = new BucketEntry(record, page.getPageNumber());
                bucket.addEntry(entry);
            }
        }
    }
    
    /**
     * Calculates collision and overflow statistics.
     */
    private void calculateStatistics() {
        long overflowCount = 0;
        
        for (Bucket bucket : buckets) {
            if (bucket.hasOverflow()) {
                overflowCount += bucket.getOverflowCount();
            }
        }
        
        statistics.setOverflows(overflowCount);
    }
    
    // Getters
    public List<Page> getPages() { return new ArrayList<>(pages); }
    public List<Bucket> getBuckets() { return new ArrayList<>(buckets); }
    public HashFunction getHashFunction() { return hashFunction; }
    public IndexStatistics getStatistics() { return statistics; }
    public int getPageSize() { return pageSize; }
    public int getBucketCapacity() { return bucketCapacity; }
    
    public void setHashFunction(HashFunction hashFunction) {
        this.hashFunction = hashFunction;
    }
    
    /**
     * Gets the first page for display purposes.
     */
    public Page getFirstPage() {
        return pages.isEmpty() ? null : pages.get(0);
    }
    
    /**
     * Gets the last page for display purposes.
     */
    public Page getLastPage() {
        return pages.isEmpty() ? null : pages.get(pages.size() - 1);
    }
}