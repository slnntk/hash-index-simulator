package com.hashindex.gui.actions;

import com.hashindex.model.SearchResult;
import com.hashindex.service.HashIndexService;
import com.hashindex.gui.components.StatusPanel;
import com.hashindex.gui.components.ControlPanel;
import com.hashindex.gui.display.DisplayManager;

import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;

/**
 * Action for performing table scan search.
 * Implements Command pattern for table scan operations.
 */
public class TableScanAction extends BaseAction {
    
    private final DisplayManager displayManager;
    
    public TableScanAction(HashIndexService service, StatusPanel statusPanel,
                          ControlPanel controlPanel, Component parentComponent,
                          DisplayManager displayManager) {
        super(service, statusPanel, controlPanel, parentComponent);
        this.displayManager = displayManager;
    }
    
    @Override
    protected void executeAction(ActionEvent e) throws Exception {
        String searchKey = validateAndGetSearchKey();
        
        statusPanel.setStatus("Performing table scan...");
        setWaitCursor();
        
        SwingWorker<SearchResult, Void> worker = new SwingWorker<SearchResult, Void>() {
            @Override
            protected SearchResult doInBackground() throws Exception {
                return service.tableScan(searchKey);
            }
            
            @Override
            protected void done() {
                try {
                    SearchResult result = get();
                    displayManager.updateSearchResults(result, searchKey, "TABLE SCAN");
                    statusPanel.setStatus("Table scan completed");
                } catch (Exception ex) {
                    handleError("Error during table scan: " + ex.getMessage());
                } finally {
                    setDefaultCursor();
                }
            }
        };
        worker.execute();
    }
    
    private String validateAndGetSearchKey() throws IllegalArgumentException {
        String searchKey = controlPanel.getSearchKey();
        if (searchKey.isEmpty()) {
            throw new IllegalArgumentException("Please enter a search key");
        }
        return searchKey;
    }
}