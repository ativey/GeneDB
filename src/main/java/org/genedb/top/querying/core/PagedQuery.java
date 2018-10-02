package org.genedb.top.querying.core;

import java.util.List;

import org.genedb.top.querying.tmpquery.GeneSummary;

public interface PagedQuery extends Query {
	
	public List<String> getResults(int start, int end) throws QueryException;
	public int getTotalResultsSize();
    //public List<GeneSummary> getResultsSummaries(int page, int length) throws QueryException;
}
