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
import com.developer.java.entity.Pais;
import com.developer.java.entity.Address;
import com.developer.java.entity.City;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class CityJpaController implements Serializable {

    public CityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(City city) {
        if (city.getAddressList() == null) {
            city.setAddressList(new ArrayList<Address>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais paisId = city.getPaisId();
            if (paisId != null) {
                paisId = em.getReference(paisId.getClass(), paisId.getId());
                city.setPaisId(paisId);
            }
            List<Address> attachedAddressList = new ArrayList<Address>();
            for (Address addressListAddressToAttach : city.getAddressList()) {
                addressListAddressToAttach = em.getReference(addressListAddressToAttach.getClass(), addressListAddressToAttach.getId());
                attachedAddressList.add(addressListAddressToAttach);
            }
            city.setAddressList(attachedAddressList);
            em.persist(city);
            if (paisId != null) {
                paisId.getCityList().add(city);
                paisId = em.merge(paisId);
            }
            for (Address addressListAddress : city.getAddressList()) {
                City oldCityidOfAddressListAddress = addressListAddress.getCityid();
                addressListAddress.setCityid(city);
                addressListAddress = em.merge(addressListAddress);
                if (oldCityidOfAddressListAddress != null) {
                    oldCityidOfAddressListAddress.getAddressList().remove(addressListAddress);
                    oldCityidOfAddressListAddress = em.merge(oldCityidOfAddressListAddress);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(City city) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            City persistentCity = em.find(City.class, city.getId());
            Pais paisIdOld = persistentCity.getPaisId();
            Pais paisIdNew = city.getPaisId();
            List<Address> addressListOld = persistentCity.getAddressList();
            List<Address> addressListNew = city.getAddressList();
            List<String> illegalOrphanMessages = null;
            for (Address addressListOldAddress : addressListOld) {
                if (!addressListNew.contains(addressListOldAddress)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Address " + addressListOldAddress + " since its cityid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (paisIdNew != null) {
                paisIdNew = em.getReference(paisIdNew.getClass(), paisIdNew.getId());
                city.setPaisId(paisIdNew);
            }
            List<Address> attachedAddressListNew = new ArrayList<Address>();
            for (Address addressListNewAddressToAttach : addressListNew) {
                addressListNewAddressToAttach = em.getReference(addressListNewAddressToAttach.getClass(), addressListNewAddressToAttach.getId());
                attachedAddressListNew.add(addressListNewAddressToAttach);
            }
            addressListNew = attachedAddressListNew;
            city.setAddressList(addressListNew);
            city = em.merge(city);
            if (paisIdOld != null && !paisIdOld.equals(paisIdNew)) {
                paisIdOld.getCityList().remove(city);
                paisIdOld = em.merge(paisIdOld);
            }
            if (paisIdNew != null && !paisIdNew.equals(paisIdOld)) {
                paisIdNew.getCityList().add(city);
                paisIdNew = em.merge(paisIdNew);
            }
            for (Address addressListNewAddress : addressListNew) {
                if (!addressListOld.contains(addressListNewAddress)) {
                    City oldCityidOfAddressListNewAddress = addressListNewAddress.getCityid();
                    addressListNewAddress.setCityid(city);
                    addressListNewAddress = em.merge(addressListNewAddress);
                    if (oldCityidOfAddressListNewAddress != null && !oldCityidOfAddressListNewAddress.equals(city)) {
                        oldCityidOfAddressListNewAddress.getAddressList().remove(addressListNewAddress);
                        oldCityidOfAddressListNewAddress = em.merge(oldCityidOfAddressListNewAddress);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = city.getId();
                if (findCity(id) == null) {
                    throw new NonexistentEntityException("The city with id " + id + " no longer exists.");
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
            City city;
            try {
                city = em.getReference(City.class, id);
                city.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The city with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Address> addressListOrphanCheck = city.getAddressList();
            for (Address addressListOrphanCheckAddress : addressListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This City (" + city + ") cannot be destroyed since the Address " + addressListOrphanCheckAddress + " in its addressList field has a non-nullable cityid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pais paisId = city.getPaisId();
            if (paisId != null) {
                paisId.getCityList().remove(city);
                paisId = em.merge(paisId);
            }
            em.remove(city);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<City> findCityEntities() {
        return findCityEntities(true, -1, -1);
    }

    public List<City> findCityEntities(int maxResults, int firstResult) {
        return findCityEntities(false, maxResults, firstResult);
    }

    private List<City> findCityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(City.class));
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

    public City findCity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(City.class, id);
        } finally {
            em.close();
        }
    }

    public int getCityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<City> rt = cq.from(City.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
