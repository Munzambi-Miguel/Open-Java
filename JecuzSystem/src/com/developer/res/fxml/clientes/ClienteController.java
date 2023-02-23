/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.clientes;

import com.developer.Main;
import com.developer.java.controller.EntityJpaController;
import com.developer.java.entity.Client;
import com.developer.java.entity.Entity;

import com.developer.res.fxml.fornecimento.FornecedorController;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
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
import javafx.scene.control.Alert;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.eclipse.persistence.config.QueryHints;
import print.Connection;
import print.MyRiports;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class ClienteController implements Initializable {

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
    private TableColumn TableColumnZona;
    @FXML
    private TableColumn TableColumnConatact;
    @FXML
    private TableColumn TableColumnAccao;

    protected EntityJpaController entidadeCliente = new EntityJpaController(Util.emf);
    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private Pagination paginacao;

    private int QUANTIDADE_PAGINA = 17;
    @FXML
    private TableColumn TableColumnCompra;
    @FXML
    private TableColumn TableColumnDivida;
    @FXML
    private JFXButton myPrinte;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myIsntance = this;
        caregarLista();

        // Padrão de definição de tela
        QUANTIDADE_PAGINA = Util.redifinerAIndexTabela();

    }

    @FXML
    private void callingHome(ActionEvent event) throws IOException {
        ((BorderPane) mainBordeClientes).getChildren().clear();
        Util.addViewPage(mainBordeClientes, "res/fxml/inicio/dashboard.fxml");
    }

    @FXML
    private void openModal(ActionEvent event) throws IOException, InterruptedException {
        ///  "res/fxml/clientes/FormeClient.fxml"
        /// Util.addViewPageLeft(myBorderPaneInternar, "res/fxml/clientes/FormeClient.fxml");
        FormeClientController.myentity = null;
        Util.frameDialog("res/fxml/clientes/FormeClient.fxml");
        // Util.frameDialog("res/fxml/produtos/formeProduto.fxml", "", false, false);
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
        TableColumnCompra.setCellValueFactory(
                new PropertyValueFactory("ultimaCompra")
        );
        TableColumnDivida.setCellValueFactory(
                new PropertyValueFactory("divida")
        );
        TableColumnConatact.setCellValueFactory(
                new PropertyValueFactory("telefone")
        );
        TableColumnAccao.setCellValueFactory(
                new PropertyValueFactory("myBox")
        );

        paginacao.setPageFactory((Integer pagina) -> {
            atualizarGrade(pagina);
            return TableViewCliente;
        });

    }

    private void editarClienteTabela(ActionEvent event) throws IOException {

        if (this.TableViewCliente.getSelectionModel().getSelectedItem() != null) {
            Entity entMyentity = ((ItemLista) this.TableViewCliente.getSelectionModel().getSelectedItem()).getEntity();
            Util.addViewPageLeft(myBorderPaneInternar, "res/fxml/clientes/FormeClient.fxml");
            FormeClientController.getInstance().setMyentity(entMyentity);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText("Não selecione a linha na tabela  \n"
                    + "\nAjuda\n\n"
                    + "Clique em alguns do itens acima listado"
                    + "\nSe estiver selecionado clique no botão altarar\n\n");
            alert.showAndWait();
        }

    }

    private void DeletarClienteTabela(ActionEvent event) {
        if (this.TableViewCliente.getSelectionModel().getSelectedItem() != null) {
            Entity entMyentity = ((ItemLista) TableViewCliente.getSelectionModel().getSelectedItem()).getEntity();
            Util.opeConnection();
            Util.enti.getTransaction().begin();
            Entity myEntity = new EntityJpaController(Util.emf).findEntity(entMyentity.getId());
            Entity remEntity = Util.enti.merge(myEntity);
            Util.enti.remove(remEntity);
            Util.enti.getTransaction().commit();

            this.caregarLista();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.setContentText("Não selecione a linha na tabela  \n"
                    + "\nAjuda\n\n"
                    + "Clique em alguns do itens acima listado"
                    + "\nSe estiver selecionado clique no botão deletar\n\n");
            alert.showAndWait();
        }
    }

    /**
     *
     * @param event
     *
     * hele in this method i printing e view information
     *
     */
    @FXML
    private void printingInformation(ActionEvent event) {

        try {

            // Caminho do report num outro jar file
            URL path = MyRiports.class.getResource("jasperFile/factura_termica.jasper");
            HashMap map = new HashMap();

            // map.put("id", "" + f.getId());
            map.put("nome_loja", "");
            map.put("n_contribuente", "");
            map.put("n_telefonico", "");
            map.put("email_loja", "");
            map.put("localizacao_rua", "");

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(path);

            JasperPrint jp = JasperFillManager.fillReport(jasperReport, map, Connection.getConnection());

            int numeroPaginas = jp.getPages().size();
            JasperPrintManager.printPages(jp, 0, numeroPaginas - 1, false);

        } catch (JRException | SQLException ex) {
            TrayNotification tray = new TrayNotification("CODIGO DE ERRO 306: ",
                    "Não Tem Acesso ao sistema nem uma informação"
                    + "erro encontrado na connexão com a impressora\n Inserção corrompida; consulte o suporte www.uteka.jecuz.com\n\n",
                    NotificationType.ERROR);
            //// com.developer.res.css.img
            // tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            // tray.setRectangleFill(Paint.valueOf("#000"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();
        }

    }

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
        private ClienteController myClient;
        private Entity entity;
        private final ImageView imageView;
        private Client myclient;

        public ItemLista(int id, int index, String nome, String nbis,
                String endereco, String telefone,
                String ultimaCompra, String divida, HBox myBox,
                JFXButton btnEliminar, JFXButton btnEditar, JFXButton btnView, ClienteController myClient, Entity entity,
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
                this.imageView.setImage(new Image("file:" + Util.configs.getDirector() + "\\" + entity.getPhotoList().get(0).toString(), 30, 30, true, true));
                System.out.println(entity.getPhotoList().get(0).toString());
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

                    FormeClientController.myentity = this.entity;
                    Util.frameDialog("res/fxml/clientes/FormeClient.fxml");

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

        public ClienteController getMyClient() {
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
                + " en.entityType=1 and en.state=0 order by en.id desc";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
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

    private ObservableList<FornecedorController.ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();

        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComands = "SELECT en FROM Entity en WHERE"
                + " en.entityType=:entityType and en.state=0 order by en.id desc";

        Query querys = Util.enti.createQuery(queryComands, Entity.class).setHint(QueryHints.REFRESH, true).setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        querys.setParameter("entityType", 1);

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
                    obj.getPhotoList().isEmpty() ? new ImageView() : new ImageView(new Image("file:/" + obj.getPhotoList().get(0).toString(), 30, 30, false, false))
            ));
        }

        return olis;
    }

}
