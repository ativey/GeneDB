package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

import javax.persistence.Entity;

@Entity
@FeatureType(cv="sequence", term="prophage")
public class Prophage extends MobileGeneticElement {
    Prophage() {
        // empty
    }

    public Prophage(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

    public Prophage(Organism organism, String uniqueName) {
        super(organism, uniqueName);
    }
}