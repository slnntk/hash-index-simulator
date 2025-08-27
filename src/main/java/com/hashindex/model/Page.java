package com.hashindex.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a physical page that stores data records.
 * Each page has a fixed capacity and contains a list of records (words).
 */
public class Page {
    private final int pageNumber;
    private final int capacity;
    private final List<String> records;
    
    public Page(int pageNumber, int capacity) {
        this.pageNumber = pageNumber;
        this.capacity = capacity;
        this.records = new ArrayList<>(capacity);
    }
    
    /**
     * Adds a record to this page if there's space available.
     * 
     * @param record the record to add
     * @return true if the record was added successfully, false if the page is full
     */
    public boolean addRecord(String record) {
        if (records.size() >= capacity) {
            return false;
        }
        records.add(record);
        return true;
    }
    
    /**
     * Searches for a record in this page.
     * 
     * @param searchKey the key to search for
     * @return true if the record is found in this page
     */
    public boolean containsRecord(String searchKey) {
        return records.contains(searchKey);
    }
    
    /**
     * Gets a record by index.
     * 
     * @param index the index of the record
     * @return the record at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public String getRecord(int index) {
        return records.get(index);
    }
    
    public int getPageNumber() {
        return pageNumber;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public List<String> getRecords() {
        return new ArrayList<>(records);
    }
    
    public int size() {
        return records.size();
    }
    
    public boolean isFull() {
        return records.size() >= capacity;
    }
    
    public boolean isEmpty() {
        return records.isEmpty();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return pageNumber == page.pageNumber;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(pageNumber);
    }
    
    @Override
    public String toString() {
        return "Page{" +
                "pageNumber=" + pageNumber +
                ", capacity=" + capacity +
                ", recordCount=" + records.size() +
                '}';
    }
}