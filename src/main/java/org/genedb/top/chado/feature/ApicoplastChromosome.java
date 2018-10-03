package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.Organism;
import org.genedb.top.chado.feature.Chromosome;

import java.sql.Timestamp;

import javax.persistence.Entity;

@Entity
@FeatureType(cv="sequence", term="apicoplast_chromosome")
public class ApicoplastChromosome extends Chromosome {

    ApicoplastChromosome() {
        // empty
    }

    public ApicoplastChromosome(Organism organism, String uniqueName,
                                boolean analysis, boolean obsolete,
                                Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }
}
