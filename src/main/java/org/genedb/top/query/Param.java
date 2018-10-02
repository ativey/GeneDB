package org.genedb.top.query;

import org.springframework.validation.Validator;

public interface Param extends Validator {
    
    //Query getQuery();
    
    String getName();
    
    Object getValue();
    
    String getDescription();
    
    String getHelp();
    
    boolean isSet();

}
