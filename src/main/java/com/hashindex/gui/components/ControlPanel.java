package com.hashindex.gui.components;

import com.hashindex.service.HashFunctionFactory;
import com.hashindex.gui.actions.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel containing all control elements for the Hash Index Simulator.
 * Follows Single Responsibility Principle by handling only UI control layout.
 */
public class ControlPanel extends JPanel {
    
    private JTextField pageSizeField;
    private JTextField bucketCapacityField;
    private JComboBox<HashFunctionFactory.HashFunctionType> hashFunctionCombo;
    private JTextField searchKeyField;
    private JButton loadDataButton;
    private JButton constructIndexButton;
    private JButton searchButton;
    private JButton tableScanButton;
    
    public ControlPanel() {
        initializePanel();
        createComponents();
        layoutComponents();
    }
    
    private void initializePanel() {
        setLayout(new GridBagLayout());
        setBorder(new TitledBorder("Controls"));
    }
    
    private void createComponents() {
        // Input fields
        pageSizeField = new JTextField("100", 10);
        bucketCapacityField = new JTextField("5", 10);
        searchKeyField = new JTextField(15);
        
        // Combo box
        hashFunctionCombo = new JComboBox<>(HashFunctionFactory.HashFunctionType.values());
        hashFunctionCombo.setSelectedItem(HashFunctionFactory.HashFunctionType.DJB2);
        
        // Buttons
        loadDataButton = new JButton("Load Data");
        constructIndexButton = new JButton("Construct Index");
        constructIndexButton.setEnabled(false);
        searchButton = new JButton("Search with Index");
        searchButton.setEnabled(false);
        tableScanButton = new JButton("Table Scan");
        tableScanButton.setEnabled(false);
    }
    
    private void layoutComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Page size input
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Page Size:"), gbc);
        gbc.gridx = 1;
        add(pageSizeField, gbc);
        
        // Bucket capacity input
        gbc.gridx = 2; gbc.gridy = 0;
        add(new JLabel("Bucket Capacity:"), gbc);
        gbc.gridx = 3;
        add(bucketCapacityField, gbc);
        
        // Hash function selection
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Hash Function:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(hashFunctionCombo, gbc);
        
        // Load data button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(loadDataButton, gbc);
        
        // Construct index button
        gbc.gridx = 2; gbc.gridwidth = 2;
        add(constructIndexButton, gbc);
        
        // Search controls
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Search Key:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(searchKeyField, gbc);
        
        // Search button
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(searchButton, gbc);
        
        // Table scan button
        gbc.gridx = 2; gbc.gridwidth = 2;
        add(tableScanButton, gbc);
    }
    
    // Action setters to connect with action classes
    public void setLoadDataAction(LoadDataAction action) {
        loadDataButton.addActionListener(action);
    }
    
    public void setConstructIndexAction(ConstructIndexAction action) {
        constructIndexButton.addActionListener(action);
    }
    
    public void setSearchAction(SearchAction action) {
        searchButton.addActionListener(action);
    }
    
    public void setTableScanAction(TableScanAction action) {
        tableScanButton.addActionListener(action);
    }
    
    // Getters for component values
    public int getPageSize() throws NumberFormatException {
        return Integer.parseInt(pageSizeField.getText().trim());
    }
    
    public int getBucketCapacity() throws NumberFormatException {
        return Integer.parseInt(bucketCapacityField.getText().trim());
    }
    
    public HashFunctionFactory.HashFunctionType getSelectedHashFunction() {
        return (HashFunctionFactory.HashFunctionType) hashFunctionCombo.getSelectedItem();
    }
    
    public String getSearchKey() {
        return searchKeyField.getText().trim();
    }
    
    // Button state management
    public void enableConstructIndexButton(boolean enabled) {
        constructIndexButton.setEnabled(enabled);
    }
    
    public void enableSearchButtons(boolean enabled) {
        searchButton.setEnabled(enabled);
        tableScanButton.setEnabled(enabled);
    }
}