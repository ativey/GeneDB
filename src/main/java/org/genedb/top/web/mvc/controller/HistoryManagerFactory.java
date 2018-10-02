package org.genedb.top.web.mvc.controller;

import org.genedb.querying.history.HistoryManager;

public interface HistoryManagerFactory {

    public abstract HistoryManager getHistoryManager(Object key);

}