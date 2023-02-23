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
import com.developer.java.entity.Grup;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class GrupJpaController implements Serializable {

    public GrupJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grup grup) {
        if (grup.getAutenticationList() == null) {
            grup.setAutenticationList(new ArrayList<Autentication>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Autentication> attachedAutenticationList = new ArrayList<Autentication>();
            for (Autentication autenticationListAutenticationToAttach : grup.getAutenticationList()) {
                autenticationListAutenticationToAttach = em.getReference(autenticationListAutenticationToAttach.getClass(), autenticationListAutenticationToAttach.getId());
                attachedAutenticationList.add(autenticationListAutenticationToAttach);
            }
            grup.setAutenticationList(attachedAutenticationList);
            em.persist(grup);
            for (Autentication autenticationListAutentication : grup.getAutenticationList()) {
                Grup oldGrupIdOfAutenticationListAutentication = autenticationListAutentication.getGrupId();
                autenticationListAutentication.setGrupId(grup);
                autenticationListAutentication = em.merge(autenticationListAutentication);
                if (oldGrupIdOfAutenticationListAutentication != null) {
                    oldGrupIdOfAutenticationListAutentication.getAutenticationList().remove(autenticationListAutentication);
                    oldGrupIdOfAutenticationListAutentication = em.merge(oldGrupIdOfAutenticationListAutentication);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grup grup) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grup persistentGrup = em.find(Grup.class, grup.getId());
            List<Autentication> autenticationListOld = persistentGrup.getAutenticationList();
            List<Autentication> autenticationListNew = grup.getAutenticationList();
            List<String> illegalOrphanMessages = null;
            for (Autentication autenticationListOldAutentication : autenticationListOld) {
                if (!autenticationListNew.contains(autenticationListOldAutentication)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Autentication " + autenticationListOldAutentication + " since its grupId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Autentication> attachedAutenticationListNew = new ArrayList<Autentication>();
            for (Autentication autenticationListNewAutenticationToAttach : autenticationListNew) {
                autenticationListNewAutenticationToAttach = em.getReference(autenticationListNewAutenticationToAttach.getClass(), autenticationListNewAutenticationToAttach.getId());
                attachedAutenticationListNew.add(autenticationListNewAutenticationToAttach);
            }
            autenticationListNew = attachedAutenticationListNew;
            grup.setAutenticationList(autenticationListNew);
            grup = em.merge(grup);
            for (Autentication autenticationListNewAutentication : autenticationListNew) {
                if (!autenticationListOld.contains(autenticationListNewAutentication)) {
                    Grup oldGrupIdOfAutenticationListNewAutentication = autenticationListNewAutentication.getGrupId();
                    autenticationListNewAutentication.setGrupId(grup);
                    autenticationListNewAutentication = em.merge(autenticationListNewAutentication);
                    if (oldGrupIdOfAutenticationListNewAutentication != null && !oldGrupIdOfAutenticationListNewAutentication.equals(grup)) {
                        oldGrupIdOfAutenticationListNewAutentication.getAutenticationList().remove(autenticationListNewAutentication);
                        oldGrupIdOfAutenticationListNewAutentication = em.merge(oldGrupIdOfAutenticationListNewAutentication);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = grup.getId();
                if (findGrup(id) == null) {
                    throw new NonexistentEntityException("The grup with id " + id + " no longer exists.");
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
            Grup grup;
            try {
                grup = em.getReference(Grup.class, id);
                grup.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grup with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Autentication> autenticationListOrphanCheck = grup.getAutenticationList();
            for (Autentication autenticationListOrphanCheckAutentication : autenticationListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grup (" + grup + ") cannot be destroyed since the Autentication " + autenticationListOrphanCheckAutentication + " in its autenticationList field has a non-nullable grupId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(grup);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grup> findGrupEntities() {
        return findGrupEntities(true, -1, -1);
    }

    public List<Grup> findGrupEntities(int maxResults, int firstResult) {
        return findGrupEntities(false, maxResults, firstResult);
    }

    private List<Grup> findGrupEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grup.class));
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

    public Grup findGrup(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grup.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grup> rt = cq.from(Grup.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
