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
@Table(name = "pais")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pais.findAll", query = "SELECT p FROM Pais p")
    , @NamedQuery(name = "Pais.findById", query = "SELECT p FROM Pais p WHERE p.id = :id")
    , @NamedQuery(name = "Pais.findByDesignation", query = "SELECT p FROM Pais p WHERE p.designation = :designation")
    , @NamedQuery(name = "Pais.findByNomeMueda", query = "SELECT p FROM Pais p WHERE p.nomeMueda = :nomeMueda")
    , @NamedQuery(name = "Pais.findBySiglaMueda", query = "SELECT p FROM Pais p WHERE p.siglaMueda = :siglaMueda")
    , @NamedQuery(name = "Pais.findByZipCode", query = "SELECT p FROM Pais p WHERE p.zipCode = :zipCode")
    , @NamedQuery(name = "Pais.findByPhoneCode", query = "SELECT p FROM Pais p WHERE p.phoneCode = :phoneCode")})
public class Pais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation")
    private String designation;
    @Column(name = "nome_mueda")
    private String nomeMueda;
    @Column(name = "sigla_mueda")
    private String siglaMueda;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "phoneCode")
    private String phoneCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paisId")
    private List<City> cityList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paisId")
    private List<Product> productList;

    public Pais() {
    }

    public Pais(Integer id) {
        this.id = id;
    }

    public Pais(Integer id, String designation) {
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

    public String getNomeMueda() {
        return nomeMueda;
    }

    public void setNomeMueda(String nomeMueda) {
        this.nomeMueda = nomeMueda;
    }

    public String getSiglaMueda() {
        return siglaMueda;
    }

    public void setSiglaMueda(String siglaMueda) {
        this.siglaMueda = siglaMueda;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    @XmlTransient
    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
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
        if (!(object instanceof Pais)) {
            return false;
        }
        Pais other = (Pais) object;
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
