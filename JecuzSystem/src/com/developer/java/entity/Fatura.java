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
@Table(name = "fatura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fatura.findAll", query = "SELECT f FROM Fatura f")
    , @NamedQuery(name = "Fatura.findById", query = "SELECT f FROM Fatura f WHERE f.id = :id")
    , @NamedQuery(name = "Fatura.findByDataInsert", query = "SELECT f FROM Fatura f WHERE f.dataInsert = :dataInsert")
    , @NamedQuery(name = "Fatura.findByDataUpdate", query = "SELECT f FROM Fatura f WHERE f.dataUpdate = :dataUpdate")
    , @NamedQuery(name = "Fatura.findByTotal", query = "SELECT f FROM Fatura f WHERE f.total = :total")
    , @NamedQuery(name = "Fatura.findByTroco", query = "SELECT f FROM Fatura f WHERE f.troco = :troco")
    , @NamedQuery(name = "Fatura.findByValorPago", query = "SELECT f FROM Fatura f WHERE f.valorPago = :valorPago")
    , @NamedQuery(name = "Fatura.findByState", query = "SELECT f FROM Fatura f WHERE f.state = :state")
    , @NamedQuery(name = "Fatura.findByTipoPagamento", query = "SELECT f FROM Fatura f WHERE f.tipoPagamento = :tipoPagamento")})
public class Fatura implements Serializable {

    @Lob
    @Column(name = "informacao")
    private String informacao;
    @JoinColumn(name = "admin", referencedColumnName = "id")
    @ManyToOne
    private Autentication admin;
    @JoinColumn(name = "tipo_devolucao_id", referencedColumnName = "id")
    @ManyToOne
    private TipoDevolucao tipoDevolucaoId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "Fatura")
    private String fatura;
    @Basic(optional = false)
    @Column(name = "data_insert")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInsert;
    @Column(name = "data_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUpdate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "total")
    private BigDecimal total;
    @Basic(optional = false)
    @Column(name = "troco")
    private BigDecimal troco;
    @Basic(optional = false)
    @Column(name = "valor_pago")
    private BigDecimal valorPago;
    @Basic(optional = false)
    @Column(name = "state")
    private int state;
    @Lob
    @Column(name = "cliente")
    private String cliente;
    @Basic(optional = false)
    @Column(name = "tipoPagamento")
    private boolean tipoPagamento;
    @Lob
    @Column(name = "num_etiqueta")
    private String numEtiqueta;
    @OneToMany(mappedBy = "faturaId")
    private List<CaixaMovement> caixaMovementList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faturaId")
    private List<Item> itemList;
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Autentication funcionarioId;
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne
    private Client clientId;

    public Fatura() {
    }

    public Fatura(Integer id) {
        this.id = id;
    }

    public Fatura(Integer id, String fatura, Date dataInsert, BigDecimal total, BigDecimal troco, BigDecimal valorPago, int state, boolean tipoPagamento) {
        this.id = id;
        this.fatura = fatura;
        this.dataInsert = dataInsert;
        this.total = total;
        this.troco = troco;
        this.valorPago = valorPago;
        this.state = state;
        this.tipoPagamento = tipoPagamento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFatura() {
        return fatura;
    }

    public void setFatura(String fatura) {
        this.fatura = fatura;
    }

    public Date getDataInsert() {
        return dataInsert;
    }

    public void setDataInsert(Date dataInsert) {
        this.dataInsert = dataInsert;
    }

    public Date getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(Date dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public void setTroco(BigDecimal troco) {
        this.troco = troco;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public boolean getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(boolean tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public String getNumEtiqueta() {
        return numEtiqueta;
    }

    public void setNumEtiqueta(String numEtiqueta) {
        this.numEtiqueta = numEtiqueta;
    }

    @XmlTransient
    public List<CaixaMovement> getCaixaMovementList() {
        return caixaMovementList;
    }

    public void setCaixaMovementList(List<CaixaMovement> caixaMovementList) {
        this.caixaMovementList = caixaMovementList;
    }

    @XmlTransient
    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public Autentication getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Autentication funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public Client getClientId() {
        return clientId;
    }

    public void setClientId(Client clientId) {
        this.clientId = clientId;
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
        if (!(object instanceof Fatura)) {
            return false;
        }
        Fatura other = (Fatura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.developer.java.entity.Fatura[ id=" + id + " ]";
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public Autentication getAdmin() {
        return admin;
    }

    public void setAdmin(Autentication admin) {
        this.admin = admin;
    }

    public TipoDevolucao getTipoDevolucaoId() {
        return tipoDevolucaoId;
    }

    public void setTipoDevolucaoId(TipoDevolucao tipoDevolucaoId) {
        this.tipoDevolucaoId = tipoDevolucaoId;
    }
    
}
