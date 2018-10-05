package org.genedb.top.db.domain.objects;

import org.genedb.top.chado.mapped.FeatureLoc;

import java.awt.Color;

public class SimplePolypeptideRegion extends PolypeptideRegion {

    private String name;
    private Color color;

    public SimplePolypeptideRegion(int fmin, int fmax, String name, String description, String score, String significance, Color color) {
        super(fmin, fmax, description, score, significance);
        this.name = name;
        this.color = color;
    }

    public static SimplePolypeptideRegion build(org.genedb.top.chado.feature.PolypeptideRegion region, String description, String score, String significance, Color color) {
        FeatureLoc regionLoc = region.getRankZeroFeatureLoc();
        return new SimplePolypeptideRegion(regionLoc.getFmin(), regionLoc.getFmax(), region.getUniqueName(), description, score, significance, color);
    }

    @Override
    public String getUniqueName() {
        return name;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
