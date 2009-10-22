package org.genedb.querying.tmpquery;

import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.ConstantScoreRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.genedb.querying.core.QueryClass;
import org.genedb.querying.core.QueryParam;

//import org.hibernate.validator.Min;
import org.springframework.validation.Errors;

@QueryClass(
        title="Coding and pseudogenes by protein length",
        shortDesc="Get a list of transcripts ",
        longDesc=""
    )
public class ProteinLengthQuery extends OrganismLuceneQuery {

    @QueryParam(
            order=2,
            title="Minimum length of protein in bases"
    )
    //@Min(value=1, message="{min.minimum}")
    private int min = 1;

    @QueryParam(
            order=3,
            title="Maximum length of protein in bases"
    )
    private int max = 500;

    private String type = "polypeptide";


    @Override
    protected String getluceneIndexName() {
        return "org.gmod.schema.mapped.Feature";
    }
    
    @Override
    public String getQueryDescription() {
    	return "Searches for proteins of a given length.";
    }

    @Override
    protected void getQueryTermsWithoutOrganisms(List<Query> queries) {
        //Get the range
        queries.add(
                new ConstantScoreRangeQuery(
                        "sequenceLength",
                        String.format("%06d",  min),
                        String.format("%06d",  max),
                        true, true));

        //Get the type
        queries.add(
                new TermQuery(
                        new Term("type.name", type)));

    }

    // ------ Autogenerated code below here

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

//    @Override
//    protected String[] getParamNames() {
//        String[] superParamNames = super.getParamNames();
//        String[] thisQuery = new String[] {"min", "max"};
//        return arrayAppend(superParamNames, thisQuery);
//    }
//
//    @Override
//    protected void populateQueryWithParams(org.hibernate.Query query) {
//        super.populateQueryWithParams(query);
//        query.setInteger("min", min);
//        query.setInteger("max", max);
//    }


    @Override
    protected void extraValidation(Errors errors) {

        //validate dependent properties
        if (!errors.hasErrors()) {
            if (getMin() > getMax()) {
                errors.reject("min.greater.than.max");
            }
        }
    }


    @Override
    protected String[] getParamNames() {
        return new String[] {"min", "max", "type"};
    }

}
