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
@Table(name = "product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
    , @NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id")
    , @NamedQuery(name = "Product.findByInsertData", query = "SELECT p FROM Product p WHERE p.insertData = :insertData")
    , @NamedQuery(name = "Product.findByUpadateData", query = "SELECT p FROM Product p WHERE p.upadateData = :upadateData")
    , @NamedQuery(name = "Product.findByState", query = "SELECT p FROM Product p WHERE p.state = :state")
    , @NamedQuery(name = "Product.findByValorUnidade", query = "SELECT p FROM Product p WHERE p.valorUnidade = :valorUnidade")
    , @NamedQuery(name = "Product.findByQuantidAtual", query = "SELECT p FROM Product p WHERE p.quantidAtual = :quantidAtual")
    , @NamedQuery(name = "Product.findByEntradada", query = "SELECT p FROM Product p WHERE p.entradada = :entradada")
    , @NamedQuery(name = "Product.findBySaidas", query = "SELECT p FROM Product p WHERE p.saidas = :saidas")
    , @NamedQuery(name = "Product.findByFaturacao", query = "SELECT p FROM Product p WHERE p.faturacao = :faturacao")
    , @NamedQuery(name = "Product.findByPrecounitario", query = "SELECT p FROM Product p WHERE p.precounitario = :precounitario")})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Column(name = "part_namber")
    private String partNamber;
    @Lob
    @Column(name = "description")
    private String description;
    @Column(name = "insert_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertData;
    @Column(name = "upadate_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date upadateData;
    @Basic(optional = false)
    @Column(name = "state")
    private int state;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valorUnidade")
    private BigDecimal valorUnidade;
    @Column(name = "quantidAtual")
    private Integer quantidAtual;
    @Column(name = "entradada")
    private BigDecimal entradada;
    @Column(name = "saidas")
    private BigDecimal saidas;
    @Column(name = "faturacao")
    private BigDecimal faturacao;
    @Column(name = "precounitario")
    private BigDecimal precounitario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private List<Preco> precoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private List<Item> itemList;
    @JoinColumn(name = "autentication_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Autentication autenticationId;
    @JoinColumn(name = "nivel_3_id", referencedColumnName = "id")
    @ManyToOne
    private Nivel3 nivel3Id;
    @JoinColumn(name = "pais_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Pais paisId;
    @JoinColumn(name = "produtType_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Produttype produtTypeid;
    @JoinColumn(name = "unidade_id", referencedColumnName = "id")
    @ManyToOne
    private Unidade unidadeId;
    @OneToMany(mappedBy = "productId")
    private List<Photo> photoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private List<Movement> movementList;

    public Product() {
    }

    public Product(Integer id) {
        this.id = id;
    }

    public Product(Integer id, int state) {
        this.id = id;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPartNamber() {
        return partNamber;
    }

    public void setPartNamber(String partNamber) {
        this.partNamber = partNamber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getInsertData() {
        return insertData;
    }

    public void setInsertData(Date insertData) {
        this.insertData = insertData;
    }

    public Date getUpadateData() {
        return upadateData;
    }

    public void setUpadateData(Date upadateData) {
        this.upadateData = upadateData;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public BigDecimal getValorUnidade() {
        return valorUnidade;
    }

    public void setValorUnidade(BigDecimal valorUnidade) {
        this.valorUnidade = valorUnidade;
    }

    public Integer getQuantidAtual() {
        return quantidAtual;
    }

    public void setQuantidAtual(Integer quantidAtual) {
        this.quantidAtual = quantidAtual;
    }

    public BigDecimal getEntradada() {
        return entradada;
    }

    public void setEntradada(BigDecimal entradada) {
        this.entradada = entradada;
    }

    public BigDecimal getSaidas() {
        return saidas;
    }

    public void setSaidas(BigDecimal saidas) {
        this.saidas = saidas;
    }

    public BigDecimal getFaturacao() {
        return faturacao;
    }

    public void setFaturacao(BigDecimal faturacao) {
        this.faturacao = faturacao;
    }

    public BigDecimal getPrecounitario() {
        return precounitario;
    }

    public void setPrecounitario(BigDecimal precounitario) {
        this.precounitario = precounitario;
    }

    @XmlTransient
    public List<Preco> getPrecoList() {
        return precoList;
    }

    public void setPrecoList(List<Preco> precoList) {
        this.precoList = precoList;
    }

    @XmlTransient
    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public Autentication getAutenticationId() {
        return autenticationId;
    }

    public void setAutenticationId(Autentication autenticationId) {
        this.autenticationId = autenticationId;
    }

    public Nivel3 getNivel3Id() {
        return nivel3Id;
    }

    public void setNivel3Id(Nivel3 nivel3Id) {
        this.nivel3Id = nivel3Id;
    }

    public Pais getPaisId() {
        return paisId;
    }

    public void setPaisId(Pais paisId) {
        this.paisId = paisId;
    }

    public Produttype getProdutTypeid() {
        return produtTypeid;
    }

    public void setProdutTypeid(Produttype produtTypeid) {
        this.produtTypeid = produtTypeid;
    }

    public Unidade getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(Unidade unidadeId) {
        this.unidadeId = unidadeId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getProdutTypeid().toString() + ""
                + (getNivel3Id() != null
                        ? ", "
                        + getNivel3Id().getNivel2Id().toString() + " "
                        + getNivel3Id().toString() + " "
                        : "");
    }

}
