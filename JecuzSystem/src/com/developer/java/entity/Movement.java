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
@Table(name = "movement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Movement.findAll", query = "SELECT m FROM Movement m")
    , @NamedQuery(name = "Movement.findById", query = "SELECT m FROM Movement m WHERE m.id = :id")
    , @NamedQuery(name = "Movement.findByQuantidade", query = "SELECT m FROM Movement m WHERE m.quantidade = :quantidade")
    , @NamedQuery(name = "Movement.findByQuantAtual", query = "SELECT m FROM Movement m WHERE m.quantAtual = :quantAtual")
    , @NamedQuery(name = "Movement.findByDataFabrica", query = "SELECT m FROM Movement m WHERE m.dataFabrica = :dataFabrica")
    , @NamedQuery(name = "Movement.findByDataCaducidade", query = "SELECT m FROM Movement m WHERE m.dataCaducidade = :dataCaducidade")
    , @NamedQuery(name = "Movement.findBySerialNamber", query = "SELECT m FROM Movement m WHERE m.serialNamber = :serialNamber")
    , @NamedQuery(name = "Movement.findByDebito", query = "SELECT m FROM Movement m WHERE m.debito = :debito")
    , @NamedQuery(name = "Movement.findByCredito", query = "SELECT m FROM Movement m WHERE m.credito = :credito")
    , @NamedQuery(name = "Movement.findByState", query = "SELECT m FROM Movement m WHERE m.state = :state")
    , @NamedQuery(name = "Movement.findByDesconto", query = "SELECT m FROM Movement m WHERE m.desconto = :desconto")
    , @NamedQuery(name = "Movement.findByPercentagemImposto", query = "SELECT m FROM Movement m WHERE m.percentagemImposto = :percentagemImposto")
    , @NamedQuery(name = "Movement.findByMovimentType", query = "SELECT m FROM Movement m WHERE m.movimentType = :movimentType")
    , @NamedQuery(name = "Movement.findByDataInsert", query = "SELECT m FROM Movement m WHERE m.dataInsert = :dataInsert")
    , @NamedQuery(name = "Movement.findByDateUpdate", query = "SELECT m FROM Movement m WHERE m.dateUpdate = :dateUpdate")})
public class Movement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "quantidade")
    private int quantidade;
    @Column(name = "quantAtual")
    private Integer quantAtual;
    @Column(name = "dataFabrica")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFabrica;
    @Column(name = "dataCaducidade")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCaducidade;
    @Column(name = "serial_namber")
    private String serialNamber;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debito")
    private BigDecimal debito;
    @Column(name = "credito")
    private BigDecimal credito;
    @Basic(optional = false)
    @Column(name = "state")
    private int state;
    @Lob
    @Column(name = "informacao")
    private String informacao;
    @Column(name = "desconto")
    private BigDecimal desconto;
    @Column(name = "percentagemImposto")
    private BigDecimal percentagemImposto;
    @Basic(optional = false)
    @Column(name = "moviment_type")
    private int movimentType;
    @Basic(optional = false)
    @Column(name = "DataInsert")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInsert;
    @Column(name = "DateUpdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "fornedor_id", referencedColumnName = "id")
    @ManyToOne
    private com.developer.java.entity.Entity fornedorId;
    @JoinColumn(name = "autentication_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Autentication autenticationId;
    @JoinColumn(name = "deposito_id", referencedColumnName = "id")
    @ManyToOne
    private Deposito depositoId;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;

    public Movement() {
    }

    public Movement(Integer id) {
        this.id = id;
    }

    public Movement(Integer id, int quantidade, int state, int movimentType, Date dataInsert) {
        this.id = id;
        this.quantidade = quantidade;
        this.state = state;
        this.movimentType = movimentType;
        this.dataInsert = dataInsert;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getQuantAtual() {
        return quantAtual;
    }

    public void setQuantAtual(Integer quantAtual) {
        this.quantAtual = quantAtual;
    }

    public Date getDataFabrica() {
        return dataFabrica;
    }

    public void setDataFabrica(Date dataFabrica) {
        this.dataFabrica = dataFabrica;
    }

    public Date getDataCaducidade() {
        return dataCaducidade;
    }

    public void setDataCaducidade(Date dataCaducidade) {
        this.dataCaducidade = dataCaducidade;
    }

    public String getSerialNamber() {
        return serialNamber;
    }

    public void setSerialNamber(String serialNamber) {
        this.serialNamber = serialNamber;
    }

    public BigDecimal getDebito() {
        return debito;
    }

    public void setDebito(BigDecimal debito) {
        this.debito = debito;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getPercentagemImposto() {
        return percentagemImposto;
    }

    public void setPercentagemImposto(BigDecimal percentagemImposto) {
        this.percentagemImposto = percentagemImposto;
    }

    public int getMovimentType() {
        return movimentType;
    }

    public void setMovimentType(int movimentType) {
        this.movimentType = movimentType;
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

    public com.developer.java.entity.Entity getFornedorId() {
        return fornedorId;
    }

    public void setFornedorId(com.developer.java.entity.Entity fornedorId) {
        this.fornedorId = fornedorId;
    }

    public Autentication getAutenticationId() {
        return autenticationId;
    }

    public void setAutenticationId(Autentication autenticationId) {
        this.autenticationId = autenticationId;
    }

    public Deposito getDepositoId() {
        return depositoId;
    }

    public void setDepositoId(Deposito depositoId) {
        this.depositoId = depositoId;
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
        if (!(object instanceof Movement)) {
            return false;
        }
        Movement other = (Movement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return productId.toString();
    }
    
}
