package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.CvTerm;
import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

import javax.persistence.Entity;

/*
 * The term name has recently changed from 'cytoplasm_location' to 'cytoplasmic_region'
 */
@Entity
@FeatureType(cv="sequence", accession="0001073")
public class CytoplasmicRegion extends ExtramembraneRegion {

    CytoplasmicRegion() {
        // empty
    }

    public CytoplasmicRegion(Organism organism, CvTerm cvTerm, String uniqueName) {
        this(organism, cvTerm, uniqueName, true, false);
    }

    public CytoplasmicRegion(Organism organism, CvTerm cvTerm, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp timeAccessioned, Timestamp timeLastModified) {
        super(organism, cvTerm, uniqueName, analysis, obsolete, timeAccessioned, timeLastModified);
    }

    public CytoplasmicRegion(Organism organism, CvTerm cvTerm, String uniqueName, boolean analysis,
            boolean obsolete) {
        super(organism, cvTerm, uniqueName, analysis, obsolete);
    }

}
