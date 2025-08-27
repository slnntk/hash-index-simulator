package com.hashindex;

import com.hashindex.model.*;
import com.hashindex.service.HashFunctionFactory;
import com.hashindex.service.HashIndexService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HashIndexServiceTest {
    
    private HashIndexService service;
    
    @BeforeEach
    void setUp() {
        service = new HashIndexService();
    }
    
    @Test
    void testLoadData() throws Exception {
        service.loadData(100);
        
        assertThat(service.getPages()).isNotEmpty();
        assertThat(service.getStatistics().getTotalRecords()).isGreaterThan(0);
        assertThat(service.getStatistics().getTotalPages()).isGreaterThan(0);
    }
    
    @Test
    void testConstructIndex() throws Exception {
        service.loadData(100);
        service.constructIndex(5);
        
        assertThat(service.getBuckets()).isNotEmpty();
        assertThat(service.getStatistics().getTotalBuckets()).isGreaterThan(0);
    }
    
    @Test
    void testSearchWithIndex() throws Exception {
        service.loadData(100);
        service.constructIndex(5);
        
        // Test with a word we know exists (first word from first page)
        Page firstPage = service.getFirstPage();
        if (firstPage != null && !firstPage.getRecords().isEmpty()) {
            String testWord = firstPage.getRecords().get(0);
            SearchResult result = service.searchWithIndex(testWord);
            
            assertThat(result.found()).isTrue();
            assertThat(result.searchKey()).isEqualTo(testWord);
            assertThat(result.accessCount()).isGreaterThan(0);
        }
    }
    
    @Test
    void testTableScan() throws Exception {
        service.loadData(100);
        service.constructIndex(5);
        
        // Test with a word we know exists
        Page firstPage = service.getFirstPage();
        if (firstPage != null && !firstPage.getRecords().isEmpty()) {
            String testWord = firstPage.getRecords().get(0);
            SearchResult result = service.tableScan(testWord);
            
            assertThat(result.found()).isTrue();
            assertThat(result.searchKey()).isEqualTo(testWord);
            assertThat(result.accessCount()).isGreaterThan(0);
        }
    }
    
    @Test
    void testSearchNonExistentWord() throws Exception {
        service.loadData(100);
        service.constructIndex(5);
        
        SearchResult indexResult = service.searchWithIndex("nonexistentword12345");
        SearchResult scanResult = service.tableScan("nonexistentword12345");
        
        assertThat(indexResult.found()).isFalse();
        assertThat(scanResult.found()).isFalse();
    }
}