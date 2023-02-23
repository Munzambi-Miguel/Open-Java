/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.entity;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hdglo
 */
@Entity
@Table(name = "armazem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Armazem.findAll", query = "SELECT a FROM Armazem a")
    , @NamedQuery(name = "Armazem.findById", query = "SELECT a FROM Armazem a WHERE a.id = :id")
    , @NamedQuery(name = "Armazem.findByTotaEntrada", query = "SELECT a FROM Armazem a WHERE a.totaEntrada = :totaEntrada")
    , @NamedQuery(name = "Armazem.findByTotalAtual", query = "SELECT a FROM Armazem a WHERE a.totalAtual = :totalAtual")})
public class Armazem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "totaEntrada")
    private BigInteger totaEntrada;
    @Column(name = "totalAtual")
    private BigInteger totalAtual;
    @JoinColumn(name = "entity_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private com.developer.java.entity.Entity entityId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "armazemid")
    private List<Deposito> depositoList;

    public Armazem() {
    }

    public Armazem(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getTotaEntrada() {
        return totaEntrada;
    }

    public void setTotaEntrada(BigInteger totaEntrada) {
        this.totaEntrada = totaEntrada;
    }

    public BigInteger getTotalAtual() {
        return totalAtual;
    }

    public void setTotalAtual(BigInteger totalAtual) {
        this.totalAtual = totalAtual;
    }

    public com.developer.java.entity.Entity getEntityId() {
        return entityId;
    }

    public void setEntityId(com.developer.java.entity.Entity entityId) {
        this.entityId = entityId;
    }

    @XmlTransient
    public List<Deposito> getDepositoList() {
        return depositoList;
    }

    public void setDepositoList(List<Deposito> depositoList) {
        this.depositoList = depositoList;
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
        if (!(object instanceof Armazem)) {
            return false;
        }
        Armazem other = (Armazem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.developer.java.entity.Armazem[ id=" + id + " ]";
    }
    
}
