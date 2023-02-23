/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.funcionario;

import com.developer.Main;
import com.developer.java.controller.EntityJpaController;
import com.developer.java.entity.Client;
import com.developer.java.entity.Entity;
import com.developer.res.fxml.clientes.ClienteController;
import com.developer.res.fxml.fornecimento.FornecedorController;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class FuncionarioController implements Initializable {

    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private BorderPane myBorderPaneInternar;

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn TebleColumnOrder;
    @FXML
    private TableColumn TableColumnName;
    @FXML
    private TableColumn TableColumnNIdem;
    @FXML
    private TableColumn TableColumnProvincia;

    @FXML
    private TableColumn TableColumnConatact;
    @FXML
    private TableColumn TableColumnAccao;

    private ObservableList<ItemLista> observableList = FXCollections.observableArrayList();
    protected EntityJpaController entidadeCliente = new EntityJpaController(Util.emf);

    @FXML
    private Pagination paginacao;

    private final int QUANTIDADE_PAGINA = 17;

    @FXML
    private TableColumn TableColumnAcesso;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO       
        this.caregarLista();
        myInstace = this;
    }

    @FXML
    private void openModal(ActionEvent event) throws IOException, InterruptedException {
        /// "res/fxml/funcionario/formeFuncionario.fxml"

        FormeFuncionarioController.myentity = null;
        Util.frameDialog("res/fxml/funcionario/formeFuncionario.fxml");
    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

    /// model intermadiario
    public void caregarLista() {

        TebleColumnOrder.setCellValueFactory(
                new PropertyValueFactory("imageView")
        );
        TableColumnName.setCellValueFactory(
                new PropertyValueFactory("nome")
        );
        TableColumnNIdem.setCellValueFactory(
                new PropertyValueFactory("nbis")
        );
        TableColumnProvincia.setCellValueFactory(
                new PropertyValueFactory("endereco")
        );
        TableColumnAcesso.setCellValueFactory(
                new PropertyValueFactory("ultimaCompra")
        );

        TableColumnConatact.setCellValueFactory(
                new PropertyValueFactory("telefone")
        );
        TableColumnAccao.setCellValueFactory(
                new PropertyValueFactory("myBox")
        );

        paginacao.setPageFactory((Integer pagina) -> {
            atualizarGrade(pagina);
            return tableView;
        });

    }

    /// SELECT * FROM `myentity` WHERE `Name` LIKE '%%'
    public final class ItemLista {

        private final int id;
        private final int index;
        private final String nome;
        private final String nbis;
        private final String endereco;
        private final String telefone;
        private final String ultimaCompra;
        private final String divida;
        private final HBox myBox;
        private final JFXButton btnEliminar;
        private final JFXButton btnEditar;
        private final JFXButton btnView;
        private FuncionarioController myClient;
        private Entity entity;
        private final ImageView imageView;
        private Client myclient;

        public ItemLista(int id, int index, String nome, String nbis,
                String endereco, String telefone,
                String ultimaCompra, String divida, HBox myBox,
                JFXButton btnEliminar, JFXButton btnEditar, JFXButton btnView, FuncionarioController myClient, Entity entity,
                ImageView imageView
        ) {
            this.id = id;
            this.index = index;
            this.nome = nome;
            this.nbis = nbis;
            this.endereco = endereco;
            this.telefone = telefone;
            this.ultimaCompra = ultimaCompra;
            this.divida = divida;
            this.myBox = myBox;
            this.btnEliminar = btnEliminar;
            this.btnEditar = btnEditar;
            this.btnView = btnView;
            this.myClient = myClient;
            this.imageView = imageView;
            // this.myclient = myclient;
            this.btnEditar.setStyle(" -fx-background-color:   #24B6F7 ! important;");
            this.btnView.setStyle(" -fx-background-color:   #F0AD4E ! important;");
            this.btnEliminar.setStyle(" -fx-background-color:   #d9534f ! important;");

            // -fx-border-radius: 10 10 10 10;
            //-fx-background-radius: 10 10 10 10;
            if (entity.getPhotoList().isEmpty()) {
                this.imageView.setImage(new Image(Main.class.getResourceAsStream("res/img/information.png"), 30, 30, true, true));
            } else {
                //this.imageView.setImage(new Image(Main.class.getResourceAsStream("res/img/information.png"), 30, 30, true, true));
                File file = new File("Imagens/").getAbsoluteFile();
                this.imageView.setImage(new Image("file:" + Util.configs.getDirector()+"\\" + entity.getPhotoList().get(0).toString(), 30, 30, true, true));
                // this.imageView.setImage(new Image("file:/C:/JBGest/application/JecuzSystem/Imagens/"+entity.getPhotoList().get(0).toString(), 30, 30, true, true));
                //System.out.println("file:" + Util.configs.getDirector() + entity.getPhotoList().get(0).toString());

            }

            // System.out.println(entity.getPath().charAt(entity.getPath().length() - 4));
            this.imageView.setPreserveRatio(false);

            this.imageView.setStyle(" -fx-border-radius: 10 10 10 10");
            this.imageView.setStyle(" -fx-background-radius: 10 10 10 10");

            this.myBox.getChildren().addAll(btnView, btnEditar, btnEliminar);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle(" -fx-cursor: hand;");
            this.entity = entity;

            evento();
        }

        private void evento() {

            btnEditar.setOnAction((event) -> {

                try {

                    FormeFuncionarioController.myentity = this.entity;
                    Util.frameDialog("res/fxml/funcionario/formeFuncionario.fxml");

                    //FormeClientController.getInstance().setMyentity();
                } catch (Exception ex) {
                    Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

            btnEliminar.setOnAction((ActionEvent event) -> {

                Util.opeConnection();
                Util.enti.getTransaction().begin();
                Entity myEntity = new EntityJpaController(Util.emf).findEntity(ItemLista.this.id);
                myEntity.setState(-1);
                Entity remEntity = Util.enti.merge(myEntity);
                // Util.enti.remove(remEntity);
                Util.enti.getTransaction().commit();
                myClient.caregarLista();
                TrayNotification tray = new TrayNotification("Cliente: " + remEntity.getName(),
                        "foi emilinado no sistema... ",
                        NotificationType.SUCCESS);
                //// com.developer.res.css.img
                tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                tray.setRectangleFill(Paint.valueOf("Gray"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
            });

        }

        public Entity getEntity() {
            return entity;
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

        public String getNbis() {
            return nbis;
        }

        public String getEndereco() {
            return endereco;
        }

        public String getTelefone() {
            return telefone;
        }

        public String getUltimaCompra() {
            return ultimaCompra;
        }

        public String getDivida() {
            return divida;
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

        public JFXButton getBtnView() {
            return btnView;
        }

        public FuncionarioController getMyClient() {
            return myClient;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public Client getMyclient() {
            return myclient;
        }

    }

    public static ClienteController myIsntance;

    private void atualizarGrade(Integer pagina) {

        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));
        String queryComands = "SELECT en FROM Entity en WHERE"
                + " en.entityType=2 and en.state=0 order by en.id desc";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        List<Entity> lista = querys.getResultList();

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

    private ObservableList<FornecedorController.ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();

        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComands = "SELECT en FROM Entity en WHERE"
                + " en.entityType=:entityType and en.state=0 order by en.id desc";

        Query querys = Util.enti.createQuery(queryComands, Entity.class).setHint(QueryHints.REFRESH, true).setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        querys.setParameter("entityType", 2);

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

            File file = new File("Imagens/").getAbsoluteFile();

            /// System.out.println(obj.getPhotoList().get(obj.getPhotoList().size() - 1).toString());
            // usando operadores ternarios para identifar campos
            olis.add(new ItemLista(
                    obj.getId(), i,
                    obj.getGenre() != -1 ? "Sr/(a).:  " + obj.getName() : "" + obj.getName(),
                    obj.getNIdetification() == null ? "Não foi encontrado seu nº de identificação ..."
                    : (obj.getNIdetification().isEmpty() ? "Não foi enserido contacto ..." : obj.getNIdetification()),
                    obj.getAddressList().isEmpty() ? "não foi encerido o endereço" : "" + obj.getAddressList().get(obj.getAddressList().size() - 1).toString(),
                    obj.getContactList().isEmpty() ? "Não foi enserido contacto ..."
                    : (obj.getContactList().get(0).getTypeContact() == 2 ? obj.getContactList().get(0).getDescription()
                    : (obj.getContactList().size() > 1 ? (obj.getContactList().get(1).getTypeContact() == 2 ? obj.getContactList().get(1).getDescription()
                    : "Não foi enserido contacto ...") : "Não foi enserido contacto ...")),
                    Util.muedaLocalT("" + obj.getCompra()),
                    Util.muedaLocalT("" + obj.getDividad()),
                    new HBox(5), new JFXButton("", iconPrints3),
                    new JFXButton("", iconPrints2), new JFXButton("", iconPrints),
                    this, obj,
                    obj.getPhotoList().isEmpty() ? new ImageView()
                    : new ImageView(new Image(file.toURI().toString() + obj.getPhotoList().get(0).toString(), 30, 30, false, false))
            ));
        }

        return olis;
    }

    public static FuncionarioController myInstace;

}
