/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.tabelas;

import com.developer.java.entity.Nivel3;
import com.developer.java.entity.Pais;
import com.developer.java.entity.Product;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class PaisViewController implements Initializable {

    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableView  tableView;
    @FXML
    private TableColumn  tc_ordem;
    @FXML
    private TableColumn  tc_designacao;
    @FXML
    private TableColumn  tc_acao;
    
    private int QUANTIDADE_PAGINA = 10;

    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        caregarLista();
    }    

    @FXML
    private void openModal(ActionEvent event) {
    }
    
     public void caregarLista() {

        tc_designacao.setCellValueFactory(
                new PropertyValueFactory("desigancao")
        );

        tc_acao.setCellValueFactory(
                new PropertyValueFactory("myBox")
        );

        paginacao.setPageFactory((Integer pagina) -> {
            atualizarGrade(pagina);
            return tableView;
        });

    }

    private void atualizarGrade(Integer pagina) {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        String queryComands = "SELECT p FROM Pais p ";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        List<Pais> lista = querys.getResultList();

        if (lista.isEmpty()) {
            paginacao.setPageCount(1);
        }
        Util.enti.getTransaction().commit();
        Util.emf.close();

        try {
            paginacao.setPageCount((int) Math.ceil(((double) lista.size()) / QUANTIDADE_PAGINA));
            tableView.setItems(listar(QUANTIDADE_PAGINA, pagina));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public final class ItemLista {

        private final int id;
        private final int index;
        private final String desigancao;
        private final HBox myBox;
        private final JFXButton btnEliminar;
        private final JFXButton btnEditar;

        private PaisViewController myInstance;
        private Pais entity;

        public ItemLista(int id, int index, String desigancao, HBox myBox,
                JFXButton btnEliminar, JFXButton btnEditar,
                PaisViewController myInstance, Pais entity) {
            this.id = id;
            this.index = index;
            this.desigancao = desigancao;
            this.myBox = myBox;
            this.btnEliminar = btnEliminar;
            this.btnEditar = btnEditar;
            this.myInstance = myInstance;
            this.entity = entity;

            this.btnEditar.setStyle(" -fx-background-color:   #24B6F7 ! important;");
            this.btnEliminar.setStyle(" -fx-background-color:   #d9534f ! important;");

            this.myBox.getChildren().addAll(btnEditar, btnEliminar);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle(" -fx-cursor: hand;");

            evento();
        }

        private void evento() {

        }

        public int getId() {
            return id;
        }

        public int getIndex() {
            return index;
        }

        public String getDesigancao() {
            return desigancao;
        }

        public HBox getMyBox() {
            return myBox;
        }

        public JFXButton getBtnEliminar() {
            return btnEliminar;
        }

        public JFXButton getBtnEditar() {
            return btnEditar;
        }

        public PaisViewController getMyInstance() {
            return myInstance;
        }

        public Pais getEntity() {
            return entity;
        }

    }

    private ObservableList<ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComandss = "SELECT p FROM Pais p";

        Query querys = Util.enti.createQuery(queryComandss, Pais.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        //querys.setParameter("state", 0);
        List<Pais> lista = querys.getResultList();
        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        for (Pais obj : lista) {

            i++;

            FontAwesomeIconView iconPrints2 = new FontAwesomeIconView();
            FontAwesomeIconView iconPrints3 = new FontAwesomeIconView();

            iconPrints2.setGlyphName("PENCIL_SQUARE_ALT");
            iconPrints3.setGlyphName("TRASH_ALT");

            iconPrints2.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));
            iconPrints3.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

            olis.add(new ItemLista(obj.getId(),
                    i,
                    obj.getDesignation(),
                     new HBox(5),
                    new JFXButton("", iconPrints3),
                    new JFXButton("", iconPrints2),
                    this,
                    obj));
        }

        return olis;
    }
    
}
