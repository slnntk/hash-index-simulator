package com.hashindex.gui.controller;

import com.hashindex.service.HashIndexService;
import com.hashindex.gui.components.*;
import com.hashindex.gui.display.DisplayManager;
import com.hashindex.gui.actions.*;

import java.awt.Component;

/**
 * Controller for the Hash Index GUI following MVC pattern.
 * Coordinates between GUI components and business logic.
 */
public class HashIndexController {
    
    private final HashIndexService service;
    private final ControlPanel controlPanel;
    private final DisplayPanel displayPanel;
    private final StatusPanel statusPanel;
    private final DisplayManager displayManager;
    private final Component parentComponent;
    
    public HashIndexController(HashIndexService service, ControlPanel controlPanel,
                              DisplayPanel displayPanel, StatusPanel statusPanel,
                              Component parentComponent) {
        this.service = service;
        this.controlPanel = controlPanel;
        this.displayPanel = displayPanel;
        this.statusPanel = statusPanel;
        this.parentComponent = parentComponent;
        this.displayManager = new DisplayManager(service, displayPanel);
        
        initializeActions();
    }
    
    private void initializeActions() {
        // Create and set action instances following Command pattern
        LoadDataAction loadDataAction = new LoadDataAction(
            service, statusPanel, controlPanel, parentComponent, displayManager
        );
        controlPanel.setLoadDataAction(loadDataAction);
        
        ConstructIndexAction constructIndexAction = new ConstructIndexAction(
            service, statusPanel, controlPanel, parentComponent, displayManager
        );
        controlPanel.setConstructIndexAction(constructIndexAction);
        
        SearchAction searchAction = new SearchAction(
            service, statusPanel, controlPanel, parentComponent, displayManager
        );
        controlPanel.setSearchAction(searchAction);
        
        TableScanAction tableScanAction = new TableScanAction(
            service, statusPanel, controlPanel, parentComponent, displayManager
        );
        controlPanel.setTableScanAction(tableScanAction);
    }
    
    public DisplayManager getDisplayManager() {
        return displayManager;
    }
}