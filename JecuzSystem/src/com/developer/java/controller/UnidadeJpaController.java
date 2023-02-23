/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.controller;

import com.developer.java.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.developer.java.entity.Product;
import com.developer.java.entity.Unidade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class UnidadeJpaController implements Serializable {

    public UnidadeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Unidade unidade) {
        if (unidade.getProductList() == null) {
            unidade.setProductList(new ArrayList<Product>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Product> attachedProductList = new ArrayList<Product>();
            for (Product productListProductToAttach : unidade.getProductList()) {
                productListProductToAttach = em.getReference(productListProductToAttach.getClass(), productListProductToAttach.getId());
                attachedProductList.add(productListProductToAttach);
            }
            unidade.setProductList(attachedProductList);
            em.persist(unidade);
            for (Product productListProduct : unidade.getProductList()) {
                Unidade oldUnidadeIdOfProductListProduct = productListProduct.getUnidadeId();
                productListProduct.setUnidadeId(unidade);
                productListProduct = em.merge(productListProduct);
                if (oldUnidadeIdOfProductListProduct != null) {
                    oldUnidadeIdOfProductListProduct.getProductList().remove(productListProduct);
                    oldUnidadeIdOfProductListProduct = em.merge(oldUnidadeIdOfProductListProduct);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Unidade unidade) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Unidade persistentUnidade = em.find(Unidade.class, unidade.getId());
            List<Product> productListOld = persistentUnidade.getProductList();
            List<Product> productListNew = unidade.getProductList();
            List<Product> attachedProductListNew = new ArrayList<Product>();
            for (Product productListNewProductToAttach : productListNew) {
                productListNewProductToAttach = em.getReference(productListNewProductToAttach.getClass(), productListNewProductToAttach.getId());
                attachedProductListNew.add(productListNewProductToAttach);
            }
            productListNew = attachedProductListNew;
            unidade.setProductList(productListNew);
            unidade = em.merge(unidade);
            for (Product productListOldProduct : productListOld) {
                if (!productListNew.contains(productListOldProduct)) {
                    productListOldProduct.setUnidadeId(null);
                    productListOldProduct = em.merge(productListOldProduct);
                }
            }
            for (Product productListNewProduct : productListNew) {
                if (!productListOld.contains(productListNewProduct)) {
                    Unidade oldUnidadeIdOfProductListNewProduct = productListNewProduct.getUnidadeId();
                    productListNewProduct.setUnidadeId(unidade);
                    productListNewProduct = em.merge(productListNewProduct);
                    if (oldUnidadeIdOfProductListNewProduct != null && !oldUnidadeIdOfProductListNewProduct.equals(unidade)) {
                        oldUnidadeIdOfProductListNewProduct.getProductList().remove(productListNewProduct);
                        oldUnidadeIdOfProductListNewProduct = em.merge(oldUnidadeIdOfProductListNewProduct);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = unidade.getId();
                if (findUnidade(id) == null) {
                    throw new NonexistentEntityException("The unidade with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Unidade unidade;
            try {
                unidade = em.getReference(Unidade.class, id);
                unidade.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The unidade with id " + id + " no longer exists.", enfe);
            }
            List<Product> productList = unidade.getProductList();
            for (Product productListProduct : productList) {
                productListProduct.setUnidadeId(null);
                productListProduct = em.merge(productListProduct);
            }
            em.remove(unidade);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Unidade> findUnidadeEntities() {
        return findUnidadeEntities(true, -1, -1);
    }

    public List<Unidade> findUnidadeEntities(int maxResults, int firstResult) {
        return findUnidadeEntities(false, maxResults, firstResult);
    }

    private List<Unidade> findUnidadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Unidade.class));
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

    public Unidade findUnidade(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Unidade.class, id);
        } finally {
            em.close();
        }
    }

    public int getUnidadeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Unidade> rt = cq.from(Unidade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
