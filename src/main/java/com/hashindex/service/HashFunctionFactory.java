package com.hashindex.service;

/**
 * Factory class for creating hash function instances.
 */
public class HashFunctionFactory {
    
    public enum HashFunctionType {
        SIMPLE_MODULO,
        DJB2,
        FNV1A
    }
    
    public static HashFunction createHashFunction(HashFunctionType type) {
        switch (type) {
            case SIMPLE_MODULO:
                return new SimpleModuloHashFunction();
            case DJB2:
                return new DJB2HashFunction();
            case FNV1A:
                return new FNV1aHashFunction();
            default:
                throw new IllegalArgumentException("Unknown hash function type: " + type);
        }
    }
    
    public static HashFunction createDefaultHashFunction() {
        return createHashFunction(HashFunctionType.DJB2);
    }
}