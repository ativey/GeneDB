package org.genedb.top.querying.tmpquery;

import org.genedb.top.querying.core.QueryClass;
import org.genedb.top.querying.core.QueryParam;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;

import java.util.List;

@QueryClass(
        title="Coding and pseudogenes by protein length",
        shortDesc="Get a list of transcripts ",
        longDesc=""
    )
public class ProteinTargetingSeqQuery extends OrganismLuceneQuery {


    @QueryParam(
            order=1,
            title="Signal peptide"
    )
    private boolean sigP;

    @QueryParam(
            order=2,
            title="GPI anchor"
    )
    private boolean gpi;

    @QueryParam(
            order=3,
            title="Apicoplast"
    )
    private boolean apicoplast;

    @Override
    public String getQueryDescription() {
    	return "Search for SignalP, GPI anchored or Apicoplast proteins.";
    }
    
    @Override
    public String getQueryName() {
        return "Protein Targeting";
    }
    
    @Override
    protected String getluceneIndexName() {
        return "org.gmod.schema.mapped.Feature";
    }

    @Override
    protected void getQueryTermsWithoutOrganisms(List<org.apache.lucene.search.Query> queries) {

        BooleanQuery bq = new BooleanQuery();
        if (sigP) {
            bq.add(new TermQuery(new Term("signalP", "true")), Occur.MUST);
        }
        if (gpi) {
            bq.add(new TermQuery(new Term("gpiAnchored", "true")), Occur.MUST);
        }
        if (apicoplast) {
            bq.add(new TermQuery(new Term("apicoplast", "true")), Occur.MUST);
        }


        queries.add(bq);

        //if (pseudogenes) {
        //    queries.add(geneOrPseudogeneQuery);
        //} else {
            //queries.add(geneQuery);
        //}

//        BooleanQuery organismQuery = makeQueryForOrganisms(orgNames);
//        queries.add(organismQuery);
    }

    // ------ Autogenerated code below here

    public boolean isSigP() {
        return sigP;
    }

    public void setSigP(boolean sigP) {
        this.sigP = sigP;
    }

    public boolean isGpi() {
        return gpi;
    }

    public void setGpi(boolean gpi) {
        this.gpi = gpi;
    }

    public boolean isApicoplast() {
        return apicoplast;
    }

    public void setApicoplast(boolean apicoplast) {
        this.apicoplast = apicoplast;
    }

    @Override
    protected String[] getParamNames() {
        return new String[] {"sigP", "gpi", "apicoplast"};
    }


}
