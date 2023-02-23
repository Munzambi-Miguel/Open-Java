/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.controller;

import com.developer.java.controller.exceptions.IllegalOrphanException;
import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Armazem;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.developer.java.entity.Entity;
import com.developer.java.entity.Deposito;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class ArmazemJpaController implements Serializable {

    public ArmazemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Armazem armazem) {
        if (armazem.getDepositoList() == null) {
            armazem.setDepositoList(new ArrayList<Deposito>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entity entityId = armazem.getEntityId();
            if (entityId != null) {
                entityId = em.getReference(entityId.getClass(), entityId.getId());
                armazem.setEntityId(entityId);
            }
            List<Deposito> attachedDepositoList = new ArrayList<Deposito>();
            for (Deposito depositoListDepositoToAttach : armazem.getDepositoList()) {
                depositoListDepositoToAttach = em.getReference(depositoListDepositoToAttach.getClass(), depositoListDepositoToAttach.getId());
                attachedDepositoList.add(depositoListDepositoToAttach);
            }
            armazem.setDepositoList(attachedDepositoList);
            em.persist(armazem);
            if (entityId != null) {
                entityId.getArmazemList().add(armazem);
                entityId = em.merge(entityId);
            }
            for (Deposito depositoListDeposito : armazem.getDepositoList()) {
                Armazem oldArmazemidOfDepositoListDeposito = depositoListDeposito.getArmazemid();
                depositoListDeposito.setArmazemid(armazem);
                depositoListDeposito = em.merge(depositoListDeposito);
                if (oldArmazemidOfDepositoListDeposito != null) {
                    oldArmazemidOfDepositoListDeposito.getDepositoList().remove(depositoListDeposito);
                    oldArmazemidOfDepositoListDeposito = em.merge(oldArmazemidOfDepositoListDeposito);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Armazem armazem) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Armazem persistentArmazem = em.find(Armazem.class, armazem.getId());
            Entity entityIdOld = persistentArmazem.getEntityId();
            Entity entityIdNew = armazem.getEntityId();
            List<Deposito> depositoListOld = persistentArmazem.getDepositoList();
            List<Deposito> depositoListNew = armazem.getDepositoList();
            List<String> illegalOrphanMessages = null;
            for (Deposito depositoListOldDeposito : depositoListOld) {
                if (!depositoListNew.contains(depositoListOldDeposito)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Deposito " + depositoListOldDeposito + " since its armazemid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (entityIdNew != null) {
                entityIdNew = em.getReference(entityIdNew.getClass(), entityIdNew.getId());
                armazem.setEntityId(entityIdNew);
            }
            List<Deposito> attachedDepositoListNew = new ArrayList<Deposito>();
            for (Deposito depositoListNewDepositoToAttach : depositoListNew) {
                depositoListNewDepositoToAttach = em.getReference(depositoListNewDepositoToAttach.getClass(), depositoListNewDepositoToAttach.getId());
                attachedDepositoListNew.add(depositoListNewDepositoToAttach);
            }
            depositoListNew = attachedDepositoListNew;
            armazem.setDepositoList(depositoListNew);
            armazem = em.merge(armazem);
            if (entityIdOld != null && !entityIdOld.equals(entityIdNew)) {
                entityIdOld.getArmazemList().remove(armazem);
                entityIdOld = em.merge(entityIdOld);
            }
            if (entityIdNew != null && !entityIdNew.equals(entityIdOld)) {
                entityIdNew.getArmazemList().add(armazem);
                entityIdNew = em.merge(entityIdNew);
            }
            for (Deposito depositoListNewDeposito : depositoListNew) {
                if (!depositoListOld.contains(depositoListNewDeposito)) {
                    Armazem oldArmazemidOfDepositoListNewDeposito = depositoListNewDeposito.getArmazemid();
                    depositoListNewDeposito.setArmazemid(armazem);
                    depositoListNewDeposito = em.merge(depositoListNewDeposito);
                    if (oldArmazemidOfDepositoListNewDeposito != null && !oldArmazemidOfDepositoListNewDeposito.equals(armazem)) {
                        oldArmazemidOfDepositoListNewDeposito.getDepositoList().remove(depositoListNewDeposito);
                        oldArmazemidOfDepositoListNewDeposito = em.merge(oldArmazemidOfDepositoListNewDeposito);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = armazem.getId();
                if (findArmazem(id) == null) {
                    throw new NonexistentEntityException("The armazem with id " + id + " no longer exists.");
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
            Armazem armazem;
            try {
                armazem = em.getReference(Armazem.class, id);
                armazem.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The armazem with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Deposito> depositoListOrphanCheck = armazem.getDepositoList();
            for (Deposito depositoListOrphanCheckDeposito : depositoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Armazem (" + armazem + ") cannot be destroyed since the Deposito " + depositoListOrphanCheckDeposito + " in its depositoList field has a non-nullable armazemid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Entity entityId = armazem.getEntityId();
            if (entityId != null) {
                entityId.getArmazemList().remove(armazem);
                entityId = em.merge(entityId);
            }
            em.remove(armazem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Armazem> findArmazemEntities() {
        return findArmazemEntities(true, -1, -1);
    }

    public List<Armazem> findArmazemEntities(int maxResults, int firstResult) {
        return findArmazemEntities(false, maxResults, firstResult);
    }

    private List<Armazem> findArmazemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Armazem.class));
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

    public Armazem findArmazem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Armazem.class, id);
        } finally {
            em.close();
        }
    }

    public int getArmazemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Armazem> rt = cq.from(Armazem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
