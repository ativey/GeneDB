package org.genedb.top.chado.feature;

import org.genedb.top.chado.cfg.FeatureType;
import org.genedb.top.chado.mapped.CvTerm;
import org.genedb.top.chado.mapped.Feature;
import org.genedb.top.chado.mapped.FeatureRelationship;
import org.genedb.top.chado.mapped.Organism;

import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Arrangement of the polypeptide with respect to the lipid bilayer.
 * Has {@link ExtramembraneRegion} and {@link IntramembraneRegion} as parts.
 *
 * @author rh11
 *
 */
@Entity
@FeatureType(cv="sequence", term="membrane_structure")
public class MembraneStructure extends PolypeptideRegion {

    MembraneStructure() {
        super();
    }

    public MembraneStructure(Organism organism, CvTerm cvTerm, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp timeAccessioned, Timestamp timeLastModified) {
        super(organism, cvTerm, uniqueName, analysis, obsolete, timeAccessioned, timeLastModified);
    }

    public MembraneStructure(Organism organism, CvTerm cvTerm, String uniqueName, boolean analysis,
            boolean obsolete) {
        super(organism, cvTerm, uniqueName, analysis, obsolete);
    }

    public MembraneStructure(Organism organism, CvTerm cvTerm, String uniqueName) {
        this(organism, cvTerm, uniqueName, true, false);
    }

    @Transient
    public <T extends MembraneStructureComponent> SortedSet<T> getComponents(Class<T> clazz) {
        SortedSet<T> components = new TreeSet<T>();

        for (FeatureRelationship relation : getFeatureRelationshipsForObjectId()) {
            Feature feature = relation.getSubjectFeature();
            if (clazz.isInstance(feature)) {
                components.add(clazz.cast(feature));
            }
        }

        return components;
    }

    @Transient
    public SortedSet<MembraneStructureComponent> getComponents() {
        return getComponents(MembraneStructureComponent.class);
    }

    @Transient
    public Polypeptide getPolypeptide() {
        return (Polypeptide) this.getPrimarySourceFeature();
    }
}
