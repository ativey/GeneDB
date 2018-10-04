package org.genedb.top.query.compatability;

import org.genedb.top.query.Result;
import org.genedb.top.query.ResultCompatibility;

/**
 * Simple ResultCompatibilty which returns true only if two datsets contain exactly the same type. 
 * 
 * @author art
 */
public class IdentityResultCompatibility implements
        ResultCompatibility {

    public boolean areCompatible(Result one, Result two) {
        return one.getType().equals(two.getType());
    }

}
