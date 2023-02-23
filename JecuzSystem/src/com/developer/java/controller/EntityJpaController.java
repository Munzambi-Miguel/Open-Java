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
import com.developer.java.entity.Company;
import com.developer.java.entity.Entity;
import com.developer.java.entity.Armazem;
import java.util.ArrayList;
import java.util.List;
import com.developer.java.entity.Contact;
import com.developer.java.entity.Client;
import com.developer.java.entity.Autentication;
import com.developer.java.entity.Address;
import com.developer.java.entity.Photo;
import com.developer.java.entity.Movement;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hdglo
 */
public class EntityJpaController implements Serializable {

    public EntityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entity entity) {
        if (entity.getArmazemList() == null) {
            entity.setArmazemList(new ArrayList<Armazem>());
        }
        if (entity.getContactList() == null) {
            entity.setContactList(new ArrayList<Contact>());
        }
        if (entity.getClientList() == null) {
            entity.setClientList(new ArrayList<Client>());
        }
        if (entity.getAutenticationList() == null) {
            entity.setAutenticationList(new ArrayList<Autentication>());
        }
        if (entity.getAddressList() == null) {
            entity.setAddressList(new ArrayList<Address>());
        }
        if (entity.getPhotoList() == null) {
            entity.setPhotoList(new ArrayList<Photo>());
        }
        if (entity.getMovementList() == null) {
            entity.setMovementList(new ArrayList<Movement>());
        }
        if (entity.getEntityList() == null) {
            entity.setEntityList(new ArrayList<Entity>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Company companyid = entity.getCompanyid();
            if (companyid != null) {
                companyid = em.getReference(companyid.getClass(), companyid.getId());
                entity.setCompanyid(companyid);
            }
            Entity ondeTrabalho = entity.getOndeTrabalho();
            if (ondeTrabalho != null) {
                ondeTrabalho = em.getReference(ondeTrabalho.getClass(), ondeTrabalho.getId());
                entity.setOndeTrabalho(ondeTrabalho);
            }
            List<Armazem> attachedArmazemList = new ArrayList<Armazem>();
            for (Armazem armazemListArmazemToAttach : entity.getArmazemList()) {
                armazemListArmazemToAttach = em.getReference(armazemListArmazemToAttach.getClass(), armazemListArmazemToAttach.getId());
                attachedArmazemList.add(armazemListArmazemToAttach);
            }
            entity.setArmazemList(attachedArmazemList);
            List<Contact> attachedContactList = new ArrayList<Contact>();
            for (Contact contactListContactToAttach : entity.getContactList()) {
                contactListContactToAttach = em.getReference(contactListContactToAttach.getClass(), contactListContactToAttach.getId());
                attachedContactList.add(contactListContactToAttach);
            }
            entity.setContactList(attachedContactList);
            List<Client> attachedClientList = new ArrayList<Client>();
            for (Client clientListClientToAttach : entity.getClientList()) {
                clientListClientToAttach = em.getReference(clientListClientToAttach.getClass(), clientListClientToAttach.getId());
                attachedClientList.add(clientListClientToAttach);
            }
            entity.setClientList(attachedClientList);
            List<Autentication> attachedAutenticationList = new ArrayList<Autentication>();
            for (Autentication autenticationListAutenticationToAttach : entity.getAutenticationList()) {
                autenticationListAutenticationToAttach = em.getReference(autenticationListAutenticationToAttach.getClass(), autenticationListAutenticationToAttach.getId());
                attachedAutenticationList.add(autenticationListAutenticationToAttach);
            }
            entity.setAutenticationList(attachedAutenticationList);
            List<Address> attachedAddressList = new ArrayList<Address>();
            for (Address addressListAddressToAttach : entity.getAddressList()) {
                addressListAddressToAttach = em.getReference(addressListAddressToAttach.getClass(), addressListAddressToAttach.getId());
                attachedAddressList.add(addressListAddressToAttach);
            }
            entity.setAddressList(attachedAddressList);
            List<Photo> attachedPhotoList = new ArrayList<Photo>();
            for (Photo photoListPhotoToAttach : entity.getPhotoList()) {
                photoListPhotoToAttach = em.getReference(photoListPhotoToAttach.getClass(), photoListPhotoToAttach.getId());
                attachedPhotoList.add(photoListPhotoToAttach);
            }
            entity.setPhotoList(attachedPhotoList);
            List<Movement> attachedMovementList = new ArrayList<Movement>();
            for (Movement movementListMovementToAttach : entity.getMovementList()) {
                movementListMovementToAttach = em.getReference(movementListMovementToAttach.getClass(), movementListMovementToAttach.getId());
                attachedMovementList.add(movementListMovementToAttach);
            }
            entity.setMovementList(attachedMovementList);
            List<Entity> attachedEntityList = new ArrayList<Entity>();
            for (Entity entityListEntityToAttach : entity.getEntityList()) {
                entityListEntityToAttach = em.getReference(entityListEntityToAttach.getClass(), entityListEntityToAttach.getId());
                attachedEntityList.add(entityListEntityToAttach);
            }
            entity.setEntityList(attachedEntityList);
            em.persist(entity);
            if (companyid != null) {
                companyid.getEntityList().add(entity);
                companyid = em.merge(companyid);
            }
            if (ondeTrabalho != null) {
                ondeTrabalho.getEntityList().add(entity);
                ondeTrabalho = em.merge(ondeTrabalho);
            }
            for (Armazem armazemListArmazem : entity.getArmazemList()) {
                Entity oldEntityIdOfArmazemListArmazem = armazemListArmazem.getEntityId();
                armazemListArmazem.setEntityId(entity);
                armazemListArmazem = em.merge(armazemListArmazem);
                if (oldEntityIdOfArmazemListArmazem != null) {
                    oldEntityIdOfArmazemListArmazem.getArmazemList().remove(armazemListArmazem);
                    oldEntityIdOfArmazemListArmazem = em.merge(oldEntityIdOfArmazemListArmazem);
                }
            }
            for (Contact contactListContact : entity.getContactList()) {
                Entity oldEntityIdOfContactListContact = contactListContact.getEntityId();
                contactListContact.setEntityId(entity);
                contactListContact = em.merge(contactListContact);
                if (oldEntityIdOfContactListContact != null) {
                    oldEntityIdOfContactListContact.getContactList().remove(contactListContact);
                    oldEntityIdOfContactListContact = em.merge(oldEntityIdOfContactListContact);
                }
            }
            for (Client clientListClient : entity.getClientList()) {
                Entity oldEntityIdOfClientListClient = clientListClient.getEntityId();
                clientListClient.setEntityId(entity);
                clientListClient = em.merge(clientListClient);
                if (oldEntityIdOfClientListClient != null) {
                    oldEntityIdOfClientListClient.getClientList().remove(clientListClient);
                    oldEntityIdOfClientListClient = em.merge(oldEntityIdOfClientListClient);
                }
            }
            for (Autentication autenticationListAutentication : entity.getAutenticationList()) {
                Entity oldEntityidOfAutenticationListAutentication = autenticationListAutentication.getEntityid();
                autenticationListAutentication.setEntityid(entity);
                autenticationListAutentication = em.merge(autenticationListAutentication);
                if (oldEntityidOfAutenticationListAutentication != null) {
                    oldEntityidOfAutenticationListAutentication.getAutenticationList().remove(autenticationListAutentication);
                    oldEntityidOfAutenticationListAutentication = em.merge(oldEntityidOfAutenticationListAutentication);
                }
            }
            for (Address addressListAddress : entity.getAddressList()) {
                Entity oldEntityIdOfAddressListAddress = addressListAddress.getEntityId();
                addressListAddress.setEntityId(entity);
                addressListAddress = em.merge(addressListAddress);
                if (oldEntityIdOfAddressListAddress != null) {
                    oldEntityIdOfAddressListAddress.getAddressList().remove(addressListAddress);
                    oldEntityIdOfAddressListAddress = em.merge(oldEntityIdOfAddressListAddress);
                }
            }
            for (Photo photoListPhoto : entity.getPhotoList()) {
                Entity oldEntityIdOfPhotoListPhoto = photoListPhoto.getEntityId();
                photoListPhoto.setEntityId(entity);
                photoListPhoto = em.merge(photoListPhoto);
                if (oldEntityIdOfPhotoListPhoto != null) {
                    oldEntityIdOfPhotoListPhoto.getPhotoList().remove(photoListPhoto);
                    oldEntityIdOfPhotoListPhoto = em.merge(oldEntityIdOfPhotoListPhoto);
                }
            }
            for (Movement movementListMovement : entity.getMovementList()) {
                Entity oldFornedorIdOfMovementListMovement = movementListMovement.getFornedorId();
                movementListMovement.setFornedorId(entity);
                movementListMovement = em.merge(movementListMovement);
                if (oldFornedorIdOfMovementListMovement != null) {
                    oldFornedorIdOfMovementListMovement.getMovementList().remove(movementListMovement);
                    oldFornedorIdOfMovementListMovement = em.merge(oldFornedorIdOfMovementListMovement);
                }
            }
            for (Entity entityListEntity : entity.getEntityList()) {
                Entity oldOndeTrabalhoOfEntityListEntity = entityListEntity.getOndeTrabalho();
                entityListEntity.setOndeTrabalho(entity);
                entityListEntity = em.merge(entityListEntity);
                if (oldOndeTrabalhoOfEntityListEntity != null) {
                    oldOndeTrabalhoOfEntityListEntity.getEntityList().remove(entityListEntity);
                    oldOndeTrabalhoOfEntityListEntity = em.merge(oldOndeTrabalhoOfEntityListEntity);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entity entity) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entity persistentEntity = em.find(Entity.class, entity.getId());
            Company companyidOld = persistentEntity.getCompanyid();
            Company companyidNew = entity.getCompanyid();
            Entity ondeTrabalhoOld = persistentEntity.getOndeTrabalho();
            Entity ondeTrabalhoNew = entity.getOndeTrabalho();
            List<Armazem> armazemListOld = persistentEntity.getArmazemList();
            List<Armazem> armazemListNew = entity.getArmazemList();
            List<Contact> contactListOld = persistentEntity.getContactList();
            List<Contact> contactListNew = entity.getContactList();
            List<Client> clientListOld = persistentEntity.getClientList();
            List<Client> clientListNew = entity.getClientList();
            List<Autentication> autenticationListOld = persistentEntity.getAutenticationList();
            List<Autentication> autenticationListNew = entity.getAutenticationList();
            List<Address> addressListOld = persistentEntity.getAddressList();
            List<Address> addressListNew = entity.getAddressList();
            List<Photo> photoListOld = persistentEntity.getPhotoList();
            List<Photo> photoListNew = entity.getPhotoList();
            List<Movement> movementListOld = persistentEntity.getMovementList();
            List<Movement> movementListNew = entity.getMovementList();
            List<Entity> entityListOld = persistentEntity.getEntityList();
            List<Entity> entityListNew = entity.getEntityList();
            List<String> illegalOrphanMessages = null;
            for (Armazem armazemListOldArmazem : armazemListOld) {
                if (!armazemListNew.contains(armazemListOldArmazem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Armazem " + armazemListOldArmazem + " since its entityId field is not nullable.");
                }
            }
            for (Contact contactListOldContact : contactListOld) {
                if (!contactListNew.contains(contactListOldContact)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contact " + contactListOldContact + " since its entityId field is not nullable.");
                }
            }
            for (Client clientListOldClient : clientListOld) {
                if (!clientListNew.contains(clientListOldClient)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Client " + clientListOldClient + " since its entityId field is not nullable.");
                }
            }
            for (Address addressListOldAddress : addressListOld) {
                if (!addressListNew.contains(addressListOldAddress)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Address " + addressListOldAddress + " since its entityId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (companyidNew != null) {
                companyidNew = em.getReference(companyidNew.getClass(), companyidNew.getId());
                entity.setCompanyid(companyidNew);
            }
            if (ondeTrabalhoNew != null) {
                ondeTrabalhoNew = em.getReference(ondeTrabalhoNew.getClass(), ondeTrabalhoNew.getId());
                entity.setOndeTrabalho(ondeTrabalhoNew);
            }
            List<Armazem> attachedArmazemListNew = new ArrayList<Armazem>();
            for (Armazem armazemListNewArmazemToAttach : armazemListNew) {
                armazemListNewArmazemToAttach = em.getReference(armazemListNewArmazemToAttach.getClass(), armazemListNewArmazemToAttach.getId());
                attachedArmazemListNew.add(armazemListNewArmazemToAttach);
            }
            armazemListNew = attachedArmazemListNew;
            entity.setArmazemList(armazemListNew);
            List<Contact> attachedContactListNew = new ArrayList<Contact>();
            for (Contact contactListNewContactToAttach : contactListNew) {
                contactListNewContactToAttach = em.getReference(contactListNewContactToAttach.getClass(), contactListNewContactToAttach.getId());
                attachedContactListNew.add(contactListNewContactToAttach);
            }
            contactListNew = attachedContactListNew;
            entity.setContactList(contactListNew);
            List<Client> attachedClientListNew = new ArrayList<Client>();
            for (Client clientListNewClientToAttach : clientListNew) {
                clientListNewClientToAttach = em.getReference(clientListNewClientToAttach.getClass(), clientListNewClientToAttach.getId());
                attachedClientListNew.add(clientListNewClientToAttach);
            }
            clientListNew = attachedClientListNew;
            entity.setClientList(clientListNew);
            List<Autentication> attachedAutenticationListNew = new ArrayList<Autentication>();
            for (Autentication autenticationListNewAutenticationToAttach : autenticationListNew) {
                autenticationListNewAutenticationToAttach = em.getReference(autenticationListNewAutenticationToAttach.getClass(), autenticationListNewAutenticationToAttach.getId());
                attachedAutenticationListNew.add(autenticationListNewAutenticationToAttach);
            }
            autenticationListNew = attachedAutenticationListNew;
            entity.setAutenticationList(autenticationListNew);
            List<Address> attachedAddressListNew = new ArrayList<Address>();
            for (Address addressListNewAddressToAttach : addressListNew) {
                addressListNewAddressToAttach = em.getReference(addressListNewAddressToAttach.getClass(), addressListNewAddressToAttach.getId());
                attachedAddressListNew.add(addressListNewAddressToAttach);
            }
            addressListNew = attachedAddressListNew;
            entity.setAddressList(addressListNew);
            List<Photo> attachedPhotoListNew = new ArrayList<Photo>();
            for (Photo photoListNewPhotoToAttach : photoListNew) {
                photoListNewPhotoToAttach = em.getReference(photoListNewPhotoToAttach.getClass(), photoListNewPhotoToAttach.getId());
                attachedPhotoListNew.add(photoListNewPhotoToAttach);
            }
            photoListNew = attachedPhotoListNew;
            entity.setPhotoList(photoListNew);
            List<Movement> attachedMovementListNew = new ArrayList<Movement>();
            for (Movement movementListNewMovementToAttach : movementListNew) {
                movementListNewMovementToAttach = em.getReference(movementListNewMovementToAttach.getClass(), movementListNewMovementToAttach.getId());
                attachedMovementListNew.add(movementListNewMovementToAttach);
            }
            movementListNew = attachedMovementListNew;
            entity.setMovementList(movementListNew);
            List<Entity> attachedEntityListNew = new ArrayList<Entity>();
            for (Entity entityListNewEntityToAttach : entityListNew) {
                entityListNewEntityToAttach = em.getReference(entityListNewEntityToAttach.getClass(), entityListNewEntityToAttach.getId());
                attachedEntityListNew.add(entityListNewEntityToAttach);
            }
            entityListNew = attachedEntityListNew;
            entity.setEntityList(entityListNew);
            entity = em.merge(entity);
            if (companyidOld != null && !companyidOld.equals(companyidNew)) {
                companyidOld.getEntityList().remove(entity);
                companyidOld = em.merge(companyidOld);
            }
            if (companyidNew != null && !companyidNew.equals(companyidOld)) {
                companyidNew.getEntityList().add(entity);
                companyidNew = em.merge(companyidNew);
            }
            if (ondeTrabalhoOld != null && !ondeTrabalhoOld.equals(ondeTrabalhoNew)) {
                ondeTrabalhoOld.getEntityList().remove(entity);
                ondeTrabalhoOld = em.merge(ondeTrabalhoOld);
            }
            if (ondeTrabalhoNew != null && !ondeTrabalhoNew.equals(ondeTrabalhoOld)) {
                ondeTrabalhoNew.getEntityList().add(entity);
                ondeTrabalhoNew = em.merge(ondeTrabalhoNew);
            }
            for (Armazem armazemListNewArmazem : armazemListNew) {
                if (!armazemListOld.contains(armazemListNewArmazem)) {
                    Entity oldEntityIdOfArmazemListNewArmazem = armazemListNewArmazem.getEntityId();
                    armazemListNewArmazem.setEntityId(entity);
                    armazemListNewArmazem = em.merge(armazemListNewArmazem);
                    if (oldEntityIdOfArmazemListNewArmazem != null && !oldEntityIdOfArmazemListNewArmazem.equals(entity)) {
                        oldEntityIdOfArmazemListNewArmazem.getArmazemList().remove(armazemListNewArmazem);
                        oldEntityIdOfArmazemListNewArmazem = em.merge(oldEntityIdOfArmazemListNewArmazem);
                    }
                }
            }
            for (Contact contactListNewContact : contactListNew) {
                if (!contactListOld.contains(contactListNewContact)) {
                    Entity oldEntityIdOfContactListNewContact = contactListNewContact.getEntityId();
                    contactListNewContact.setEntityId(entity);
                    contactListNewContact = em.merge(contactListNewContact);
                    if (oldEntityIdOfContactListNewContact != null && !oldEntityIdOfContactListNewContact.equals(entity)) {
                        oldEntityIdOfContactListNewContact.getContactList().remove(contactListNewContact);
                        oldEntityIdOfContactListNewContact = em.merge(oldEntityIdOfContactListNewContact);
                    }
                }
            }
            for (Client clientListNewClient : clientListNew) {
                if (!clientListOld.contains(clientListNewClient)) {
                    Entity oldEntityIdOfClientListNewClient = clientListNewClient.getEntityId();
                    clientListNewClient.setEntityId(entity);
                    clientListNewClient = em.merge(clientListNewClient);
                    if (oldEntityIdOfClientListNewClient != null && !oldEntityIdOfClientListNewClient.equals(entity)) {
                        oldEntityIdOfClientListNewClient.getClientList().remove(clientListNewClient);
                        oldEntityIdOfClientListNewClient = em.merge(oldEntityIdOfClientListNewClient);
                    }
                }
            }
            for (Autentication autenticationListOldAutentication : autenticationListOld) {
                if (!autenticationListNew.contains(autenticationListOldAutentication)) {
                    autenticationListOldAutentication.setEntityid(null);
                    autenticationListOldAutentication = em.merge(autenticationListOldAutentication);
                }
            }
            for (Autentication autenticationListNewAutentication : autenticationListNew) {
                if (!autenticationListOld.contains(autenticationListNewAutentication)) {
                    Entity oldEntityidOfAutenticationListNewAutentication = autenticationListNewAutentication.getEntityid();
                    autenticationListNewAutentication.setEntityid(entity);
                    autenticationListNewAutentication = em.merge(autenticationListNewAutentication);
                    if (oldEntityidOfAutenticationListNewAutentication != null && !oldEntityidOfAutenticationListNewAutentication.equals(entity)) {
                        oldEntityidOfAutenticationListNewAutentication.getAutenticationList().remove(autenticationListNewAutentication);
                        oldEntityidOfAutenticationListNewAutentication = em.merge(oldEntityidOfAutenticationListNewAutentication);
                    }
                }
            }
            for (Address addressListNewAddress : addressListNew) {
                if (!addressListOld.contains(addressListNewAddress)) {
                    Entity oldEntityIdOfAddressListNewAddress = addressListNewAddress.getEntityId();
                    addressListNewAddress.setEntityId(entity);
                    addressListNewAddress = em.merge(addressListNewAddress);
                    if (oldEntityIdOfAddressListNewAddress != null && !oldEntityIdOfAddressListNewAddress.equals(entity)) {
                        oldEntityIdOfAddressListNewAddress.getAddressList().remove(addressListNewAddress);
                        oldEntityIdOfAddressListNewAddress = em.merge(oldEntityIdOfAddressListNewAddress);
                    }
                }
            }
            for (Photo photoListOldPhoto : photoListOld) {
                if (!photoListNew.contains(photoListOldPhoto)) {
                    photoListOldPhoto.setEntityId(null);
                    photoListOldPhoto = em.merge(photoListOldPhoto);
                }
            }
            for (Photo photoListNewPhoto : photoListNew) {
                if (!photoListOld.contains(photoListNewPhoto)) {
                    Entity oldEntityIdOfPhotoListNewPhoto = photoListNewPhoto.getEntityId();
                    photoListNewPhoto.setEntityId(entity);
                    photoListNewPhoto = em.merge(photoListNewPhoto);
                    if (oldEntityIdOfPhotoListNewPhoto != null && !oldEntityIdOfPhotoListNewPhoto.equals(entity)) {
                        oldEntityIdOfPhotoListNewPhoto.getPhotoList().remove(photoListNewPhoto);
                        oldEntityIdOfPhotoListNewPhoto = em.merge(oldEntityIdOfPhotoListNewPhoto);
                    }
                }
            }
            for (Movement movementListOldMovement : movementListOld) {
                if (!movementListNew.contains(movementListOldMovement)) {
                    movementListOldMovement.setFornedorId(null);
                    movementListOldMovement = em.merge(movementListOldMovement);
                }
            }
            for (Movement movementListNewMovement : movementListNew) {
                if (!movementListOld.contains(movementListNewMovement)) {
                    Entity oldFornedorIdOfMovementListNewMovement = movementListNewMovement.getFornedorId();
                    movementListNewMovement.setFornedorId(entity);
                    movementListNewMovement = em.merge(movementListNewMovement);
                    if (oldFornedorIdOfMovementListNewMovement != null && !oldFornedorIdOfMovementListNewMovement.equals(entity)) {
                        oldFornedorIdOfMovementListNewMovement.getMovementList().remove(movementListNewMovement);
                        oldFornedorIdOfMovementListNewMovement = em.merge(oldFornedorIdOfMovementListNewMovement);
                    }
                }
            }
            for (Entity entityListOldEntity : entityListOld) {
                if (!entityListNew.contains(entityListOldEntity)) {
                    entityListOldEntity.setOndeTrabalho(null);
                    entityListOldEntity = em.merge(entityListOldEntity);
                }
            }
            for (Entity entityListNewEntity : entityListNew) {
                if (!entityListOld.contains(entityListNewEntity)) {
                    Entity oldOndeTrabalhoOfEntityListNewEntity = entityListNewEntity.getOndeTrabalho();
                    entityListNewEntity.setOndeTrabalho(entity);
                    entityListNewEntity = em.merge(entityListNewEntity);
                    if (oldOndeTrabalhoOfEntityListNewEntity != null && !oldOndeTrabalhoOfEntityListNewEntity.equals(entity)) {
                        oldOndeTrabalhoOfEntityListNewEntity.getEntityList().remove(entityListNewEntity);
                        oldOndeTrabalhoOfEntityListNewEntity = em.merge(oldOndeTrabalhoOfEntityListNewEntity);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entity.getId();
                if (findEntity(id) == null) {
                    throw new NonexistentEntityException("The entity with id " + id + " no longer exists.");
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
            Entity entity;
            try {
                entity = em.getReference(Entity.class, id);
                entity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entity with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Armazem> armazemListOrphanCheck = entity.getArmazemList();
            for (Armazem armazemListOrphanCheckArmazem : armazemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Entity (" + entity + ") cannot be destroyed since the Armazem " + armazemListOrphanCheckArmazem + " in its armazemList field has a non-nullable entityId field.");
            }
            List<Contact> contactListOrphanCheck = entity.getContactList();
            for (Contact contactListOrphanCheckContact : contactListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Entity (" + entity + ") cannot be destroyed since the Contact " + contactListOrphanCheckContact + " in its contactList field has a non-nullable entityId field.");
            }
            List<Client> clientListOrphanCheck = entity.getClientList();
            for (Client clientListOrphanCheckClient : clientListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Entity (" + entity + ") cannot be destroyed since the Client " + clientListOrphanCheckClient + " in its clientList field has a non-nullable entityId field.");
            }
            List<Address> addressListOrphanCheck = entity.getAddressList();
            for (Address addressListOrphanCheckAddress : addressListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Entity (" + entity + ") cannot be destroyed since the Address " + addressListOrphanCheckAddress + " in its addressList field has a non-nullable entityId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Company companyid = entity.getCompanyid();
            if (companyid != null) {
                companyid.getEntityList().remove(entity);
                companyid = em.merge(companyid);
            }
            Entity ondeTrabalho = entity.getOndeTrabalho();
            if (ondeTrabalho != null) {
                ondeTrabalho.getEntityList().remove(entity);
                ondeTrabalho = em.merge(ondeTrabalho);
            }
            List<Autentication> autenticationList = entity.getAutenticationList();
            for (Autentication autenticationListAutentication : autenticationList) {
                autenticationListAutentication.setEntityid(null);
                autenticationListAutentication = em.merge(autenticationListAutentication);
            }
            List<Photo> photoList = entity.getPhotoList();
            for (Photo photoListPhoto : photoList) {
                photoListPhoto.setEntityId(null);
                photoListPhoto = em.merge(photoListPhoto);
            }
            List<Movement> movementList = entity.getMovementList();
            for (Movement movementListMovement : movementList) {
                movementListMovement.setFornedorId(null);
                movementListMovement = em.merge(movementListMovement);
            }
            List<Entity> entityList = entity.getEntityList();
            for (Entity entityListEntity : entityList) {
                entityListEntity.setOndeTrabalho(null);
                entityListEntity = em.merge(entityListEntity);
            }
            em.remove(entity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entity> findEntityEntities() {
        return findEntityEntities(true, -1, -1);
    }

    public List<Entity> findEntityEntities(int maxResults, int firstResult) {
        return findEntityEntities(false, maxResults, firstResult);
    }

    private List<Entity> findEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entity.class));
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

    public Entity findEntity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entity.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entity> rt = cq.from(Entity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
