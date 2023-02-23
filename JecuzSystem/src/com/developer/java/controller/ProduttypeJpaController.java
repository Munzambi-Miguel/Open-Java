/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.controller;

import com.developer.java.controller.exceptions.IllegalOrphanException;
import com.developer.java.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.developer.java.entity.Autentication;
import com.developer.java.entity.Product;
import com.developer.java.entity.Produttype;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class ProduttypeJpaController implements Serializable {

    public ProduttypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produttype produttype) {
        if (produttype.getProductList() == null) {
            produttype.setProductList(new ArrayList<Product>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autentication funcionario = produttype.getFuncionario();
            if (funcionario != null) {
                funcionario = em.getReference(funcionario.getClass(), funcionario.getId());
                produttype.setFuncionario(funcionario);
            }
            List<Product> attachedProductList = new ArrayList<Product>();
            for (Product productListProductToAttach : produttype.getProductList()) {
                productListProductToAttach = em.getReference(productListProductToAttach.getClass(), productListProductToAttach.getId());
                attachedProductList.add(productListProductToAttach);
            }
            produttype.setProductList(attachedProductList);
            em.persist(produttype);
            if (funcionario != null) {
                funcionario.getProduttypeList().add(produttype);
                funcionario = em.merge(funcionario);
            }
            for (Product productListProduct : produttype.getProductList()) {
                Produttype oldProdutTypeidOfProductListProduct = productListProduct.getProdutTypeid();
                productListProduct.setProdutTypeid(produttype);
                productListProduct = em.merge(productListProduct);
                if (oldProdutTypeidOfProductListProduct != null) {
                    oldProdutTypeidOfProductListProduct.getProductList().remove(productListProduct);
                    oldProdutTypeidOfProductListProduct = em.merge(oldProdutTypeidOfProductListProduct);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produttype produttype) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produttype persistentProduttype = em.find(Produttype.class, produttype.getId());
            Autentication funcionarioOld = persistentProduttype.getFuncionario();
            Autentication funcionarioNew = produttype.getFuncionario();
            List<Product> productListOld = persistentProduttype.getProductList();
            List<Product> productListNew = produttype.getProductList();
            List<String> illegalOrphanMessages = null;
            for (Product productListOldProduct : productListOld) {
                if (!productListNew.contains(productListOldProduct)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Product " + productListOldProduct + " since its produtTypeid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (funcionarioNew != null) {
                funcionarioNew = em.getReference(funcionarioNew.getClass(), funcionarioNew.getId());
                produttype.setFuncionario(funcionarioNew);
            }
            List<Product> attachedProductListNew = new ArrayList<Product>();
            for (Product productListNewProductToAttach : productListNew) {
                productListNewProductToAttach = em.getReference(productListNewProductToAttach.getClass(), productListNewProductToAttach.getId());
                attachedProductListNew.add(productListNewProductToAttach);
            }
            productListNew = attachedProductListNew;
            produttype.setProductList(productListNew);
            produttype = em.merge(produttype);
            if (funcionarioOld != null && !funcionarioOld.equals(funcionarioNew)) {
                funcionarioOld.getProduttypeList().remove(produttype);
                funcionarioOld = em.merge(funcionarioOld);
            }
            if (funcionarioNew != null && !funcionarioNew.equals(funcionarioOld)) {
                funcionarioNew.getProduttypeList().add(produttype);
                funcionarioNew = em.merge(funcionarioNew);
            }
            for (Product productListNewProduct : productListNew) {
                if (!productListOld.contains(productListNewProduct)) {
                    Produttype oldProdutTypeidOfProductListNewProduct = productListNewProduct.getProdutTypeid();
                    productListNewProduct.setProdutTypeid(produttype);
                    productListNewProduct = em.merge(productListNewProduct);
                    if (oldProdutTypeidOfProductListNewProduct != null && !oldProdutTypeidOfProductListNewProduct.equals(produttype)) {
                        oldProdutTypeidOfProductListNewProduct.getProductList().remove(productListNewProduct);
                        oldProdutTypeidOfProductListNewProduct = em.merge(oldProdutTypeidOfProductListNewProduct);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produttype.getId();
                if (findProduttype(id) == null) {
                    throw new NonexistentEntityException("The produttype with id " + id + " no longer exists.");
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
            Produttype produttype;
            try {
                produttype = em.getReference(Produttype.class, id);
                produttype.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produttype with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Product> productListOrphanCheck = produttype.getProductList();
            for (Product productListOrphanCheckProduct : productListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Produttype (" + produttype + ") cannot be destroyed since the Product " + productListOrphanCheckProduct + " in its productList field has a non-nullable produtTypeid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Autentication funcionario = produttype.getFuncionario();
            if (funcionario != null) {
                funcionario.getProduttypeList().remove(produttype);
                funcionario = em.merge(funcionario);
            }
            em.remove(produttype);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Produttype> findProduttypeEntities() {
        return findProduttypeEntities(true, -1, -1);
    }

    public List<Produttype> findProduttypeEntities(int maxResults, int firstResult) {
        return findProduttypeEntities(false, maxResults, firstResult);
    }

    private List<Produttype> findProduttypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produttype.class));
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

    public Produttype findProduttype(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produttype.class, id);
        } finally {
            em.close();
        }
    }

    public int getProduttypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produttype> rt = cq.from(Produttype.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
