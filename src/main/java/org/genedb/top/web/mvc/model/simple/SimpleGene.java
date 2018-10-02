package org.genedb.top.web.mvc.model.simple;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleGene extends SimpleFeature {

    private static final Logger logger = LoggerFactory.getLogger(SimpleGene.class);

    private int fmin;

    private int sourceFeatureId;

    private String cvtName;

    private String topLevelFeatureUniqueName;

    public String getCvtName() {
        return cvtName;
    }

    public int getFmin() {
        return fmin;
    }

    public int getSourceFeatureId() {
        return sourceFeatureId;
    }

    public String getTopLevelFeatureUniqueName() {
        return topLevelFeatureUniqueName;
    }

    public void setCvtName(String cvtName) {
        this.cvtName = cvtName;
    }

    public void setFmin(int fmin) {
        this.fmin = fmin;
    }

    public void setSourceFeatureId(int sourceFeatureId) {
        this.sourceFeatureId = sourceFeatureId;
    }

    public void setTopLevelFeatureUniqueName(String topLevelFeatureUniqueName) {
        this.topLevelFeatureUniqueName = topLevelFeatureUniqueName;
    }

}
