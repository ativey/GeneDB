package org.gmod.schema.utils;

/**
 * Class to store GeneName and OrganismName returned by searches like
 * getFeaturesByCvTermNameAndCvName
 */

public class GeneNameOrganism {
    
    private String geneName;
    private String organismName;
    
    
    public GeneNameOrganism(String geneName, String organism) {
        this.geneName = geneName;
        this.organismName = organism;
    }
    
    public String getGeneName() {
        return geneName;
    }
    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }
    public String getOrganismName() {
        return organismName;
    }
    public void setOrganismName(String organismName) {
        this.organismName = organismName;
    }
    
    
}
