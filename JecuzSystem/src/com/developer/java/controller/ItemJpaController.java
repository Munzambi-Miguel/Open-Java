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
import com.developer.java.entity.Fatura;
import com.developer.java.entity.Item;
import com.developer.java.entity.Product;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class ItemJpaController implements Serializable {

    public ItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Item item) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fatura faturaId = item.getFaturaId();
            if (faturaId != null) {
                faturaId = em.getReference(faturaId.getClass(), faturaId.getId());
                item.setFaturaId(faturaId);
            }
            Product productId = item.getProductId();
            if (productId != null) {
                productId = em.getReference(productId.getClass(), productId.getId());
                item.setProductId(productId);
            }
            em.persist(item);
            if (faturaId != null) {
                faturaId.getItemList().add(item);
                faturaId = em.merge(faturaId);
            }
            if (productId != null) {
                productId.getItemList().add(item);
                productId = em.merge(productId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Item item) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item persistentItem = em.find(Item.class, item.getId());
            Fatura faturaIdOld = persistentItem.getFaturaId();
            Fatura faturaIdNew = item.getFaturaId();
            Product productIdOld = persistentItem.getProductId();
            Product productIdNew = item.getProductId();
            if (faturaIdNew != null) {
                faturaIdNew = em.getReference(faturaIdNew.getClass(), faturaIdNew.getId());
                item.setFaturaId(faturaIdNew);
            }
            if (productIdNew != null) {
                productIdNew = em.getReference(productIdNew.getClass(), productIdNew.getId());
                item.setProductId(productIdNew);
            }
            item = em.merge(item);
            if (faturaIdOld != null && !faturaIdOld.equals(faturaIdNew)) {
                faturaIdOld.getItemList().remove(item);
                faturaIdOld = em.merge(faturaIdOld);
            }
            if (faturaIdNew != null && !faturaIdNew.equals(faturaIdOld)) {
                faturaIdNew.getItemList().add(item);
                faturaIdNew = em.merge(faturaIdNew);
            }
            if (productIdOld != null && !productIdOld.equals(productIdNew)) {
                productIdOld.getItemList().remove(item);
                productIdOld = em.merge(productIdOld);
            }
            if (productIdNew != null && !productIdNew.equals(productIdOld)) {
                productIdNew.getItemList().add(item);
                productIdNew = em.merge(productIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = item.getId();
                if (findItem(id) == null) {
                    throw new NonexistentEntityException("The item with id " + id + " no longer exists.");
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
            Item item;
            try {
                item = em.getReference(Item.class, id);
                item.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The item with id " + id + " no longer exists.", enfe);
            }
            Fatura faturaId = item.getFaturaId();
            if (faturaId != null) {
                faturaId.getItemList().remove(item);
                faturaId = em.merge(faturaId);
            }
            Product productId = item.getProductId();
            if (productId != null) {
                productId.getItemList().remove(item);
                productId = em.merge(productId);
            }
            em.remove(item);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Item> findItemEntities() {
        return findItemEntities(true, -1, -1);
    }

    public List<Item> findItemEntities(int maxResults, int firstResult) {
        return findItemEntities(false, maxResults, firstResult);
    }

    private List<Item> findItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Item.class));
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

    public Item findItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Item.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Item> rt = cq.from(Item.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
