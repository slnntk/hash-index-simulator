package com.hashindex;

import com.hashindex.gui.HashIndexGUI;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application entry point for the Hash Index Simulator.
 */
public class HashIndexSimulatorApp {
    
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback to default look and feel
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Start GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new HashIndexGUI();
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}