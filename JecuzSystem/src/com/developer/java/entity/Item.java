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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i")
    , @NamedQuery(name = "Item.findById", query = "SELECT i FROM Item i WHERE i.id = :id")
    , @NamedQuery(name = "Item.findByQuantity", query = "SELECT i FROM Item i WHERE i.quantity = :quantity")
    , @NamedQuery(name = "Item.findByProduto", query = "SELECT i FROM Item i WHERE i.produto = :produto")
    , @NamedQuery(name = "Item.findByTotal", query = "SELECT i FROM Item i WHERE i.total = :total")})
public class Item implements Serializable {

    @Lob
    @Column(name = "informa\u00e7\u00e3o")
    private String informação;

    @JoinColumn(name = "operador", referencedColumnName = "id")
    @ManyToOne
    private Autentication operador;

    @Basic(optional = false)
    @Column(name = "state")
    private int state;
    @Column(name = "data_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUpdate;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;
    @Basic(optional = false)
    @Column(name = "produto")
    private String produto;
    @Basic(optional = false)
    @Column(name = "total")
    private String total;
    @JoinColumn(name = "fatura_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Fatura faturaId;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;

    public Item() {
    }

    public Item(Integer id) {
        this.id = id;
    }

    public Item(Integer id, int quantity, String produto, String total) {
        this.id = id;
        this.quantity = quantity;
        this.produto = produto;
        this.total = total;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Fatura getFaturaId() {
        return faturaId;
    }

    public void setFaturaId(Fatura faturaId) {
        this.faturaId = faturaId;
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
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.developer.java.entity.Item[ id=" + id + " ]";
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(Date dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    public Autentication getOperador() {
        return operador;
    }

    public void setOperador(Autentication operador) {
        this.operador = operador;
    }

    public String getInformação() {
        return informação;
    }

    public void setInformação(String informação) {
        this.informação = informação;
    }
    
}
