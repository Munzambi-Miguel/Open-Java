/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.fornecimento;

import com.developer.Main;
import com.developer.java.controller.EntityJpaController;
import com.developer.java.entity.Entity;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javax.persistence.Query;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class FornecedorController implements Initializable {

    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private TableView TableViewCliente;
    @FXML
    private TableColumn TebleColumnOrder;
    @FXML
    private TableColumn TableColumnName;
    @FXML
    private TableColumn TableColumnNIdem;
    @FXML
    private TableColumn TableColumnProvincia;
    @FXML
    private TableColumn TableColumnZona;
    @FXML
    private TableColumn TableColumnConatact;
    @FXML
    private TableColumn TableColumnAccao;

    private ObservableList<ItemLista> observableList = FXCollections.observableArrayList();
    protected EntityJpaController entidadeCliente = new EntityJpaController(Util.emf);
    @FXML
    private Pagination paginacao;
    private final int QUANTIDADE_PAGINA = 10;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        myIsntance = this;

        caregarLista();
    }

    @FXML
    private void callingHome(ActionEvent event) throws IOException {

    }

    @FXML
    private void openModal(ActionEvent event) throws IOException, InterruptedException {

        // com.developer.res.fxml.fornecimento
        FormeFornecedorController.myentity = null;
        Util.frameDialog("res/fxml/fornecimento/formeFornecedor.fxml");
    }

    public void caregarLista() {

        TebleColumnOrder.setCellValueFactory(
                new PropertyValueFactory("index")
        );
        TableColumnName.setCellValueFactory(
                new PropertyValueFactory("nome")
        );
        TableColumnNIdem.setCellValueFactory(
                new PropertyValueFactory("nbis")
        );
        TableColumnProvincia.setCellValueFactory(
                new PropertyValueFactory("provincia")
        );
        TableColumnZona.setCellValueFactory(
                new PropertyValueFactory("zona")
        );
        TableColumnConatact.setCellValueFactory(
                new PropertyValueFactory("contacto")
        );
        TableColumnAccao.setCellValueFactory(
                new PropertyValueFactory("myBox")
        );

        paginacao.setPageFactory((Integer pagina) -> {
            atualizarGrade(pagina);
            return TableViewCliente;
        });

    }

    private void atualizarGrade(Integer pagina) {
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComands = "SELECT en FROM Entity en WHERE"
                + " en.entityType=3";

        Query querys = Util.enti.createQuery(queryComands);
        List<Entity> lista = querys.getResultList();

        if (lista.isEmpty()) {
            paginacao.setPageCount(1);
        }
        Util.enti.getTransaction().commit();
        Util.emf.close();

        try {
            paginacao.setPageCount((int) Math.ceil(((double) lista.size()) / QUANTIDADE_PAGINA));
            TableViewCliente.setItems(listar(QUANTIDADE_PAGINA, pagina));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObservableList<ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComandss = "SELECT en FROM Entity en WHERE"
                + " en.entityType=:entityType";

        Query querys = Util.enti.createQuery(queryComandss, Entity.class).setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        querys.setParameter("entityType", 3);

        List<Entity> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        for (Entity obj : lista) {

            i++;
            FontAwesomeIconView iconPrints = new FontAwesomeIconView();
            FontAwesomeIconView iconPrints2 = new FontAwesomeIconView();
            FontAwesomeIconView iconPrints3 = new FontAwesomeIconView();

            iconPrints.setGlyphName("EYE");
            iconPrints2.setGlyphName("PENCIL_SQUARE_ALT");
            iconPrints3.setGlyphName("TRASH_ALT");

            iconPrints.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));
            iconPrints2.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));
            iconPrints3.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

            olis.add(new ItemLista(
                    obj.getId(), i,
                    obj.getName(),
                    obj.getNIdetification() == null ? "Não foi ensirido o nº de identificação ..." : obj.getNIdetification().isEmpty()? "Não foi ensirido o nº de identificação ...":obj.getNIdetification(),
                    obj.getAddressList().isEmpty() ? "não foi encerido o endereço" : "" + obj.getAddressList().get(obj.getAddressList().size() - 1).toString(),
                    Util.muedaLocalT("" + obj.getCompra()),
                    obj.getContactList().isEmpty() ? "Não foi enserido contacto ..."
                    : (obj.getContactList().get(0).getTypeContact() == 2 ? obj.getContactList().get(0).getDescription()
                    : (obj.getContactList().size() > 1 ? (obj.getContactList().get(1).getTypeContact() == 2 ? obj.getContactList().get(1).getDescription()
                    : "Não foi enserido contacto ...") : "Não foi enserido contacto ...")),
                    new HBox(5), new JFXButton("", iconPrints3), new JFXButton("", iconPrints2), new JFXButton("", iconPrints),
                    this, obj
            ));
        }

        return olis;
    }

    public final class ItemLista {

        private final int id;
        private final int index;
        private final String nome;
        private final String nbis;
        private final String provincia;
        private final String zona;
        private final String contacto;
        private final HBox myBox;
        private final JFXButton btnEliminar;
        private final JFXButton btnEditar;
        private final JFXButton btnView;
        private FornecedorController myClient;
        Entity entity;

        public ItemLista(int id, int index, String nome, String nbis,
                String provincia, String zona, String contacto, HBox myBox,
                JFXButton btnEliminar, JFXButton btnEditar, JFXButton btnView, FornecedorController myClient, Entity entity) {
            this.id = id;
            this.index = index;
            this.nome = nome;
            this.nbis = nbis;
            this.provincia = provincia;
            this.zona = zona;
            this.contacto = contacto;
            this.myBox = myBox;
            this.btnEliminar = btnEliminar;
            this.btnEditar = btnEditar;
            this.btnView = btnView;
            this.myClient = myClient;

            this.btnEditar.setStyle(" -fx-background-color:   #24B6F7 ! important;");
            this.btnView.setStyle(" -fx-background-color:   #F0AD4E ! important;");
            this.btnEliminar.setStyle(" -fx-background-color:   #d9534f ! important;");

            this.myBox.getChildren().addAll(btnView, btnEditar, btnEliminar);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle(" -fx-cursor: hand;");
            this.entity = entity;

            evento();
        }

        public int getId() {
            return id;
        }

        public int getIndex() {
            return index;
        }

        public String getNome() {
            return nome;
        }

        public FornecedorController getMyClient() {
            return myClient;
        }

        public Entity getEntity() {
            return entity;
        }

        public String getNbis() {
            return nbis;
        }

        public String getProvincia() {
            return provincia;
        }

        public String getZona() {
            return zona;
        }

        public String getContacto() {
            return contacto;
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

        private void evento() {

            btnEditar.setOnAction((ActionEvent event) -> {
                try {
                    FormeFornecedorController.myentity = ItemLista.this.entity;
                    Util.frameDialog("res/fxml/fornecimento/formeFornecedor.fxml");

                } catch (IOException | InterruptedException ex) {
                    Logger.getLogger(FornecedorController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            btnEliminar.setOnAction((ActionEvent event) -> {

                Util.opeConnection();
                Util.enti.getTransaction().begin();
                Entity myEntity = new EntityJpaController(Util.emf).findEntity(ItemLista.this.id);
                myEntity.setState(-3);
                Entity remEntity = Util.enti.merge(myEntity);
                Util.enti.getTransaction().commit();

                TrayNotification tray = new TrayNotification("Fornecedor: " + remEntity.getName(),
                        "foi eliminado no sistema... ",
                        NotificationType.SUCCESS);
                //// com.developer.res.css.img
                tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                tray.setRectangleFill(Paint.valueOf("Gray"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
            });

        }

        public JFXButton getBtnView() {
            return btnView;
        }

    }

    public static FornecedorController myIsntance;
}
