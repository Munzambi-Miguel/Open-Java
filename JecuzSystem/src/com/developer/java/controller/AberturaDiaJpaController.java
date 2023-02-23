/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.controller;

import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.controller.exceptions.PreexistingEntityException;
import com.developer.java.entity.AberturaDia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.developer.java.entity.CaixaMovement;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class AberturaDiaJpaController implements Serializable {

    public AberturaDiaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AberturaDia aberturaDia) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CaixaMovement caixaMovementId = aberturaDia.getCaixaMovementId();
            if (caixaMovementId != null) {
                caixaMovementId = em.getReference(caixaMovementId.getClass(), caixaMovementId.getId());
                aberturaDia.setCaixaMovementId(caixaMovementId);
            }
            em.persist(aberturaDia);
            if (caixaMovementId != null) {
                caixaMovementId.getAberturaDiaList().add(aberturaDia);
                caixaMovementId = em.merge(caixaMovementId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAberturaDia(aberturaDia.getNumCaixa()) != null) {
                throw new PreexistingEntityException("AberturaDia " + aberturaDia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AberturaDia aberturaDia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AberturaDia persistentAberturaDia = em.find(AberturaDia.class, aberturaDia.getNumCaixa());
            CaixaMovement caixaMovementIdOld = persistentAberturaDia.getCaixaMovementId();
            CaixaMovement caixaMovementIdNew = aberturaDia.getCaixaMovementId();
            if (caixaMovementIdNew != null) {
                caixaMovementIdNew = em.getReference(caixaMovementIdNew.getClass(), caixaMovementIdNew.getId());
                aberturaDia.setCaixaMovementId(caixaMovementIdNew);
            }
            aberturaDia = em.merge(aberturaDia);
            if (caixaMovementIdOld != null && !caixaMovementIdOld.equals(caixaMovementIdNew)) {
                caixaMovementIdOld.getAberturaDiaList().remove(aberturaDia);
                caixaMovementIdOld = em.merge(caixaMovementIdOld);
            }
            if (caixaMovementIdNew != null && !caixaMovementIdNew.equals(caixaMovementIdOld)) {
                caixaMovementIdNew.getAberturaDiaList().add(aberturaDia);
                caixaMovementIdNew = em.merge(caixaMovementIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = aberturaDia.getNumCaixa();
                if (findAberturaDia(id) == null) {
                    throw new NonexistentEntityException("The aberturaDia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AberturaDia aberturaDia;
            try {
                aberturaDia = em.getReference(AberturaDia.class, id);
                aberturaDia.getNumCaixa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The aberturaDia with id " + id + " no longer exists.", enfe);
            }
            CaixaMovement caixaMovementId = aberturaDia.getCaixaMovementId();
            if (caixaMovementId != null) {
                caixaMovementId.getAberturaDiaList().remove(aberturaDia);
                caixaMovementId = em.merge(caixaMovementId);
            }
            em.remove(aberturaDia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AberturaDia> findAberturaDiaEntities() {
        return findAberturaDiaEntities(true, -1, -1);
    }

    public List<AberturaDia> findAberturaDiaEntities(int maxResults, int firstResult) {
        return findAberturaDiaEntities(false, maxResults, firstResult);
    }

    private List<AberturaDia> findAberturaDiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AberturaDia.class));
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

    public AberturaDia findAberturaDia(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AberturaDia.class, id);
        } finally {
            em.close();
        }
    }

    public int getAberturaDiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AberturaDia> rt = cq.from(AberturaDia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
