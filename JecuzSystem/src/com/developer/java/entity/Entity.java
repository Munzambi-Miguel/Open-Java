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
@javax.persistence.Entity
@Table(name = "entity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Entity.findAll", query = "SELECT e FROM Entity e")
    , @NamedQuery(name = "Entity.findById", query = "SELECT e FROM Entity e WHERE e.id = :id")
    , @NamedQuery(name = "Entity.findByName", query = "SELECT e FROM Entity e WHERE e.name = :name")
    , @NamedQuery(name = "Entity.findByEntityType", query = "SELECT e FROM Entity e WHERE e.entityType = :entityType")
    , @NamedQuery(name = "Entity.findByDataInsert", query = "SELECT e FROM Entity e WHERE e.dataInsert = :dataInsert")
    , @NamedQuery(name = "Entity.findByDateUpdate", query = "SELECT e FROM Entity e WHERE e.dateUpdate = :dateUpdate")
    , @NamedQuery(name = "Entity.findByNIdetification", query = "SELECT e FROM Entity e WHERE e.nIdetification = :nIdetification")
    , @NamedQuery(name = "Entity.findByState", query = "SELECT e FROM Entity e WHERE e.state = :state")
    , @NamedQuery(name = "Entity.findByGenre", query = "SELECT e FROM Entity e WHERE e.genre = :genre")
    , @NamedQuery(name = "Entity.findByCompra", query = "SELECT e FROM Entity e WHERE e.compra = :compra")
    , @NamedQuery(name = "Entity.findByDividad", query = "SELECT e FROM Entity e WHERE e.dividad = :dividad")})
public class Entity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Name")
    private String name;
    @Basic(optional = false)
    @Column(name = "EntityType")
    private int entityType;
    @Column(name = "DataInsert")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInsert;
    @Column(name = "DateUpdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "n_Idetification")
    private String nIdetification;
    @Lob
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @Column(name = "state")
    private int state;
    @Column(name = "genre")
    private Integer genre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "compra")
    private BigDecimal compra;
    @Column(name = "dividad")
    private BigDecimal dividad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityId")
    private List<Armazem> armazemList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityId")
    private List<Contact> contactList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityId")
    private List<Client> clientList;
    @OneToMany(mappedBy = "entityid")
    private List<Autentication> autenticationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityId")
    private List<Address> addressList;
    @OneToMany(mappedBy = "entityId")
    private List<Photo> photoList;
    @OneToMany(mappedBy = "fornedorId")
    private List<Movement> movementList;
    @JoinColumn(name = "Company_id", referencedColumnName = "id")
    @ManyToOne
    private Company companyid;
    @OneToMany(mappedBy = "ondeTrabalho")
    private List<Entity> entityList;
    @JoinColumn(name = "ondeTrabalho", referencedColumnName = "id")
    @ManyToOne
    private Entity ondeTrabalho;

    public Entity() {
    }

    public Entity(Integer id) {
        this.id = id;
    }

    public Entity(Integer id, String name, int entityType, int state) {
        this.id = id;
        this.name = name;
        this.entityType = entityType;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public Date getDataInsert() {
        return dataInsert;
    }

    public void setDataInsert(Date dataInsert) {
        this.dataInsert = dataInsert;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getNIdetification() {
        return nIdetification;
    }

    public void setNIdetification(String nIdetification) {
        this.nIdetification = nIdetification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Integer getGenre() {
        return genre;
    }

    public void setGenre(Integer genre) {
        this.genre = genre;
    }

    public BigDecimal getCompra() {
        return compra;
    }

    public void setCompra(BigDecimal compra) {
        this.compra = compra;
    }

    public BigDecimal getDividad() {
        return dividad;
    }

    public void setDividad(BigDecimal dividad) {
        this.dividad = dividad;
    }

    @XmlTransient
    public List<Armazem> getArmazemList() {
        return armazemList;
    }

    public void setArmazemList(List<Armazem> armazemList) {
        this.armazemList = armazemList;
    }

    @XmlTransient
    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @XmlTransient
    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    @XmlTransient
    public List<Autentication> getAutenticationList() {
        return autenticationList;
    }

    public void setAutenticationList(List<Autentication> autenticationList) {
        this.autenticationList = autenticationList;
    }

    @XmlTransient
    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    @XmlTransient
    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    @XmlTransient
    public List<Movement> getMovementList() {
        return movementList;
    }

    public void setMovementList(List<Movement> movementList) {
        this.movementList = movementList;
    }

    public Company getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Company companyid) {
        this.companyid = companyid;
    }

    @XmlTransient
    public List<Entity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<Entity> entityList) {
        this.entityList = entityList;
    }

    public Entity getOndeTrabalho() {
        return ondeTrabalho;
    }

    public void setOndeTrabalho(Entity ondeTrabalho) {
        this.ondeTrabalho = ondeTrabalho;
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
        if (!(object instanceof Entity)) {
            return false;
        }
        Entity other = (Entity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
