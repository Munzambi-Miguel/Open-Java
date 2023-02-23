/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.java.controller;

import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Client;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.developer.java.entity.Tipocliente;
import com.developer.java.entity.Entity;
import com.developer.java.entity.Fatura;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class ClientJpaController implements Serializable {

    public ClientJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Client client) {
        if (client.getFaturaList() == null) {
            client.setFaturaList(new ArrayList<Fatura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipocliente tipoClienteid = client.getTipoClienteid();
            if (tipoClienteid != null) {
                tipoClienteid = em.getReference(tipoClienteid.getClass(), tipoClienteid.getId());
                client.setTipoClienteid(tipoClienteid);
            }
            Entity entityId = client.getEntityId();
            if (entityId != null) {
                entityId = em.getReference(entityId.getClass(), entityId.getId());
                client.setEntityId(entityId);
            }
            List<Fatura> attachedFaturaList = new ArrayList<Fatura>();
            for (Fatura faturaListFaturaToAttach : client.getFaturaList()) {
                faturaListFaturaToAttach = em.getReference(faturaListFaturaToAttach.getClass(), faturaListFaturaToAttach.getId());
                attachedFaturaList.add(faturaListFaturaToAttach);
            }
            client.setFaturaList(attachedFaturaList);
            em.persist(client);
            if (tipoClienteid != null) {
                tipoClienteid.getClientList().add(client);
                tipoClienteid = em.merge(tipoClienteid);
            }
            if (entityId != null) {
                entityId.getClientList().add(client);
                entityId = em.merge(entityId);
            }
            for (Fatura faturaListFatura : client.getFaturaList()) {
                Client oldClientIdOfFaturaListFatura = faturaListFatura.getClientId();
                faturaListFatura.setClientId(client);
                faturaListFatura = em.merge(faturaListFatura);
                if (oldClientIdOfFaturaListFatura != null) {
                    oldClientIdOfFaturaListFatura.getFaturaList().remove(faturaListFatura);
                    oldClientIdOfFaturaListFatura = em.merge(oldClientIdOfFaturaListFatura);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Client client) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client persistentClient = em.find(Client.class, client.getId());
            Tipocliente tipoClienteidOld = persistentClient.getTipoClienteid();
            Tipocliente tipoClienteidNew = client.getTipoClienteid();
            Entity entityIdOld = persistentClient.getEntityId();
            Entity entityIdNew = client.getEntityId();
            List<Fatura> faturaListOld = persistentClient.getFaturaList();
            List<Fatura> faturaListNew = client.getFaturaList();
            if (tipoClienteidNew != null) {
                tipoClienteidNew = em.getReference(tipoClienteidNew.getClass(), tipoClienteidNew.getId());
                client.setTipoClienteid(tipoClienteidNew);
            }
            if (entityIdNew != null) {
                entityIdNew = em.getReference(entityIdNew.getClass(), entityIdNew.getId());
                client.setEntityId(entityIdNew);
            }
            List<Fatura> attachedFaturaListNew = new ArrayList<Fatura>();
            for (Fatura faturaListNewFaturaToAttach : faturaListNew) {
                faturaListNewFaturaToAttach = em.getReference(faturaListNewFaturaToAttach.getClass(), faturaListNewFaturaToAttach.getId());
                attachedFaturaListNew.add(faturaListNewFaturaToAttach);
            }
            faturaListNew = attachedFaturaListNew;
            client.setFaturaList(faturaListNew);
            client = em.merge(client);
            if (tipoClienteidOld != null && !tipoClienteidOld.equals(tipoClienteidNew)) {
                tipoClienteidOld.getClientList().remove(client);
                tipoClienteidOld = em.merge(tipoClienteidOld);
            }
            if (tipoClienteidNew != null && !tipoClienteidNew.equals(tipoClienteidOld)) {
                tipoClienteidNew.getClientList().add(client);
                tipoClienteidNew = em.merge(tipoClienteidNew);
            }
            if (entityIdOld != null && !entityIdOld.equals(entityIdNew)) {
                entityIdOld.getClientList().remove(client);
                entityIdOld = em.merge(entityIdOld);
            }
            if (entityIdNew != null && !entityIdNew.equals(entityIdOld)) {
                entityIdNew.getClientList().add(client);
                entityIdNew = em.merge(entityIdNew);
            }
            for (Fatura faturaListOldFatura : faturaListOld) {
                if (!faturaListNew.contains(faturaListOldFatura)) {
                    faturaListOldFatura.setClientId(null);
                    faturaListOldFatura = em.merge(faturaListOldFatura);
                }
            }
            for (Fatura faturaListNewFatura : faturaListNew) {
                if (!faturaListOld.contains(faturaListNewFatura)) {
                    Client oldClientIdOfFaturaListNewFatura = faturaListNewFatura.getClientId();
                    faturaListNewFatura.setClientId(client);
                    faturaListNewFatura = em.merge(faturaListNewFatura);
                    if (oldClientIdOfFaturaListNewFatura != null && !oldClientIdOfFaturaListNewFatura.equals(client)) {
                        oldClientIdOfFaturaListNewFatura.getFaturaList().remove(faturaListNewFatura);
                        oldClientIdOfFaturaListNewFatura = em.merge(oldClientIdOfFaturaListNewFatura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = client.getId();
                if (findClient(id) == null) {
                    throw new NonexistentEntityException("The client with id " + id + " no longer exists.");
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
            Client client;
            try {
                client = em.getReference(Client.class, id);
                client.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The client with id " + id + " no longer exists.", enfe);
            }
            Tipocliente tipoClienteid = client.getTipoClienteid();
            if (tipoClienteid != null) {
                tipoClienteid.getClientList().remove(client);
                tipoClienteid = em.merge(tipoClienteid);
            }
            Entity entityId = client.getEntityId();
            if (entityId != null) {
                entityId.getClientList().remove(client);
                entityId = em.merge(entityId);
            }
            List<Fatura> faturaList = client.getFaturaList();
            for (Fatura faturaListFatura : faturaList) {
                faturaListFatura.setClientId(null);
                faturaListFatura = em.merge(faturaListFatura);
            }
            em.remove(client);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Client> findClientEntities() {
        return findClientEntities(true, -1, -1);
    }

    public List<Client> findClientEntities(int maxResults, int firstResult) {
        return findClientEntities(false, maxResults, firstResult);
    }

    private List<Client> findClientEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Client.class));
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

    public Client findClient(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Client> rt = cq.from(Client.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
