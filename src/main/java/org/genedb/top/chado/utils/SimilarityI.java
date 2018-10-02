package org.genedb.top.chado.utils;

import org.genedb.top.chado.mapped.Analysis;
import org.genedb.top.chado.mapped.DbXRef;

import java.util.Collection;

/**
 * A SimilarityI object represents a similarity (as defined by a /similarity
 * qualifier in an EMBL file. The method {@link Feature#addSimilarity(SimilarityI)}
 * will add a similarity to the database.
 * <p>
 * The only implementation of this interface is {@link org.genedb.db.loading.Similarity}.
 * The interface is defined here in genedb-db in order that we don't create a dependency
 * of genedb-db on genedb-access.
 *
 * @author rh11
 *
 */
public interface SimilarityI {

    public String getAnalysisProgram();

    public String getAnalysisProgramVersion();

    public Analysis getAnalysis();

    public String getOrganismName();

    public String getGeneName();

    public String getProduct();

    public DbXRef getPrimaryDbXRef();

    public Collection<DbXRef> getSecondaryDbXRefs();

    public int getLength();

    public Double getRawScore();

    public Double getEValue();

    public int getOverlap();

    public int getQueryStart();

    public int getQueryEnd();

    public int getTargetStart();

    public int getTargetEnd();

    public Double getId();

    public Double getUngappedId();

    public String getUniqueIdentifier();

}