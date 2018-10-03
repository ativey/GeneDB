package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.Organism;

import javax.persistence.Entity;

@Entity
@FeatureType(cv = "sequence", term = "nucleotide_match")
public class NucleotideMatch extends Match {

    NucleotideMatch() {
        super();
    }

    public NucleotideMatch(Organism organism, String uniqueName, boolean analysis, boolean obsolete) {
        super(organism, uniqueName, analysis, obsolete);
    }

    public NucleotideMatch(Organism organism, String uniqueName) {
        super(organism, uniqueName);
    }

}
