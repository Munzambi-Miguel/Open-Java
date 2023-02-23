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
import com.developer.java.entity.Photo;
import com.developer.java.entity.Product;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class PhotoJpaController implements Serializable {

    public PhotoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Photo photo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entity entityId = photo.getEntityId();
            if (entityId != null) {
                entityId = em.getReference(entityId.getClass(), entityId.getId());
                photo.setEntityId(entityId);
            }
            Product productId = photo.getProductId();
            if (productId != null) {
                productId = em.getReference(productId.getClass(), productId.getId());
                photo.setProductId(productId);
            }
            em.persist(photo);
            if (entityId != null) {
                entityId.getPhotoList().add(photo);
                entityId = em.merge(entityId);
            }
            if (productId != null) {
                productId.getPhotoList().add(photo);
                productId = em.merge(productId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Photo photo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Photo persistentPhoto = em.find(Photo.class, photo.getId());
            Entity entityIdOld = persistentPhoto.getEntityId();
            Entity entityIdNew = photo.getEntityId();
            Product productIdOld = persistentPhoto.getProductId();
            Product productIdNew = photo.getProductId();
            if (entityIdNew != null) {
                entityIdNew = em.getReference(entityIdNew.getClass(), entityIdNew.getId());
                photo.setEntityId(entityIdNew);
            }
            if (productIdNew != null) {
                productIdNew = em.getReference(productIdNew.getClass(), productIdNew.getId());
                photo.setProductId(productIdNew);
            }
            photo = em.merge(photo);
            if (entityIdOld != null && !entityIdOld.equals(entityIdNew)) {
                entityIdOld.getPhotoList().remove(photo);
                entityIdOld = em.merge(entityIdOld);
            }
            if (entityIdNew != null && !entityIdNew.equals(entityIdOld)) {
                entityIdNew.getPhotoList().add(photo);
                entityIdNew = em.merge(entityIdNew);
            }
            if (productIdOld != null && !productIdOld.equals(productIdNew)) {
                productIdOld.getPhotoList().remove(photo);
                productIdOld = em.merge(productIdOld);
            }
            if (productIdNew != null && !productIdNew.equals(productIdOld)) {
                productIdNew.getPhotoList().add(photo);
                productIdNew = em.merge(productIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = photo.getId();
                if (findPhoto(id) == null) {
                    throw new NonexistentEntityException("The photo with id " + id + " no longer exists.");
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
            Photo photo;
            try {
                photo = em.getReference(Photo.class, id);
                photo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The photo with id " + id + " no longer exists.", enfe);
            }
            Entity entityId = photo.getEntityId();
            if (entityId != null) {
                entityId.getPhotoList().remove(photo);
                entityId = em.merge(entityId);
            }
            Product productId = photo.getProductId();
            if (productId != null) {
                productId.getPhotoList().remove(photo);
                productId = em.merge(productId);
            }
            em.remove(photo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Photo> findPhotoEntities() {
        return findPhotoEntities(true, -1, -1);
    }

    public List<Photo> findPhotoEntities(int maxResults, int firstResult) {
        return findPhotoEntities(false, maxResults, firstResult);
    }

    private List<Photo> findPhotoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Photo.class));
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

    public Photo findPhoto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Photo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPhotoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Photo> rt = cq.from(Photo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
