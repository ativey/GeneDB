package org.gmod.schema.sequence.feature;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.gmod.schema.cfg.FeatureType;
import org.hibernate.search.annotations.Indexed;

@Entity
@FeatureType(cv="sequence", term="tRNA")
@Indexed
public class TRNA extends Transcript {

    @Override @Transient
    public Integer getColourId() {
        return null;
    }

}
