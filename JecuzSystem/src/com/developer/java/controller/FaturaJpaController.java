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
import com.developer.java.entity.Client;
import com.developer.java.entity.TipoDevolucao;
import com.developer.java.entity.CaixaMovement;
import com.developer.java.entity.Fatura;
import java.util.ArrayList;
import java.util.List;
import com.developer.java.entity.Item;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class FaturaJpaController implements Serializable {

    public FaturaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fatura fatura) {
        if (fatura.getCaixaMovementList() == null) {
            fatura.setCaixaMovementList(new ArrayList<CaixaMovement>());
        }
        if (fatura.getItemList() == null) {
            fatura.setItemList(new ArrayList<Item>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autentication funcionarioId = fatura.getFuncionarioId();
            if (funcionarioId != null) {
                funcionarioId = em.getReference(funcionarioId.getClass(), funcionarioId.getId());
                fatura.setFuncionarioId(funcionarioId);
            }
            Client clientId = fatura.getClientId();
            if (clientId != null) {
                clientId = em.getReference(clientId.getClass(), clientId.getId());
                fatura.setClientId(clientId);
            }
            Autentication admin = fatura.getAdmin();
            if (admin != null) {
                admin = em.getReference(admin.getClass(), admin.getId());
                fatura.setAdmin(admin);
            }
            TipoDevolucao tipoDevolucaoId = fatura.getTipoDevolucaoId();
            if (tipoDevolucaoId != null) {
                tipoDevolucaoId = em.getReference(tipoDevolucaoId.getClass(), tipoDevolucaoId.getId());
                fatura.setTipoDevolucaoId(tipoDevolucaoId);
            }
            List<CaixaMovement> attachedCaixaMovementList = new ArrayList<CaixaMovement>();
            for (CaixaMovement caixaMovementListCaixaMovementToAttach : fatura.getCaixaMovementList()) {
                caixaMovementListCaixaMovementToAttach = em.getReference(caixaMovementListCaixaMovementToAttach.getClass(), caixaMovementListCaixaMovementToAttach.getId());
                attachedCaixaMovementList.add(caixaMovementListCaixaMovementToAttach);
            }
            fatura.setCaixaMovementList(attachedCaixaMovementList);
            List<Item> attachedItemList = new ArrayList<Item>();
            for (Item itemListItemToAttach : fatura.getItemList()) {
                itemListItemToAttach = em.getReference(itemListItemToAttach.getClass(), itemListItemToAttach.getId());
                attachedItemList.add(itemListItemToAttach);
            }
            fatura.setItemList(attachedItemList);
            em.persist(fatura);
            if (funcionarioId != null) {
                funcionarioId.getFaturaList().add(fatura);
                funcionarioId = em.merge(funcionarioId);
            }
            if (clientId != null) {
                clientId.getFaturaList().add(fatura);
                clientId = em.merge(clientId);
            }
            if (admin != null) {
                admin.getFaturaList().add(fatura);
                admin = em.merge(admin);
            }
            if (tipoDevolucaoId != null) {
                tipoDevolucaoId.getFaturaList().add(fatura);
                tipoDevolucaoId = em.merge(tipoDevolucaoId);
            }
            for (CaixaMovement caixaMovementListCaixaMovement : fatura.getCaixaMovementList()) {
                Fatura oldFaturaIdOfCaixaMovementListCaixaMovement = caixaMovementListCaixaMovement.getFaturaId();
                caixaMovementListCaixaMovement.setFaturaId(fatura);
                caixaMovementListCaixaMovement = em.merge(caixaMovementListCaixaMovement);
                if (oldFaturaIdOfCaixaMovementListCaixaMovement != null) {
                    oldFaturaIdOfCaixaMovementListCaixaMovement.getCaixaMovementList().remove(caixaMovementListCaixaMovement);
                    oldFaturaIdOfCaixaMovementListCaixaMovement = em.merge(oldFaturaIdOfCaixaMovementListCaixaMovement);
                }
            }
            for (Item itemListItem : fatura.getItemList()) {
                Fatura oldFaturaIdOfItemListItem = itemListItem.getFaturaId();
                itemListItem.setFaturaId(fatura);
                itemListItem = em.merge(itemListItem);
                if (oldFaturaIdOfItemListItem != null) {
                    oldFaturaIdOfItemListItem.getItemList().remove(itemListItem);
                    oldFaturaIdOfItemListItem = em.merge(oldFaturaIdOfItemListItem);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fatura fatura) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fatura persistentFatura = em.find(Fatura.class, fatura.getId());
            Autentication funcionarioIdOld = persistentFatura.getFuncionarioId();
            Autentication funcionarioIdNew = fatura.getFuncionarioId();
            Client clientIdOld = persistentFatura.getClientId();
            Client clientIdNew = fatura.getClientId();
            Autentication adminOld = persistentFatura.getAdmin();
            Autentication adminNew = fatura.getAdmin();
            TipoDevolucao tipoDevolucaoIdOld = persistentFatura.getTipoDevolucaoId();
            TipoDevolucao tipoDevolucaoIdNew = fatura.getTipoDevolucaoId();
            List<CaixaMovement> caixaMovementListOld = persistentFatura.getCaixaMovementList();
            List<CaixaMovement> caixaMovementListNew = fatura.getCaixaMovementList();
            List<Item> itemListOld = persistentFatura.getItemList();
            List<Item> itemListNew = fatura.getItemList();
            List<String> illegalOrphanMessages = null;
            for (Item itemListOldItem : itemListOld) {
                if (!itemListNew.contains(itemListOldItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Item " + itemListOldItem + " since its faturaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (funcionarioIdNew != null) {
                funcionarioIdNew = em.getReference(funcionarioIdNew.getClass(), funcionarioIdNew.getId());
                fatura.setFuncionarioId(funcionarioIdNew);
            }
            if (clientIdNew != null) {
                clientIdNew = em.getReference(clientIdNew.getClass(), clientIdNew.getId());
                fatura.setClientId(clientIdNew);
            }
            if (adminNew != null) {
                adminNew = em.getReference(adminNew.getClass(), adminNew.getId());
                fatura.setAdmin(adminNew);
            }
            if (tipoDevolucaoIdNew != null) {
                tipoDevolucaoIdNew = em.getReference(tipoDevolucaoIdNew.getClass(), tipoDevolucaoIdNew.getId());
                fatura.setTipoDevolucaoId(tipoDevolucaoIdNew);
            }
            List<CaixaMovement> attachedCaixaMovementListNew = new ArrayList<CaixaMovement>();
            for (CaixaMovement caixaMovementListNewCaixaMovementToAttach : caixaMovementListNew) {
                caixaMovementListNewCaixaMovementToAttach = em.getReference(caixaMovementListNewCaixaMovementToAttach.getClass(), caixaMovementListNewCaixaMovementToAttach.getId());
                attachedCaixaMovementListNew.add(caixaMovementListNewCaixaMovementToAttach);
            }
            caixaMovementListNew = attachedCaixaMovementListNew;
            fatura.setCaixaMovementList(caixaMovementListNew);
            List<Item> attachedItemListNew = new ArrayList<Item>();
            for (Item itemListNewItemToAttach : itemListNew) {
                itemListNewItemToAttach = em.getReference(itemListNewItemToAttach.getClass(), itemListNewItemToAttach.getId());
                attachedItemListNew.add(itemListNewItemToAttach);
            }
            itemListNew = attachedItemListNew;
            fatura.setItemList(itemListNew);
            fatura = em.merge(fatura);
            if (funcionarioIdOld != null && !funcionarioIdOld.equals(funcionarioIdNew)) {
                funcionarioIdOld.getFaturaList().remove(fatura);
                funcionarioIdOld = em.merge(funcionarioIdOld);
            }
            if (funcionarioIdNew != null && !funcionarioIdNew.equals(funcionarioIdOld)) {
                funcionarioIdNew.getFaturaList().add(fatura);
                funcionarioIdNew = em.merge(funcionarioIdNew);
            }
            if (clientIdOld != null && !clientIdOld.equals(clientIdNew)) {
                clientIdOld.getFaturaList().remove(fatura);
                clientIdOld = em.merge(clientIdOld);
            }
            if (clientIdNew != null && !clientIdNew.equals(clientIdOld)) {
                clientIdNew.getFaturaList().add(fatura);
                clientIdNew = em.merge(clientIdNew);
            }
            if (adminOld != null && !adminOld.equals(adminNew)) {
                adminOld.getFaturaList().remove(fatura);
                adminOld = em.merge(adminOld);
            }
            if (adminNew != null && !adminNew.equals(adminOld)) {
                adminNew.getFaturaList().add(fatura);
                adminNew = em.merge(adminNew);
            }
            if (tipoDevolucaoIdOld != null && !tipoDevolucaoIdOld.equals(tipoDevolucaoIdNew)) {
                tipoDevolucaoIdOld.getFaturaList().remove(fatura);
                tipoDevolucaoIdOld = em.merge(tipoDevolucaoIdOld);
            }
            if (tipoDevolucaoIdNew != null && !tipoDevolucaoIdNew.equals(tipoDevolucaoIdOld)) {
                tipoDevolucaoIdNew.getFaturaList().add(fatura);
                tipoDevolucaoIdNew = em.merge(tipoDevolucaoIdNew);
            }
            for (CaixaMovement caixaMovementListOldCaixaMovement : caixaMovementListOld) {
                if (!caixaMovementListNew.contains(caixaMovementListOldCaixaMovement)) {
                    caixaMovementListOldCaixaMovement.setFaturaId(null);
                    caixaMovementListOldCaixaMovement = em.merge(caixaMovementListOldCaixaMovement);
                }
            }
            for (CaixaMovement caixaMovementListNewCaixaMovement : caixaMovementListNew) {
                if (!caixaMovementListOld.contains(caixaMovementListNewCaixaMovement)) {
                    Fatura oldFaturaIdOfCaixaMovementListNewCaixaMovement = caixaMovementListNewCaixaMovement.getFaturaId();
                    caixaMovementListNewCaixaMovement.setFaturaId(fatura);
                    caixaMovementListNewCaixaMovement = em.merge(caixaMovementListNewCaixaMovement);
                    if (oldFaturaIdOfCaixaMovementListNewCaixaMovement != null && !oldFaturaIdOfCaixaMovementListNewCaixaMovement.equals(fatura)) {
                        oldFaturaIdOfCaixaMovementListNewCaixaMovement.getCaixaMovementList().remove(caixaMovementListNewCaixaMovement);
                        oldFaturaIdOfCaixaMovementListNewCaixaMovement = em.merge(oldFaturaIdOfCaixaMovementListNewCaixaMovement);
                    }
                }
            }
            for (Item itemListNewItem : itemListNew) {
                if (!itemListOld.contains(itemListNewItem)) {
                    Fatura oldFaturaIdOfItemListNewItem = itemListNewItem.getFaturaId();
                    itemListNewItem.setFaturaId(fatura);
                    itemListNewItem = em.merge(itemListNewItem);
                    if (oldFaturaIdOfItemListNewItem != null && !oldFaturaIdOfItemListNewItem.equals(fatura)) {
                        oldFaturaIdOfItemListNewItem.getItemList().remove(itemListNewItem);
                        oldFaturaIdOfItemListNewItem = em.merge(oldFaturaIdOfItemListNewItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = fatura.getId();
                if (findFatura(id) == null) {
                    throw new NonexistentEntityException("The fatura with id " + id + " no longer exists.");
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
            Fatura fatura;
            try {
                fatura = em.getReference(Fatura.class, id);
                fatura.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fatura with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Item> itemListOrphanCheck = fatura.getItemList();
            for (Item itemListOrphanCheckItem : itemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Fatura (" + fatura + ") cannot be destroyed since the Item " + itemListOrphanCheckItem + " in its itemList field has a non-nullable faturaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Autentication funcionarioId = fatura.getFuncionarioId();
            if (funcionarioId != null) {
                funcionarioId.getFaturaList().remove(fatura);
                funcionarioId = em.merge(funcionarioId);
            }
            Client clientId = fatura.getClientId();
            if (clientId != null) {
                clientId.getFaturaList().remove(fatura);
                clientId = em.merge(clientId);
            }
            Autentication admin = fatura.getAdmin();
            if (admin != null) {
                admin.getFaturaList().remove(fatura);
                admin = em.merge(admin);
            }
            TipoDevolucao tipoDevolucaoId = fatura.getTipoDevolucaoId();
            if (tipoDevolucaoId != null) {
                tipoDevolucaoId.getFaturaList().remove(fatura);
                tipoDevolucaoId = em.merge(tipoDevolucaoId);
            }
            List<CaixaMovement> caixaMovementList = fatura.getCaixaMovementList();
            for (CaixaMovement caixaMovementListCaixaMovement : caixaMovementList) {
                caixaMovementListCaixaMovement.setFaturaId(null);
                caixaMovementListCaixaMovement = em.merge(caixaMovementListCaixaMovement);
            }
            em.remove(fatura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fatura> findFaturaEntities() {
        return findFaturaEntities(true, -1, -1);
    }

    public List<Fatura> findFaturaEntities(int maxResults, int firstResult) {
        return findFaturaEntities(false, maxResults, firstResult);
    }

    private List<Fatura> findFaturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fatura.class));
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

    public Fatura findFatura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fatura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFaturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fatura> rt = cq.from(Fatura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
