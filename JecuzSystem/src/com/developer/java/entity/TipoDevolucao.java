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
@Table(name = "tipo_devolucao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoDevolucao.findAll", query = "SELECT t FROM TipoDevolucao t")
    , @NamedQuery(name = "TipoDevolucao.findById", query = "SELECT t FROM TipoDevolucao t WHERE t.id = :id")
    , @NamedQuery(name = "TipoDevolucao.findByDesignation", query = "SELECT t FROM TipoDevolucao t WHERE t.designation = :designation")
    , @NamedQuery(name = "TipoDevolucao.findByPermitirEntrata", query = "SELECT t FROM TipoDevolucao t WHERE t.permitirEntrata = :permitirEntrata")})
public class TipoDevolucao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "designation")
    private String designation;
    @Column(name = "permitir_entrata")
    private Integer permitirEntrata;
    @OneToMany(mappedBy = "tipoDevolucaoId")
    private List<Fatura> faturaList;

    public TipoDevolucao() {
    }

    public TipoDevolucao(Integer id) {
        this.id = id;
    }

    public TipoDevolucao(Integer id, String designation) {
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

    public Integer getPermitirEntrata() {
        return permitirEntrata;
    }

    public void setPermitirEntrata(Integer permitirEntrata) {
        this.permitirEntrata = permitirEntrata;
    }

    @XmlTransient
    public List<Fatura> getFaturaList() {
        return faturaList;
    }

    public void setFaturaList(List<Fatura> faturaList) {
        this.faturaList = faturaList;
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
        if (!(object instanceof TipoDevolucao)) {
            return false;
        }
        TipoDevolucao other = (TipoDevolucao) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.developer.java.entity.TipoDevolucao[ id=" + id + " ]";
    }
    
}
