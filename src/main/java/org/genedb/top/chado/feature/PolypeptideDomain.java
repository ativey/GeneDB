package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.CvTerm;
import org.genedb.top.chado.mapped.DbXRef;
import org.genedb.top.chado.mapped.FeatureDbXRef;
import org.genedb.top.chado.mapped.Organism;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@FeatureType(cv="sequence", term="polypeptide_domain")
public class PolypeptideDomain extends PolypeptideRegion {

    private static final Logger logger = LoggerFactory.getLogger(PolypeptideDomain.class);

    PolypeptideDomain() {
        // empty
    }
    public PolypeptideDomain(Organism organism, CvTerm type, String uniqueName) {
        this(organism, type, uniqueName, true, false);
    }
    public PolypeptideDomain(Organism organism, CvTerm type, String uniqueName, boolean analysis,
            boolean obsolete) {
        super(organism, type, uniqueName, analysis, obsolete);
    }

    public PolypeptideDomain(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }
    public PolypeptideDomain(Organism organism, String uniqueName) {
        super(organism, uniqueName);
    }

    @Transient
    @Override
    public String getScore() {
        String score = super.getScore();
        if (score == null) {
            logger.error(String.format("Polypeptide domain '%s' has no score", getUniqueName()));
        }
        return score;
    }

    @Transient
    public DbXRef getInterProDbXRef() {
        for (FeatureDbXRef featureDbXRef: this.getFeatureDbXRefs()) {
            DbXRef dbXRef = featureDbXRef.getDbXRef();
            if ("InterPro".equals(dbXRef.getDb().getName())) {
                return dbXRef;
            }
        }
        return null;
    }
}
