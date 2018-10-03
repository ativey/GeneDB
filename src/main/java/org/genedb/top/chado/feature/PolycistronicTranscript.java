package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.Organism;

import org.hibernate.search.annotations.Indexed;

import java.sql.Timestamp;

import javax.persistence.Entity;

@Entity
@FeatureType(cv = "sequence", term = "polycistronic_transcript")
@Indexed
public class PolycistronicTranscript extends TranscriptRegion {

    PolycistronicTranscript() {
        // empty
    }

    public PolycistronicTranscript(Organism organism, String uniqueName, boolean analysis, boolean obsolete,
            Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }
    PolycistronicTranscript(Organism organism, String uniqueName, String name) {
        this(organism, uniqueName, false, false, new Timestamp(System.currentTimeMillis()));
        setName(name);
    }

}