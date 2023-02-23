/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hdglo
 */
@Entity
@Table(name = "produttype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Produttype.findAll", query = "SELECT p FROM Produttype p")
    , @NamedQuery(name = "Produttype.findById", query = "SELECT p FROM Produttype p WHERE p.id = :id")
    , @NamedQuery(name = "Produttype.findByDesignation", query = "SELECT p FROM Produttype p WHERE p.designation = :designation")
    , @NamedQuery(name = "Produttype.findByDateInsert", query = "SELECT p FROM Produttype p WHERE p.dateInsert = :dateInsert")
    , @NamedQuery(name = "Produttype.findByDateUpdate", query = "SELECT p FROM Produttype p WHERE p.dateUpdate = :dateUpdate")})
public class Produttype implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation")
    private String designation;
    @Column(name = "date_insert")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateInsert;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "funcionario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Autentication funcionario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "produtTypeid")
    private List<Product> productList;

    public Produttype() {
    }

    public Produttype(Integer id) {
        this.id = id;
    }

    public Produttype(Integer id, String designation) {
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

    public Date getDateInsert() {
        return dateInsert;
    }

    public void setDateInsert(Date dateInsert) {
        this.dateInsert = dateInsert;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Autentication getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Autentication funcionario) {
        this.funcionario = funcionario;
    }

    @XmlTransient
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
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
        if (!(object instanceof Produttype)) {
            return false;
        }
        Produttype other = (Produttype) object;
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
