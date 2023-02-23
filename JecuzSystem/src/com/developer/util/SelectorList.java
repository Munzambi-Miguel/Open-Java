/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.util;

import com.developer.java.controller.CityJpaController;
import com.developer.java.controller.Nivel1JpaController;
import com.developer.java.controller.Nivel2JpaController;
import com.developer.java.controller.Nivel3JpaController;
import com.developer.java.controller.PaisJpaController;
import com.developer.java.controller.UnidadeJpaController;
import com.developer.java.entity.City;
import com.developer.java.entity.Entity;
import com.developer.java.entity.Grup;
import com.developer.java.entity.Maquina;
import com.developer.java.entity.Nivel1;
import com.developer.java.entity.Nivel2;
import com.developer.java.entity.Nivel3;
import com.developer.java.entity.Pais;
import com.developer.java.entity.Product;
import com.developer.java.entity.Produttype;
import com.developer.java.entity.Tipocliente;
import com.developer.java.entity.Unidade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author hdglo
 */
public abstract class SelectorList {

    public static ObservableList<Tipocliente> tipoCLiente() {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<Tipocliente> my = FXCollections.observableArrayList();

        String queryComands = "SELECT en FROM Tipocliente en ";

        Query querys = Util.enti.createQuery(queryComands, Nivel2.class)
                .setHint(QueryHints.REFRESH, true);

        querys.getResultList().forEach((cliente) -> {
            my.add((Tipocliente) cliente);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }

    public static ObservableList<City> citySelector() {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<City> my = FXCollections.observableArrayList();
        new CityJpaController(Util.emf).findCityEntities().forEach((city) -> {
            my.add(city);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }

    public static ObservableList<Nivel1> nivel1Selector() {
        Util.opeConnection();
        //Util.enti.refresh(Nivel1.class);
        Util.enti.getTransaction().begin();

        ObservableList<Nivel1> my = FXCollections.observableArrayList();
        new Nivel1JpaController(Util.emf).findNivel1Entities().forEach((niveis) -> {
            my.add(niveis);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }

    public static ObservableList<Nivel2> nivel2Selector() {

        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<Nivel2> my = FXCollections.observableArrayList();
        new Nivel2JpaController(Util.emf).findNivel2Entities().forEach((niveis) -> {
            my.add(niveis);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }

    public static ObservableList<Nivel2> nivel2Selector(Nivel1 nivel) {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<Nivel2> my = FXCollections.observableArrayList();

        String queryComands = "SELECT en FROM Nivel2 en WHERE"
                + " en.nivel1Id =:nivel1Id";

        Query querys = Util.enti.createQuery(queryComands, Nivel2.class).setHint(QueryHints.REFRESH, true);

        querys.setParameter("nivel1Id", nivel);

        querys.getResultList().forEach((niveis) -> {
            my.add((Nivel2) niveis);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }

    public static ObservableList<Nivel3> nivel3Selector() {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<Nivel3> my = FXCollections.observableArrayList();
        new Nivel3JpaController(Util.emf).findNivel3Entities().forEach((niveis) -> {
            my.add(niveis);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }

    public static ObservableList<Nivel3> nivel3Selector(Nivel2 nivel) {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<Nivel3> my = FXCollections.observableArrayList();

        String queryComands = "SELECT en FROM Nivel3 en WHERE"
                + " en.nivel2Id =:nivel2Id";

        Query querys = Util.enti.createQuery(queryComands, Nivel3.class);

        querys.setParameter("nivel2Id", nivel);

        querys.getResultList().forEach((niveis) -> {
            my.add((Nivel3) niveis);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }

    public static ObservableList<Produttype> produtTypeSelector() {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<Produttype> my = FXCollections.observableArrayList();

        String queryComands = "SELECT en FROM Produttype en "
                + "";

        Query querys = Util.enti.createQuery(queryComands, Produttype.class);

        // querys.getSingleResult();
        querys.getResultList().forEach((produtType) -> {
            my.add((Produttype) produtType);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }

    public static ObservableList<Pais> paisSelector() {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<Pais> my = FXCollections.observableArrayList();
        new PaisJpaController(Util.emf).findPaisEntities().forEach((pais) -> {
            my.add(pais);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }

    public static ObservableList<Unidade> unidadeSelector() {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<Unidade> my = FXCollections.observableArrayList();
        new UnidadeJpaController(Util.emf).findUnidadeEntities().forEach((unidade) -> {
            my.add(unidade);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }

    public static ObservableList<Entity> setListFornecedor() {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<Entity> my = FXCollections.observableArrayList();

        String queryComands = "SELECT en FROM Entity en WHERE"
                + " en.state=0 and en.entityType=3";

        Query querys = Util.enti.createQuery(queryComands, Entity.class).setHint(QueryHints.REFRESH, true);

        // querys.getSingleResult();
        querys.getResultList().forEach((entity) -> {
            my.add((Entity) entity);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }

    public static ObservableList<Product> setListProduto() {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<Product> my = FXCollections.observableArrayList();

        String queryComands = "SELECT en FROM Product en WHERE"
                + " en.state=0 and en.quantidAtual>0";

        Query querys = Util.enti.createQuery(queryComands, Product.class).setHint(QueryHints.REFRESH, true);

        // querys.getSingleResult();
        querys.getResultList().forEach((product) -> {
            my.add((Product) product);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }
    public static ObservableList<Grup> grupSelector() {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<Grup> my = FXCollections.observableArrayList();

        String queryComands = "SELECT en FROM Grup en "
                + "";

        Query querys = Util.enti.createQuery(queryComands, Grup.class).setHint(QueryHints.REFRESH, true);

        // querys.getSingleResult();
        querys.getResultList().forEach((grup) -> {
            my.add((Grup) grup);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }
    
    public static ObservableList<Maquina> maquiSelector() {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        ObservableList<Maquina> my = FXCollections.observableArrayList();

        String queryComands = "SELECT en FROM Maquina en "
                + "WHERE en.state=1";

        Query querys = Util.enti.createQuery(queryComands, Maquina.class).setHint(QueryHints.REFRESH, true);

        // querys.getSingleResult();
        querys.getResultList().forEach((maqui) -> {
            my.add((Maquina) maqui);
        });

        Util.enti.getTransaction().commit();
        Util.emf.close();
        return my;
    }
    
    

}

/*

String queryComands = "SELECT en FROM Entity en WHERE"
                + " en.state=0 and en.EntityType=3";
*/
