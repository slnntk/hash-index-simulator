package com.hashindex.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bucket in the hash index that contains mappings from search keys to page addresses.
 * Each bucket has a fixed capacity and uses overflow buckets for handling bucket overflow.
 */
public class Bucket {
    private final int bucketNumber;
    private final int capacity;
    private final List<BucketEntry> entries;
    private Bucket overflowBucket;
    private boolean isOverflow;
    
    public Bucket(int bucketNumber, int capacity) {
        this(bucketNumber, capacity, false);
    }
    
    private Bucket(int bucketNumber, int capacity, boolean isOverflow) {
        this.bucketNumber = bucketNumber;
        this.capacity = capacity;
        this.entries = new ArrayList<>(capacity);
        this.isOverflow = isOverflow;
    }
    
    /**
     * Adds an entry to this bucket. If the bucket is full, creates or uses an overflow bucket.
     * 
     * @param entry the entry to add
     * @return true if the entry was added successfully
     */
    public boolean addEntry(BucketEntry entry) {
        if (entries.size() < capacity) {
            entries.add(entry);
            return true;
        } else {
            // Handle overflow
            if (overflowBucket == null) {
                overflowBucket = new Bucket(bucketNumber, capacity, true);
            }
            return overflowBucket.addEntry(entry);
        }
    }
    
    /**
     * Searches for an entry with the given search key.
     * 
     * @param searchKey the key to search for
     * @return the bucket entry if found, null otherwise
     */
    public BucketEntry findEntry(String searchKey) {
        // Search in main bucket
        for (BucketEntry entry : entries) {
            if (entry.searchKey().equals(searchKey)) {
                return entry;
            }
        }
        
        // Search in overflow bucket if exists
        if (overflowBucket != null) {
            return overflowBucket.findEntry(searchKey);
        }
        
        return null;
    }
    
    /**
     * Gets the page number for the given search key.
     * 
     * @param searchKey the key to search for
     * @return the page number if found, -1 otherwise
     */
    public int getPageNumber(String searchKey) {
        BucketEntry entry = findEntry(searchKey);
        return entry != null ? entry.pageNumber() : -1;
    }
    
    /**
     * Returns the total number of entries in this bucket (including overflow buckets).
     * 
     * @return total number of entries
     */
    public int getTotalEntries() {
        int total = entries.size();
        if (overflowBucket != null) {
            total += overflowBucket.getTotalEntries();
        }
        return total;
    }
    
    /**
     * Checks if this bucket has overflow.
     * 
     * @return true if this bucket has overflow buckets
     */
    public boolean hasOverflow() {
        return overflowBucket != null;
    }
    
    /**
     * Counts the total number of overflow buckets in the chain.
     * 
     * @return number of overflow buckets
     */
    public int getOverflowCount() {
        if (overflowBucket == null) {
            return 0;
        }
        return 1 + overflowBucket.getOverflowCount();
    }
    
    public int getBucketNumber() {
        return bucketNumber;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public List<BucketEntry> getEntries() {
        return new ArrayList<>(entries);
    }
    
    public Bucket getOverflowBucket() {
        return overflowBucket;
    }
    
    public boolean isOverflow() {
        return isOverflow;
    }
    
    public boolean isFull() {
        return entries.size() >= capacity;
    }
    
    public int size() {
        return entries.size();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bucket{bucketNumber=").append(bucketNumber)
          .append(", capacity=").append(capacity)
          .append(", entries=").append(entries.size());
        
        if (hasOverflow()) {
            sb.append(", hasOverflow=true, overflowCount=").append(getOverflowCount());
        }
        
        sb.append('}');
        return sb.toString();
    }
}