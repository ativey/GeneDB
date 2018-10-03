package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@FeatureType(cv="sequence", term="microsatellite")
public class Microsatellite extends TandemRepeat {

     Microsatellite() {
        super();
    }

    public Microsatellite(Organism organism, String uniqueName, boolean analysis, boolean obsolete,
            Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

    public Microsatellite(Organism organism, String uniqueName, String name) {
        super(organism, uniqueName, name);
    }

}
