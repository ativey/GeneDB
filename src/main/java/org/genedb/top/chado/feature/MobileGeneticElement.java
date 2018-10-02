package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

import javax.persistence.Entity;

@Entity
@FeatureType(cv="sequence", term="mobile_genetic_element")
public abstract class MobileGeneticElement extends Region {
    MobileGeneticElement() {
        // empty
    }

    public MobileGeneticElement(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

    public MobileGeneticElement(Organism organism, String uniqueName) {
        this(organism, uniqueName, false, false, new Timestamp(System.currentTimeMillis()));
    }
}