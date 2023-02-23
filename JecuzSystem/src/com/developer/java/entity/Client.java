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
@Table(name = "client")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c")
    , @NamedQuery(name = "Client.findById", query = "SELECT c FROM Client c WHERE c.id = :id")
    , @NamedQuery(name = "Client.findByBarCode", query = "SELECT c FROM Client c WHERE c.barCode = :barCode")
    , @NamedQuery(name = "Client.findByDefaultDescription", query = "SELECT c FROM Client c WHERE c.defaultDescription = :defaultDescription")})
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "bar_code")
    private String barCode;
    @Basic(optional = false)
    @Column(name = "default_description")
    private String defaultDescription;
    @JoinColumn(name = "tipoCliente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tipocliente tipoClienteid;
    @JoinColumn(name = "entity_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private com.developer.java.entity.Entity entityId;
    @OneToMany(mappedBy = "clientId")
    private List<Fatura> faturaList;

    public Client() {
    }

    public Client(Integer id) {
        this.id = id;
    }

    public Client(Integer id, String defaultDescription) {
        this.id = id;
        this.defaultDescription = defaultDescription;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getDefaultDescription() {
        return defaultDescription;
    }

    public void setDefaultDescription(String defaultDescription) {
        this.defaultDescription = defaultDescription;
    }

    public Tipocliente getTipoClienteid() {
        return tipoClienteid;
    }

    public void setTipoClienteid(Tipocliente tipoClienteid) {
        this.tipoClienteid = tipoClienteid;
    }

    public com.developer.java.entity.Entity getEntityId() {
        return entityId;
    }

    public void setEntityId(com.developer.java.entity.Entity entityId) {
        this.entityId = entityId;
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
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.developer.java.entity.Client[ id=" + id + " ]";
    }
    
}
