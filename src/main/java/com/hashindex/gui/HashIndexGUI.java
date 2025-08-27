package com.hashindex.gui;

import com.hashindex.service.HashIndexService;
import com.hashindex.gui.components.*;
import com.hashindex.gui.controller.HashIndexController;

import javax.swing.*;
import java.awt.*;

/**
 * Main GUI window for the Hash Index Simulator.
 * Refactored to follow MVC pattern and SOLID principles.
 */
public class HashIndexGUI extends JFrame {
    
    private final HashIndexService service;
    private final ControlPanel controlPanel;
    private final DisplayPanel displayPanel;
    private final StatusPanel statusPanel;
    private final HashIndexController controller;
    
    public HashIndexGUI() {
        this.service = new HashIndexService();
        this.controlPanel = new ControlPanel();
        this.displayPanel = new DisplayPanel();
        this.statusPanel = new StatusPanel();
        this.controller = new HashIndexController(service, controlPanel, displayPanel, statusPanel, this);
        
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Hash Index Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Add components using composition
        add(controlPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}