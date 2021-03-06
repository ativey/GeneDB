package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.FeatureProp;
import org.genedb.top.chado.mapped.Organism;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * A remark about a portion of the sequence.
 * Should have a FeatureProperty of type 'comment'.
 *
 * @author rh11
 */
@Entity
@FeatureType(cv="sequence", term="remark")
public class Remark extends Region {
    private static final Logger logger = LoggerFactory.getLogger(Remark.class);

    Remark() {
        // empty
    }

    public Remark(Organism organism, String uniqueName, String comment) {
        super(organism, uniqueName, /*analysis:*/false, /*obsolete:*/false, new Timestamp(System.currentTimeMillis()));
        addFeatureProp(comment, "comment", "feature_property", 0);
    }

    /**
     * Get the comment attached to this Remark feature.
     * @return the comment, or null if no comment was found
     */
    @Transient
    public String getComment() {
        for(FeatureProp featureProp: this.getFeatureProps()) {
            if (featureProp.getType().getCv().getName().equals("feature_property")
                    && featureProp.getType().getName().equals("comment")) {
                return featureProp.getValue();
            }
        }

        logger.error(String.format("Remark feature '%s' (ID=%d) has no comment",
            this.getUniqueName(), this.getFeatureId()));
        return null;
    }
}
