package org.gmod.schema.mapped;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * PhylonodeOrganism generated by hbm2java
 */
@Entity
@Table(name = "phylonode_organism", uniqueConstraints = { @UniqueConstraint(columnNames = { "phylonode_id" }) })
public class PhylonodeOrganism implements java.io.Serializable {

    // Fields

    @Id
    @Column(name = "phylonode_organism_id", unique = true, nullable = false, insertable = true, updatable = true)
    private int phylonodeOrganismId;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "organism_id", unique = false, nullable = false, insertable = true, updatable = true)
    private Organism organism;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "phylonode_id", unique = true, nullable = false, insertable = true, updatable = true)
    private Phylonode phylonode;

    // Constructors

    /** default constructor */
    PhylonodeOrganism() {
        // Deliberately empty default constructor
    }

    /** full constructor */
    public PhylonodeOrganism(Organism organism, Phylonode phylonode) {
        this.organism = organism;
        this.phylonode = phylonode;
    }

    // Property accessors
    public int getPhylonodeOrganismId() {
        return this.phylonodeOrganismId;
    }

    public Organism getOrganism() {
        return this.organism;
    }

    void setOrganism(Organism organism) {
        this.organism = organism;
    }

    public Phylonode getPhylonode() {
        return this.phylonode;
    }

    void setPhylonode(Phylonode phylonode) {
        this.phylonode = phylonode;
    }

}
