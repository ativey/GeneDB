package org.gmod.schema.mapped;

import java.sql.Timestamp;

import org.genedb.top.chado.feature.Chromosome;
import org.genedb.top.chado.mapped.CvTerm;
import org.genedb.top.chado.mapped.Feature;
import org.genedb.top.chado.mapped.FeatureCvTerm;
import org.genedb.top.chado.mapped.Organism;
import org.genedb.top.chado.mapped.Pub;

public class MockChromosome extends Chromosome {
    private String uniqueName;
    public MockChromosome(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
        this.uniqueName = uniqueName;
    }
    @Override
    protected Pub nullPub() {
        return null;
    }
    
    @Override
    public FeatureCvTerm addCvTerm(CvTerm cvTerm) {
        ((Feature)this).setType(cvTerm);
        return null;
    }
    
    @Override
    public int getFeatureId(){
        return Integer.parseInt(uniqueName);
    }
}
