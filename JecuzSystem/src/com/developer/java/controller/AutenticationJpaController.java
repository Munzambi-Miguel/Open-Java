/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.controller;

import com.developer.java.controller.exceptions.IllegalOrphanException;
import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Autentication;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.developer.java.entity.Entity;
import com.developer.java.entity.Grup;
import com.developer.java.entity.Caixa;
import java.util.ArrayList;
import java.util.List;
import com.developer.java.entity.CaixaMovement;
import com.developer.java.entity.Produttype;
import com.developer.java.entity.Product;
import com.developer.java.entity.Fatura;
import com.developer.java.entity.Movement;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class AutenticationJpaController implements Serializable {

    public AutenticationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Autentication autentication) {
        if (autentication.getCaixaList() == null) {
            autentication.setCaixaList(new ArrayList<Caixa>());
        }
        if (autentication.getCaixaMovementList() == null) {
            autentication.setCaixaMovementList(new ArrayList<CaixaMovement>());
        }
        if (autentication.getProduttypeList() == null) {
            autentication.setProduttypeList(new ArrayList<Produttype>());
        }
        if (autentication.getProductList() == null) {
            autentication.setProductList(new ArrayList<Product>());
        }
        if (autentication.getFaturaList() == null) {
            autentication.setFaturaList(new ArrayList<Fatura>());
        }
        if (autentication.getMovementList() == null) {
            autentication.setMovementList(new ArrayList<Movement>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entity entityid = autentication.getEntityid();
            if (entityid != null) {
                entityid = em.getReference(entityid.getClass(), entityid.getId());
                autentication.setEntityid(entityid);
            }
            Grup grupId = autentication.getGrupId();
            if (grupId != null) {
                grupId = em.getReference(grupId.getClass(), grupId.getId());
                autentication.setGrupId(grupId);
            }
            List<Caixa> attachedCaixaList = new ArrayList<Caixa>();
            for (Caixa caixaListCaixaToAttach : autentication.getCaixaList()) {
                caixaListCaixaToAttach = em.getReference(caixaListCaixaToAttach.getClass(), caixaListCaixaToAttach.getId());
                attachedCaixaList.add(caixaListCaixaToAttach);
            }
            autentication.setCaixaList(attachedCaixaList);
            List<CaixaMovement> attachedCaixaMovementList = new ArrayList<CaixaMovement>();
            for (CaixaMovement caixaMovementListCaixaMovementToAttach : autentication.getCaixaMovementList()) {
                caixaMovementListCaixaMovementToAttach = em.getReference(caixaMovementListCaixaMovementToAttach.getClass(), caixaMovementListCaixaMovementToAttach.getId());
                attachedCaixaMovementList.add(caixaMovementListCaixaMovementToAttach);
            }
            autentication.setCaixaMovementList(attachedCaixaMovementList);
            List<Produttype> attachedProduttypeList = new ArrayList<Produttype>();
            for (Produttype produttypeListProduttypeToAttach : autentication.getProduttypeList()) {
                produttypeListProduttypeToAttach = em.getReference(produttypeListProduttypeToAttach.getClass(), produttypeListProduttypeToAttach.getId());
                attachedProduttypeList.add(produttypeListProduttypeToAttach);
            }
            autentication.setProduttypeList(attachedProduttypeList);
            List<Product> attachedProductList = new ArrayList<Product>();
            for (Product productListProductToAttach : autentication.getProductList()) {
                productListProductToAttach = em.getReference(productListProductToAttach.getClass(), productListProductToAttach.getId());
                attachedProductList.add(productListProductToAttach);
            }
            autentication.setProductList(attachedProductList);
            List<Fatura> attachedFaturaList = new ArrayList<Fatura>();
            for (Fatura faturaListFaturaToAttach : autentication.getFaturaList()) {
                faturaListFaturaToAttach = em.getReference(faturaListFaturaToAttach.getClass(), faturaListFaturaToAttach.getId());
                attachedFaturaList.add(faturaListFaturaToAttach);
            }
            autentication.setFaturaList(attachedFaturaList);
            List<Movement> attachedMovementList = new ArrayList<Movement>();
            for (Movement movementListMovementToAttach : autentication.getMovementList()) {
                movementListMovementToAttach = em.getReference(movementListMovementToAttach.getClass(), movementListMovementToAttach.getId());
                attachedMovementList.add(movementListMovementToAttach);
            }
            autentication.setMovementList(attachedMovementList);
            em.persist(autentication);
            if (entityid != null) {
                entityid.getAutenticationList().add(autentication);
                entityid = em.merge(entityid);
            }
            if (grupId != null) {
                grupId.getAutenticationList().add(autentication);
                grupId = em.merge(grupId);
            }
            for (Caixa caixaListCaixa : autentication.getCaixaList()) {
                Autentication oldAutenticationIdOfCaixaListCaixa = caixaListCaixa.getAutenticationId();
                caixaListCaixa.setAutenticationId(autentication);
                caixaListCaixa = em.merge(caixaListCaixa);
                if (oldAutenticationIdOfCaixaListCaixa != null) {
                    oldAutenticationIdOfCaixaListCaixa.getCaixaList().remove(caixaListCaixa);
                    oldAutenticationIdOfCaixaListCaixa = em.merge(oldAutenticationIdOfCaixaListCaixa);
                }
            }
            for (CaixaMovement caixaMovementListCaixaMovement : autentication.getCaixaMovementList()) {
                Autentication oldAutenticationIdOfCaixaMovementListCaixaMovement = caixaMovementListCaixaMovement.getAutenticationId();
                caixaMovementListCaixaMovement.setAutenticationId(autentication);
                caixaMovementListCaixaMovement = em.merge(caixaMovementListCaixaMovement);
                if (oldAutenticationIdOfCaixaMovementListCaixaMovement != null) {
                    oldAutenticationIdOfCaixaMovementListCaixaMovement.getCaixaMovementList().remove(caixaMovementListCaixaMovement);
                    oldAutenticationIdOfCaixaMovementListCaixaMovement = em.merge(oldAutenticationIdOfCaixaMovementListCaixaMovement);
                }
            }
            for (Produttype produttypeListProduttype : autentication.getProduttypeList()) {
                Autentication oldFuncionarioOfProduttypeListProduttype = produttypeListProduttype.getFuncionario();
                produttypeListProduttype.setFuncionario(autentication);
                produttypeListProduttype = em.merge(produttypeListProduttype);
                if (oldFuncionarioOfProduttypeListProduttype != null) {
                    oldFuncionarioOfProduttypeListProduttype.getProduttypeList().remove(produttypeListProduttype);
                    oldFuncionarioOfProduttypeListProduttype = em.merge(oldFuncionarioOfProduttypeListProduttype);
                }
            }
            for (Product productListProduct : autentication.getProductList()) {
                Autentication oldAutenticationIdOfProductListProduct = productListProduct.getAutenticationId();
                productListProduct.setAutenticationId(autentication);
                productListProduct = em.merge(productListProduct);
                if (oldAutenticationIdOfProductListProduct != null) {
                    oldAutenticationIdOfProductListProduct.getProductList().remove(productListProduct);
                    oldAutenticationIdOfProductListProduct = em.merge(oldAutenticationIdOfProductListProduct);
                }
            }
            for (Fatura faturaListFatura : autentication.getFaturaList()) {
                Autentication oldFuncionarioIdOfFaturaListFatura = faturaListFatura.getFuncionarioId();
                faturaListFatura.setFuncionarioId(autentication);
                faturaListFatura = em.merge(faturaListFatura);
                if (oldFuncionarioIdOfFaturaListFatura != null) {
                    oldFuncionarioIdOfFaturaListFatura.getFaturaList().remove(faturaListFatura);
                    oldFuncionarioIdOfFaturaListFatura = em.merge(oldFuncionarioIdOfFaturaListFatura);
                }
            }
            for (Movement movementListMovement : autentication.getMovementList()) {
                Autentication oldAutenticationIdOfMovementListMovement = movementListMovement.getAutenticationId();
                movementListMovement.setAutenticationId(autentication);
                movementListMovement = em.merge(movementListMovement);
                if (oldAutenticationIdOfMovementListMovement != null) {
                    oldAutenticationIdOfMovementListMovement.getMovementList().remove(movementListMovement);
                    oldAutenticationIdOfMovementListMovement = em.merge(oldAutenticationIdOfMovementListMovement);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Autentication autentication) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autentication persistentAutentication = em.find(Autentication.class, autentication.getId());
            Entity entityidOld = persistentAutentication.getEntityid();
            Entity entityidNew = autentication.getEntityid();
            Grup grupIdOld = persistentAutentication.getGrupId();
            Grup grupIdNew = autentication.getGrupId();
            List<Caixa> caixaListOld = persistentAutentication.getCaixaList();
            List<Caixa> caixaListNew = autentication.getCaixaList();
            List<CaixaMovement> caixaMovementListOld = persistentAutentication.getCaixaMovementList();
            List<CaixaMovement> caixaMovementListNew = autentication.getCaixaMovementList();
            List<Produttype> produttypeListOld = persistentAutentication.getProduttypeList();
            List<Produttype> produttypeListNew = autentication.getProduttypeList();
            List<Product> productListOld = persistentAutentication.getProductList();
            List<Product> productListNew = autentication.getProductList();
            List<Fatura> faturaListOld = persistentAutentication.getFaturaList();
            List<Fatura> faturaListNew = autentication.getFaturaList();
            List<Movement> movementListOld = persistentAutentication.getMovementList();
            List<Movement> movementListNew = autentication.getMovementList();
            List<String> illegalOrphanMessages = null;
            for (Caixa caixaListOldCaixa : caixaListOld) {
                if (!caixaListNew.contains(caixaListOldCaixa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Caixa " + caixaListOldCaixa + " since its autenticationId field is not nullable.");
                }
            }
            for (CaixaMovement caixaMovementListOldCaixaMovement : caixaMovementListOld) {
                if (!caixaMovementListNew.contains(caixaMovementListOldCaixaMovement)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CaixaMovement " + caixaMovementListOldCaixaMovement + " since its autenticationId field is not nullable.");
                }
            }
            for (Produttype produttypeListOldProduttype : produttypeListOld) {
                if (!produttypeListNew.contains(produttypeListOldProduttype)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Produttype " + produttypeListOldProduttype + " since its funcionario field is not nullable.");
                }
            }
            for (Product productListOldProduct : productListOld) {
                if (!productListNew.contains(productListOldProduct)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Product " + productListOldProduct + " since its autenticationId field is not nullable.");
                }
            }
            for (Fatura faturaListOldFatura : faturaListOld) {
                if (!faturaListNew.contains(faturaListOldFatura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Fatura " + faturaListOldFatura + " since its funcionarioId field is not nullable.");
                }
            }
            for (Movement movementListOldMovement : movementListOld) {
                if (!movementListNew.contains(movementListOldMovement)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Movement " + movementListOldMovement + " since its autenticationId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (entityidNew != null) {
                entityidNew = em.getReference(entityidNew.getClass(), entityidNew.getId());
                autentication.setEntityid(entityidNew);
            }
            if (grupIdNew != null) {
                grupIdNew = em.getReference(grupIdNew.getClass(), grupIdNew.getId());
                autentication.setGrupId(grupIdNew);
            }
            List<Caixa> attachedCaixaListNew = new ArrayList<Caixa>();
            for (Caixa caixaListNewCaixaToAttach : caixaListNew) {
                caixaListNewCaixaToAttach = em.getReference(caixaListNewCaixaToAttach.getClass(), caixaListNewCaixaToAttach.getId());
                attachedCaixaListNew.add(caixaListNewCaixaToAttach);
            }
            caixaListNew = attachedCaixaListNew;
            autentication.setCaixaList(caixaListNew);
            List<CaixaMovement> attachedCaixaMovementListNew = new ArrayList<CaixaMovement>();
            for (CaixaMovement caixaMovementListNewCaixaMovementToAttach : caixaMovementListNew) {
                caixaMovementListNewCaixaMovementToAttach = em.getReference(caixaMovementListNewCaixaMovementToAttach.getClass(), caixaMovementListNewCaixaMovementToAttach.getId());
                attachedCaixaMovementListNew.add(caixaMovementListNewCaixaMovementToAttach);
            }
            caixaMovementListNew = attachedCaixaMovementListNew;
            autentication.setCaixaMovementList(caixaMovementListNew);
            List<Produttype> attachedProduttypeListNew = new ArrayList<Produttype>();
            for (Produttype produttypeListNewProduttypeToAttach : produttypeListNew) {
                produttypeListNewProduttypeToAttach = em.getReference(produttypeListNewProduttypeToAttach.getClass(), produttypeListNewProduttypeToAttach.getId());
                attachedProduttypeListNew.add(produttypeListNewProduttypeToAttach);
            }
            produttypeListNew = attachedProduttypeListNew;
            autentication.setProduttypeList(produttypeListNew);
            List<Product> attachedProductListNew = new ArrayList<Product>();
            for (Product productListNewProductToAttach : productListNew) {
                productListNewProductToAttach = em.getReference(productListNewProductToAttach.getClass(), productListNewProductToAttach.getId());
                attachedProductListNew.add(productListNewProductToAttach);
            }
            productListNew = attachedProductListNew;
            autentication.setProductList(productListNew);
            List<Fatura> attachedFaturaListNew = new ArrayList<Fatura>();
            for (Fatura faturaListNewFaturaToAttach : faturaListNew) {
                faturaListNewFaturaToAttach = em.getReference(faturaListNewFaturaToAttach.getClass(), faturaListNewFaturaToAttach.getId());
                attachedFaturaListNew.add(faturaListNewFaturaToAttach);
            }
            faturaListNew = attachedFaturaListNew;
            autentication.setFaturaList(faturaListNew);
            List<Movement> attachedMovementListNew = new ArrayList<Movement>();
            for (Movement movementListNewMovementToAttach : movementListNew) {
                movementListNewMovementToAttach = em.getReference(movementListNewMovementToAttach.getClass(), movementListNewMovementToAttach.getId());
                attachedMovementListNew.add(movementListNewMovementToAttach);
            }
            movementListNew = attachedMovementListNew;
            autentication.setMovementList(movementListNew);
            autentication = em.merge(autentication);
            if (entityidOld != null && !entityidOld.equals(entityidNew)) {
                entityidOld.getAutenticationList().remove(autentication);
                entityidOld = em.merge(entityidOld);
            }
            if (entityidNew != null && !entityidNew.equals(entityidOld)) {
                entityidNew.getAutenticationList().add(autentication);
                entityidNew = em.merge(entityidNew);
            }
            if (grupIdOld != null && !grupIdOld.equals(grupIdNew)) {
                grupIdOld.getAutenticationList().remove(autentication);
                grupIdOld = em.merge(grupIdOld);
            }
            if (grupIdNew != null && !grupIdNew.equals(grupIdOld)) {
                grupIdNew.getAutenticationList().add(autentication);
                grupIdNew = em.merge(grupIdNew);
            }
            for (Caixa caixaListNewCaixa : caixaListNew) {
                if (!caixaListOld.contains(caixaListNewCaixa)) {
                    Autentication oldAutenticationIdOfCaixaListNewCaixa = caixaListNewCaixa.getAutenticationId();
                    caixaListNewCaixa.setAutenticationId(autentication);
                    caixaListNewCaixa = em.merge(caixaListNewCaixa);
                    if (oldAutenticationIdOfCaixaListNewCaixa != null && !oldAutenticationIdOfCaixaListNewCaixa.equals(autentication)) {
                        oldAutenticationIdOfCaixaListNewCaixa.getCaixaList().remove(caixaListNewCaixa);
                        oldAutenticationIdOfCaixaListNewCaixa = em.merge(oldAutenticationIdOfCaixaListNewCaixa);
                    }
                }
            }
            for (CaixaMovement caixaMovementListNewCaixaMovement : caixaMovementListNew) {
                if (!caixaMovementListOld.contains(caixaMovementListNewCaixaMovement)) {
                    Autentication oldAutenticationIdOfCaixaMovementListNewCaixaMovement = caixaMovementListNewCaixaMovement.getAutenticationId();
                    caixaMovementListNewCaixaMovement.setAutenticationId(autentication);
                    caixaMovementListNewCaixaMovement = em.merge(caixaMovementListNewCaixaMovement);
                    if (oldAutenticationIdOfCaixaMovementListNewCaixaMovement != null && !oldAutenticationIdOfCaixaMovementListNewCaixaMovement.equals(autentication)) {
                        oldAutenticationIdOfCaixaMovementListNewCaixaMovement.getCaixaMovementList().remove(caixaMovementListNewCaixaMovement);
                        oldAutenticationIdOfCaixaMovementListNewCaixaMovement = em.merge(oldAutenticationIdOfCaixaMovementListNewCaixaMovement);
                    }
                }
            }
            for (Produttype produttypeListNewProduttype : produttypeListNew) {
                if (!produttypeListOld.contains(produttypeListNewProduttype)) {
                    Autentication oldFuncionarioOfProduttypeListNewProduttype = produttypeListNewProduttype.getFuncionario();
                    produttypeListNewProduttype.setFuncionario(autentication);
                    produttypeListNewProduttype = em.merge(produttypeListNewProduttype);
                    if (oldFuncionarioOfProduttypeListNewProduttype != null && !oldFuncionarioOfProduttypeListNewProduttype.equals(autentication)) {
                        oldFuncionarioOfProduttypeListNewProduttype.getProduttypeList().remove(produttypeListNewProduttype);
                        oldFuncionarioOfProduttypeListNewProduttype = em.merge(oldFuncionarioOfProduttypeListNewProduttype);
                    }
                }
            }
            for (Product productListNewProduct : productListNew) {
                if (!productListOld.contains(productListNewProduct)) {
                    Autentication oldAutenticationIdOfProductListNewProduct = productListNewProduct.getAutenticationId();
                    productListNewProduct.setAutenticationId(autentication);
                    productListNewProduct = em.merge(productListNewProduct);
                    if (oldAutenticationIdOfProductListNewProduct != null && !oldAutenticationIdOfProductListNewProduct.equals(autentication)) {
                        oldAutenticationIdOfProductListNewProduct.getProductList().remove(productListNewProduct);
                        oldAutenticationIdOfProductListNewProduct = em.merge(oldAutenticationIdOfProductListNewProduct);
                    }
                }
            }
            for (Fatura faturaListNewFatura : faturaListNew) {
                if (!faturaListOld.contains(faturaListNewFatura)) {
                    Autentication oldFuncionarioIdOfFaturaListNewFatura = faturaListNewFatura.getFuncionarioId();
                    faturaListNewFatura.setFuncionarioId(autentication);
                    faturaListNewFatura = em.merge(faturaListNewFatura);
                    if (oldFuncionarioIdOfFaturaListNewFatura != null && !oldFuncionarioIdOfFaturaListNewFatura.equals(autentication)) {
                        oldFuncionarioIdOfFaturaListNewFatura.getFaturaList().remove(faturaListNewFatura);
                        oldFuncionarioIdOfFaturaListNewFatura = em.merge(oldFuncionarioIdOfFaturaListNewFatura);
                    }
                }
            }
            for (Movement movementListNewMovement : movementListNew) {
                if (!movementListOld.contains(movementListNewMovement)) {
                    Autentication oldAutenticationIdOfMovementListNewMovement = movementListNewMovement.getAutenticationId();
                    movementListNewMovement.setAutenticationId(autentication);
                    movementListNewMovement = em.merge(movementListNewMovement);
                    if (oldAutenticationIdOfMovementListNewMovement != null && !oldAutenticationIdOfMovementListNewMovement.equals(autentication)) {
                        oldAutenticationIdOfMovementListNewMovement.getMovementList().remove(movementListNewMovement);
                        oldAutenticationIdOfMovementListNewMovement = em.merge(oldAutenticationIdOfMovementListNewMovement);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = autentication.getId();
                if (findAutentication(id) == null) {
                    throw new NonexistentEntityException("The autentication with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autentication autentication;
            try {
                autentication = em.getReference(Autentication.class, id);
                autentication.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The autentication with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Caixa> caixaListOrphanCheck = autentication.getCaixaList();
            for (Caixa caixaListOrphanCheckCaixa : caixaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Autentication (" + autentication + ") cannot be destroyed since the Caixa " + caixaListOrphanCheckCaixa + " in its caixaList field has a non-nullable autenticationId field.");
            }
            List<CaixaMovement> caixaMovementListOrphanCheck = autentication.getCaixaMovementList();
            for (CaixaMovement caixaMovementListOrphanCheckCaixaMovement : caixaMovementListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Autentication (" + autentication + ") cannot be destroyed since the CaixaMovement " + caixaMovementListOrphanCheckCaixaMovement + " in its caixaMovementList field has a non-nullable autenticationId field.");
            }
            List<Produttype> produttypeListOrphanCheck = autentication.getProduttypeList();
            for (Produttype produttypeListOrphanCheckProduttype : produttypeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Autentication (" + autentication + ") cannot be destroyed since the Produttype " + produttypeListOrphanCheckProduttype + " in its produttypeList field has a non-nullable funcionario field.");
            }
            List<Product> productListOrphanCheck = autentication.getProductList();
            for (Product productListOrphanCheckProduct : productListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Autentication (" + autentication + ") cannot be destroyed since the Product " + productListOrphanCheckProduct + " in its productList field has a non-nullable autenticationId field.");
            }
            List<Fatura> faturaListOrphanCheck = autentication.getFaturaList();
            for (Fatura faturaListOrphanCheckFatura : faturaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Autentication (" + autentication + ") cannot be destroyed since the Fatura " + faturaListOrphanCheckFatura + " in its faturaList field has a non-nullable funcionarioId field.");
            }
            List<Movement> movementListOrphanCheck = autentication.getMovementList();
            for (Movement movementListOrphanCheckMovement : movementListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Autentication (" + autentication + ") cannot be destroyed since the Movement " + movementListOrphanCheckMovement + " in its movementList field has a non-nullable autenticationId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Entity entityid = autentication.getEntityid();
            if (entityid != null) {
                entityid.getAutenticationList().remove(autentication);
                entityid = em.merge(entityid);
            }
            Grup grupId = autentication.getGrupId();
            if (grupId != null) {
                grupId.getAutenticationList().remove(autentication);
                grupId = em.merge(grupId);
            }
            em.remove(autentication);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Autentication> findAutenticationEntities() {
        return findAutenticationEntities(true, -1, -1);
    }

    public List<Autentication> findAutenticationEntities(int maxResults, int firstResult) {
        return findAutenticationEntities(false, maxResults, firstResult);
    }

    private List<Autentication> findAutenticationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Autentication.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Autentication findAutentication(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Autentication.class, id);
        } finally {
            em.close();
        }
    }

    public int getAutenticationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Autentication> rt = cq.from(Autentication.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
