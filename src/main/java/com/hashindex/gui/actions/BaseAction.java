package com.hashindex.gui.actions;

import com.hashindex.service.HashIndexService;
import com.hashindex.gui.components.StatusPanel;
import com.hashindex.gui.components.ControlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Base class for all GUI actions in the Hash Index Simulator.
 * Implements Command pattern and provides common functionality.
 */
public abstract class BaseAction implements ActionListener {
    
    protected final HashIndexService service;
    protected final StatusPanel statusPanel;
    protected final ControlPanel controlPanel;
    protected final Component parentComponent;
    
    public BaseAction(HashIndexService service, StatusPanel statusPanel, 
                     ControlPanel controlPanel, Component parentComponent) {
        this.service = service;
        this.statusPanel = statusPanel;
        this.controlPanel = controlPanel;
        this.parentComponent = parentComponent;
    }
    
    @Override
    public final void actionPerformed(ActionEvent e) {
        try {
            executeAction(e);
        } catch (Exception ex) {
            handleError("Error executing action: " + ex.getMessage());
        }
    }
    
    /**
     * Template method for action execution.
     * Subclasses must implement this method.
     */
    protected abstract void executeAction(ActionEvent e) throws Exception;
    
    protected void showError(String message) {
        JOptionPane.showMessageDialog(parentComponent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    protected void handleError(String message) {
        showError(message);
        statusPanel.setStatus("Error occurred");
    }
    
    protected void setCursor(Cursor cursor) {
        if (parentComponent != null) {
            parentComponent.setCursor(cursor);
        }
    }
    
    protected void setWaitCursor() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    protected void setDefaultCursor() {
        setCursor(Cursor.getDefaultCursor());
    }
}