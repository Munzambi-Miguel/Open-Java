/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "grup")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grup.findAll", query = "SELECT g FROM Grup g")
    , @NamedQuery(name = "Grup.findById", query = "SELECT g FROM Grup g WHERE g.id = :id")
    , @NamedQuery(name = "Grup.findByDesignation", query = "SELECT g FROM Grup g WHERE g.designation = :designation")
    , @NamedQuery(name = "Grup.findByGrupType", query = "SELECT g FROM Grup g WHERE g.grupType = :grupType")})
public class Grup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation")
    private String designation;
    @Basic(optional = false)
    @Column(name = "grup_type")
    private int grupType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupId")
    private List<Autentication> autenticationList;

    public Grup() {
    }

    public Grup(Integer id) {
        this.id = id;
    }

    public Grup(Integer id, String designation, int grupType) {
        this.id = id;
        this.designation = designation;
        this.grupType = grupType;
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

    public int getGrupType() {
        return grupType;
    }

    public void setGrupType(int grupType) {
        this.grupType = grupType;
    }

    @XmlTransient
    public List<Autentication> getAutenticationList() {
        return autenticationList;
    }

    public void setAutenticationList(List<Autentication> autenticationList) {
        this.autenticationList = autenticationList;
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
        if (!(object instanceof Grup)) {
            return false;
        }
        Grup other = (Grup) object;
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
