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
import com.developer.java.entity.Nivel3;
import com.developer.java.entity.Pais;
import com.developer.java.entity.Produttype;
import com.developer.java.entity.Unidade;
import com.developer.java.entity.Preco;
import java.util.ArrayList;
import java.util.List;
import com.developer.java.entity.Item;
import com.developer.java.entity.Photo;
import com.developer.java.entity.Movement;
import com.developer.java.entity.Product;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class ProductJpaController implements Serializable {

    public ProductJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Product product) {
        if (product.getPrecoList() == null) {
            product.setPrecoList(new ArrayList<Preco>());
        }
        if (product.getItemList() == null) {
            product.setItemList(new ArrayList<Item>());
        }
        if (product.getPhotoList() == null) {
            product.setPhotoList(new ArrayList<Photo>());
        }
        if (product.getMovementList() == null) {
            product.setMovementList(new ArrayList<Movement>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Autentication autenticationId = product.getAutenticationId();
            if (autenticationId != null) {
                autenticationId = em.getReference(autenticationId.getClass(), autenticationId.getId());
                product.setAutenticationId(autenticationId);
            }
            Nivel3 nivel3Id = product.getNivel3Id();
            if (nivel3Id != null) {
                nivel3Id = em.getReference(nivel3Id.getClass(), nivel3Id.getId());
                product.setNivel3Id(nivel3Id);
            }
            Pais paisId = product.getPaisId();
            if (paisId != null) {
                paisId = em.getReference(paisId.getClass(), paisId.getId());
                product.setPaisId(paisId);
            }
            Produttype produtTypeid = product.getProdutTypeid();
            if (produtTypeid != null) {
                produtTypeid = em.getReference(produtTypeid.getClass(), produtTypeid.getId());
                product.setProdutTypeid(produtTypeid);
            }
            Unidade unidadeId = product.getUnidadeId();
            if (unidadeId != null) {
                unidadeId = em.getReference(unidadeId.getClass(), unidadeId.getId());
                product.setUnidadeId(unidadeId);
            }
            List<Preco> attachedPrecoList = new ArrayList<Preco>();
            for (Preco precoListPrecoToAttach : product.getPrecoList()) {
                precoListPrecoToAttach = em.getReference(precoListPrecoToAttach.getClass(), precoListPrecoToAttach.getId());
                attachedPrecoList.add(precoListPrecoToAttach);
            }
            product.setPrecoList(attachedPrecoList);
            List<Item> attachedItemList = new ArrayList<Item>();
            for (Item itemListItemToAttach : product.getItemList()) {
                itemListItemToAttach = em.getReference(itemListItemToAttach.getClass(), itemListItemToAttach.getId());
                attachedItemList.add(itemListItemToAttach);
            }
            product.setItemList(attachedItemList);
            List<Photo> attachedPhotoList = new ArrayList<Photo>();
            for (Photo photoListPhotoToAttach : product.getPhotoList()) {
                photoListPhotoToAttach = em.getReference(photoListPhotoToAttach.getClass(), photoListPhotoToAttach.getId());
                attachedPhotoList.add(photoListPhotoToAttach);
            }
            product.setPhotoList(attachedPhotoList);
            List<Movement> attachedMovementList = new ArrayList<Movement>();
            for (Movement movementListMovementToAttach : product.getMovementList()) {
                movementListMovementToAttach = em.getReference(movementListMovementToAttach.getClass(), movementListMovementToAttach.getId());
                attachedMovementList.add(movementListMovementToAttach);
            }
            product.setMovementList(attachedMovementList);
            em.persist(product);
            if (autenticationId != null) {
                autenticationId.getProductList().add(product);
                autenticationId = em.merge(autenticationId);
            }
            if (nivel3Id != null) {
                nivel3Id.getProductList().add(product);
                nivel3Id = em.merge(nivel3Id);
            }
            if (paisId != null) {
                paisId.getProductList().add(product);
                paisId = em.merge(paisId);
            }
            if (produtTypeid != null) {
                produtTypeid.getProductList().add(product);
                produtTypeid = em.merge(produtTypeid);
            }
            if (unidadeId != null) {
                unidadeId.getProductList().add(product);
                unidadeId = em.merge(unidadeId);
            }
            for (Preco precoListPreco : product.getPrecoList()) {
                Product oldProductIdOfPrecoListPreco = precoListPreco.getProductId();
                precoListPreco.setProductId(product);
                precoListPreco = em.merge(precoListPreco);
                if (oldProductIdOfPrecoListPreco != null) {
                    oldProductIdOfPrecoListPreco.getPrecoList().remove(precoListPreco);
                    oldProductIdOfPrecoListPreco = em.merge(oldProductIdOfPrecoListPreco);
                }
            }
            for (Item itemListItem : product.getItemList()) {
                Product oldProductIdOfItemListItem = itemListItem.getProductId();
                itemListItem.setProductId(product);
                itemListItem = em.merge(itemListItem);
                if (oldProductIdOfItemListItem != null) {
                    oldProductIdOfItemListItem.getItemList().remove(itemListItem);
                    oldProductIdOfItemListItem = em.merge(oldProductIdOfItemListItem);
                }
            }
            for (Photo photoListPhoto : product.getPhotoList()) {
                Product oldProductIdOfPhotoListPhoto = photoListPhoto.getProductId();
                photoListPhoto.setProductId(product);
                photoListPhoto = em.merge(photoListPhoto);
                if (oldProductIdOfPhotoListPhoto != null) {
                    oldProductIdOfPhotoListPhoto.getPhotoList().remove(photoListPhoto);
                    oldProductIdOfPhotoListPhoto = em.merge(oldProductIdOfPhotoListPhoto);
                }
            }
            for (Movement movementListMovement : product.getMovementList()) {
                Product oldProductIdOfMovementListMovement = movementListMovement.getProductId();
                movementListMovement.setProductId(product);
                movementListMovement = em.merge(movementListMovement);
                if (oldProductIdOfMovementListMovement != null) {
                    oldProductIdOfMovementListMovement.getMovementList().remove(movementListMovement);
                    oldProductIdOfMovementListMovement = em.merge(oldProductIdOfMovementListMovement);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Product product) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Product persistentProduct = em.find(Product.class, product.getId());
            Autentication autenticationIdOld = persistentProduct.getAutenticationId();
            Autentication autenticationIdNew = product.getAutenticationId();
            Nivel3 nivel3IdOld = persistentProduct.getNivel3Id();
            Nivel3 nivel3IdNew = product.getNivel3Id();
            Pais paisIdOld = persistentProduct.getPaisId();
            Pais paisIdNew = product.getPaisId();
            Produttype produtTypeidOld = persistentProduct.getProdutTypeid();
            Produttype produtTypeidNew = product.getProdutTypeid();
            Unidade unidadeIdOld = persistentProduct.getUnidadeId();
            Unidade unidadeIdNew = product.getUnidadeId();
            List<Preco> precoListOld = persistentProduct.getPrecoList();
            List<Preco> precoListNew = product.getPrecoList();
            List<Item> itemListOld = persistentProduct.getItemList();
            List<Item> itemListNew = product.getItemList();
            List<Photo> photoListOld = persistentProduct.getPhotoList();
            List<Photo> photoListNew = product.getPhotoList();
            List<Movement> movementListOld = persistentProduct.getMovementList();
            List<Movement> movementListNew = product.getMovementList();
            List<String> illegalOrphanMessages = null;
            for (Preco precoListOldPreco : precoListOld) {
                if (!precoListNew.contains(precoListOldPreco)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Preco " + precoListOldPreco + " since its productId field is not nullable.");
                }
            }
            for (Item itemListOldItem : itemListOld) {
                if (!itemListNew.contains(itemListOldItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Item " + itemListOldItem + " since its productId field is not nullable.");
                }
            }
            for (Movement movementListOldMovement : movementListOld) {
                if (!movementListNew.contains(movementListOldMovement)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Movement " + movementListOldMovement + " since its productId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (autenticationIdNew != null) {
                autenticationIdNew = em.getReference(autenticationIdNew.getClass(), autenticationIdNew.getId());
                product.setAutenticationId(autenticationIdNew);
            }
            if (nivel3IdNew != null) {
                nivel3IdNew = em.getReference(nivel3IdNew.getClass(), nivel3IdNew.getId());
                product.setNivel3Id(nivel3IdNew);
            }
            if (paisIdNew != null) {
                paisIdNew = em.getReference(paisIdNew.getClass(), paisIdNew.getId());
                product.setPaisId(paisIdNew);
            }
            if (produtTypeidNew != null) {
                produtTypeidNew = em.getReference(produtTypeidNew.getClass(), produtTypeidNew.getId());
                product.setProdutTypeid(produtTypeidNew);
            }
            if (unidadeIdNew != null) {
                unidadeIdNew = em.getReference(unidadeIdNew.getClass(), unidadeIdNew.getId());
                product.setUnidadeId(unidadeIdNew);
            }
            List<Preco> attachedPrecoListNew = new ArrayList<Preco>();
            for (Preco precoListNewPrecoToAttach : precoListNew) {
                precoListNewPrecoToAttach = em.getReference(precoListNewPrecoToAttach.getClass(), precoListNewPrecoToAttach.getId());
                attachedPrecoListNew.add(precoListNewPrecoToAttach);
            }
            precoListNew = attachedPrecoListNew;
            product.setPrecoList(precoListNew);
            List<Item> attachedItemListNew = new ArrayList<Item>();
            for (Item itemListNewItemToAttach : itemListNew) {
                itemListNewItemToAttach = em.getReference(itemListNewItemToAttach.getClass(), itemListNewItemToAttach.getId());
                attachedItemListNew.add(itemListNewItemToAttach);
            }
            itemListNew = attachedItemListNew;
            product.setItemList(itemListNew);
            List<Photo> attachedPhotoListNew = new ArrayList<Photo>();
            for (Photo photoListNewPhotoToAttach : photoListNew) {
                photoListNewPhotoToAttach = em.getReference(photoListNewPhotoToAttach.getClass(), photoListNewPhotoToAttach.getId());
                attachedPhotoListNew.add(photoListNewPhotoToAttach);
            }
            photoListNew = attachedPhotoListNew;
            product.setPhotoList(photoListNew);
            List<Movement> attachedMovementListNew = new ArrayList<Movement>();
            for (Movement movementListNewMovementToAttach : movementListNew) {
                movementListNewMovementToAttach = em.getReference(movementListNewMovementToAttach.getClass(), movementListNewMovementToAttach.getId());
                attachedMovementListNew.add(movementListNewMovementToAttach);
            }
            movementListNew = attachedMovementListNew;
            product.setMovementList(movementListNew);
            product = em.merge(product);
            if (autenticationIdOld != null && !autenticationIdOld.equals(autenticationIdNew)) {
                autenticationIdOld.getProductList().remove(product);
                autenticationIdOld = em.merge(autenticationIdOld);
            }
            if (autenticationIdNew != null && !autenticationIdNew.equals(autenticationIdOld)) {
                autenticationIdNew.getProductList().add(product);
                autenticationIdNew = em.merge(autenticationIdNew);
            }
            if (nivel3IdOld != null && !nivel3IdOld.equals(nivel3IdNew)) {
                nivel3IdOld.getProductList().remove(product);
                nivel3IdOld = em.merge(nivel3IdOld);
            }
            if (nivel3IdNew != null && !nivel3IdNew.equals(nivel3IdOld)) {
                nivel3IdNew.getProductList().add(product);
                nivel3IdNew = em.merge(nivel3IdNew);
            }
            if (paisIdOld != null && !paisIdOld.equals(paisIdNew)) {
                paisIdOld.getProductList().remove(product);
                paisIdOld = em.merge(paisIdOld);
            }
            if (paisIdNew != null && !paisIdNew.equals(paisIdOld)) {
                paisIdNew.getProductList().add(product);
                paisIdNew = em.merge(paisIdNew);
            }
            if (produtTypeidOld != null && !produtTypeidOld.equals(produtTypeidNew)) {
                produtTypeidOld.getProductList().remove(product);
                produtTypeidOld = em.merge(produtTypeidOld);
            }
            if (produtTypeidNew != null && !produtTypeidNew.equals(produtTypeidOld)) {
                produtTypeidNew.getProductList().add(product);
                produtTypeidNew = em.merge(produtTypeidNew);
            }
            if (unidadeIdOld != null && !unidadeIdOld.equals(unidadeIdNew)) {
                unidadeIdOld.getProductList().remove(product);
                unidadeIdOld = em.merge(unidadeIdOld);
            }
            if (unidadeIdNew != null && !unidadeIdNew.equals(unidadeIdOld)) {
                unidadeIdNew.getProductList().add(product);
                unidadeIdNew = em.merge(unidadeIdNew);
            }
            for (Preco precoListNewPreco : precoListNew) {
                if (!precoListOld.contains(precoListNewPreco)) {
                    Product oldProductIdOfPrecoListNewPreco = precoListNewPreco.getProductId();
                    precoListNewPreco.setProductId(product);
                    precoListNewPreco = em.merge(precoListNewPreco);
                    if (oldProductIdOfPrecoListNewPreco != null && !oldProductIdOfPrecoListNewPreco.equals(product)) {
                        oldProductIdOfPrecoListNewPreco.getPrecoList().remove(precoListNewPreco);
                        oldProductIdOfPrecoListNewPreco = em.merge(oldProductIdOfPrecoListNewPreco);
                    }
                }
            }
            for (Item itemListNewItem : itemListNew) {
                if (!itemListOld.contains(itemListNewItem)) {
                    Product oldProductIdOfItemListNewItem = itemListNewItem.getProductId();
                    itemListNewItem.setProductId(product);
                    itemListNewItem = em.merge(itemListNewItem);
                    if (oldProductIdOfItemListNewItem != null && !oldProductIdOfItemListNewItem.equals(product)) {
                        oldProductIdOfItemListNewItem.getItemList().remove(itemListNewItem);
                        oldProductIdOfItemListNewItem = em.merge(oldProductIdOfItemListNewItem);
                    }
                }
            }
            for (Photo photoListOldPhoto : photoListOld) {
                if (!photoListNew.contains(photoListOldPhoto)) {
                    photoListOldPhoto.setProductId(null);
                    photoListOldPhoto = em.merge(photoListOldPhoto);
                }
            }
            for (Photo photoListNewPhoto : photoListNew) {
                if (!photoListOld.contains(photoListNewPhoto)) {
                    Product oldProductIdOfPhotoListNewPhoto = photoListNewPhoto.getProductId();
                    photoListNewPhoto.setProductId(product);
                    photoListNewPhoto = em.merge(photoListNewPhoto);
                    if (oldProductIdOfPhotoListNewPhoto != null && !oldProductIdOfPhotoListNewPhoto.equals(product)) {
                        oldProductIdOfPhotoListNewPhoto.getPhotoList().remove(photoListNewPhoto);
                        oldProductIdOfPhotoListNewPhoto = em.merge(oldProductIdOfPhotoListNewPhoto);
                    }
                }
            }
            for (Movement movementListNewMovement : movementListNew) {
                if (!movementListOld.contains(movementListNewMovement)) {
                    Product oldProductIdOfMovementListNewMovement = movementListNewMovement.getProductId();
                    movementListNewMovement.setProductId(product);
                    movementListNewMovement = em.merge(movementListNewMovement);
                    if (oldProductIdOfMovementListNewMovement != null && !oldProductIdOfMovementListNewMovement.equals(product)) {
                        oldProductIdOfMovementListNewMovement.getMovementList().remove(movementListNewMovement);
                        oldProductIdOfMovementListNewMovement = em.merge(oldProductIdOfMovementListNewMovement);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = product.getId();
                if (findProduct(id) == null) {
                    throw new NonexistentEntityException("The product with id " + id + " no longer exists.");
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
            Product product;
            try {
                product = em.getReference(Product.class, id);
                product.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The product with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Preco> precoListOrphanCheck = product.getPrecoList();
            for (Preco precoListOrphanCheckPreco : precoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Product (" + product + ") cannot be destroyed since the Preco " + precoListOrphanCheckPreco + " in its precoList field has a non-nullable productId field.");
            }
            List<Item> itemListOrphanCheck = product.getItemList();
            for (Item itemListOrphanCheckItem : itemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Product (" + product + ") cannot be destroyed since the Item " + itemListOrphanCheckItem + " in its itemList field has a non-nullable productId field.");
            }
            List<Movement> movementListOrphanCheck = product.getMovementList();
            for (Movement movementListOrphanCheckMovement : movementListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Product (" + product + ") cannot be destroyed since the Movement " + movementListOrphanCheckMovement + " in its movementList field has a non-nullable productId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Autentication autenticationId = product.getAutenticationId();
            if (autenticationId != null) {
                autenticationId.getProductList().remove(product);
                autenticationId = em.merge(autenticationId);
            }
            Nivel3 nivel3Id = product.getNivel3Id();
            if (nivel3Id != null) {
                nivel3Id.getProductList().remove(product);
                nivel3Id = em.merge(nivel3Id);
            }
            Pais paisId = product.getPaisId();
            if (paisId != null) {
                paisId.getProductList().remove(product);
                paisId = em.merge(paisId);
            }
            Produttype produtTypeid = product.getProdutTypeid();
            if (produtTypeid != null) {
                produtTypeid.getProductList().remove(product);
                produtTypeid = em.merge(produtTypeid);
            }
            Unidade unidadeId = product.getUnidadeId();
            if (unidadeId != null) {
                unidadeId.getProductList().remove(product);
                unidadeId = em.merge(unidadeId);
            }
            List<Photo> photoList = product.getPhotoList();
            for (Photo photoListPhoto : photoList) {
                photoListPhoto.setProductId(null);
                photoListPhoto = em.merge(photoListPhoto);
            }
            em.remove(product);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Product> findProductEntities() {
        return findProductEntities(true, -1, -1);
    }

    public List<Product> findProductEntities(int maxResults, int firstResult) {
        return findProductEntities(false, maxResults, firstResult);
    }

    private List<Product> findProductEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Product.class));
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

    public Product findProduct(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Product.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Product> rt = cq.from(Product.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
