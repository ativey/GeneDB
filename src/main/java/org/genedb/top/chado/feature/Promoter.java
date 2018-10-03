package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.CvTerm;
import org.genedb.top.chado.mapped.Feature;
import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@FeatureType(cv="sequence", term="promoter")
public class Promoter extends Feature {

    public Promoter() {
        super();
    }

    public Promoter(Organism organism, CvTerm cvTerm, String uniqueName,
            boolean analysis, boolean obsolete, Timestamp timeAccessioned,
            Timestamp timeLastModified) {
        super(organism, cvTerm, uniqueName, analysis, obsolete, timeAccessioned, timeLastModified);
    }

    public Promoter(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp timeAccessioned) {
        super(organism, uniqueName, analysis, obsolete, timeAccessioned, timeAccessioned);
    }

}
