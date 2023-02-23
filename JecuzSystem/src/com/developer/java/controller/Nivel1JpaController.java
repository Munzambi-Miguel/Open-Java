/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.controller;

import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Nivel1;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.developer.java.entity.Nivel2;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class Nivel1JpaController implements Serializable {

    public Nivel1JpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nivel1 nivel1) {
        if (nivel1.getNivel2List() == null) {
            nivel1.setNivel2List(new ArrayList<Nivel2>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Nivel2> attachedNivel2List = new ArrayList<Nivel2>();
            for (Nivel2 nivel2ListNivel2ToAttach : nivel1.getNivel2List()) {
                nivel2ListNivel2ToAttach = em.getReference(nivel2ListNivel2ToAttach.getClass(), nivel2ListNivel2ToAttach.getId());
                attachedNivel2List.add(nivel2ListNivel2ToAttach);
            }
            nivel1.setNivel2List(attachedNivel2List);
            em.persist(nivel1);
            for (Nivel2 nivel2ListNivel2 : nivel1.getNivel2List()) {
                Nivel1 oldNivel1IdOfNivel2ListNivel2 = nivel2ListNivel2.getNivel1Id();
                nivel2ListNivel2.setNivel1Id(nivel1);
                nivel2ListNivel2 = em.merge(nivel2ListNivel2);
                if (oldNivel1IdOfNivel2ListNivel2 != null) {
                    oldNivel1IdOfNivel2ListNivel2.getNivel2List().remove(nivel2ListNivel2);
                    oldNivel1IdOfNivel2ListNivel2 = em.merge(oldNivel1IdOfNivel2ListNivel2);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nivel1 nivel1) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nivel1 persistentNivel1 = em.find(Nivel1.class, nivel1.getId());
            List<Nivel2> nivel2ListOld = persistentNivel1.getNivel2List();
            List<Nivel2> nivel2ListNew = nivel1.getNivel2List();
            List<Nivel2> attachedNivel2ListNew = new ArrayList<Nivel2>();
            for (Nivel2 nivel2ListNewNivel2ToAttach : nivel2ListNew) {
                nivel2ListNewNivel2ToAttach = em.getReference(nivel2ListNewNivel2ToAttach.getClass(), nivel2ListNewNivel2ToAttach.getId());
                attachedNivel2ListNew.add(nivel2ListNewNivel2ToAttach);
            }
            nivel2ListNew = attachedNivel2ListNew;
            nivel1.setNivel2List(nivel2ListNew);
            nivel1 = em.merge(nivel1);
            for (Nivel2 nivel2ListOldNivel2 : nivel2ListOld) {
                if (!nivel2ListNew.contains(nivel2ListOldNivel2)) {
                    nivel2ListOldNivel2.setNivel1Id(null);
                    nivel2ListOldNivel2 = em.merge(nivel2ListOldNivel2);
                }
            }
            for (Nivel2 nivel2ListNewNivel2 : nivel2ListNew) {
                if (!nivel2ListOld.contains(nivel2ListNewNivel2)) {
                    Nivel1 oldNivel1IdOfNivel2ListNewNivel2 = nivel2ListNewNivel2.getNivel1Id();
                    nivel2ListNewNivel2.setNivel1Id(nivel1);
                    nivel2ListNewNivel2 = em.merge(nivel2ListNewNivel2);
                    if (oldNivel1IdOfNivel2ListNewNivel2 != null && !oldNivel1IdOfNivel2ListNewNivel2.equals(nivel1)) {
                        oldNivel1IdOfNivel2ListNewNivel2.getNivel2List().remove(nivel2ListNewNivel2);
                        oldNivel1IdOfNivel2ListNewNivel2 = em.merge(oldNivel1IdOfNivel2ListNewNivel2);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nivel1.getId();
                if (findNivel1(id) == null) {
                    throw new NonexistentEntityException("The nivel1 with id " + id + " no longer exists.");
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
            Nivel1 nivel1;
            try {
                nivel1 = em.getReference(Nivel1.class, id);
                nivel1.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nivel1 with id " + id + " no longer exists.", enfe);
            }
            List<Nivel2> nivel2List = nivel1.getNivel2List();
            for (Nivel2 nivel2ListNivel2 : nivel2List) {
                nivel2ListNivel2.setNivel1Id(null);
                nivel2ListNivel2 = em.merge(nivel2ListNivel2);
            }
            em.remove(nivel1);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Nivel1> findNivel1Entities() {
        return findNivel1Entities(true, -1, -1);
    }

    public List<Nivel1> findNivel1Entities(int maxResults, int firstResult) {
        return findNivel1Entities(false, maxResults, firstResult);
    }

    private List<Nivel1> findNivel1Entities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nivel1.class));
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

    public Nivel1 findNivel1(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nivel1.class, id);
        } finally {
            em.close();
        }
    }

    public int getNivel1Count() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nivel1> rt = cq.from(Nivel1.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
