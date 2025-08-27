package com.hashindex.gui.display;

import com.hashindex.model.*;
import com.hashindex.service.HashIndexService;
import com.hashindex.gui.components.DisplayPanel;

import java.util.List;

/**
 * Manages all display updates for the Hash Index Simulator.
 * Follows Single Responsibility Principle by handling only display logic.
 */
public class DisplayManager {
    
    private final HashIndexService service;
    private final DisplayPanel displayPanel;
    
    public DisplayManager(HashIndexService service, DisplayPanel displayPanel) {
        this.service = service;
        this.displayPanel = displayPanel;
    }
    
    public void updatePageDisplays() {
        Page firstPage = service.getFirstPage();
        Page lastPage = service.getLastPage();
        
        updateFirstPage(firstPage);
        updateLastPage(lastPage, firstPage);
    }
    
    private void updateFirstPage(Page firstPage) {
        if (firstPage != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Page ").append(firstPage.getPageNumber()).append(" (First)\n");
            sb.append("Capacity: ").append(firstPage.getCapacity()).append("\n");
            sb.append("Records: ").append(firstPage.size()).append("\n");
            sb.append("Content:\n");
            
            List<String> records = firstPage.getRecords();
            appendRecords(sb, records);
            
            displayPanel.getFirstPageArea().setText(sb.toString());
        }
    }
    
    private void updateLastPage(Page lastPage, Page firstPage) {
        if (lastPage != null && !lastPage.equals(firstPage)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Page ").append(lastPage.getPageNumber()).append(" (Last)\n");
            sb.append("Capacity: ").append(lastPage.getCapacity()).append("\n");
            sb.append("Records: ").append(lastPage.size()).append("\n");
            sb.append("Content:\n");
            
            List<String> records = lastPage.getRecords();
            appendRecords(sb, records);
            
            displayPanel.getLastPageArea().setText(sb.toString());
        }
    }
    
    private void appendRecords(StringBuilder sb, List<String> records) {
        int maxDisplay = 10;
        for (int i = 0; i < Math.min(maxDisplay, records.size()); i++) {
            sb.append("  ").append(records.get(i)).append("\n");
        }
        if (records.size() > maxDisplay) {
            sb.append("  ... and ").append(records.size() - maxDisplay).append(" more\n");
        }
    }
    
    public void updateBucketDisplay() {
        StringBuilder sb = new StringBuilder();
        List<Bucket> buckets = service.getBuckets();
        
        sb.append("Total Buckets: ").append(buckets.size()).append("\n");
        sb.append("Bucket Capacity: ").append(service.getBucketCapacity()).append("\n");
        sb.append("Hash Function: ").append(service.getHashFunction().getName()).append("\n\n");
        
        sb.append("First 10 buckets:\n");
        for (int i = 0; i < Math.min(10, buckets.size()); i++) {
            Bucket bucket = buckets.get(i);
            sb.append("Bucket ").append(i).append(": ");
            sb.append(bucket.size()).append(" entries");
            if (bucket.hasOverflow()) {
                sb.append(" (").append(bucket.getOverflowCount()).append(" overflow)");
            }
            sb.append("\n");
        }
        
        displayPanel.getBucketsArea().setText(sb.toString());
    }
    
    public void updateStatisticsDisplay() {
        displayPanel.getStatisticsArea().setText(service.getStatistics().toString());
    }
    
    public void updateSearchResults(SearchResult result, String searchKey, String searchType) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(searchType.toUpperCase()).append(" ===\n");
        sb.append("Search Key: ").append(searchKey).append("\n");
        sb.append("Result: ").append(result.found() ? "FOUND" : "NOT FOUND").append("\n");
        if (result.found()) {
            sb.append("Page Number: ").append(result.pageNumber()).append("\n");
        }
        sb.append("Pages Accessed: ").append(result.accessCount()).append("\n");
        
        if ("SEARCH WITH INDEX".equals(searchType)) {
            sb.append("Time: ").append(String.format("%.2f ms", 
                service.getStatistics().getSearchTimeNanos() / 1_000_000.0)).append("\n");
        } else if ("TABLE SCAN".equals(searchType)) {
            sb.append("Time: ").append(String.format("%.2f ms", 
                service.getStatistics().getTableScanTimeNanos() / 1_000_000.0)).append("\n\n");
            
            sb.append("=== PERFORMANCE COMPARISON ===\n");
            sb.append("Time Difference: ").append(String.format("%.2f ms", 
                service.getStatistics().getTimeDifferenceMillis())).append("\n");
            sb.append("(Positive = Table Scan slower)\n");
        }
        
        displayPanel.getSearchResultArea().setText(sb.toString());
        updateStatisticsDisplay();
    }
}