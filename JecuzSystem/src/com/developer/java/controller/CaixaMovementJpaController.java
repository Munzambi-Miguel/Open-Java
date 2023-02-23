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
import com.developer.java.entity.Fatura;
import com.developer.java.entity.AberturaDia;
import com.developer.java.entity.CaixaMovement;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class CaixaMovementJpaController implements Serializable {

    public CaixaMovementJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CaixaMovement caixaMovement) {
        if (caixaMovement.getAberturaDiaList() == null) {
            caixaMovement.setAberturaDiaList(new ArrayList<AberturaDia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autentication autenticationId = caixaMovement.getAutenticationId();
            if (autenticationId != null) {
                autenticationId = em.getReference(autenticationId.getClass(), autenticationId.getId());
                caixaMovement.setAutenticationId(autenticationId);
            }
            Caixa caixaId = caixaMovement.getCaixaId();
            if (caixaId != null) {
                caixaId = em.getReference(caixaId.getClass(), caixaId.getId());
                caixaMovement.setCaixaId(caixaId);
            }
            Fatura faturaId = caixaMovement.getFaturaId();
            if (faturaId != null) {
                faturaId = em.getReference(faturaId.getClass(), faturaId.getId());
                caixaMovement.setFaturaId(faturaId);
            }
            List<AberturaDia> attachedAberturaDiaList = new ArrayList<AberturaDia>();
            for (AberturaDia aberturaDiaListAberturaDiaToAttach : caixaMovement.getAberturaDiaList()) {
                aberturaDiaListAberturaDiaToAttach = em.getReference(aberturaDiaListAberturaDiaToAttach.getClass(), aberturaDiaListAberturaDiaToAttach.getNumCaixa());
                attachedAberturaDiaList.add(aberturaDiaListAberturaDiaToAttach);
            }
            caixaMovement.setAberturaDiaList(attachedAberturaDiaList);
            em.persist(caixaMovement);
            if (autenticationId != null) {
                autenticationId.getCaixaMovementList().add(caixaMovement);
                autenticationId = em.merge(autenticationId);
            }
            if (caixaId != null) {
                caixaId.getCaixaMovementList().add(caixaMovement);
                caixaId = em.merge(caixaId);
            }
            if (faturaId != null) {
                faturaId.getCaixaMovementList().add(caixaMovement);
                faturaId = em.merge(faturaId);
            }
            for (AberturaDia aberturaDiaListAberturaDia : caixaMovement.getAberturaDiaList()) {
                CaixaMovement oldCaixaMovementIdOfAberturaDiaListAberturaDia = aberturaDiaListAberturaDia.getCaixaMovementId();
                aberturaDiaListAberturaDia.setCaixaMovementId(caixaMovement);
                aberturaDiaListAberturaDia = em.merge(aberturaDiaListAberturaDia);
                if (oldCaixaMovementIdOfAberturaDiaListAberturaDia != null) {
                    oldCaixaMovementIdOfAberturaDiaListAberturaDia.getAberturaDiaList().remove(aberturaDiaListAberturaDia);
                    oldCaixaMovementIdOfAberturaDiaListAberturaDia = em.merge(oldCaixaMovementIdOfAberturaDiaListAberturaDia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CaixaMovement caixaMovement) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CaixaMovement persistentCaixaMovement = em.find(CaixaMovement.class, caixaMovement.getId());
            Autentication autenticationIdOld = persistentCaixaMovement.getAutenticationId();
            Autentication autenticationIdNew = caixaMovement.getAutenticationId();
            Caixa caixaIdOld = persistentCaixaMovement.getCaixaId();
            Caixa caixaIdNew = caixaMovement.getCaixaId();
            Fatura faturaIdOld = persistentCaixaMovement.getFaturaId();
            Fatura faturaIdNew = caixaMovement.getFaturaId();
            List<AberturaDia> aberturaDiaListOld = persistentCaixaMovement.getAberturaDiaList();
            List<AberturaDia> aberturaDiaListNew = caixaMovement.getAberturaDiaList();
            List<String> illegalOrphanMessages = null;
            for (AberturaDia aberturaDiaListOldAberturaDia : aberturaDiaListOld) {
                if (!aberturaDiaListNew.contains(aberturaDiaListOldAberturaDia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AberturaDia " + aberturaDiaListOldAberturaDia + " since its caixaMovementId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (autenticationIdNew != null) {
                autenticationIdNew = em.getReference(autenticationIdNew.getClass(), autenticationIdNew.getId());
                caixaMovement.setAutenticationId(autenticationIdNew);
            }
            if (caixaIdNew != null) {
                caixaIdNew = em.getReference(caixaIdNew.getClass(), caixaIdNew.getId());
                caixaMovement.setCaixaId(caixaIdNew);
            }
            if (faturaIdNew != null) {
                faturaIdNew = em.getReference(faturaIdNew.getClass(), faturaIdNew.getId());
                caixaMovement.setFaturaId(faturaIdNew);
            }
            List<AberturaDia> attachedAberturaDiaListNew = new ArrayList<AberturaDia>();
            for (AberturaDia aberturaDiaListNewAberturaDiaToAttach : aberturaDiaListNew) {
                aberturaDiaListNewAberturaDiaToAttach = em.getReference(aberturaDiaListNewAberturaDiaToAttach.getClass(), aberturaDiaListNewAberturaDiaToAttach.getNumCaixa());
                attachedAberturaDiaListNew.add(aberturaDiaListNewAberturaDiaToAttach);
            }
            aberturaDiaListNew = attachedAberturaDiaListNew;
            caixaMovement.setAberturaDiaList(aberturaDiaListNew);
            caixaMovement = em.merge(caixaMovement);
            if (autenticationIdOld != null && !autenticationIdOld.equals(autenticationIdNew)) {
                autenticationIdOld.getCaixaMovementList().remove(caixaMovement);
                autenticationIdOld = em.merge(autenticationIdOld);
            }
            if (autenticationIdNew != null && !autenticationIdNew.equals(autenticationIdOld)) {
                autenticationIdNew.getCaixaMovementList().add(caixaMovement);
                autenticationIdNew = em.merge(autenticationIdNew);
            }
            if (caixaIdOld != null && !caixaIdOld.equals(caixaIdNew)) {
                caixaIdOld.getCaixaMovementList().remove(caixaMovement);
                caixaIdOld = em.merge(caixaIdOld);
            }
            if (caixaIdNew != null && !caixaIdNew.equals(caixaIdOld)) {
                caixaIdNew.getCaixaMovementList().add(caixaMovement);
                caixaIdNew = em.merge(caixaIdNew);
            }
            if (faturaIdOld != null && !faturaIdOld.equals(faturaIdNew)) {
                faturaIdOld.getCaixaMovementList().remove(caixaMovement);
                faturaIdOld = em.merge(faturaIdOld);
            }
            if (faturaIdNew != null && !faturaIdNew.equals(faturaIdOld)) {
                faturaIdNew.getCaixaMovementList().add(caixaMovement);
                faturaIdNew = em.merge(faturaIdNew);
            }
            for (AberturaDia aberturaDiaListNewAberturaDia : aberturaDiaListNew) {
                if (!aberturaDiaListOld.contains(aberturaDiaListNewAberturaDia)) {
                    CaixaMovement oldCaixaMovementIdOfAberturaDiaListNewAberturaDia = aberturaDiaListNewAberturaDia.getCaixaMovementId();
                    aberturaDiaListNewAberturaDia.setCaixaMovementId(caixaMovement);
                    aberturaDiaListNewAberturaDia = em.merge(aberturaDiaListNewAberturaDia);
                    if (oldCaixaMovementIdOfAberturaDiaListNewAberturaDia != null && !oldCaixaMovementIdOfAberturaDiaListNewAberturaDia.equals(caixaMovement)) {
                        oldCaixaMovementIdOfAberturaDiaListNewAberturaDia.getAberturaDiaList().remove(aberturaDiaListNewAberturaDia);
                        oldCaixaMovementIdOfAberturaDiaListNewAberturaDia = em.merge(oldCaixaMovementIdOfAberturaDiaListNewAberturaDia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = caixaMovement.getId();
                if (findCaixaMovement(id) == null) {
                    throw new NonexistentEntityException("The caixaMovement with id " + id + " no longer exists.");
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
            CaixaMovement caixaMovement;
            try {
                caixaMovement = em.getReference(CaixaMovement.class, id);
                caixaMovement.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The caixaMovement with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AberturaDia> aberturaDiaListOrphanCheck = caixaMovement.getAberturaDiaList();
            for (AberturaDia aberturaDiaListOrphanCheckAberturaDia : aberturaDiaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CaixaMovement (" + caixaMovement + ") cannot be destroyed since the AberturaDia " + aberturaDiaListOrphanCheckAberturaDia + " in its aberturaDiaList field has a non-nullable caixaMovementId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Autentication autenticationId = caixaMovement.getAutenticationId();
            if (autenticationId != null) {
                autenticationId.getCaixaMovementList().remove(caixaMovement);
                autenticationId = em.merge(autenticationId);
            }
            Caixa caixaId = caixaMovement.getCaixaId();
            if (caixaId != null) {
                caixaId.getCaixaMovementList().remove(caixaMovement);
                caixaId = em.merge(caixaId);
            }
            Fatura faturaId = caixaMovement.getFaturaId();
            if (faturaId != null) {
                faturaId.getCaixaMovementList().remove(caixaMovement);
                faturaId = em.merge(faturaId);
            }
            em.remove(caixaMovement);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CaixaMovement> findCaixaMovementEntities() {
        return findCaixaMovementEntities(true, -1, -1);
    }

    public List<CaixaMovement> findCaixaMovementEntities(int maxResults, int firstResult) {
        return findCaixaMovementEntities(false, maxResults, firstResult);
    }

    private List<CaixaMovement> findCaixaMovementEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CaixaMovement.class));
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

    public CaixaMovement findCaixaMovement(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CaixaMovement.class, id);
        } finally {
            em.close();
        }
    }

    public int getCaixaMovementCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CaixaMovement> rt = cq.from(CaixaMovement.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
