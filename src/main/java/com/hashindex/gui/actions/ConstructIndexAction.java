package com.hashindex.gui.actions;

import com.hashindex.service.HashIndexService;
import com.hashindex.service.HashFunctionFactory;
import com.hashindex.gui.components.StatusPanel;
import com.hashindex.gui.components.ControlPanel;
import com.hashindex.gui.display.DisplayManager;

import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;

/**
 * Action for constructing the hash index.
 * Implements Command pattern for index construction operations.
 */
public class ConstructIndexAction extends BaseAction {
    
    private final DisplayManager displayManager;
    
    public ConstructIndexAction(HashIndexService service, StatusPanel statusPanel,
                               ControlPanel controlPanel, Component parentComponent,
                               DisplayManager displayManager) {
        super(service, statusPanel, controlPanel, parentComponent);
        this.displayManager = displayManager;
    }
    
    @Override
    protected void executeAction(ActionEvent e) throws Exception {
        int bucketCapacity = validateAndGetBucketCapacity();
        HashFunctionFactory.HashFunctionType hashType = controlPanel.getSelectedHashFunction();
        
        statusPanel.setStatus("Constructing index...");
        setWaitCursor();
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                service.setHashFunction(HashFunctionFactory.createHashFunction(hashType));
                service.constructIndex(bucketCapacity);
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions
                    displayManager.updateBucketDisplay();
                    displayManager.updateStatisticsDisplay();
                    controlPanel.enableSearchButtons(true);
                    statusPanel.setStatus("Index constructed successfully. Total buckets: " + 
                                        service.getBuckets().size());
                } catch (Exception ex) {
                    handleError("Error constructing index: " + ex.getMessage());
                } finally {
                    setDefaultCursor();
                }
            }
        };
        worker.execute();
    }
    
    private int validateAndGetBucketCapacity() throws IllegalArgumentException {
        try {
            int bucketCapacity = controlPanel.getBucketCapacity();
            if (bucketCapacity <= 0) {
                throw new IllegalArgumentException("Bucket capacity must be positive");
            }
            return bucketCapacity;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid bucket capacity");
        }
    }
}