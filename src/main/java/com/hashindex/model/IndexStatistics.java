package com.hashindex.model;

/**
 * Represents the statistics collected during hash index operations.
 */
public class IndexStatistics {
    private long totalRecords;
    private int totalPages;
    private int totalBuckets;
    private int bucketCapacity;
    private long collisions;
    private long overflows;
    private long searchAccesses;
    private long tableScanAccesses;
    private long searchTimeNanos;
    private long tableScanTimeNanos;
    
    public IndexStatistics() {
        reset();
    }
    
    public void reset() {
        this.totalRecords = 0;
        this.totalPages = 0;
        this.totalBuckets = 0;
        this.bucketCapacity = 0;
        this.collisions = 0;
        this.overflows = 0;
        this.searchAccesses = 0;
        this.tableScanAccesses = 0;
        this.searchTimeNanos = 0;
        this.tableScanTimeNanos = 0;
    }
    
    /**
     * Calculates the collision rate as a percentage.
     * 
     * @return collision rate percentage
     */
    public double getCollisionRate() {
        if (totalRecords == 0) return 0.0;
        return (collisions * 100.0) / totalRecords;
    }
    
    /**
     * Calculates the overflow rate as a percentage.
     * 
     * @return overflow rate percentage
     */
    public double getOverflowRate() {
        if (totalBuckets == 0) return 0.0;
        return (overflows * 100.0) / totalBuckets;
    }
    
    /**
     * Gets the time difference between search and table scan in milliseconds.
     * 
     * @return time difference in milliseconds
     */
    public double getTimeDifferenceMillis() {
        return (tableScanTimeNanos - searchTimeNanos) / 1_000_000.0;
    }
    
    // Getters and setters
    public long getTotalRecords() { return totalRecords; }
    public void setTotalRecords(long totalRecords) { this.totalRecords = totalRecords; }
    
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    
    public int getTotalBuckets() { return totalBuckets; }
    public void setTotalBuckets(int totalBuckets) { this.totalBuckets = totalBuckets; }
    
    public int getBucketCapacity() { return bucketCapacity; }
    public void setBucketCapacity(int bucketCapacity) { this.bucketCapacity = bucketCapacity; }
    
    public long getCollisions() { return collisions; }
    public void setCollisions(long collisions) { this.collisions = collisions; }
    public void incrementCollisions() { this.collisions++; }
    
    public long getOverflows() { return overflows; }
    public void setOverflows(long overflows) { this.overflows = overflows; }
    public void incrementOverflows() { this.overflows++; }
    
    public long getSearchAccesses() { return searchAccesses; }
    public void setSearchAccesses(long searchAccesses) { this.searchAccesses = searchAccesses; }
    
    public long getTableScanAccesses() { return tableScanAccesses; }
    public void setTableScanAccesses(long tableScanAccesses) { this.tableScanAccesses = tableScanAccesses; }
    
    public long getSearchTimeNanos() { return searchTimeNanos; }
    public void setSearchTimeNanos(long searchTimeNanos) { this.searchTimeNanos = searchTimeNanos; }
    
    public long getTableScanTimeNanos() { return tableScanTimeNanos; }
    public void setTableScanTimeNanos(long tableScanTimeNanos) { this.tableScanTimeNanos = tableScanTimeNanos; }
    
    @Override
    public String toString() {
        return "IndexStatistics {\n" +
                "    Total Records: " + String.format("%,d", totalRecords) + "\n" +
                "    Total Pages: " + String.format("%,d", totalPages) + "\n" +
                "    Total Buckets: " + String.format("%,d", totalBuckets) + "\n" +
                "    Bucket Capacity: " + bucketCapacity + "\n" +
                "    Collisions: " + String.format("%,d", collisions) + " (" + String.format("%.2f", getCollisionRate()) + "%)\n" +
                "    Overflows: " + String.format("%,d", overflows) + " (" + String.format("%.2f", getOverflowRate()) + "%)\n" +
                "    Search Accesses: " + String.format("%,d", searchAccesses) + "\n" +
                "    Table Scan Accesses: " + String.format("%,d", tableScanAccesses) + "\n" +
                "    Search Time: " + String.format("%.2f", searchTimeNanos / 1_000_000.0) + " ms\n" +
                "    Table Scan Time: " + String.format("%.2f", tableScanTimeNanos / 1_000_000.0) + " ms\n" +
                "    Time Difference: " + String.format("%.2f", getTimeDifferenceMillis()) + " ms\n" +
                "}";
    }
}