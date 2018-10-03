package org.genedb.top.web.tags.db;

import org.genedb.top.chado.utils.CountedName;

import org.displaytag.decorator.TableDecorator;

public class TableWrapper extends TableDecorator {
    
    public String getdisplayTerm(){
        CountedName product = (CountedName) getCurrentRowObject();
        String name = product.getName();
        return "<a href=\"./FeatureByCvTermNameAndCvName?name=" + name + "&cvName=genedb_products" + "\">" + name + "</a>";
    }

}
