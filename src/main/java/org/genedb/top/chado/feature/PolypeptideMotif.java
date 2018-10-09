package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.CvTerm;
import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@FeatureType(cv="sequence", term="polypeptide_motif")
public class PolypeptideMotif extends PolypeptideDomain {
    public PolypeptideMotif(Organism organism, CvTerm type, String uniqueName, boolean analysis,
            boolean obsolete) {
        super(organism, type, uniqueName, analysis, obsolete);
    }

    public PolypeptideMotif(Organism organism, CvTerm type, String uniqueName) {
        super(organism, type, uniqueName);
    }

    public PolypeptideMotif(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

    public PolypeptideMotif(Organism organism, String uniqueName) {
        super(organism, uniqueName);
    }

    PolypeptideMotif() {
        // empty
    }

}