package org.genedb.top.web.mvc.controller;

import org.genedb.top.querying.history.HistoryManager;

public interface HistoryManagerFactory {

    public abstract HistoryManager getHistoryManager(Object key);

}