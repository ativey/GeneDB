package org.genedb.top.chado.feature;

import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * An {@link Exon} or {@link PseudogenicExon}.
 *
 * @author rh11
 *
 */
@Entity
public abstract class AbstractExon extends TranscriptRegion {

    public AbstractExon() {
        // Deliberately empty
    }

    public AbstractExon(Organism organism, String uniqueName, boolean analysis, boolean obsolete,
            Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }
}
