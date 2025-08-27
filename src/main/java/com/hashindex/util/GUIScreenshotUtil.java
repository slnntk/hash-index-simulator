package com.hashindex.util;

import com.hashindex.gui.HashIndexGUI;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Utility to create a screenshot of the GUI for validation purposes.
 */
public class GUIScreenshotUtil {
    
    public static void main(String[] args) {
        System.out.println("=== GUI Screenshot Utility ===");
        
        // Check if we're in a headless environment
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Running in headless environment - setting system property to allow GUI");
            System.setProperty("java.awt.headless", "false");
        }
        
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    System.out.println("Creating GUI window...");
                    HashIndexGUI gui = new HashIndexGUI();
                    
                    // Give the window time to render
                    Thread.sleep(2000);
                    
                    // Take screenshot
                    takeScreenshot(gui, "gui_refactored.png");
                    
                    // Clean up
                    gui.dispose();
                    System.out.println("Screenshot saved successfully!");
                    
                } catch (Exception e) {
                    System.err.println("Error during screenshot: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            
        } catch (Exception e) {
            System.err.println("Error during GUI screenshot: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("GUI screenshot utility completed");
    }
    
    private static void takeScreenshot(Component component, String filename) throws IOException {
        Rectangle bounds = component.getBounds();
        BufferedImage screenshot = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = screenshot.createGraphics();
        component.paint(g2d);
        g2d.dispose();
        
        File outputFile = new File(filename);
        ImageIO.write(screenshot, "PNG", outputFile);
        System.out.println("Screenshot saved to: " + outputFile.getAbsolutePath());
    }
}