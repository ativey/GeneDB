package org.genedb.top.chado.feature;

import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

import javax.persistence.Entity;

@Entity
public abstract class UTR extends TranscriptRegion {

    UTR() {
        // empty
    }

    public UTR(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

}
