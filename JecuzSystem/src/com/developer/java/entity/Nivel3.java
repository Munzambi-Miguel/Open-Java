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
@Table(name = "nivel_3")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nivel3.findAll", query = "SELECT n FROM Nivel3 n")
    , @NamedQuery(name = "Nivel3.findById", query = "SELECT n FROM Nivel3 n WHERE n.id = :id")
    , @NamedQuery(name = "Nivel3.findByDesignation", query = "SELECT n FROM Nivel3 n WHERE n.designation = :designation")})
public class Nivel3 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation")
    private String designation;
    @OneToMany(mappedBy = "nivel3Id")
    private List<Product> productList;
    @JoinColumn(name = "nivel_2_id", referencedColumnName = "id")
    @ManyToOne
    private Nivel2 nivel2Id;

    public Nivel3() {
    }

    public Nivel3(Integer id) {
        this.id = id;
    }

    public Nivel3(Integer id, String designation) {
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

    @XmlTransient
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Nivel2 getNivel2Id() {
        return nivel2Id;
    }

    public void setNivel2Id(Nivel2 nivel2Id) {
        this.nivel2Id = nivel2Id;
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
        if (!(object instanceof Nivel3)) {
            return false;
        }
        Nivel3 other = (Nivel3) object;
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
