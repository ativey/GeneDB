package org.gmod.schema.sequence;

import static javax.persistence.GenerationType.SEQUENCE;

import org.gmod.schema.general.DbXRef;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="feature_cvterm_dbxref")
public class FeatureCvTermDbXRef implements Serializable {

    // Fields
    @SequenceGenerator(name="generator", sequenceName="feature_cvterm_dbxref_feature_cvterm_dbxref_id_seq")
    @Id @GeneratedValue(strategy=SEQUENCE, generator="generator")
    @Column(name="feature_cvterm_dbxref_id", unique=false, nullable=false, insertable=true, updatable=true)
     private int featureCvTermDbXRefId;

     @ManyToOne(cascade={}, fetch=FetchType.LAZY)
         @JoinColumn(name="dbxref_id", unique=false, nullable=false, insertable=true, updatable=true)
     private DbXRef dbXRef;

     @ManyToOne(cascade={}, fetch=FetchType.LAZY)
         @JoinColumn(name="feature_cvterm_id", unique=false, nullable=false, insertable=true, updatable=true)
     private FeatureCvTerm featureCvTerm;

     // Constructors

    /** default constructor */
    private FeatureCvTermDbXRef() {
        // Deliberately empty default constructor
    }

    /** full constructor */
    public FeatureCvTermDbXRef(DbXRef dbXRef, FeatureCvTerm featureCvTerm) {
       this.dbXRef = dbXRef;
       this.featureCvTerm = featureCvTerm;
    }


    // Property accessors
    private int getFeatureCvTermDbXRefId() {
        return this.featureCvTermDbXRefId;
    }

    private void setFeatureCvTermDbXRefId(int featureCvTermDbXRefId) {
        this.featureCvTermDbXRefId = featureCvTermDbXRefId;
    }

    public DbXRef getDbXRef() {
        return this.dbXRef;
    }

    private void setDbXRef(DbXRef dbXRef) {
        this.dbXRef = dbXRef;
    }

    private FeatureCvTerm getFeatureCvTerm() {
        return this.featureCvTerm;
    }

    private void setFeatureCvTerm(FeatureCvTerm featureCvTerm) {
        this.featureCvTerm = featureCvTerm;
    }
}

