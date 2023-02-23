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
import com.developer.java.entity.Caixa;
import com.developer.java.entity.Maquina;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class MaquinaJpaController implements Serializable {

    public MaquinaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Maquina maquina) {
        if (maquina.getCaixaList() == null) {
            maquina.setCaixaList(new ArrayList<Caixa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Caixa> attachedCaixaList = new ArrayList<Caixa>();
            for (Caixa caixaListCaixaToAttach : maquina.getCaixaList()) {
                caixaListCaixaToAttach = em.getReference(caixaListCaixaToAttach.getClass(), caixaListCaixaToAttach.getId());
                attachedCaixaList.add(caixaListCaixaToAttach);
            }
            maquina.setCaixaList(attachedCaixaList);
            em.persist(maquina);
            for (Caixa caixaListCaixa : maquina.getCaixaList()) {
                Maquina oldMaquinaIdOfCaixaListCaixa = caixaListCaixa.getMaquinaId();
                caixaListCaixa.setMaquinaId(maquina);
                caixaListCaixa = em.merge(caixaListCaixa);
                if (oldMaquinaIdOfCaixaListCaixa != null) {
                    oldMaquinaIdOfCaixaListCaixa.getCaixaList().remove(caixaListCaixa);
                    oldMaquinaIdOfCaixaListCaixa = em.merge(oldMaquinaIdOfCaixaListCaixa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Maquina maquina) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Maquina persistentMaquina = em.find(Maquina.class, maquina.getId());
            List<Caixa> caixaListOld = persistentMaquina.getCaixaList();
            List<Caixa> caixaListNew = maquina.getCaixaList();
            List<String> illegalOrphanMessages = null;
            for (Caixa caixaListOldCaixa : caixaListOld) {
                if (!caixaListNew.contains(caixaListOldCaixa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Caixa " + caixaListOldCaixa + " since its maquinaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Caixa> attachedCaixaListNew = new ArrayList<Caixa>();
            for (Caixa caixaListNewCaixaToAttach : caixaListNew) {
                caixaListNewCaixaToAttach = em.getReference(caixaListNewCaixaToAttach.getClass(), caixaListNewCaixaToAttach.getId());
                attachedCaixaListNew.add(caixaListNewCaixaToAttach);
            }
            caixaListNew = attachedCaixaListNew;
            maquina.setCaixaList(caixaListNew);
            maquina = em.merge(maquina);
            for (Caixa caixaListNewCaixa : caixaListNew) {
                if (!caixaListOld.contains(caixaListNewCaixa)) {
                    Maquina oldMaquinaIdOfCaixaListNewCaixa = caixaListNewCaixa.getMaquinaId();
                    caixaListNewCaixa.setMaquinaId(maquina);
                    caixaListNewCaixa = em.merge(caixaListNewCaixa);
                    if (oldMaquinaIdOfCaixaListNewCaixa != null && !oldMaquinaIdOfCaixaListNewCaixa.equals(maquina)) {
                        oldMaquinaIdOfCaixaListNewCaixa.getCaixaList().remove(caixaListNewCaixa);
                        oldMaquinaIdOfCaixaListNewCaixa = em.merge(oldMaquinaIdOfCaixaListNewCaixa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = maquina.getId();
                if (findMaquina(id) == null) {
                    throw new NonexistentEntityException("The maquina with id " + id + " no longer exists.");
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
            Maquina maquina;
            try {
                maquina = em.getReference(Maquina.class, id);
                maquina.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The maquina with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Caixa> caixaListOrphanCheck = maquina.getCaixaList();
            for (Caixa caixaListOrphanCheckCaixa : caixaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Maquina (" + maquina + ") cannot be destroyed since the Caixa " + caixaListOrphanCheckCaixa + " in its caixaList field has a non-nullable maquinaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(maquina);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Maquina> findMaquinaEntities() {
        return findMaquinaEntities(true, -1, -1);
    }

    public List<Maquina> findMaquinaEntities(int maxResults, int firstResult) {
        return findMaquinaEntities(false, maxResults, firstResult);
    }

    private List<Maquina> findMaquinaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Maquina.class));
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

    public Maquina findMaquina(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Maquina.class, id);
        } finally {
            em.close();
        }
    }

    public int getMaquinaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Maquina> rt = cq.from(Maquina.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
