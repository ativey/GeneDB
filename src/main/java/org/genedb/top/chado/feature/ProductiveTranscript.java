package org.genedb.top.chado.feature;

//import org.genedb.top.db.analyzers.AllNamesAnalyzer;
//import org.genedb.top.db.analyzers.AlphaNumericAnalyzer;

import org.genedb.top.chado.mapped.Feature;
import org.genedb.top.chado.mapped.FeatureRelationship;
import org.genedb.top.chado.mapped.Organism;
import org.genedb.top.chado.mapped.Synonym;

import org.hibernate.search.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import java.sql.Timestamp;import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * A transcript that may have products associated with it, i.e. an mRNA or
 * a PseudogenicTranscript.
 *
 * @author rh11
 */
@Entity
public abstract class ProductiveTranscript extends Transcript {

    private static final Logger logger = LoggerFactory.getLogger(ProductiveTranscript.class);

    ProductiveTranscript() {
        // empty
    }

    public ProductiveTranscript(Organism organism, String uniqueName, boolean analysis,
            boolean obsolete, Timestamp dateAccessioned) {
        super(organism, uniqueName, analysis, obsolete, dateAccessioned);
    }

    /**
     * Return the uniqueName of the associated polypeptide.
     *
     * Indexed as <code>protein</code> in the Lucene index.
     *
     * @return the uniqueName of the associated polypeptide
     */
    @Transient
    @Field(name = "protein", store = Store.YES)
    public String getProteinUniqueName() {
        Feature protein = getProtein();
        if (protein == null) {
            return null;
        }
        return protein.getUniqueName();
    }

    /**
     * Get the associated polypeptide feature.
     *
     * A pseudogenic transcript may or may not have an associated
     * polypeptide.Every mRNA transcript should have one, though this
     * constraint cannot be enforced at the database level, so in practice
     * this method may return <code>null</code> even for an mRNA, which
     * indicates a curation issue with that transcript.
     *
     * @return
     */
    @Transient
    public Polypeptide getProtein() {
        // Note: Overridden on MRNA to complain if there isn't a protein.
        return getProteinWithoutComplaining();
    }

    /**
     * Get the associated polypeptide, but don't worry if there isn't one.
     * This method is used internally while the gene model is being built,
     * and should not be public.
     *
     * @return
     */
    protected final Polypeptide getProteinWithoutComplaining() {
        for (FeatureRelationship relation : getFeatureRelationshipsForObjectId()) {
            Feature feature = relation.getSubjectFeature();
//            if (feature instanceof HibernateProxy) {
//                feature = (Feature) ((HibernateProxy) feature).getHibernateLazyInitializer().getImplementation();
//            }
            if (feature instanceof Polypeptide) {
                return (Polypeptide) feature;
            }
        }
        return null;
    }

    @Transient
    public void setProtein(Polypeptide polypeptide) {
        if (getProteinWithoutComplaining() != null) {
            throw new RuntimeException(String.format(
                "Attempting to set a protein on transcript '%s' which already has one",
                getUniqueName()));
        }
        addFeatureRelationship(polypeptide, "sequence", "derives_from");
    }

    @Transient
    public List<String> getProducts() {
        Polypeptide protein = getProtein();
        if (protein == null) {
            return null;
        }
        return protein.getProducts();
    }

    @Transient
 //   @Analyzer(impl = AllNamesAnalyzer.class)
    @Field(name = "product", store = Store.YES)
    public String getProductsAsSpaceSeparatedString() {
    	Polypeptide protein = getProtein();
    	if (protein != null) {
    		return protein.getProductsAsSpaceSeparatedString();
    	}
    	return null;
    }

    @Transient
//    @Analyzer(impl = AllNamesAnalyzer.class)
    @Field(name = "expandedProduct", store = Store.YES)
    public String getProductsAsSeparatedString() {
    	Polypeptide protein = getProtein();
    	if (protein != null) {
    		return protein.getProductsAsSeparatedString();
    	}
    	return null;
    }
    
    @Transient
//    @Analyzer(impl = AlphaNumericAnalyzer.class)
    @Field(name = "productAlphanumeric", store = Store.YES)
    public String getProductsAlphanumeric(){
        Polypeptide protein = getProtein();
        if (protein != null) {
            return protein.getProductsAlphanumeric();
        }
        return null;
    }
    

    @Override
    @Transient
    @Field(name = "colour", analyze = Analyze.NO, store = Store.YES)
    public Integer getColourId() {
        Polypeptide protein = getProtein();
        if (protein == null) {
            return null;
        }
        return protein.getColourId();
    }

    @Override
    public AbstractExon createExon(String exonUniqueName, int fmin, int fmax, Integer phase) {
        Polypeptide polypeptide = getProtein();
        if (polypeptide != null) {
            polypeptide.lowerFminTo(fmin);
            polypeptide.raiseFmaxTo(fmax);
        }
        return super.createExon(exonUniqueName, fmin, fmax, phase);
    }


    @Transient
    @Field(name = "allNames", store = Store.YES)
    //@Analyzer(impl = AllNamesAnalyzer.class)
    public String getAllTranscriptNames() {
    	List<String> names = Lists.newArrayList();

        //gene name like say PGKC should be indexed on it's transcript
        if (gene!= null && gene.getName() != null) {
            names.add(gene.getName());
            names.add(gene.getUniqueName());
        }

        if (gene!= null && gene.getSynonyms().size()>0) {
            for (Synonym synonym : gene.getSynonyms()){
                names.add(synonym.getName());
            }
        }


        //Process Unique Name
        String uniqueName = getUniqueName();
        names.add(uniqueName);

        //if say Smp_000030.1:mRNA is uniqueName, then add Smp_000030.1
        int before = uniqueName.toLowerCase().indexOf(":");
        if (before != -1) {
            String firstPart = uniqueName.substring(0, before);
            //add something like Smp_000030.1
            names.add(firstPart);
        }

        if (this.getGene().getTranscripts().size() > 1) {
            Transcript first = getGene().getFirstTranscript();
            if (first.getUniqueName().equals(getUniqueName())) {
                names.add(this.getGene().getUniqueName());
            }

        }
        String ret = allNamesSupport(names);
        //System.err.println("** T "+ret);
        return ret;
    }
}