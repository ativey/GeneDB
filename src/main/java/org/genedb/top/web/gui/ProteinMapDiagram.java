package org.genedb.top.web.gui;

import org.genedb.top.db.domain.objects.PolypeptideRegionGroup;
import org.genedb.top.db.domain.objects.SimplePolypeptideRegion;
import org.genedb.top.db.domain.objects.SimpleRegionGroup;

import org.genedb.top.chado.feature.GPIAnchorCleavageSite;
import org.genedb.top.chado.feature.MembraneStructure;
import org.genedb.top.chado.feature.Polypeptide;
import org.genedb.top.chado.feature.PolypeptideRegion;
import org.genedb.top.chado.feature.SignalPeptide;
import org.genedb.top.chado.feature.Transcript;

import java.awt.Color;
import java.util.Collection;

public class ProteinMapDiagram extends TrackedDiagram {

    private String organism;
    private String polypeptideUniqueName;
    private String transcriptUniqueName;
    private int transcriptFeatureId;
    private MembraneStructure membraneStructure;

    public ProteinMapDiagram(Polypeptide polypeptide, Transcript transcript,
            Collection<PolypeptideRegionGroup> regionGroups) {
        super(0, polypeptide.getSeqLen());
        this.organism = polypeptide.getOrganism().getCommonName();
        this.polypeptideUniqueName = polypeptide.getUniqueName();
        this.transcriptUniqueName = transcript.getUniqueName();
        this.transcriptFeatureId = transcript.getFeatureId();
        this.membraneStructure = polypeptide.getMembraneStructure();

        this.packSubfeatures = AllocatedCompoundFeature.Mode.STRATIFIED_LTR;
        this.numberOfBlankTracksAboveCompoundFeature = 2;

        addRegion           (polypeptide, regionGroups, "Signal peptide", "Sig. pep.",
            Color.GREEN,        SignalPeptide.class);

        if (polypeptide.isGPIAnchored()) {
            addRegionToNTerminus(polypeptide, regionGroups, "GPI anchor", "GPI",
                new Color(255, 165, 0), GPIAnchorCleavageSite.class);
        }

        allocateTracks(regionGroups, false);
    }

    private <T extends PolypeptideRegion> void addRegion(Polypeptide polypeptide,
            Collection<PolypeptideRegionGroup> regionGroups, String title, String abbreviation,
            Color color, Class<T> regionClass) {

        Collection<T> regions = polypeptide.getRegions(regionClass);
        if (!regions.isEmpty()) {
            PolypeptideRegionGroup regionGroup = new SimpleRegionGroup(title, abbreviation);
            for(T region: regions) {
                regionGroup.addRegion(SimplePolypeptideRegion.build(region, title, null, null, color));
            }
            regionGroups.add(regionGroup);
        }
    }

    private <T extends PolypeptideRegion> void addRegionToNTerminus(Polypeptide polypeptide,
            Collection<PolypeptideRegionGroup> regionGroups, String title, String abbreviation,
            Color color, Class<T> regionClass) {

        Collection<T> regions = polypeptide.getRegions(regionClass);
        if (!regions.isEmpty()) {
            PolypeptideRegionGroup regionGroup = new SimpleRegionGroup(title, abbreviation);
            for(T region: regions) {
                SimplePolypeptideRegion simplePolypeptideRegion = new SimplePolypeptideRegion(
                    region.getFmin(), polypeptide.getSeqLen(), region.getUniqueName(), title, null, null, color);
                regionGroup.addRegion(simplePolypeptideRegion);
            }
            regionGroups.add(regionGroup);
        }
    }

    String getOrganism() {
        return organism;
    }

    String getPolypeptideUniqueName() {
        return polypeptideUniqueName;
    }

    String getTranscriptUniqueName() {
        return transcriptUniqueName;
    }

    int getTranscriptFeatureId() {
        return transcriptFeatureId;
    }

    MembraneStructure getMembraneStructure() {
        return membraneStructure;
    }

    public boolean isEmpty() {
        return (getAllocatedCompoundFeatures().isEmpty() && membraneStructure == null)
            || getSize() <= 0;
    }
}
