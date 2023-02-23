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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "autentication")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Autentication.findAll", query = "SELECT a FROM Autentication a")
    , @NamedQuery(name = "Autentication.findById", query = "SELECT a FROM Autentication a WHERE a.id = :id")
    , @NamedQuery(name = "Autentication.findByUsername", query = "SELECT a FROM Autentication a WHERE a.username = :username")
    , @NamedQuery(name = "Autentication.findByAutenticationType", query = "SELECT a FROM Autentication a WHERE a.autenticationType = :autenticationType")
    , @NamedQuery(name = "Autentication.findByState", query = "SELECT a FROM Autentication a WHERE a.state = :state")
    , @NamedQuery(name = "Autentication.findByViewPermition", query = "SELECT a FROM Autentication a WHERE a.viewPermition = :viewPermition")})
public class Autentication implements Serializable {

    @OneToMany(mappedBy = "operador")
    private List<Item> itemList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Lob
    @Column(name = "passsword")
    private String passsword;
    @Basic(optional = false)
    @Column(name = "autentication_type")
    private int autenticationType;
    @Basic(optional = false)
    @Column(name = "state")
    private int state;
    @Basic(optional = false)
    @Column(name = "viewPermition")
    private int viewPermition;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autenticationId")
    private List<Caixa> caixaList;
    @JoinColumn(name = "Entity_id", referencedColumnName = "id")
    @ManyToOne
    private com.developer.java.entity.Entity entityid;
    @JoinColumn(name = "grup_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Grup grupId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autenticationId")
    private List<CaixaMovement> caixaMovementList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario")
    private List<Produttype> produttypeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autenticationId")
    private List<Product> productList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionarioId")
    private List<Fatura> faturaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "autenticationId")
    private List<Movement> movementList;

    public Autentication() {
    }

    public Autentication(Integer id) {
        this.id = id;
    }

    public Autentication(Integer id, String username, String passsword, int autenticationType, int state, int viewPermition) {
        this.id = id;
        this.username = username;
        this.passsword = passsword;
        this.autenticationType = autenticationType;
        this.state = state;
        this.viewPermition = viewPermition;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasssword() {
        return passsword;
    }

    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }

    public int getAutenticationType() {
        return autenticationType;
    }

    public void setAutenticationType(int autenticationType) {
        this.autenticationType = autenticationType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getViewPermition() {
        return viewPermition;
    }

    public void setViewPermition(int viewPermition) {
        this.viewPermition = viewPermition;
    }

    @XmlTransient
    public List<Caixa> getCaixaList() {
        return caixaList;
    }

    public void setCaixaList(List<Caixa> caixaList) {
        this.caixaList = caixaList;
    }

    public com.developer.java.entity.Entity getEntityid() {
        return entityid;
    }

    public void setEntityid(com.developer.java.entity.Entity entityid) {
        this.entityid = entityid;
    }

    public Grup getGrupId() {
        return grupId;
    }

    public void setGrupId(Grup grupId) {
        this.grupId = grupId;
    }

    @XmlTransient
    public List<CaixaMovement> getCaixaMovementList() {
        return caixaMovementList;
    }

    public void setCaixaMovementList(List<CaixaMovement> caixaMovementList) {
        this.caixaMovementList = caixaMovementList;
    }

    @XmlTransient
    public List<Produttype> getProduttypeList() {
        return produttypeList;
    }

    public void setProduttypeList(List<Produttype> produttypeList) {
        this.produttypeList = produttypeList;
    }

    @XmlTransient
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @XmlTransient
    public List<Fatura> getFaturaList() {
        return faturaList;
    }

    public void setFaturaList(List<Fatura> faturaList) {
        this.faturaList = faturaList;
    }

    @XmlTransient
    public List<Movement> getMovementList() {
        return movementList;
    }

    public void setMovementList(List<Movement> movementList) {
        this.movementList = movementList;
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
        if (!(object instanceof Autentication)) {
            return false;
        }
        Autentication other = (Autentication) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return entityid.toString();
    }

    @XmlTransient
    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
    
}
