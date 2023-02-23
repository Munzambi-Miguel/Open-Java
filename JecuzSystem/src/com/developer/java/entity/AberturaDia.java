/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hdglo
 */
@Entity
@Table(name = "abertura_dia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AberturaDia.findAll", query = "SELECT a FROM AberturaDia a")
    , @NamedQuery(name = "AberturaDia.findByNumCaixa", query = "SELECT a FROM AberturaDia a WHERE a.numCaixa = :numCaixa")
    , @NamedQuery(name = "AberturaDia.findByDiaCorrent", query = "SELECT a FROM AberturaDia a WHERE a.diaCorrent = :diaCorrent")})
public class AberturaDia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "num_caixa")
    private String numCaixa;
    @Basic(optional = false)
    @Column(name = "dia_corrent")
    @Temporal(TemporalType.TIMESTAMP)
    private Date diaCorrent;
    @JoinColumn(name = "caixa_movement_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CaixaMovement caixaMovementId;

    public AberturaDia() {
    }

    public AberturaDia(String numCaixa) {
        this.numCaixa = numCaixa;
    }

    public AberturaDia(String numCaixa, Date diaCorrent) {
        this.numCaixa = numCaixa;
        this.diaCorrent = diaCorrent;
    }

    public String getNumCaixa() {
        return numCaixa;
    }

    public void setNumCaixa(String numCaixa) {
        this.numCaixa = numCaixa;
    }

    public Date getDiaCorrent() {
        return diaCorrent;
    }

    public void setDiaCorrent(Date diaCorrent) {
        this.diaCorrent = diaCorrent;
    }

    public CaixaMovement getCaixaMovementId() {
        return caixaMovementId;
    }

    public void setCaixaMovementId(CaixaMovement caixaMovementId) {
        this.caixaMovementId = caixaMovementId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numCaixa != null ? numCaixa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AberturaDia)) {
            return false;
        }
        AberturaDia other = (AberturaDia) object;
        if ((this.numCaixa == null && other.numCaixa != null) || (this.numCaixa != null && !this.numCaixa.equals(other.numCaixa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.developer.java.entity.AberturaDia[ numCaixa=" + numCaixa + " ]";
    }
    
}
