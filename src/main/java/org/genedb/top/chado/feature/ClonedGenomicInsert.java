package org.genedb.top.chado.feature;

import java.sql.Timestamp;

import javax.persistence.Entity;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.CvTerm;
import org.genedb.top.chado.mapped.Organism;

@Entity
@FeatureType(cv="sequence", term="cloned_genomic_insert")
public class ClonedGenomicInsert extends CloneInsert {

    public ClonedGenomicInsert(Organism organism, CvTerm cvTerm, String uniqueName, boolean analysis, boolean obsolete, Timestamp timeAccessioned, Timestamp timeLastModified) {
        super(organism, cvTerm, uniqueName, analysis, obsolete, timeAccessioned, timeLastModified);
    }
    
    public ClonedGenomicInsert(Organism organism, String uniqueName, boolean analysis, boolean obsolete,
            Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

}
