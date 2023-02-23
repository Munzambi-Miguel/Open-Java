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
import com.developer.java.entity.Client;
import com.developer.java.entity.Tipocliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class TipoclienteJpaController implements Serializable {

    public TipoclienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipocliente tipocliente) {
        if (tipocliente.getClientList() == null) {
            tipocliente.setClientList(new ArrayList<Client>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Client> attachedClientList = new ArrayList<Client>();
            for (Client clientListClientToAttach : tipocliente.getClientList()) {
                clientListClientToAttach = em.getReference(clientListClientToAttach.getClass(), clientListClientToAttach.getId());
                attachedClientList.add(clientListClientToAttach);
            }
            tipocliente.setClientList(attachedClientList);
            em.persist(tipocliente);
            for (Client clientListClient : tipocliente.getClientList()) {
                Tipocliente oldTipoClienteidOfClientListClient = clientListClient.getTipoClienteid();
                clientListClient.setTipoClienteid(tipocliente);
                clientListClient = em.merge(clientListClient);
                if (oldTipoClienteidOfClientListClient != null) {
                    oldTipoClienteidOfClientListClient.getClientList().remove(clientListClient);
                    oldTipoClienteidOfClientListClient = em.merge(oldTipoClienteidOfClientListClient);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipocliente tipocliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipocliente persistentTipocliente = em.find(Tipocliente.class, tipocliente.getId());
            List<Client> clientListOld = persistentTipocliente.getClientList();
            List<Client> clientListNew = tipocliente.getClientList();
            List<String> illegalOrphanMessages = null;
            for (Client clientListOldClient : clientListOld) {
                if (!clientListNew.contains(clientListOldClient)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Client " + clientListOldClient + " since its tipoClienteid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Client> attachedClientListNew = new ArrayList<Client>();
            for (Client clientListNewClientToAttach : clientListNew) {
                clientListNewClientToAttach = em.getReference(clientListNewClientToAttach.getClass(), clientListNewClientToAttach.getId());
                attachedClientListNew.add(clientListNewClientToAttach);
            }
            clientListNew = attachedClientListNew;
            tipocliente.setClientList(clientListNew);
            tipocliente = em.merge(tipocliente);
            for (Client clientListNewClient : clientListNew) {
                if (!clientListOld.contains(clientListNewClient)) {
                    Tipocliente oldTipoClienteidOfClientListNewClient = clientListNewClient.getTipoClienteid();
                    clientListNewClient.setTipoClienteid(tipocliente);
                    clientListNewClient = em.merge(clientListNewClient);
                    if (oldTipoClienteidOfClientListNewClient != null && !oldTipoClienteidOfClientListNewClient.equals(tipocliente)) {
                        oldTipoClienteidOfClientListNewClient.getClientList().remove(clientListNewClient);
                        oldTipoClienteidOfClientListNewClient = em.merge(oldTipoClienteidOfClientListNewClient);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipocliente.getId();
                if (findTipocliente(id) == null) {
                    throw new NonexistentEntityException("The tipocliente with id " + id + " no longer exists.");
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
            Tipocliente tipocliente;
            try {
                tipocliente = em.getReference(Tipocliente.class, id);
                tipocliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipocliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Client> clientListOrphanCheck = tipocliente.getClientList();
            for (Client clientListOrphanCheckClient : clientListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipocliente (" + tipocliente + ") cannot be destroyed since the Client " + clientListOrphanCheckClient + " in its clientList field has a non-nullable tipoClienteid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipocliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipocliente> findTipoclienteEntities() {
        return findTipoclienteEntities(true, -1, -1);
    }

    public List<Tipocliente> findTipoclienteEntities(int maxResults, int firstResult) {
        return findTipoclienteEntities(false, maxResults, firstResult);
    }

    private List<Tipocliente> findTipoclienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipocliente.class));
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

    public Tipocliente findTipocliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipocliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoclienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipocliente> rt = cq.from(Tipocliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
