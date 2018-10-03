package org.genedb.top.web.mvc.model;



import java.io.Serializable;
import java.util.List;

import org.genedb.top.db.domain.objects.PolypeptideRegionGroup;
import org.genedb.top.chado.utils.PeptideProperties;


//@XStreamAlias("transcript")
public class TranscriptDTO extends FeatureDTO implements Serializable {

    private PolypeptideDTO polypeptide;
    private int transcriptId;
    
    private List<PolypeptideRegionGroup> domainInformation;
    private PeptideProperties polypeptideProperties;
    private List<MembraneStructureComponentDTO> membraneStructureComponents;
    
    public int getTranscriptId() {
        return transcriptId;
    }

    public void setTranscriptId(int transcriptId) {
        this.transcriptId = transcriptId;
    }
    
	public PolypeptideDTO getPolypeptide() {
		return polypeptide;
	}

	public void setPolypeptide(PolypeptideDTO polypeptide) {
		this.polypeptide=polypeptide;
	}
	
	public List<PolypeptideRegionGroup> getDomainInformation() {
	    return domainInformation;
	}
	
	public void setDomainInformation(List<PolypeptideRegionGroup> domainInformation) {
	    this.domainInformation = domainInformation;
	}
	
	public PeptideProperties getPolypeptideProperties() {
	    return polypeptideProperties;
	}
	
	public void setPolypeptideProperties(PeptideProperties polypeptideProperties) {
	    this.polypeptideProperties = polypeptideProperties;
	}
	
	public void setMembraneStructureComponents(List<MembraneStructureComponentDTO> membraneStructureComponents) {
		this.membraneStructureComponents=membraneStructureComponents;
	}
	
	public List<MembraneStructureComponentDTO> getMembraneStructureComponents() {
		return membraneStructureComponents;
	}
	
}
