package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

import javax.persistence.Entity;

@Entity
@FeatureType(cv = "sequence", term = "contig")
public class Contig extends TopLevelFeature {
    Contig() {
        // empty
    }

    public Contig(Organism organism, String uniqueName, boolean analysis, boolean obsolete,
            Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

    public static Contig make(String uniqueName, Organism organism) {
        return new Contig(organism, uniqueName, false, false, new Timestamp(System.currentTimeMillis()));
    }
}
