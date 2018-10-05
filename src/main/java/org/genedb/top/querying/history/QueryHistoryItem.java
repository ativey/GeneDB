package org.genedb.top.querying.history;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.genedb.top.querying.core.QueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryHistoryItem extends HistoryItem {
	
	private static final long serialVersionUID = 1178201547605983421L;
	private static final Logger logger = LoggerFactory.getLogger(QueryHistoryItem.class);

	public QueryHistoryItem(String name) {
		super(name);
	}
	
	@Override
	public List<String> getIds() {
		
		// only generate ids if not done before, or if the query returns 0 results it's unlikely to be expensive
    	if (ids.size() == 0) {
    		
    		int maxIndex = query.getTotalResultsSize() -1;
    		
    		if (maxIndex >= 0) {
    			try {
    				ids = new HashSet<String>(query.getResults(0, maxIndex));
    				logger.info(" ids ");
    				logger.info(ids.toString());
    			} catch (QueryException e) {
    				throw new RuntimeException(e);
    			}
    		}
			
    	}
        return new ArrayList<String>(ids);
    }
	
	@Override
	public int getNumberItems() {
		if (ids.size() == 0) {
			return query.getTotalResultsSize();
		}
		return ids.size();
	}
	
}
