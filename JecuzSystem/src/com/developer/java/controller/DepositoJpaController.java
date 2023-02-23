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
import com.developer.java.entity.Armazem;
import com.developer.java.entity.Deposito;
import com.developer.java.entity.Movement;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class DepositoJpaController implements Serializable {

    public DepositoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Deposito deposito) {
        if (deposito.getMovementList() == null) {
            deposito.setMovementList(new ArrayList<Movement>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Armazem armazemid = deposito.getArmazemid();
            if (armazemid != null) {
                armazemid = em.getReference(armazemid.getClass(), armazemid.getId());
                deposito.setArmazemid(armazemid);
            }
            List<Movement> attachedMovementList = new ArrayList<Movement>();
            for (Movement movementListMovementToAttach : deposito.getMovementList()) {
                movementListMovementToAttach = em.getReference(movementListMovementToAttach.getClass(), movementListMovementToAttach.getId());
                attachedMovementList.add(movementListMovementToAttach);
            }
            deposito.setMovementList(attachedMovementList);
            em.persist(deposito);
            if (armazemid != null) {
                armazemid.getDepositoList().add(deposito);
                armazemid = em.merge(armazemid);
            }
            for (Movement movementListMovement : deposito.getMovementList()) {
                Deposito oldDepositoIdOfMovementListMovement = movementListMovement.getDepositoId();
                movementListMovement.setDepositoId(deposito);
                movementListMovement = em.merge(movementListMovement);
                if (oldDepositoIdOfMovementListMovement != null) {
                    oldDepositoIdOfMovementListMovement.getMovementList().remove(movementListMovement);
                    oldDepositoIdOfMovementListMovement = em.merge(oldDepositoIdOfMovementListMovement);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Deposito deposito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Deposito persistentDeposito = em.find(Deposito.class, deposito.getId());
            Armazem armazemidOld = persistentDeposito.getArmazemid();
            Armazem armazemidNew = deposito.getArmazemid();
            List<Movement> movementListOld = persistentDeposito.getMovementList();
            List<Movement> movementListNew = deposito.getMovementList();
            if (armazemidNew != null) {
                armazemidNew = em.getReference(armazemidNew.getClass(), armazemidNew.getId());
                deposito.setArmazemid(armazemidNew);
            }
            List<Movement> attachedMovementListNew = new ArrayList<Movement>();
            for (Movement movementListNewMovementToAttach : movementListNew) {
                movementListNewMovementToAttach = em.getReference(movementListNewMovementToAttach.getClass(), movementListNewMovementToAttach.getId());
                attachedMovementListNew.add(movementListNewMovementToAttach);
            }
            movementListNew = attachedMovementListNew;
            deposito.setMovementList(movementListNew);
            deposito = em.merge(deposito);
            if (armazemidOld != null && !armazemidOld.equals(armazemidNew)) {
                armazemidOld.getDepositoList().remove(deposito);
                armazemidOld = em.merge(armazemidOld);
            }
            if (armazemidNew != null && !armazemidNew.equals(armazemidOld)) {
                armazemidNew.getDepositoList().add(deposito);
                armazemidNew = em.merge(armazemidNew);
            }
            for (Movement movementListOldMovement : movementListOld) {
                if (!movementListNew.contains(movementListOldMovement)) {
                    movementListOldMovement.setDepositoId(null);
                    movementListOldMovement = em.merge(movementListOldMovement);
                }
            }
            for (Movement movementListNewMovement : movementListNew) {
                if (!movementListOld.contains(movementListNewMovement)) {
                    Deposito oldDepositoIdOfMovementListNewMovement = movementListNewMovement.getDepositoId();
                    movementListNewMovement.setDepositoId(deposito);
                    movementListNewMovement = em.merge(movementListNewMovement);
                    if (oldDepositoIdOfMovementListNewMovement != null && !oldDepositoIdOfMovementListNewMovement.equals(deposito)) {
                        oldDepositoIdOfMovementListNewMovement.getMovementList().remove(movementListNewMovement);
                        oldDepositoIdOfMovementListNewMovement = em.merge(oldDepositoIdOfMovementListNewMovement);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = deposito.getId();
                if (findDeposito(id) == null) {
                    throw new NonexistentEntityException("The deposito with id " + id + " no longer exists.");
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
            Deposito deposito;
            try {
                deposito = em.getReference(Deposito.class, id);
                deposito.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The deposito with id " + id + " no longer exists.", enfe);
            }
            Armazem armazemid = deposito.getArmazemid();
            if (armazemid != null) {
                armazemid.getDepositoList().remove(deposito);
                armazemid = em.merge(armazemid);
            }
            List<Movement> movementList = deposito.getMovementList();
            for (Movement movementListMovement : movementList) {
                movementListMovement.setDepositoId(null);
                movementListMovement = em.merge(movementListMovement);
            }
            em.remove(deposito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Deposito> findDepositoEntities() {
        return findDepositoEntities(true, -1, -1);
    }

    public List<Deposito> findDepositoEntities(int maxResults, int firstResult) {
        return findDepositoEntities(false, maxResults, firstResult);
    }

    private List<Deposito> findDepositoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Deposito.class));
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

    public Deposito findDeposito(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Deposito.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepositoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Deposito> rt = cq.from(Deposito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
