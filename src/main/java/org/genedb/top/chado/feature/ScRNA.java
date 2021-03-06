package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.Organism;

import org.hibernate.search.annotations.Indexed;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@FeatureType(cv = "sequence", term = "scRNA")
@Indexed
public class ScRNA extends NcRNA {

    ScRNA() {
        // empty
    }

    public ScRNA(Organism organism, String uniqueName, boolean analysis, boolean obsolete,
            Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }
    ScRNA(Organism organism, String uniqueName, String name) {
        this(organism, uniqueName, false, false, new Timestamp(System.currentTimeMillis()));
        setName(name);
    }

    @Override
    @Transient
    public Integer getColourId() {
        return null;
    }

}
