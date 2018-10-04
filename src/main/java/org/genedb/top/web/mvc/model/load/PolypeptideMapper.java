package org.genedb.top.web.mvc.model.load;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;



public class PolypeptideMapper extends FeatureMapper {
    private Logger logger = LoggerFactory.getLogger(PolypeptideMapper.class);

    public static final String SQL = "select f.* " +
    		" from feature f, feature_relationship fr, cvterm cvt, cv" +
    		" where fr.object_id = ?" +
    		" and fr.subject_id = f.feature_id" +
    		" and f.type_id = cvt.cvterm_id" +
    		" and cvt.name = 'polypeptide'" +
    		" and cv.name = 'sequence'";
}
