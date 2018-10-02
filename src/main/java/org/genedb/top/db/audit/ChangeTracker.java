package org.genedb.top.db.audit;

import java.sql.SQLException;

public interface ChangeTracker {
    public ChangeSet changes(String clientName) throws SQLException;
}
