package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

import javax.persistence.Entity;

@Entity
@FeatureType(cv="sequence", term="inverted_repeat")
public class InvertedRepeatRegion extends RepeatRegion {

    public InvertedRepeatRegion() {
        super();
    }

    public InvertedRepeatRegion(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

    public InvertedRepeatRegion(Organism organism, String uniqueName, String name) {
        super(organism, uniqueName, name);
    }

}
