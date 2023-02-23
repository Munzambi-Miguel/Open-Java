/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.controller;

import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Company;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.developer.java.entity.Entity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class CompanyJpaController implements Serializable {

    public CompanyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Company company) {
        if (company.getEntityList() == null) {
            company.setEntityList(new ArrayList<Entity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Entity> attachedEntityList = new ArrayList<Entity>();
            for (Entity entityListEntityToAttach : company.getEntityList()) {
                entityListEntityToAttach = em.getReference(entityListEntityToAttach.getClass(), entityListEntityToAttach.getId());
                attachedEntityList.add(entityListEntityToAttach);
            }
            company.setEntityList(attachedEntityList);
            em.persist(company);
            for (Entity entityListEntity : company.getEntityList()) {
                Company oldCompanyidOfEntityListEntity = entityListEntity.getCompanyid();
                entityListEntity.setCompanyid(company);
                entityListEntity = em.merge(entityListEntity);
                if (oldCompanyidOfEntityListEntity != null) {
                    oldCompanyidOfEntityListEntity.getEntityList().remove(entityListEntity);
                    oldCompanyidOfEntityListEntity = em.merge(oldCompanyidOfEntityListEntity);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Company company) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Company persistentCompany = em.find(Company.class, company.getId());
            List<Entity> entityListOld = persistentCompany.getEntityList();
            List<Entity> entityListNew = company.getEntityList();
            List<Entity> attachedEntityListNew = new ArrayList<Entity>();
            for (Entity entityListNewEntityToAttach : entityListNew) {
                entityListNewEntityToAttach = em.getReference(entityListNewEntityToAttach.getClass(), entityListNewEntityToAttach.getId());
                attachedEntityListNew.add(entityListNewEntityToAttach);
            }
            entityListNew = attachedEntityListNew;
            company.setEntityList(entityListNew);
            company = em.merge(company);
            for (Entity entityListOldEntity : entityListOld) {
                if (!entityListNew.contains(entityListOldEntity)) {
                    entityListOldEntity.setCompanyid(null);
                    entityListOldEntity = em.merge(entityListOldEntity);
                }
            }
            for (Entity entityListNewEntity : entityListNew) {
                if (!entityListOld.contains(entityListNewEntity)) {
                    Company oldCompanyidOfEntityListNewEntity = entityListNewEntity.getCompanyid();
                    entityListNewEntity.setCompanyid(company);
                    entityListNewEntity = em.merge(entityListNewEntity);
                    if (oldCompanyidOfEntityListNewEntity != null && !oldCompanyidOfEntityListNewEntity.equals(company)) {
                        oldCompanyidOfEntityListNewEntity.getEntityList().remove(entityListNewEntity);
                        oldCompanyidOfEntityListNewEntity = em.merge(oldCompanyidOfEntityListNewEntity);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = company.getId();
                if (findCompany(id) == null) {
                    throw new NonexistentEntityException("The company with id " + id + " no longer exists.");
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
            Company company;
            try {
                company = em.getReference(Company.class, id);
                company.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The company with id " + id + " no longer exists.", enfe);
            }
            List<Entity> entityList = company.getEntityList();
            for (Entity entityListEntity : entityList) {
                entityListEntity.setCompanyid(null);
                entityListEntity = em.merge(entityListEntity);
            }
            em.remove(company);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Company> findCompanyEntities() {
        return findCompanyEntities(true, -1, -1);
    }

    public List<Company> findCompanyEntities(int maxResults, int firstResult) {
        return findCompanyEntities(false, maxResults, firstResult);
    }

    private List<Company> findCompanyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Company.class));
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

    public Company findCompany(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Company.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompanyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Company> rt = cq.from(Company.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
