/*
 * Copyright (c) 2006 Genome Research Limited.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by  the Free Software Foundation; either version 2 of the License or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this program; see the file COPYING.LIB.  If not, write to
 * the Free Software Foundation Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307 USA
 */

package org.genedb.top.web.mvc.controller;

import static org.genedb.top.web.mvc.controller.TaxonManagerListener.TAXON_NODE_MANAGER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.genedb.top.db.taxon.TaxonNode;
import org.genedb.top.db.taxon.TaxonNodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Choose an organism, or set of orgs, usually as a 'filter' before going to
 * another controller
 * 
 * @author Adrian Tivey (art)
 */
@Controller
public class OrganismChooserController {

    @Autowired
    private ServletContext servletContext;

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String organism = ServletRequestUtils.getStringParameter(request, "organism");
        TaxonNodeManager tnm = (TaxonNodeManager) servletContext.getAttribute(
            TAXON_NODE_MANAGER);
        TaxonNode taxonNode = tnm.getTaxonNodeForLabel(organism);
        // OrganismChooserBean ocb = (OrganismChooserBean) command;

        // TaxonNode taxonNode = ocb.getOrganism();

        Map<String, Object> model = new HashMap<String, Object>(4);
        String viewName = null;

        if (taxonNode.getChildren().size() >= 1) {
            List<String> nodes = new ArrayList<String>();
            List<TaxonNode> childrens = taxonNode.getChildren();
            for (TaxonNode node : childrens) {
                nodes.add(node.getLabel());
            }
            model.put("nodes", nodes);
            model.put("parent", taxonNode.getLabel());
            return new ModelAndView("organism/intermediate", model);
        }

        model.put("organism", taxonNode.getLabel());

        return new ModelAndView("organism/organism", model);
    }

    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException arg3) throws Exception {

        OrganismChooserBean ocb = (OrganismChooserBean) command;

        // TaxonNode taxonNode = ocb.getOrganism();
        String organism = ServletRequestUtils.getStringParameter(request, "organism");
        TaxonNodeManager tnm = (TaxonNodeManager) servletContext.getAttribute(
            TAXON_NODE_MANAGER);
        TaxonNode taxonNode = tnm.getTaxonNodeForLabel(organism);

        Map<String, Object> model = new HashMap<String, Object>(4);
        String viewName = null;

        if (taxonNode.getChildren().size() >= 1) {
            List<String> nodes = new ArrayList<String>();
            List<TaxonNode> childrens = taxonNode.getChildren();
            for (TaxonNode node : childrens) {
                nodes.add(node.getLabel());
            }
            model.put("nodes", nodes);
            model.put("parent", taxonNode.getLabel());
            return new ModelAndView("organism/intermediate", model);
        }

        model.put("organism", taxonNode.getLabel());

        return new ModelAndView("organism/organism", model);

    }
}

class OrganismChooserBean {

    private TaxonNode organism;

    public TaxonNode getOrganism() {
        return this.organism;
    }

    public void setOrganism(TaxonNode organism) {
        this.organism = organism;
    }

}
