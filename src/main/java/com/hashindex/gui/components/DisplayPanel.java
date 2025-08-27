package com.hashindex.gui.components;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel containing all display areas for showing simulation results.
 * Follows Single Responsibility Principle by handling only display layout.
 */
public class DisplayPanel extends JPanel {
    
    private JTextArea firstPageArea;
    private JTextArea lastPageArea;
    private JTextArea bucketsArea;
    private JTextArea statisticsArea;
    private JTextArea searchResultArea;
    
    public DisplayPanel() {
        initializePanel();
        createComponents();
        layoutComponents();
    }
    
    private void initializePanel() {
        setLayout(new GridLayout(2, 3, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    private void createComponents() {
        // First page display
        firstPageArea = createTextArea();
        
        // Last page display
        lastPageArea = createTextArea();
        
        // Buckets display
        bucketsArea = createTextArea();
        
        // Statistics display
        statisticsArea = createTextArea();
        
        // Search results display
        searchResultArea = createTextArea();
    }
    
    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea(8, 20);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        return textArea;
    }
    
    private void layoutComponents() {
        // First page panel
        add(createDisplaySubPanel("First Page", firstPageArea));
        
        // Last page panel
        add(createDisplaySubPanel("Last Page", lastPageArea));
        
        // Buckets panel
        add(createDisplaySubPanel("Bucket Information", bucketsArea));
        
        // Statistics panel
        add(createDisplaySubPanel("Statistics", statisticsArea));
        
        // Search results panel
        add(createDisplaySubPanel("Search Results", searchResultArea));
        
        // Empty panel for layout
        add(new JPanel());
    }
    
    private JPanel createDisplaySubPanel(String title, JTextArea textArea) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder(title));
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        return panel;
    }
    
    // Getters for display areas (for updates from display managers)
    public JTextArea getFirstPageArea() {
        return firstPageArea;
    }
    
    public JTextArea getLastPageArea() {
        return lastPageArea;
    }
    
    public JTextArea getBucketsArea() {
        return bucketsArea;
    }
    
    public JTextArea getStatisticsArea() {
        return statisticsArea;
    }
    
    public JTextArea getSearchResultArea() {
        return searchResultArea;
    }
}