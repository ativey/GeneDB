package org.genedb.top.web.mvc.model.simple;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

public class SimpleTranscript extends SimpleFeature {

    private String cvtName;

    private int fmin;

    Logger logger = LoggerFactory.getLogger(SimpleTranscript.class);

    public String getCvtName() {
        return cvtName;
    }

    public int getFmin() {
        return fmin;
    }

    public void setCvtName(String cvtName) {
        this.cvtName = cvtName;
    }

    public void setFmin(int fmin) {
        this.fmin = fmin;
    }

}
