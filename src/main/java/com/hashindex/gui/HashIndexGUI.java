package com.hashindex.gui;

import com.hashindex.model.*;
import com.hashindex.service.HashFunctionFactory;
import com.hashindex.service.HashIndexService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Main GUI window for the Hash Index Simulator.
 */
public class HashIndexGUI extends JFrame {
    
    private final HashIndexService service;
    
    // GUI components
    private JTextField pageSizeField;
    private JTextField bucketCapacityField;
    private JComboBox<HashFunctionFactory.HashFunctionType> hashFunctionCombo;
    private JTextField searchKeyField;
    private JButton loadDataButton;
    private JButton constructIndexButton;
    private JButton searchButton;
    private JButton tableScanButton;
    
    // Display areas
    private JTextArea firstPageArea;
    private JTextArea lastPageArea;
    private JTextArea bucketsArea;
    private JTextArea statisticsArea;
    private JTextArea searchResultArea;
    private JLabel statusLabel;
    
    public HashIndexGUI() {
        this.service = new HashIndexService();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Hash Index Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create components
        createControlPanel();
        createDisplayPanel();
        createStatusBar();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void createControlPanel() {
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(new TitledBorder("Controls"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Page size input
        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Page Size:"), gbc);
        gbc.gridx = 1;
        pageSizeField = new JTextField("100", 10);
        controlPanel.add(pageSizeField, gbc);
        
        // Bucket capacity input
        gbc.gridx = 2; gbc.gridy = 0;
        controlPanel.add(new JLabel("Bucket Capacity:"), gbc);
        gbc.gridx = 3;
        bucketCapacityField = new JTextField("5", 10);
        controlPanel.add(bucketCapacityField, gbc);
        
        // Hash function selection
        gbc.gridx = 0; gbc.gridy = 1;
        controlPanel.add(new JLabel("Hash Function:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        hashFunctionCombo = new JComboBox<>(HashFunctionFactory.HashFunctionType.values());
        hashFunctionCombo.setSelectedItem(HashFunctionFactory.HashFunctionType.DJB2);
        controlPanel.add(hashFunctionCombo, gbc);
        
        // Load data button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        loadDataButton = new JButton("Load Data");
        loadDataButton.addActionListener(new LoadDataAction());
        controlPanel.add(loadDataButton, gbc);
        
        // Construct index button
        gbc.gridx = 2; gbc.gridwidth = 2;
        constructIndexButton = new JButton("Construct Index");
        constructIndexButton.addActionListener(new ConstructIndexAction());
        constructIndexButton.setEnabled(false);
        controlPanel.add(constructIndexButton, gbc);
        
        // Search controls
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        controlPanel.add(new JLabel("Search Key:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        searchKeyField = new JTextField(15);
        controlPanel.add(searchKeyField, gbc);
        
        // Search button
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        searchButton = new JButton("Search with Index");
        searchButton.addActionListener(new SearchAction());
        searchButton.setEnabled(false);
        controlPanel.add(searchButton, gbc);
        
        // Table scan button
        gbc.gridx = 2; gbc.gridwidth = 2;
        tableScanButton = new JButton("Table Scan");
        tableScanButton.addActionListener(new TableScanAction());
        tableScanButton.setEnabled(false);
        controlPanel.add(tableScanButton, gbc);
        
        add(controlPanel, BorderLayout.NORTH);
    }
    
    private void createDisplayPanel() {
        JPanel displayPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        displayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // First page display
        JPanel firstPagePanel = new JPanel(new BorderLayout());
        firstPagePanel.setBorder(new TitledBorder("First Page"));
        firstPageArea = new JTextArea(8, 20);
        firstPageArea.setEditable(false);
        firstPageArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        firstPagePanel.add(new JScrollPane(firstPageArea), BorderLayout.CENTER);
        displayPanel.add(firstPagePanel);
        
        // Last page display
        JPanel lastPagePanel = new JPanel(new BorderLayout());
        lastPagePanel.setBorder(new TitledBorder("Last Page"));
        lastPageArea = new JTextArea(8, 20);
        lastPageArea.setEditable(false);
        lastPageArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        lastPagePanel.add(new JScrollPane(lastPageArea), BorderLayout.CENTER);
        displayPanel.add(lastPagePanel);
        
        // Buckets display
        JPanel bucketsPanel = new JPanel(new BorderLayout());
        bucketsPanel.setBorder(new TitledBorder("Bucket Information"));
        bucketsArea = new JTextArea(8, 20);
        bucketsArea.setEditable(false);
        bucketsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        bucketsPanel.add(new JScrollPane(bucketsArea), BorderLayout.CENTER);
        displayPanel.add(bucketsPanel);
        
        // Statistics display
        JPanel statisticsPanel = new JPanel(new BorderLayout());
        statisticsPanel.setBorder(new TitledBorder("Statistics"));
        statisticsArea = new JTextArea(8, 20);
        statisticsArea.setEditable(false);
        statisticsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        statisticsPanel.add(new JScrollPane(statisticsArea), BorderLayout.CENTER);
        displayPanel.add(statisticsPanel);
        
        // Search results display
        JPanel searchResultPanel = new JPanel(new BorderLayout());
        searchResultPanel.setBorder(new TitledBorder("Search Results"));
        searchResultArea = new JTextArea(8, 20);
        searchResultArea.setEditable(false);
        searchResultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        searchResultPanel.add(new JScrollPane(searchResultArea), BorderLayout.CENTER);
        displayPanel.add(searchResultPanel);
        
        // Empty panel for layout
        displayPanel.add(new JPanel());
        
        add(displayPanel, BorderLayout.CENTER);
    }
    
    private void createStatusBar() {
        statusLabel = new JLabel("Ready to load data...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private class LoadDataAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int pageSize = Integer.parseInt(pageSizeField.getText().trim());
                if (pageSize <= 0) {
                    showError("Page size must be positive");
                    return;
                }
                
                statusLabel.setText("Loading data...");
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
                // Load data in background thread to keep GUI responsive
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        service.loadData(pageSize);
                        return null;
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            get(); // Check for exceptions
                            updatePageDisplays();
                            constructIndexButton.setEnabled(true);
                            statusLabel.setText("Data loaded successfully. Total records: " + 
                                              String.format("%,d", service.getStatistics().getTotalRecords()));
                        } catch (Exception ex) {
                            showError("Error loading data: " + ex.getMessage());
                            statusLabel.setText("Error loading data");
                        } finally {
                            setCursor(Cursor.getDefaultCursor());
                        }
                    }
                };
                worker.execute();
                
            } catch (NumberFormatException ex) {
                showError("Invalid page size");
            }
        }
    }
    
    private class ConstructIndexAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int bucketCapacity = Integer.parseInt(bucketCapacityField.getText().trim());
                if (bucketCapacity <= 0) {
                    showError("Bucket capacity must be positive");
                    return;
                }
                
                statusLabel.setText("Constructing index...");
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        HashFunctionFactory.HashFunctionType selectedType = 
                            (HashFunctionFactory.HashFunctionType) hashFunctionCombo.getSelectedItem();
                        service.setHashFunction(HashFunctionFactory.createHashFunction(selectedType));
                        service.constructIndex(bucketCapacity);
                        return null;
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            get(); // Check for exceptions
                            updateBucketDisplay();
                            updateStatisticsDisplay();
                            searchButton.setEnabled(true);
                            tableScanButton.setEnabled(true);
                            statusLabel.setText("Index constructed successfully. Total buckets: " + 
                                              service.getBuckets().size());
                        } catch (Exception ex) {
                            showError("Error constructing index: " + ex.getMessage());
                            statusLabel.setText("Error constructing index");
                        } finally {
                            setCursor(Cursor.getDefaultCursor());
                        }
                    }
                };
                worker.execute();
                
            } catch (NumberFormatException ex) {
                showError("Invalid bucket capacity");
            }
        }
    }
    
    private class SearchAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchKey = searchKeyField.getText().trim();
            if (searchKey.isEmpty()) {
                showError("Please enter a search key");
                return;
            }
            
            statusLabel.setText("Searching with index...");
            SearchResult result = service.searchWithIndex(searchKey);
            
            StringBuilder sb = new StringBuilder();
            sb.append("=== SEARCH WITH INDEX ===\n");
            sb.append("Search Key: ").append(searchKey).append("\n");
            sb.append("Result: ").append(result.found() ? "FOUND" : "NOT FOUND").append("\n");
            if (result.found()) {
                sb.append("Page Number: ").append(result.pageNumber()).append("\n");
            }
            sb.append("Pages Accessed: ").append(result.accessCount()).append("\n");
            sb.append("Time: ").append(String.format("%.2f ms", 
                service.getStatistics().getSearchTimeNanos() / 1_000_000.0)).append("\n");
            
            searchResultArea.setText(sb.toString());
            updateStatisticsDisplay();
            statusLabel.setText("Search completed");
        }
    }
    
    private class TableScanAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchKey = searchKeyField.getText().trim();
            if (searchKey.isEmpty()) {
                showError("Please enter a search key");
                return;
            }
            
            statusLabel.setText("Performing table scan...");
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            SwingWorker<SearchResult, Void> worker = new SwingWorker<SearchResult, Void>() {
                @Override
                protected SearchResult doInBackground() throws Exception {
                    return service.tableScan(searchKey);
                }
                
                @Override
                protected void done() {
                    try {
                        SearchResult result = get();
                        
                        StringBuilder sb = new StringBuilder();
                        sb.append("=== TABLE SCAN ===\n");
                        sb.append("Search Key: ").append(searchKey).append("\n");
                        sb.append("Result: ").append(result.found() ? "FOUND" : "NOT FOUND").append("\n");
                        if (result.found()) {
                            sb.append("Page Number: ").append(result.pageNumber()).append("\n");
                        }
                        sb.append("Pages Accessed: ").append(result.accessCount()).append("\n");
                        sb.append("Time: ").append(String.format("%.2f ms", 
                            service.getStatistics().getTableScanTimeNanos() / 1_000_000.0)).append("\n\n");
                        
                        sb.append("=== PERFORMANCE COMPARISON ===\n");
                        sb.append("Time Difference: ").append(String.format("%.2f ms", 
                            service.getStatistics().getTimeDifferenceMillis())).append("\n");
                        sb.append("(Positive = Table Scan slower)\n");
                        
                        searchResultArea.setText(sb.toString());
                        updateStatisticsDisplay();
                        statusLabel.setText("Table scan completed");
                    } catch (Exception ex) {
                        showError("Error during table scan: " + ex.getMessage());
                        statusLabel.setText("Table scan error");
                    } finally {
                        setCursor(Cursor.getDefaultCursor());
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void updatePageDisplays() {
        Page firstPage = service.getFirstPage();
        Page lastPage = service.getLastPage();
        
        if (firstPage != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Page ").append(firstPage.getPageNumber()).append(" (First)\n");
            sb.append("Capacity: ").append(firstPage.getCapacity()).append("\n");
            sb.append("Records: ").append(firstPage.size()).append("\n");
            sb.append("Content:\n");
            List<String> records = firstPage.getRecords();
            for (int i = 0; i < Math.min(10, records.size()); i++) {
                sb.append("  ").append(records.get(i)).append("\n");
            }
            if (records.size() > 10) {
                sb.append("  ... and ").append(records.size() - 10).append(" more\n");
            }
            firstPageArea.setText(sb.toString());
        }
        
        if (lastPage != null && !lastPage.equals(firstPage)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Page ").append(lastPage.getPageNumber()).append(" (Last)\n");
            sb.append("Capacity: ").append(lastPage.getCapacity()).append("\n");
            sb.append("Records: ").append(lastPage.size()).append("\n");
            sb.append("Content:\n");
            List<String> records = lastPage.getRecords();
            for (int i = 0; i < Math.min(10, records.size()); i++) {
                sb.append("  ").append(records.get(i)).append("\n");
            }
            if (records.size() > 10) {
                sb.append("  ... and ").append(records.size() - 10).append(" more\n");
            }
            lastPageArea.setText(sb.toString());
        }
    }
    
    private void updateBucketDisplay() {
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
        
        bucketsArea.setText(sb.toString());
    }
    
    private void updateStatisticsDisplay() {
        statisticsArea.setText(service.getStatistics().toString());
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}