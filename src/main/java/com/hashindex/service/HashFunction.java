package com.hashindex.service;

/**
 * Hash function interface for mapping search keys to bucket addresses.
 */
public interface HashFunction {
    
    /**
     * Computes the hash value for the given key.
     * 
     * @param key the key to hash
     * @param bucketCount the number of buckets available
     * @return the bucket number (0 to bucketCount-1)
     */
    int hash(String key, int bucketCount);
    
    /**
     * Returns the name of this hash function.
     * 
     * @return the name of the hash function
     */
    String getName();
}

/**
 * Simple modulo-based hash function using Java's hashCode().
 */
class SimpleModuloHashFunction implements HashFunction {
    
    @Override
    public int hash(String key, int bucketCount) {
        if (key == null || bucketCount <= 0) {
            return 0;
        }
        return Math.abs(key.hashCode()) % bucketCount;
    }
    
    @Override
    public String getName() {
        return "Simple Modulo Hash";
    }
}

/**
 * DJB2 hash function - a popular string hashing algorithm.
 */
class DJB2HashFunction implements HashFunction {
    
    @Override
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
    
    @Override
    public String getName() {
        return "DJB2 Hash";
    }
}

/**
 * FNV-1a hash function - another popular string hashing algorithm.
 */
class FNV1aHashFunction implements HashFunction {
    private static final long FNV_OFFSET_BASIS = 2166136261L;
    private static final long FNV_PRIME = 16777619L;
    
    @Override
    public int hash(String key, int bucketCount) {
        if (key == null || bucketCount <= 0) {
            return 0;
        }
        
        long hash = FNV_OFFSET_BASIS;
        for (byte b : key.getBytes()) {
            hash ^= b;
            hash *= FNV_PRIME;
        }
        
        return (int) (Math.abs(hash) % bucketCount);
    }
    
    @Override
    public String getName() {
        return "FNV-1a Hash";
    }
}