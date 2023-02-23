/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.controller;

import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Preco;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.developer.java.entity.Product;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class PrecoJpaController implements Serializable {

    public PrecoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Preco preco) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Product productId = preco.getProductId();
            if (productId != null) {
                productId = em.getReference(productId.getClass(), productId.getId());
                preco.setProductId(productId);
            }
            em.persist(preco);
            if (productId != null) {
                productId.getPrecoList().add(preco);
                productId = em.merge(productId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Preco preco) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Preco persistentPreco = em.find(Preco.class, preco.getId());
            Product productIdOld = persistentPreco.getProductId();
            Product productIdNew = preco.getProductId();
            if (productIdNew != null) {
                productIdNew = em.getReference(productIdNew.getClass(), productIdNew.getId());
                preco.setProductId(productIdNew);
            }
            preco = em.merge(preco);
            if (productIdOld != null && !productIdOld.equals(productIdNew)) {
                productIdOld.getPrecoList().remove(preco);
                productIdOld = em.merge(productIdOld);
            }
            if (productIdNew != null && !productIdNew.equals(productIdOld)) {
                productIdNew.getPrecoList().add(preco);
                productIdNew = em.merge(productIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = preco.getId();
                if (findPreco(id) == null) {
                    throw new NonexistentEntityException("The preco with id " + id + " no longer exists.");
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
            Preco preco;
            try {
                preco = em.getReference(Preco.class, id);
                preco.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The preco with id " + id + " no longer exists.", enfe);
            }
            Product productId = preco.getProductId();
            if (productId != null) {
                productId.getPrecoList().remove(preco);
                productId = em.merge(productId);
            }
            em.remove(preco);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Preco> findPrecoEntities() {
        return findPrecoEntities(true, -1, -1);
    }

    public List<Preco> findPrecoEntities(int maxResults, int firstResult) {
        return findPrecoEntities(false, maxResults, firstResult);
    }

    private List<Preco> findPrecoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Preco.class));
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

    public Preco findPreco(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Preco.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrecoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Preco> rt = cq.from(Preco.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
