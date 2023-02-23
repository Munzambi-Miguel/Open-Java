/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Lob;
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
@Table(name = "caixa_movement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CaixaMovement.findAll", query = "SELECT c FROM CaixaMovement c")
    , @NamedQuery(name = "CaixaMovement.findById", query = "SELECT c FROM CaixaMovement c WHERE c.id = :id")
    , @NamedQuery(name = "CaixaMovement.findByCorrentValue", query = "SELECT c FROM CaixaMovement c WHERE c.correntValue = :correntValue")
    , @NamedQuery(name = "CaixaMovement.findByInitialValue", query = "SELECT c FROM CaixaMovement c WHERE c.initialValue = :initialValue")
    , @NamedQuery(name = "CaixaMovement.findByDataInsert", query = "SELECT c FROM CaixaMovement c WHERE c.dataInsert = :dataInsert")
    , @NamedQuery(name = "CaixaMovement.findByTipoMovimento", query = "SELECT c FROM CaixaMovement c WHERE c.tipoMovimento = :tipoMovimento")})
public class CaixaMovement implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "caixaMovementId")
    private List<AberturaDia> aberturaDiaList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "corrent_value")
    private BigDecimal correntValue;
    @Basic(optional = false)
    @Column(name = "initial_value")
    private BigDecimal initialValue;
    @Basic(optional = false)
    @Column(name = "data_insert")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInsert;
    @Lob
    @Column(name = "informacao")
    private String informacao;
    @Column(name = "tipo_movimento")
    private Integer tipoMovimento;
    @JoinColumn(name = "autentication_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Autentication autenticationId;
    @JoinColumn(name = "caixa_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Caixa caixaId;
    @JoinColumn(name = "fatura_id", referencedColumnName = "id")
    @ManyToOne
    private Fatura faturaId;

    public CaixaMovement() {
    }

    public CaixaMovement(Integer id) {
        this.id = id;
    }

    public CaixaMovement(Integer id, BigDecimal correntValue, BigDecimal initialValue, Date dataInsert) {
        this.id = id;
        this.correntValue = correntValue;
        this.initialValue = initialValue;
        this.dataInsert = dataInsert;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getCorrentValue() {
        return correntValue;
    }

    public void setCorrentValue(BigDecimal correntValue) {
        this.correntValue = correntValue;
    }

    public BigDecimal getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(BigDecimal initialValue) {
        this.initialValue = initialValue;
    }

    public Date getDataInsert() {
        return dataInsert;
    }

    public void setDataInsert(Date dataInsert) {
        this.dataInsert = dataInsert;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public Integer getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(Integer tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public Autentication getAutenticationId() {
        return autenticationId;
    }

    public void setAutenticationId(Autentication autenticationId) {
        this.autenticationId = autenticationId;
    }

    public Caixa getCaixaId() {
        return caixaId;
    }

    public void setCaixaId(Caixa caixaId) {
        this.caixaId = caixaId;
    }

    public Fatura getFaturaId() {
        return faturaId;
    }

    public void setFaturaId(Fatura faturaId) {
        this.faturaId = faturaId;
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
        if (!(object instanceof CaixaMovement)) {
            return false;
        }
        CaixaMovement other = (CaixaMovement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.developer.java.entity.CaixaMovement[ id=" + id + " ]";
    }

    @XmlTransient
    public List<AberturaDia> getAberturaDiaList() {
        return aberturaDiaList;
    }

    public void setAberturaDiaList(List<AberturaDia> aberturaDiaList) {
        this.aberturaDiaList = aberturaDiaList;
    }
    
}
