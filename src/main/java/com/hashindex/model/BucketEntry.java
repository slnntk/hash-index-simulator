package com.hashindex.model;

/**
 * Represents a bucket entry that maps a search key to a page address.
 */
public record BucketEntry(String searchKey, int pageNumber) {
    
    @Override
    public String toString() {
        return String.format("(%s -> Page %d)", searchKey, pageNumber);
    }
}