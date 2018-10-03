package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

import javax.persistence.Entity;

@Entity
@FeatureType(cv="sequence", term="exon")
public class Exon extends AbstractExon {

    Exon() {
        // empty
    }

    public Exon(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

    /*
     * This constructor is invoked reflectively by Transcript.createRegion.
     * All TranscriptRegions should have one.
     */
    public Exon(Organism organism, String uniqueName) {
        this(organism, uniqueName, false, false, new Timestamp(System.currentTimeMillis()));
    }

}
