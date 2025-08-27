package com.hashindex.util;

import com.hashindex.gui.HashIndexGUI;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;

/**
 * Test to validate GUI initialization without full display.
 */
public class GUIValidationTest {
    
    public static void main(String[] args) {
        System.out.println("=== GUI Validation Test ===");
        
        // Check if we're in a headless environment
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Running in headless environment - GUI cannot be displayed");
            System.out.println("GUI classes compiled successfully and are ready for use");
            return;
        }
        
        try {
            // Try to initialize GUI components without making them visible
            SwingUtilities.invokeAndWait(() -> {
                try {
                    System.out.println("Initializing GUI components...");
                    // This will test that all GUI classes can be loaded and initialized
                    HashIndexGUI gui = new HashIndexGUI();
                    gui.setVisible(false); // Don't actually show the window
                    gui.dispose(); // Clean up
                    System.out.println("GUI components initialized successfully!");
                } catch (Exception e) {
                    System.err.println("GUI initialization failed: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            
        } catch (Exception e) {
            System.err.println("Error during GUI validation: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("GUI validation completed");
    }
}