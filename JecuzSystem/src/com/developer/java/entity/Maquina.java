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
@Table(name = "maquina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Maquina.findAll", query = "SELECT m FROM Maquina m")
    , @NamedQuery(name = "Maquina.findById", query = "SELECT m FROM Maquina m WHERE m.id = :id")
    , @NamedQuery(name = "Maquina.findByIp", query = "SELECT m FROM Maquina m WHERE m.ip = :ip")
    , @NamedQuery(name = "Maquina.findByMaquiAddress", query = "SELECT m FROM Maquina m WHERE m.maquiAddress = :maquiAddress")
    , @NamedQuery(name = "Maquina.findByDataInsert", query = "SELECT m FROM Maquina m WHERE m.dataInsert = :dataInsert")
    , @NamedQuery(name = "Maquina.findByNumero", query = "SELECT m FROM Maquina m WHERE m.numero = :numero")
    , @NamedQuery(name = "Maquina.findByState", query = "SELECT m FROM Maquina m WHERE m.state = :state")})
public class Maquina implements Serializable {

    @Basic(optional = false)
    @Column(name = "log_state")
    private int logState;

    @Basic(optional = false)
    @Column(name = "state")
    private int state;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "ip")
    private String ip;
    @Column(name = "maqui_address")
    private String maquiAddress;
    @Basic(optional = false)
    @Column(name = "data_insert")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInsert;
    @Basic(optional = false)
    @Column(name = "numero")
    private int numero;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "maquinaId")
    private List<Caixa> caixaList;

    public Maquina() {
    }

    public Maquina(Integer id) {
        this.id = id;
    }

    public Maquina(Integer id, String ip, Date dataInsert, int numero) {
        this.id = id;
        this.ip = ip;
        this.dataInsert = dataInsert;
        this.numero = numero;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMaquiAddress() {
        return maquiAddress;
    }

    public void setMaquiAddress(String maquiAddress) {
        this.maquiAddress = maquiAddress;
    }

    public Date getDataInsert() {
        return dataInsert;
    }

    public void setDataInsert(Date dataInsert) {
        this.dataInsert = dataInsert;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }


    @XmlTransient
    public List<Caixa> getCaixaList() {
        return caixaList;
    }

    public void setCaixaList(List<Caixa> caixaList) {
        this.caixaList = caixaList;
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
        if (!(object instanceof Maquina)) {
            return false;
        }
        Maquina other = (Maquina) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Maquina: "+numero;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getLogState() {
        return logState;
    }

    public void setLogState(int logState) {
        this.logState = logState;
    }
    
}
