package com.hashindex.model;

/**
 * Represents the result of a search operation.
 */
public record SearchResult(
    boolean found,
    int pageNumber,
    int accessCount,
    String searchKey
) {
    
    @Override
    public String toString() {
        if (found) {
            return String.format("Found '%s' on page %d (accessed %d pages)", 
                               searchKey, pageNumber, accessCount);
        } else {
            return String.format("Key '%s' not found (accessed %d pages)", 
                               searchKey, accessCount);
        }
    }
}