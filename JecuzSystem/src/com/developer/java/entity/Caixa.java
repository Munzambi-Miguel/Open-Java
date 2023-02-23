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
@Table(name = "caixa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Caixa.findAll", query = "SELECT c FROM Caixa c")
    , @NamedQuery(name = "Caixa.findById", query = "SELECT c FROM Caixa c WHERE c.id = :id")
    , @NamedQuery(name = "Caixa.findByDataInsert", query = "SELECT c FROM Caixa c WHERE c.dataInsert = :dataInsert")
    , @NamedQuery(name = "Caixa.findByState", query = "SELECT c FROM Caixa c WHERE c.state = :state")})
public class Caixa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "data_insert")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInsert;
    @Basic(optional = false)
    @Column(name = "state")
    private int state;
    @JoinColumn(name = "autentication_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Autentication autenticationId;
    @JoinColumn(name = "maquina_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Maquina maquinaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "caixaId")
    private List<CaixaMovement> caixaMovementList;

    public Caixa() {
    }

    public Caixa(Integer id) {
        this.id = id;
    }

    public Caixa(Integer id, Date dataInsert, int state) {
        this.id = id;
        this.dataInsert = dataInsert;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataInsert() {
        return dataInsert;
    }

    public void setDataInsert(Date dataInsert) {
        this.dataInsert = dataInsert;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Autentication getAutenticationId() {
        return autenticationId;
    }

    public void setAutenticationId(Autentication autenticationId) {
        this.autenticationId = autenticationId;
    }

    public Maquina getMaquinaId() {
        return maquinaId;
    }

    public void setMaquinaId(Maquina maquinaId) {
        this.maquinaId = maquinaId;
    }

    @XmlTransient
    public List<CaixaMovement> getCaixaMovementList() {
        return caixaMovementList;
    }

    public void setCaixaMovementList(List<CaixaMovement> caixaMovementList) {
        this.caixaMovementList = caixaMovementList;
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
        if (!(object instanceof Caixa)) {
            return false;
        }
        Caixa other = (Caixa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.developer.java.entity.Caixa[ id=" + id + " ]";
    }
    
}
