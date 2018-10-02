package org.genedb.top.chado.feature;

import org.genedb.top.chado.mapped.CvTerm;
import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;

/**
 * A PolypeptideRegion that represents part of the membrane structure of the polypeptide.
 * This class does not correspond to an SO term.
 *
 * @author rh11
 *
 */
public abstract class MembraneStructureComponent extends PolypeptideRegion {

    MembraneStructureComponent() {
        super();
    }

    public MembraneStructureComponent(Organism organism, CvTerm cvTerm, String uniqueName,
            boolean analysis, boolean obsolete, Timestamp timeAccessioned,
            Timestamp timeLastModified) {
        super(organism, cvTerm, uniqueName, analysis, obsolete, timeAccessioned, timeLastModified);
    }

    public MembraneStructureComponent(Organism organism, CvTerm cvTerm, String uniqueName,
            boolean analysis, boolean obsolete) {
        super(organism, cvTerm, uniqueName, analysis, obsolete);
    }

}
