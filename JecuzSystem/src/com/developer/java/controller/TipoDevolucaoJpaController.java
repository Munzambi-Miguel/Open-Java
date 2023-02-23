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
import com.developer.java.entity.Fatura;
import com.developer.java.entity.TipoDevolucao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class TipoDevolucaoJpaController implements Serializable {

    public TipoDevolucaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoDevolucao tipoDevolucao) {
        if (tipoDevolucao.getFaturaList() == null) {
            tipoDevolucao.setFaturaList(new ArrayList<Fatura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Fatura> attachedFaturaList = new ArrayList<Fatura>();
            for (Fatura faturaListFaturaToAttach : tipoDevolucao.getFaturaList()) {
                faturaListFaturaToAttach = em.getReference(faturaListFaturaToAttach.getClass(), faturaListFaturaToAttach.getId());
                attachedFaturaList.add(faturaListFaturaToAttach);
            }
            tipoDevolucao.setFaturaList(attachedFaturaList);
            em.persist(tipoDevolucao);
            for (Fatura faturaListFatura : tipoDevolucao.getFaturaList()) {
                TipoDevolucao oldTipoDevolucaoIdOfFaturaListFatura = faturaListFatura.getTipoDevolucaoId();
                faturaListFatura.setTipoDevolucaoId(tipoDevolucao);
                faturaListFatura = em.merge(faturaListFatura);
                if (oldTipoDevolucaoIdOfFaturaListFatura != null) {
                    oldTipoDevolucaoIdOfFaturaListFatura.getFaturaList().remove(faturaListFatura);
                    oldTipoDevolucaoIdOfFaturaListFatura = em.merge(oldTipoDevolucaoIdOfFaturaListFatura);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoDevolucao tipoDevolucao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoDevolucao persistentTipoDevolucao = em.find(TipoDevolucao.class, tipoDevolucao.getId());
            List<Fatura> faturaListOld = persistentTipoDevolucao.getFaturaList();
            List<Fatura> faturaListNew = tipoDevolucao.getFaturaList();
            List<Fatura> attachedFaturaListNew = new ArrayList<Fatura>();
            for (Fatura faturaListNewFaturaToAttach : faturaListNew) {
                faturaListNewFaturaToAttach = em.getReference(faturaListNewFaturaToAttach.getClass(), faturaListNewFaturaToAttach.getId());
                attachedFaturaListNew.add(faturaListNewFaturaToAttach);
            }
            faturaListNew = attachedFaturaListNew;
            tipoDevolucao.setFaturaList(faturaListNew);
            tipoDevolucao = em.merge(tipoDevolucao);
            for (Fatura faturaListOldFatura : faturaListOld) {
                if (!faturaListNew.contains(faturaListOldFatura)) {
                    faturaListOldFatura.setTipoDevolucaoId(null);
                    faturaListOldFatura = em.merge(faturaListOldFatura);
                }
            }
            for (Fatura faturaListNewFatura : faturaListNew) {
                if (!faturaListOld.contains(faturaListNewFatura)) {
                    TipoDevolucao oldTipoDevolucaoIdOfFaturaListNewFatura = faturaListNewFatura.getTipoDevolucaoId();
                    faturaListNewFatura.setTipoDevolucaoId(tipoDevolucao);
                    faturaListNewFatura = em.merge(faturaListNewFatura);
                    if (oldTipoDevolucaoIdOfFaturaListNewFatura != null && !oldTipoDevolucaoIdOfFaturaListNewFatura.equals(tipoDevolucao)) {
                        oldTipoDevolucaoIdOfFaturaListNewFatura.getFaturaList().remove(faturaListNewFatura);
                        oldTipoDevolucaoIdOfFaturaListNewFatura = em.merge(oldTipoDevolucaoIdOfFaturaListNewFatura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoDevolucao.getId();
                if (findTipoDevolucao(id) == null) {
                    throw new NonexistentEntityException("The tipoDevolucao with id " + id + " no longer exists.");
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
            TipoDevolucao tipoDevolucao;
            try {
                tipoDevolucao = em.getReference(TipoDevolucao.class, id);
                tipoDevolucao.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoDevolucao with id " + id + " no longer exists.", enfe);
            }
            List<Fatura> faturaList = tipoDevolucao.getFaturaList();
            for (Fatura faturaListFatura : faturaList) {
                faturaListFatura.setTipoDevolucaoId(null);
                faturaListFatura = em.merge(faturaListFatura);
            }
            em.remove(tipoDevolucao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoDevolucao> findTipoDevolucaoEntities() {
        return findTipoDevolucaoEntities(true, -1, -1);
    }

    public List<TipoDevolucao> findTipoDevolucaoEntities(int maxResults, int firstResult) {
        return findTipoDevolucaoEntities(false, maxResults, firstResult);
    }

    private List<TipoDevolucao> findTipoDevolucaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoDevolucao.class));
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

    public TipoDevolucao findTipoDevolucao(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoDevolucao.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoDevolucaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoDevolucao> rt = cq.from(TipoDevolucao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
