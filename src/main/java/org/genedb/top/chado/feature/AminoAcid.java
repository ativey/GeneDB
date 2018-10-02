package org.genedb.top.chado.feature;

import java.sql.Timestamp;

import javax.persistence.Entity;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.CvTerm;
import org.genedb.top.chado.mapped.Organism;

@Entity
@FeatureType(cv="sequence", term="amino_acid")
public class AminoAcid extends Region {
	
    AminoAcid () {
        // empty
    }
    
    public AminoAcid(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

    public AminoAcid(Organism organism, CvTerm cvTerm, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp timeAccessioned, Timestamp timeLastModified) {
        super(organism, cvTerm, uniqueName, analysis, obsolete, timeAccessioned, timeLastModified);
    }
    
}
