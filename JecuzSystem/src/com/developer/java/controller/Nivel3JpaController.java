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
import com.developer.java.entity.Nivel2;
import com.developer.java.entity.Nivel3;
import com.developer.java.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class Nivel3JpaController implements Serializable {

    public Nivel3JpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nivel3 nivel3) {
        if (nivel3.getProductList() == null) {
            nivel3.setProductList(new ArrayList<Product>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nivel2 nivel2Id = nivel3.getNivel2Id();
            if (nivel2Id != null) {
                nivel2Id = em.getReference(nivel2Id.getClass(), nivel2Id.getId());
                nivel3.setNivel2Id(nivel2Id);
            }
            List<Product> attachedProductList = new ArrayList<Product>();
            for (Product productListProductToAttach : nivel3.getProductList()) {
                productListProductToAttach = em.getReference(productListProductToAttach.getClass(), productListProductToAttach.getId());
                attachedProductList.add(productListProductToAttach);
            }
            nivel3.setProductList(attachedProductList);
            em.persist(nivel3);
            if (nivel2Id != null) {
                nivel2Id.getNivel3List().add(nivel3);
                nivel2Id = em.merge(nivel2Id);
            }
            for (Product productListProduct : nivel3.getProductList()) {
                Nivel3 oldNivel3IdOfProductListProduct = productListProduct.getNivel3Id();
                productListProduct.setNivel3Id(nivel3);
                productListProduct = em.merge(productListProduct);
                if (oldNivel3IdOfProductListProduct != null) {
                    oldNivel3IdOfProductListProduct.getProductList().remove(productListProduct);
                    oldNivel3IdOfProductListProduct = em.merge(oldNivel3IdOfProductListProduct);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nivel3 nivel3) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nivel3 persistentNivel3 = em.find(Nivel3.class, nivel3.getId());
            Nivel2 nivel2IdOld = persistentNivel3.getNivel2Id();
            Nivel2 nivel2IdNew = nivel3.getNivel2Id();
            List<Product> productListOld = persistentNivel3.getProductList();
            List<Product> productListNew = nivel3.getProductList();
            if (nivel2IdNew != null) {
                nivel2IdNew = em.getReference(nivel2IdNew.getClass(), nivel2IdNew.getId());
                nivel3.setNivel2Id(nivel2IdNew);
            }
            List<Product> attachedProductListNew = new ArrayList<Product>();
            for (Product productListNewProductToAttach : productListNew) {
                productListNewProductToAttach = em.getReference(productListNewProductToAttach.getClass(), productListNewProductToAttach.getId());
                attachedProductListNew.add(productListNewProductToAttach);
            }
            productListNew = attachedProductListNew;
            nivel3.setProductList(productListNew);
            nivel3 = em.merge(nivel3);
            if (nivel2IdOld != null && !nivel2IdOld.equals(nivel2IdNew)) {
                nivel2IdOld.getNivel3List().remove(nivel3);
                nivel2IdOld = em.merge(nivel2IdOld);
            }
            if (nivel2IdNew != null && !nivel2IdNew.equals(nivel2IdOld)) {
                nivel2IdNew.getNivel3List().add(nivel3);
                nivel2IdNew = em.merge(nivel2IdNew);
            }
            for (Product productListOldProduct : productListOld) {
                if (!productListNew.contains(productListOldProduct)) {
                    productListOldProduct.setNivel3Id(null);
                    productListOldProduct = em.merge(productListOldProduct);
                }
            }
            for (Product productListNewProduct : productListNew) {
                if (!productListOld.contains(productListNewProduct)) {
                    Nivel3 oldNivel3IdOfProductListNewProduct = productListNewProduct.getNivel3Id();
                    productListNewProduct.setNivel3Id(nivel3);
                    productListNewProduct = em.merge(productListNewProduct);
                    if (oldNivel3IdOfProductListNewProduct != null && !oldNivel3IdOfProductListNewProduct.equals(nivel3)) {
                        oldNivel3IdOfProductListNewProduct.getProductList().remove(productListNewProduct);
                        oldNivel3IdOfProductListNewProduct = em.merge(oldNivel3IdOfProductListNewProduct);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nivel3.getId();
                if (findNivel3(id) == null) {
                    throw new NonexistentEntityException("The nivel3 with id " + id + " no longer exists.");
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
            Nivel3 nivel3;
            try {
                nivel3 = em.getReference(Nivel3.class, id);
                nivel3.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nivel3 with id " + id + " no longer exists.", enfe);
            }
            Nivel2 nivel2Id = nivel3.getNivel2Id();
            if (nivel2Id != null) {
                nivel2Id.getNivel3List().remove(nivel3);
                nivel2Id = em.merge(nivel2Id);
            }
            List<Product> productList = nivel3.getProductList();
            for (Product productListProduct : productList) {
                productListProduct.setNivel3Id(null);
                productListProduct = em.merge(productListProduct);
            }
            em.remove(nivel3);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Nivel3> findNivel3Entities() {
        return findNivel3Entities(true, -1, -1);
    }

    public List<Nivel3> findNivel3Entities(int maxResults, int firstResult) {
        return findNivel3Entities(false, maxResults, firstResult);
    }

    private List<Nivel3> findNivel3Entities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nivel3.class));
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

    public Nivel3 findNivel3(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nivel3.class, id);
        } finally {
            em.close();
        }
    }

    public int getNivel3Count() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nivel3> rt = cq.from(Nivel3.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
