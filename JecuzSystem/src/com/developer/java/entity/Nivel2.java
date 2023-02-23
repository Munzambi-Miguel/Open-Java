/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hdglo
 */
@Entity
@Table(name = "nivel_2")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nivel2.findAll", query = "SELECT n FROM Nivel2 n")
    , @NamedQuery(name = "Nivel2.findById", query = "SELECT n FROM Nivel2 n WHERE n.id = :id")
    , @NamedQuery(name = "Nivel2.findByDesignation", query = "SELECT n FROM Nivel2 n WHERE n.designation = :designation")})
public class Nivel2 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation")
    private String designation;
    @JoinColumn(name = "nivel_1_id", referencedColumnName = "id")
    @ManyToOne
    private Nivel1 nivel1Id;
    @OneToMany(mappedBy = "nivel2Id")
    private List<Nivel3> nivel3List;

    public Nivel2() {
    }

    public Nivel2(Integer id) {
        this.id = id;
    }

    public Nivel2(Integer id, String designation) {
        this.id = id;
        this.designation = designation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Nivel1 getNivel1Id() {
        return nivel1Id;
    }

    public void setNivel1Id(Nivel1 nivel1Id) {
        this.nivel1Id = nivel1Id;
    }

    @XmlTransient
    public List<Nivel3> getNivel3List() {
        return nivel3List;
    }

    public void setNivel3List(List<Nivel3> nivel3List) {
        this.nivel3List = nivel3List;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nivel2)) {
            return false;
        }
        Nivel2 other = (Nivel2) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return designation;
    }
    
}
