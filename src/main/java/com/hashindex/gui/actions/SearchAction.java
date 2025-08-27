package com.hashindex.gui.actions;

import com.hashindex.model.SearchResult;
import com.hashindex.service.HashIndexService;
import com.hashindex.gui.components.StatusPanel;
import com.hashindex.gui.components.ControlPanel;
import com.hashindex.gui.display.DisplayManager;

import java.awt.Component;
import java.awt.event.ActionEvent;

/**
 * Action for performing index-based search.
 * Implements Command pattern for search operations.
 */
public class SearchAction extends BaseAction {
    
    private final DisplayManager displayManager;
    
    public SearchAction(HashIndexService service, StatusPanel statusPanel,
                       ControlPanel controlPanel, Component parentComponent,
                       DisplayManager displayManager) {
        super(service, statusPanel, controlPanel, parentComponent);
        this.displayManager = displayManager;
    }
    
    @Override
    protected void executeAction(ActionEvent e) throws Exception {
        String searchKey = validateAndGetSearchKey();
        
        statusPanel.setStatus("Searching with index...");
        SearchResult result = service.searchWithIndex(searchKey);
        
        displayManager.updateSearchResults(result, searchKey, "SEARCH WITH INDEX");
        statusPanel.setStatus("Search completed");
    }
    
    private String validateAndGetSearchKey() throws IllegalArgumentException {
        String searchKey = controlPanel.getSearchKey();
        if (searchKey.isEmpty()) {
            throw new IllegalArgumentException("Please enter a search key");
        }
        return searchKey;
    }
}