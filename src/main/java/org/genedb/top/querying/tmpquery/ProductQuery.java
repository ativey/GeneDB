package org.genedb.top.querying.tmpquery;

import org.genedb.top.querying.core.QueryClass;
import org.genedb.top.querying.core.QueryParam;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.springframework.util.StringUtils;

import java.util.List;

@QueryClass(
        title="Coding and pseudogenes by protein length",
        shortDesc="Get a list of transcripts ",
        longDesc=""
    )
public class ProductQuery extends OrganismLuceneQuery {

    @QueryParam(
            order=1,
            title="The search string"
    )
    private String search = "";

    @QueryParam(order = 3, title = "Include pseudogenes")
    private boolean pseudogenes;


    @Override
    protected String getluceneIndexName() {
        return "org.gmod.schema.mapped.Feature";
    }

    @Override
    public String getQueryDescription() {
    	return "Searches for polypeptides of a certain function.";
    }

    @Override
    public String getQueryName() {
        return "Product";
    }
    
    // TODO bring this search strategy in line with QuickSearch's
    @Override
    protected void getQueryTermsWithoutOrganisms(List<org.apache.lucene.search.Query> queries) {

        BooleanQuery bq = new BooleanQuery();
        if (StringUtils.containsWhitespace(search)) {
            for (String term : search.split(" ")) {
                bq.add(new TermQuery(new Term("expandedProduct",term.toLowerCase()
                    )), Occur.MUST);
            }
        } else {
            if (search.indexOf('*') == -1) {
                bq.add(new TermQuery(new Term("expandedProduct",search.toLowerCase())), Occur.SHOULD);
            } else {
                bq.add(new WildcardQuery(new Term("expandedProduct", search.toLowerCase())), Occur.SHOULD);
            }
        }

        queries.add(bq);

        if (pseudogenes) {
            queries.add(productiveTranscriptQuery);
        } else {
            queries.add(mRNAQuery);
        }
    }

    // ------ Autogenerated code below here

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    @Override
    protected String[] getParamNames() {
        return new String[] {"search"};
    }

    public boolean isPseudogenes() {
        return pseudogenes;
    }

    public void setPseudogenes(boolean pseudogenes) {
        this.pseudogenes = pseudogenes;
    }

}
