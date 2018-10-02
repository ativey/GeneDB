package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * An expressed sequence tag (<code>SO:0000345</code>).
 *
 * @author rh11
 */
@Entity
@FeatureType(cv = "sequence", term = "EST")
public class EST extends TopLevelFeature {
    EST() {
        // empty
    }

    public EST(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

}
