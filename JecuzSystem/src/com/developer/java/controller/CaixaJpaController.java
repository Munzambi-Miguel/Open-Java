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
import com.developer.java.entity.Caixa;
import com.developer.java.entity.Maquina;
import com.developer.java.entity.CaixaMovement;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class CaixaJpaController implements Serializable {

    public CaixaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Caixa caixa) {
        if (caixa.getCaixaMovementList() == null) {
            caixa.setCaixaMovementList(new ArrayList<CaixaMovement>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autentication autenticationId = caixa.getAutenticationId();
            if (autenticationId != null) {
                autenticationId = em.getReference(autenticationId.getClass(), autenticationId.getId());
                caixa.setAutenticationId(autenticationId);
            }
            Maquina maquinaId = caixa.getMaquinaId();
            if (maquinaId != null) {
                maquinaId = em.getReference(maquinaId.getClass(), maquinaId.getId());
                caixa.setMaquinaId(maquinaId);
            }
            List<CaixaMovement> attachedCaixaMovementList = new ArrayList<CaixaMovement>();
            for (CaixaMovement caixaMovementListCaixaMovementToAttach : caixa.getCaixaMovementList()) {
                caixaMovementListCaixaMovementToAttach = em.getReference(caixaMovementListCaixaMovementToAttach.getClass(), caixaMovementListCaixaMovementToAttach.getId());
                attachedCaixaMovementList.add(caixaMovementListCaixaMovementToAttach);
            }
            caixa.setCaixaMovementList(attachedCaixaMovementList);
            em.persist(caixa);
            if (autenticationId != null) {
                autenticationId.getCaixaList().add(caixa);
                autenticationId = em.merge(autenticationId);
            }
            if (maquinaId != null) {
                maquinaId.getCaixaList().add(caixa);
                maquinaId = em.merge(maquinaId);
            }
            for (CaixaMovement caixaMovementListCaixaMovement : caixa.getCaixaMovementList()) {
                Caixa oldCaixaIdOfCaixaMovementListCaixaMovement = caixaMovementListCaixaMovement.getCaixaId();
                caixaMovementListCaixaMovement.setCaixaId(caixa);
                caixaMovementListCaixaMovement = em.merge(caixaMovementListCaixaMovement);
                if (oldCaixaIdOfCaixaMovementListCaixaMovement != null) {
                    oldCaixaIdOfCaixaMovementListCaixaMovement.getCaixaMovementList().remove(caixaMovementListCaixaMovement);
                    oldCaixaIdOfCaixaMovementListCaixaMovement = em.merge(oldCaixaIdOfCaixaMovementListCaixaMovement);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Caixa caixa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Caixa persistentCaixa = em.find(Caixa.class, caixa.getId());
            Autentication autenticationIdOld = persistentCaixa.getAutenticationId();
            Autentication autenticationIdNew = caixa.getAutenticationId();
            Maquina maquinaIdOld = persistentCaixa.getMaquinaId();
            Maquina maquinaIdNew = caixa.getMaquinaId();
            List<CaixaMovement> caixaMovementListOld = persistentCaixa.getCaixaMovementList();
            List<CaixaMovement> caixaMovementListNew = caixa.getCaixaMovementList();
            List<String> illegalOrphanMessages = null;
            for (CaixaMovement caixaMovementListOldCaixaMovement : caixaMovementListOld) {
                if (!caixaMovementListNew.contains(caixaMovementListOldCaixaMovement)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CaixaMovement " + caixaMovementListOldCaixaMovement + " since its caixaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (autenticationIdNew != null) {
                autenticationIdNew = em.getReference(autenticationIdNew.getClass(), autenticationIdNew.getId());
                caixa.setAutenticationId(autenticationIdNew);
            }
            if (maquinaIdNew != null) {
                maquinaIdNew = em.getReference(maquinaIdNew.getClass(), maquinaIdNew.getId());
                caixa.setMaquinaId(maquinaIdNew);
            }
            List<CaixaMovement> attachedCaixaMovementListNew = new ArrayList<CaixaMovement>();
            for (CaixaMovement caixaMovementListNewCaixaMovementToAttach : caixaMovementListNew) {
                caixaMovementListNewCaixaMovementToAttach = em.getReference(caixaMovementListNewCaixaMovementToAttach.getClass(), caixaMovementListNewCaixaMovementToAttach.getId());
                attachedCaixaMovementListNew.add(caixaMovementListNewCaixaMovementToAttach);
            }
            caixaMovementListNew = attachedCaixaMovementListNew;
            caixa.setCaixaMovementList(caixaMovementListNew);
            caixa = em.merge(caixa);
            if (autenticationIdOld != null && !autenticationIdOld.equals(autenticationIdNew)) {
                autenticationIdOld.getCaixaList().remove(caixa);
                autenticationIdOld = em.merge(autenticationIdOld);
            }
            if (autenticationIdNew != null && !autenticationIdNew.equals(autenticationIdOld)) {
                autenticationIdNew.getCaixaList().add(caixa);
                autenticationIdNew = em.merge(autenticationIdNew);
            }
            if (maquinaIdOld != null && !maquinaIdOld.equals(maquinaIdNew)) {
                maquinaIdOld.getCaixaList().remove(caixa);
                maquinaIdOld = em.merge(maquinaIdOld);
            }
            if (maquinaIdNew != null && !maquinaIdNew.equals(maquinaIdOld)) {
                maquinaIdNew.getCaixaList().add(caixa);
                maquinaIdNew = em.merge(maquinaIdNew);
            }
            for (CaixaMovement caixaMovementListNewCaixaMovement : caixaMovementListNew) {
                if (!caixaMovementListOld.contains(caixaMovementListNewCaixaMovement)) {
                    Caixa oldCaixaIdOfCaixaMovementListNewCaixaMovement = caixaMovementListNewCaixaMovement.getCaixaId();
                    caixaMovementListNewCaixaMovement.setCaixaId(caixa);
                    caixaMovementListNewCaixaMovement = em.merge(caixaMovementListNewCaixaMovement);
                    if (oldCaixaIdOfCaixaMovementListNewCaixaMovement != null && !oldCaixaIdOfCaixaMovementListNewCaixaMovement.equals(caixa)) {
                        oldCaixaIdOfCaixaMovementListNewCaixaMovement.getCaixaMovementList().remove(caixaMovementListNewCaixaMovement);
                        oldCaixaIdOfCaixaMovementListNewCaixaMovement = em.merge(oldCaixaIdOfCaixaMovementListNewCaixaMovement);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = caixa.getId();
                if (findCaixa(id) == null) {
                    throw new NonexistentEntityException("The caixa with id " + id + " no longer exists.");
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
            Caixa caixa;
            try {
                caixa = em.getReference(Caixa.class, id);
                caixa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The caixa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CaixaMovement> caixaMovementListOrphanCheck = caixa.getCaixaMovementList();
            for (CaixaMovement caixaMovementListOrphanCheckCaixaMovement : caixaMovementListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Caixa (" + caixa + ") cannot be destroyed since the CaixaMovement " + caixaMovementListOrphanCheckCaixaMovement + " in its caixaMovementList field has a non-nullable caixaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Autentication autenticationId = caixa.getAutenticationId();
            if (autenticationId != null) {
                autenticationId.getCaixaList().remove(caixa);
                autenticationId = em.merge(autenticationId);
            }
            Maquina maquinaId = caixa.getMaquinaId();
            if (maquinaId != null) {
                maquinaId.getCaixaList().remove(caixa);
                maquinaId = em.merge(maquinaId);
            }
            em.remove(caixa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Caixa> findCaixaEntities() {
        return findCaixaEntities(true, -1, -1);
    }

    public List<Caixa> findCaixaEntities(int maxResults, int firstResult) {
        return findCaixaEntities(false, maxResults, firstResult);
    }

    private List<Caixa> findCaixaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Caixa.class));
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

    public Caixa findCaixa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Caixa.class, id);
        } finally {
            em.close();
        }
    }

    public int getCaixaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Caixa> rt = cq.from(Caixa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
