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
import com.developer.java.entity.Entity;
import com.developer.java.entity.Autentication;
import com.developer.java.entity.Deposito;
import com.developer.java.entity.Movement;
import com.developer.java.entity.Product;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class MovementJpaController implements Serializable {

    public MovementJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movement movement) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entity fornedorId = movement.getFornedorId();
            if (fornedorId != null) {
                fornedorId = em.getReference(fornedorId.getClass(), fornedorId.getId());
                movement.setFornedorId(fornedorId);
            }
            Autentication autenticationId = movement.getAutenticationId();
            if (autenticationId != null) {
                autenticationId = em.getReference(autenticationId.getClass(), autenticationId.getId());
                movement.setAutenticationId(autenticationId);
            }
            Deposito depositoId = movement.getDepositoId();
            if (depositoId != null) {
                depositoId = em.getReference(depositoId.getClass(), depositoId.getId());
                movement.setDepositoId(depositoId);
            }
            Product productId = movement.getProductId();
            if (productId != null) {
                productId = em.getReference(productId.getClass(), productId.getId());
                movement.setProductId(productId);
            }
            em.persist(movement);
            if (fornedorId != null) {
                fornedorId.getMovementList().add(movement);
                fornedorId = em.merge(fornedorId);
            }
            if (autenticationId != null) {
                autenticationId.getMovementList().add(movement);
                autenticationId = em.merge(autenticationId);
            }
            if (depositoId != null) {
                depositoId.getMovementList().add(movement);
                depositoId = em.merge(depositoId);
            }
            if (productId != null) {
                productId.getMovementList().add(movement);
                productId = em.merge(productId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Movement movement) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movement persistentMovement = em.find(Movement.class, movement.getId());
            Entity fornedorIdOld = persistentMovement.getFornedorId();
            Entity fornedorIdNew = movement.getFornedorId();
            Autentication autenticationIdOld = persistentMovement.getAutenticationId();
            Autentication autenticationIdNew = movement.getAutenticationId();
            Deposito depositoIdOld = persistentMovement.getDepositoId();
            Deposito depositoIdNew = movement.getDepositoId();
            Product productIdOld = persistentMovement.getProductId();
            Product productIdNew = movement.getProductId();
            if (fornedorIdNew != null) {
                fornedorIdNew = em.getReference(fornedorIdNew.getClass(), fornedorIdNew.getId());
                movement.setFornedorId(fornedorIdNew);
            }
            if (autenticationIdNew != null) {
                autenticationIdNew = em.getReference(autenticationIdNew.getClass(), autenticationIdNew.getId());
                movement.setAutenticationId(autenticationIdNew);
            }
            if (depositoIdNew != null) {
                depositoIdNew = em.getReference(depositoIdNew.getClass(), depositoIdNew.getId());
                movement.setDepositoId(depositoIdNew);
            }
            if (productIdNew != null) {
                productIdNew = em.getReference(productIdNew.getClass(), productIdNew.getId());
                movement.setProductId(productIdNew);
            }
            movement = em.merge(movement);
            if (fornedorIdOld != null && !fornedorIdOld.equals(fornedorIdNew)) {
                fornedorIdOld.getMovementList().remove(movement);
                fornedorIdOld = em.merge(fornedorIdOld);
            }
            if (fornedorIdNew != null && !fornedorIdNew.equals(fornedorIdOld)) {
                fornedorIdNew.getMovementList().add(movement);
                fornedorIdNew = em.merge(fornedorIdNew);
            }
            if (autenticationIdOld != null && !autenticationIdOld.equals(autenticationIdNew)) {
                autenticationIdOld.getMovementList().remove(movement);
                autenticationIdOld = em.merge(autenticationIdOld);
            }
            if (autenticationIdNew != null && !autenticationIdNew.equals(autenticationIdOld)) {
                autenticationIdNew.getMovementList().add(movement);
                autenticationIdNew = em.merge(autenticationIdNew);
            }
            if (depositoIdOld != null && !depositoIdOld.equals(depositoIdNew)) {
                depositoIdOld.getMovementList().remove(movement);
                depositoIdOld = em.merge(depositoIdOld);
            }
            if (depositoIdNew != null && !depositoIdNew.equals(depositoIdOld)) {
                depositoIdNew.getMovementList().add(movement);
                depositoIdNew = em.merge(depositoIdNew);
            }
            if (productIdOld != null && !productIdOld.equals(productIdNew)) {
                productIdOld.getMovementList().remove(movement);
                productIdOld = em.merge(productIdOld);
            }
            if (productIdNew != null && !productIdNew.equals(productIdOld)) {
                productIdNew.getMovementList().add(movement);
                productIdNew = em.merge(productIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = movement.getId();
                if (findMovement(id) == null) {
                    throw new NonexistentEntityException("The movement with id " + id + " no longer exists.");
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
            Movement movement;
            try {
                movement = em.getReference(Movement.class, id);
                movement.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movement with id " + id + " no longer exists.", enfe);
            }
            Entity fornedorId = movement.getFornedorId();
            if (fornedorId != null) {
                fornedorId.getMovementList().remove(movement);
                fornedorId = em.merge(fornedorId);
            }
            Autentication autenticationId = movement.getAutenticationId();
            if (autenticationId != null) {
                autenticationId.getMovementList().remove(movement);
                autenticationId = em.merge(autenticationId);
            }
            Deposito depositoId = movement.getDepositoId();
            if (depositoId != null) {
                depositoId.getMovementList().remove(movement);
                depositoId = em.merge(depositoId);
            }
            Product productId = movement.getProductId();
            if (productId != null) {
                productId.getMovementList().remove(movement);
                productId = em.merge(productId);
            }
            em.remove(movement);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Movement> findMovementEntities() {
        return findMovementEntities(true, -1, -1);
    }

    public List<Movement> findMovementEntities(int maxResults, int firstResult) {
        return findMovementEntities(false, maxResults, firstResult);
    }

    private List<Movement> findMovementEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Movement.class));
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

    public Movement findMovement(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movement.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovementCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Movement> rt = cq.from(Movement.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
