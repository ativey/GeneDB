package org.genedb.top.web.mvc.model.simple;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleFeature {

    private static final Logger logger = LoggerFactory.getLogger(SimpleFeature.class);

    private int featureId;

    private String uniqueName;

    public int getFeatureId() {
        return featureId;
    }

    public Logger getLogger() {
        return logger;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

}
