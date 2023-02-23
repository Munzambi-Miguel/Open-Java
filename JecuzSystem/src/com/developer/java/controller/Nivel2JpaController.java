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
import com.developer.java.entity.Nivel1;
import com.developer.java.entity.Nivel2;
import com.developer.java.entity.Nivel3;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class Nivel2JpaController implements Serializable {

    public Nivel2JpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nivel2 nivel2) {
        if (nivel2.getNivel3List() == null) {
            nivel2.setNivel3List(new ArrayList<Nivel3>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nivel1 nivel1Id = nivel2.getNivel1Id();
            if (nivel1Id != null) {
                nivel1Id = em.getReference(nivel1Id.getClass(), nivel1Id.getId());
                nivel2.setNivel1Id(nivel1Id);
            }
            List<Nivel3> attachedNivel3List = new ArrayList<Nivel3>();
            for (Nivel3 nivel3ListNivel3ToAttach : nivel2.getNivel3List()) {
                nivel3ListNivel3ToAttach = em.getReference(nivel3ListNivel3ToAttach.getClass(), nivel3ListNivel3ToAttach.getId());
                attachedNivel3List.add(nivel3ListNivel3ToAttach);
            }
            nivel2.setNivel3List(attachedNivel3List);
            em.persist(nivel2);
            if (nivel1Id != null) {
                nivel1Id.getNivel2List().add(nivel2);
                nivel1Id = em.merge(nivel1Id);
            }
            for (Nivel3 nivel3ListNivel3 : nivel2.getNivel3List()) {
                Nivel2 oldNivel2IdOfNivel3ListNivel3 = nivel3ListNivel3.getNivel2Id();
                nivel3ListNivel3.setNivel2Id(nivel2);
                nivel3ListNivel3 = em.merge(nivel3ListNivel3);
                if (oldNivel2IdOfNivel3ListNivel3 != null) {
                    oldNivel2IdOfNivel3ListNivel3.getNivel3List().remove(nivel3ListNivel3);
                    oldNivel2IdOfNivel3ListNivel3 = em.merge(oldNivel2IdOfNivel3ListNivel3);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nivel2 nivel2) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nivel2 persistentNivel2 = em.find(Nivel2.class, nivel2.getId());
            Nivel1 nivel1IdOld = persistentNivel2.getNivel1Id();
            Nivel1 nivel1IdNew = nivel2.getNivel1Id();
            List<Nivel3> nivel3ListOld = persistentNivel2.getNivel3List();
            List<Nivel3> nivel3ListNew = nivel2.getNivel3List();
            if (nivel1IdNew != null) {
                nivel1IdNew = em.getReference(nivel1IdNew.getClass(), nivel1IdNew.getId());
                nivel2.setNivel1Id(nivel1IdNew);
            }
            List<Nivel3> attachedNivel3ListNew = new ArrayList<Nivel3>();
            for (Nivel3 nivel3ListNewNivel3ToAttach : nivel3ListNew) {
                nivel3ListNewNivel3ToAttach = em.getReference(nivel3ListNewNivel3ToAttach.getClass(), nivel3ListNewNivel3ToAttach.getId());
                attachedNivel3ListNew.add(nivel3ListNewNivel3ToAttach);
            }
            nivel3ListNew = attachedNivel3ListNew;
            nivel2.setNivel3List(nivel3ListNew);
            nivel2 = em.merge(nivel2);
            if (nivel1IdOld != null && !nivel1IdOld.equals(nivel1IdNew)) {
                nivel1IdOld.getNivel2List().remove(nivel2);
                nivel1IdOld = em.merge(nivel1IdOld);
            }
            if (nivel1IdNew != null && !nivel1IdNew.equals(nivel1IdOld)) {
                nivel1IdNew.getNivel2List().add(nivel2);
                nivel1IdNew = em.merge(nivel1IdNew);
            }
            for (Nivel3 nivel3ListOldNivel3 : nivel3ListOld) {
                if (!nivel3ListNew.contains(nivel3ListOldNivel3)) {
                    nivel3ListOldNivel3.setNivel2Id(null);
                    nivel3ListOldNivel3 = em.merge(nivel3ListOldNivel3);
                }
            }
            for (Nivel3 nivel3ListNewNivel3 : nivel3ListNew) {
                if (!nivel3ListOld.contains(nivel3ListNewNivel3)) {
                    Nivel2 oldNivel2IdOfNivel3ListNewNivel3 = nivel3ListNewNivel3.getNivel2Id();
                    nivel3ListNewNivel3.setNivel2Id(nivel2);
                    nivel3ListNewNivel3 = em.merge(nivel3ListNewNivel3);
                    if (oldNivel2IdOfNivel3ListNewNivel3 != null && !oldNivel2IdOfNivel3ListNewNivel3.equals(nivel2)) {
                        oldNivel2IdOfNivel3ListNewNivel3.getNivel3List().remove(nivel3ListNewNivel3);
                        oldNivel2IdOfNivel3ListNewNivel3 = em.merge(oldNivel2IdOfNivel3ListNewNivel3);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nivel2.getId();
                if (findNivel2(id) == null) {
                    throw new NonexistentEntityException("The nivel2 with id " + id + " no longer exists.");
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
            Nivel2 nivel2;
            try {
                nivel2 = em.getReference(Nivel2.class, id);
                nivel2.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nivel2 with id " + id + " no longer exists.", enfe);
            }
            Nivel1 nivel1Id = nivel2.getNivel1Id();
            if (nivel1Id != null) {
                nivel1Id.getNivel2List().remove(nivel2);
                nivel1Id = em.merge(nivel1Id);
            }
            List<Nivel3> nivel3List = nivel2.getNivel3List();
            for (Nivel3 nivel3ListNivel3 : nivel3List) {
                nivel3ListNivel3.setNivel2Id(null);
                nivel3ListNivel3 = em.merge(nivel3ListNivel3);
            }
            em.remove(nivel2);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Nivel2> findNivel2Entities() {
        return findNivel2Entities(true, -1, -1);
    }

    public List<Nivel2> findNivel2Entities(int maxResults, int firstResult) {
        return findNivel2Entities(false, maxResults, firstResult);
    }

    private List<Nivel2> findNivel2Entities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nivel2.class));
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

    public Nivel2 findNivel2(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nivel2.class, id);
        } finally {
            em.close();
        }
    }

    public int getNivel2Count() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nivel2> rt = cq.from(Nivel2.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
