package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.Organism;
import org.hibernate.search.annotations.Indexed;

import java.sql.Timestamp;

import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@FeatureType(cv = "sequence", term = "splice_site")
@Indexed
public class SpliceSite extends TranscriptRegion {

	SpliceSite() {
        // empty
    }

    public SpliceSite(Organism organism, String uniqueName, boolean analysis, boolean obsolete,
            Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }
    SpliceSite(Organism organism, String uniqueName, String name) {
        this(organism, uniqueName, false, false, new Timestamp(System.currentTimeMillis()));
        setName(name);
    }

}