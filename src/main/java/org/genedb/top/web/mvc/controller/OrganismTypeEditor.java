package org.genedb.top.web.mvc.controller;

import org.genedb.top.db.dao.OrganismDao;

import org.genedb.top.chado.mapped.Organism;



public class OrganismTypeEditor extends java.beans.PropertyEditorSupport {

    private OrganismDao organismDao;

    public OrganismDao getOrganismDao() {
        return organismDao;
    }

    public void setOrganismDao(OrganismDao organismDao) {
        this.organismDao = organismDao;
    }
    
    public void setAsOrganism(String org) {
        Organism o = organismDao.getOrganismByCommonName(org);
        setValue(o);
    }
}
