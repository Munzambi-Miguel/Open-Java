/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.controller;

import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Address;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.developer.java.entity.City;
import com.developer.java.entity.Entity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class AddressJpaController implements Serializable {

    public AddressJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Address address) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            City cityid = address.getCityid();
            if (cityid != null) {
                cityid = em.getReference(cityid.getClass(), cityid.getId());
                address.setCityid(cityid);
            }
            Entity entityId = address.getEntityId();
            if (entityId != null) {
                entityId = em.getReference(entityId.getClass(), entityId.getId());
                address.setEntityId(entityId);
            }
            em.persist(address);
            if (cityid != null) {
                cityid.getAddressList().add(address);
                cityid = em.merge(cityid);
            }
            if (entityId != null) {
                entityId.getAddressList().add(address);
                entityId = em.merge(entityId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Address address) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Address persistentAddress = em.find(Address.class, address.getId());
            City cityidOld = persistentAddress.getCityid();
            City cityidNew = address.getCityid();
            Entity entityIdOld = persistentAddress.getEntityId();
            Entity entityIdNew = address.getEntityId();
            if (cityidNew != null) {
                cityidNew = em.getReference(cityidNew.getClass(), cityidNew.getId());
                address.setCityid(cityidNew);
            }
            if (entityIdNew != null) {
                entityIdNew = em.getReference(entityIdNew.getClass(), entityIdNew.getId());
                address.setEntityId(entityIdNew);
            }
            address = em.merge(address);
            if (cityidOld != null && !cityidOld.equals(cityidNew)) {
                cityidOld.getAddressList().remove(address);
                cityidOld = em.merge(cityidOld);
            }
            if (cityidNew != null && !cityidNew.equals(cityidOld)) {
                cityidNew.getAddressList().add(address);
                cityidNew = em.merge(cityidNew);
            }
            if (entityIdOld != null && !entityIdOld.equals(entityIdNew)) {
                entityIdOld.getAddressList().remove(address);
                entityIdOld = em.merge(entityIdOld);
            }
            if (entityIdNew != null && !entityIdNew.equals(entityIdOld)) {
                entityIdNew.getAddressList().add(address);
                entityIdNew = em.merge(entityIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = address.getId();
                if (findAddress(id) == null) {
                    throw new NonexistentEntityException("The address with id " + id + " no longer exists.");
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
            Address address;
            try {
                address = em.getReference(Address.class, id);
                address.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The address with id " + id + " no longer exists.", enfe);
            }
            City cityid = address.getCityid();
            if (cityid != null) {
                cityid.getAddressList().remove(address);
                cityid = em.merge(cityid);
            }
            Entity entityId = address.getEntityId();
            if (entityId != null) {
                entityId.getAddressList().remove(address);
                entityId = em.merge(entityId);
            }
            em.remove(address);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Address> findAddressEntities() {
        return findAddressEntities(true, -1, -1);
    }

    public List<Address> findAddressEntities(int maxResults, int firstResult) {
        return findAddressEntities(false, maxResults, firstResult);
    }

    private List<Address> findAddressEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Address.class));
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

    public Address findAddress(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Address.class, id);
        } finally {
            em.close();
        }
    }

    public int getAddressCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Address> rt = cq.from(Address.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
