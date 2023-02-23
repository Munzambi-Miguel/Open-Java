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
import com.developer.java.entity.City;
import com.developer.java.entity.Pais;
import java.util.ArrayList;
import java.util.List;
import com.developer.java.entity.Product;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class PaisJpaController implements Serializable {

    public PaisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pais pais) {
        if (pais.getCityList() == null) {
            pais.setCityList(new ArrayList<City>());
        }
        if (pais.getProductList() == null) {
            pais.setProductList(new ArrayList<Product>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<City> attachedCityList = new ArrayList<City>();
            for (City cityListCityToAttach : pais.getCityList()) {
                cityListCityToAttach = em.getReference(cityListCityToAttach.getClass(), cityListCityToAttach.getId());
                attachedCityList.add(cityListCityToAttach);
            }
            pais.setCityList(attachedCityList);
            List<Product> attachedProductList = new ArrayList<Product>();
            for (Product productListProductToAttach : pais.getProductList()) {
                productListProductToAttach = em.getReference(productListProductToAttach.getClass(), productListProductToAttach.getId());
                attachedProductList.add(productListProductToAttach);
            }
            pais.setProductList(attachedProductList);
            em.persist(pais);
            for (City cityListCity : pais.getCityList()) {
                Pais oldPaisIdOfCityListCity = cityListCity.getPaisId();
                cityListCity.setPaisId(pais);
                cityListCity = em.merge(cityListCity);
                if (oldPaisIdOfCityListCity != null) {
                    oldPaisIdOfCityListCity.getCityList().remove(cityListCity);
                    oldPaisIdOfCityListCity = em.merge(oldPaisIdOfCityListCity);
                }
            }
            for (Product productListProduct : pais.getProductList()) {
                Pais oldPaisIdOfProductListProduct = productListProduct.getPaisId();
                productListProduct.setPaisId(pais);
                productListProduct = em.merge(productListProduct);
                if (oldPaisIdOfProductListProduct != null) {
                    oldPaisIdOfProductListProduct.getProductList().remove(productListProduct);
                    oldPaisIdOfProductListProduct = em.merge(oldPaisIdOfProductListProduct);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pais pais) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais persistentPais = em.find(Pais.class, pais.getId());
            List<City> cityListOld = persistentPais.getCityList();
            List<City> cityListNew = pais.getCityList();
            List<Product> productListOld = persistentPais.getProductList();
            List<Product> productListNew = pais.getProductList();
            List<String> illegalOrphanMessages = null;
            for (City cityListOldCity : cityListOld) {
                if (!cityListNew.contains(cityListOldCity)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain City " + cityListOldCity + " since its paisId field is not nullable.");
                }
            }
            for (Product productListOldProduct : productListOld) {
                if (!productListNew.contains(productListOldProduct)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Product " + productListOldProduct + " since its paisId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<City> attachedCityListNew = new ArrayList<City>();
            for (City cityListNewCityToAttach : cityListNew) {
                cityListNewCityToAttach = em.getReference(cityListNewCityToAttach.getClass(), cityListNewCityToAttach.getId());
                attachedCityListNew.add(cityListNewCityToAttach);
            }
            cityListNew = attachedCityListNew;
            pais.setCityList(cityListNew);
            List<Product> attachedProductListNew = new ArrayList<Product>();
            for (Product productListNewProductToAttach : productListNew) {
                productListNewProductToAttach = em.getReference(productListNewProductToAttach.getClass(), productListNewProductToAttach.getId());
                attachedProductListNew.add(productListNewProductToAttach);
            }
            productListNew = attachedProductListNew;
            pais.setProductList(productListNew);
            pais = em.merge(pais);
            for (City cityListNewCity : cityListNew) {
                if (!cityListOld.contains(cityListNewCity)) {
                    Pais oldPaisIdOfCityListNewCity = cityListNewCity.getPaisId();
                    cityListNewCity.setPaisId(pais);
                    cityListNewCity = em.merge(cityListNewCity);
                    if (oldPaisIdOfCityListNewCity != null && !oldPaisIdOfCityListNewCity.equals(pais)) {
                        oldPaisIdOfCityListNewCity.getCityList().remove(cityListNewCity);
                        oldPaisIdOfCityListNewCity = em.merge(oldPaisIdOfCityListNewCity);
                    }
                }
            }
            for (Product productListNewProduct : productListNew) {
                if (!productListOld.contains(productListNewProduct)) {
                    Pais oldPaisIdOfProductListNewProduct = productListNewProduct.getPaisId();
                    productListNewProduct.setPaisId(pais);
                    productListNewProduct = em.merge(productListNewProduct);
                    if (oldPaisIdOfProductListNewProduct != null && !oldPaisIdOfProductListNewProduct.equals(pais)) {
                        oldPaisIdOfProductListNewProduct.getProductList().remove(productListNewProduct);
                        oldPaisIdOfProductListNewProduct = em.merge(oldPaisIdOfProductListNewProduct);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pais.getId();
                if (findPais(id) == null) {
                    throw new NonexistentEntityException("The pais with id " + id + " no longer exists.");
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
            Pais pais;
            try {
                pais = em.getReference(Pais.class, id);
                pais.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pais with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<City> cityListOrphanCheck = pais.getCityList();
            for (City cityListOrphanCheckCity : cityListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pais (" + pais + ") cannot be destroyed since the City " + cityListOrphanCheckCity + " in its cityList field has a non-nullable paisId field.");
            }
            List<Product> productListOrphanCheck = pais.getProductList();
            for (Product productListOrphanCheckProduct : productListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pais (" + pais + ") cannot be destroyed since the Product " + productListOrphanCheckProduct + " in its productList field has a non-nullable paisId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pais);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pais> findPaisEntities() {
        return findPaisEntities(true, -1, -1);
    }

    public List<Pais> findPaisEntities(int maxResults, int firstResult) {
        return findPaisEntities(false, maxResults, firstResult);
    }

    private List<Pais> findPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pais.class));
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

    public Pais findPais(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pais> rt = cq.from(Pais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
