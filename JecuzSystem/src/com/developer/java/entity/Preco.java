/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hdglo
 */
@Entity
@Table(name = "preco")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Preco.findAll", query = "SELECT p FROM Preco p")
    , @NamedQuery(name = "Preco.findById", query = "SELECT p FROM Preco p WHERE p.id = :id")
    , @NamedQuery(name = "Preco.findByValor", query = "SELECT p FROM Preco p WHERE p.valor = :valor")
    , @NamedQuery(name = "Preco.findByDesconto", query = "SELECT p FROM Preco p WHERE p.desconto = :desconto")
    , @NamedQuery(name = "Preco.findByEstado", query = "SELECT p FROM Preco p WHERE p.estado = :estado")
    , @NamedQuery(name = "Preco.findByDataInsert", query = "SELECT p FROM Preco p WHERE p.dataInsert = :dataInsert")})
public class Preco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "desconto")
    private BigDecimal desconto;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Basic(optional = false)
    @Column(name = "dataInsert")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInsert;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;

    public Preco() {
    }

    public Preco(Integer id) {
        this.id = id;
    }

    public Preco(Integer id, BigDecimal valor, boolean estado, Date dataInsert) {
        this.id = id;
        this.valor = valor;
        this.estado = estado;
        this.dataInsert = dataInsert;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getDataInsert() {
        return dataInsert;
    }

    public void setDataInsert(Date dataInsert) {
        this.dataInsert = dataInsert;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
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
        if (!(object instanceof Preco)) {
            return false;
        }
        Preco other = (Preco) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.developer.java.entity.Preco[ id=" + id + " ]";
    }
    
}
