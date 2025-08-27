package com.hashindex.gui.actions;

import com.hashindex.service.HashIndexService;
import com.hashindex.gui.components.StatusPanel;
import com.hashindex.gui.components.ControlPanel;
import com.hashindex.gui.display.DisplayManager;

import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;

/**
 * Action for loading data into pages.
 * Implements Command pattern for data loading operations.
 */
public class LoadDataAction extends BaseAction {
    
    private final DisplayManager displayManager;
    
    public LoadDataAction(HashIndexService service, StatusPanel statusPanel,
                         ControlPanel controlPanel, Component parentComponent,
                         DisplayManager displayManager) {
        super(service, statusPanel, controlPanel, parentComponent);
        this.displayManager = displayManager;
    }
    
    @Override
    protected void executeAction(ActionEvent e) throws Exception {
        int pageSize = validateAndGetPageSize();
        
        statusPanel.setStatus("Loading data...");
        setWaitCursor();
        
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
                    displayManager.updatePageDisplays();
                    controlPanel.enableConstructIndexButton(true);
                    statusPanel.setStatus("Data loaded successfully. Total records: " + 
                                        String.format("%,d", service.getStatistics().getTotalRecords()));
                } catch (Exception ex) {
                    handleError("Error loading data: " + ex.getMessage());
                } finally {
                    setDefaultCursor();
                }
            }
        };
        worker.execute();
    }
    
    private int validateAndGetPageSize() throws IllegalArgumentException {
        try {
            int pageSize = controlPanel.getPageSize();
            if (pageSize <= 0) {
                throw new IllegalArgumentException("Page size must be positive");
            }
            return pageSize;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid page size");
        }
    }
}