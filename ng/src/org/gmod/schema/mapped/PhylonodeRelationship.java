package org.gmod.schema.mapped;

// Generated Aug 31, 2006 4:02:18 PM by Hibernate Tools 3.2.0.beta7

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import static javax.persistence.GenerationType.SEQUENCE; //Added explicit sequence generation behaviour 2.12.2015

/**
 * PhylonodeRelationship generated by hbm2java
 */
@Entity
@Table(name = "phylonode_relationship", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "subject_id", "object_id", "type_id" }) })
public class PhylonodeRelationship implements java.io.Serializable {
  
    @SequenceGenerator(name = "generator", sequenceName = "phylonode_relationship_phylonode_relationship_id_seq",  allocationSize=1)
    @Id @GeneratedValue(strategy = SEQUENCE, generator = "generator")
    @Column(name = "phylonode_relationship_id", unique = true, nullable = false, insertable = true, updatable = true)
    private int phylonodeRelationshipId;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", unique = false, nullable = false, insertable = true, updatable = true)
    private Phylonode phylonodeBySubjectId;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "object_id", unique = false, nullable = false, insertable = true, updatable = true)
    private Phylonode phylonodeByObjectId;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", unique = false, nullable = false, insertable = true, updatable = true)
    private CvTerm cvTerm;

    @Column(name = "rank", unique = false, nullable = true, insertable = true, updatable = true)
    private Integer rank;

    // Constructors

    PhylonodeRelationship() {
        // Deliberately empty default constructor
    }

    /** minimal constructor */
    public PhylonodeRelationship(Phylonode subject,
            Phylonode object, CvTerm type) {
        this.phylonodeBySubjectId = subject;
        this.phylonodeByObjectId = object;
        this.cvTerm = type;
    }

    /** full constructor */
    public PhylonodeRelationship(Phylonode subject,
            Phylonode object, CvTerm type, Integer rank) {
        this(subject, object, type);
        this.rank = rank;
    }

    // Property accessors
    public int getPhylonodeRelationshipId() {
        return this.phylonodeRelationshipId;
    }

    public Phylonode getSubjectPhylonode() {
        return this.phylonodeBySubjectId;
    }

    void setSubjectPhylonode(Phylonode subject) {
        this.phylonodeBySubjectId = subject;
    }

    public Phylonode getObjectPhylonode() {
        return this.phylonodeByObjectId;
    }

    void setObjectPhylonode(Phylonode object) {
        this.phylonodeByObjectId = object;
    }

    public CvTerm getType() {
        return this.cvTerm;
    }

    void setType(CvTerm type) {
        this.cvTerm = type;
    }

    public Integer getRank() {
        return this.rank;
    }

    void setRank(Integer rank) {
        this.rank = rank;
    }

}
