package org.genedb.top.query.compatability;

import org.genedb.query.Result;
import org.genedb.top.query.ResultCompatibility;



/**
 * Simple ResultCompatibility which always returns true!
 * 
 * @author art
 */
public class AlwaysTrueResultDataSetCompatibility implements
        ResultCompatibility {

    public boolean areCompatible(Result one, Result two) {
        return true;
    }

}
