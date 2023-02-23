/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.tabelas;

import com.developer.java.entity.Nivel2;
import com.developer.java.entity.Nivel3;
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
public class Nivel3TabelaController implements Initializable {

    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn tc_designacao;
    @FXML
    private TableColumn tc_acao;
    @FXML
    private TableColumn tc_categoria;

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
        tc_categoria.setCellValueFactory(
                new PropertyValueFactory("categoria")
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

        String queryComands = "SELECT p FROM Nivel1 p ";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        List<Product> lista = querys.getResultList();

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
        private final String categoria;
        private final HBox myBox;
        private final JFXButton btnEliminar;
        private final JFXButton btnEditar;

        private Nivel3TabelaController myInstance;
        private Nivel3 entity;

        public ItemLista(int id, int index, String desigancao,String categoria, HBox myBox,
                JFXButton btnEliminar, JFXButton btnEditar,
                Nivel3TabelaController myInstance, Nivel3 entity) {
            this.id = id;
            this.index = index;
            this.desigancao = desigancao;
            this.categoria = categoria;
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

        public Nivel3TabelaController getMyInstance() {
            return myInstance;
        }

        public Nivel3 getEntity() {
            return entity;
        }

        public String getCategoria() {
            return categoria;
        }
        
        
    }

    private ObservableList<ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComandss = "SELECT p FROM Nivel3 p";

        Query querys = Util.enti.createQuery(queryComandss, Nivel3.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        //querys.setParameter("state", 0);
        List<Nivel3> lista = querys.getResultList();
        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        for (Nivel3 obj : lista) {

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
                     obj.getNivel2Id().getDesignation(),
                     new HBox(5),
                    new JFXButton("", iconPrints3),
                    new JFXButton("", iconPrints2),
                    this,
                    obj));
        }

        return olis;
    }

}
