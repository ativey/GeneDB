package org.genedb.top.web.mvc.model.types;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

public class FeatureCVTPropType {

    private static Logger logger = LoggerFactory.getLogger(FeatureCVTPropType.class);

    private String fctTypeName;
    private String fctValue;

    public FeatureCVTPropType(String fctTypeName, String fctValue) {
        super();
        this.fctTypeName = fctTypeName;
        this.fctValue = fctValue;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("(");

        sb.append("'");
        sb.append(escape(fctTypeName));
        sb.append("',");

        sb.append("'");
        sb.append(escape(fctValue));
        sb.append("'");

        sb.append(")");
        return sb.toString();
    }

    private String escape(String in) {
        logger.error(String.format("About to try and substitute '%s'", in));
        return in.replaceAll("\\(", "_").replaceAll("\\)", "_");
    }

}
