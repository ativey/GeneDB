package org.genedb.top.web.tags.db;

import org.genedb.top.db.taxon.TaxonNameType;
import org.genedb.top.db.taxon.TaxonNode;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

public class HomepageTreeTag extends AbstractHomepageTag {
    
    @Override
    protected void display(TaxonNode node, JspWriter out) throws IOException {
    	display(node, out, 1);
    }
    
    protected void display(TaxonNode node, JspWriter out, int indent) throws IOException {
        out.write("<ul>");
        out.write("<li>");
        out.write(node.getName(TaxonNameType.FULL));
        out.write("</li>");
        for (TaxonNode child : node.getChildren()) {
            display(child, out, indent+1);
        }
        out.write("</ul>");
    }

}
