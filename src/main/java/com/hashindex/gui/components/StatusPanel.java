package com.hashindex.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for displaying status information at the bottom of the application.
 * Follows Single Responsibility Principle by handling only status display.
 */
public class StatusPanel extends JPanel {
    
    private JLabel statusLabel;
    
    public StatusPanel() {
        initializePanel();
        createComponents();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
    }
    
    private void createComponents() {
        statusLabel = new JLabel("Ready to load data...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.WEST);
    }
    
    public void setStatus(String message) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(message));
    }
    
    public String getStatus() {
        return statusLabel.getText();
    }
}