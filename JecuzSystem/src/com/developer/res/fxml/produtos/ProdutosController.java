/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.produtos;

import com.developer.Main;
import com.developer.java.controller.ProductJpaController;
import com.developer.java.entity.Product;
import com.developer.res.fxml.clientes.ClienteController;
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
import javafx.scene.Cursor;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
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
public class ProdutosController implements Initializable {

    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn TebleColumnOrder;
    @FXML
    private TableColumn TableColumndesignacao;

    @FXML
    private TableColumn TableColumnAccao;

    private final int QUANTIDADE_PAGINA = 13;

    public static ProdutosController instace;
    @FXML
    private TableColumn TableColumnSaida;
    @FXML
    private TableColumn TableColumnUnitario;
    @FXML
    private Text informação;
    @FXML
    private TableColumn TableColumnNumRef;
    @FXML
    private TableColumn TableColumnFaturamento;
    @FXML
    private TableColumn TableColumnQuantidade;
    @FXML
    private TableColumn TableColumnEntrada;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        instace = this;
        caregarLista();
    }

    @FXML
    private void callingHome(ActionEvent event) throws IOException {

    }

    @FXML
    private void openModal(ActionEvent event) throws IOException, InterruptedException {

        // com.developer.res.fxml.produtos
        FormeProdutoController.myProduct = null;
        Util.frameDialog("res/fxml/produtos/formeProduto.fxml");
    }

    public void caregarLista() {

        TebleColumnOrder.setCellValueFactory(
                new PropertyValueFactory("imageView")
        );
        TableColumndesignacao.setCellValueFactory(
                new PropertyValueFactory("desigancao")
        );

        TableColumnEntrada.setCellValueFactory(
                new PropertyValueFactory("entrada")
        );
        TableColumnFaturamento.setCellValueFactory(
                new PropertyValueFactory("faturamento")
        );
        TableColumnNumRef.setCellValueFactory(
                new PropertyValueFactory("serialN")
        );
        TableColumnQuantidade.setCellValueFactory(
                new PropertyValueFactory("quantidade")
        );

        TableColumnSaida.setCellValueFactory(
                new PropertyValueFactory("saida")
        );
        TableColumnUnitario.setCellValueFactory(
                new PropertyValueFactory("unitario")
        );

        TableColumnAccao.setCellValueFactory(
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

        String queryComands = "SELECT p FROM Product p WHERE"
                + " p.state=0 order by p.id desc";

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
        private final String serialN;
        private final String faturamento;
        private final String quantidade;
        private final String entrada;
        private final String saida;
        private final HBox myBox;
        private final JFXButton btnEliminar;
        private final JFXButton btnEditar;
        private final JFXButton btnView;
        private ProdutosController myClient;
        private Product entity;
        private final VBox myBoxImagem;
        private final ImageView imageView;
        private final String unitario;

        public ItemLista(int id,
                int index,
                String desigancao,
                String serialN,
                String faturamento,
                String quantidade,
                String entrada,
                String saida,
                HBox myBox, JFXButton btnEliminar,
                JFXButton btnEditar, JFXButton btnView, ProdutosController myClient,
                Product entity,
                VBox myBoxImagem, ImageView imageView, String valorUnitario) {
            this.id = id;
            this.index = index;
            this.desigancao = desigancao;
            this.faturamento = faturamento;
            this.quantidade = quantidade;
            this.entrada = entrada;
            this.saida = saida;
            this.myBox = myBox;
            this.btnEliminar = btnEliminar;
            this.btnEditar = btnEditar;
            this.myClient = myClient;
            this.entity = entity;
            this.btnView = btnView;
            this.myBoxImagem = myBoxImagem;
            this.imageView = imageView;
            this.unitario = valorUnitario;
            this.serialN = serialN;

            this.imageView.setClip(null);
            this.imageView.setCursor(Cursor.HAND);

            // -fx-border-radius: 10 10 10 10;
            //-fx-background-radius: 10 10 10 10;
            if (entity.getPhotoList().isEmpty()) {
                this.imageView.setImage(new Image(Main.class.getResourceAsStream("res/img/information.png"), 30, 30, true, true));
            } else {
                // "file:" + Util.configs.getDirector()+"\\" +
                this.imageView.setImage(new Image("file:" + Util.configs.getDirector() + "\\" + entity.getPhotoList().get(0).toString(), 30, 30, true, true));
                System.out.println(entity.getPhotoList().get(0).toString());
            }

            // System.out.println(entity.getPath().charAt(entity.getPath().length() - 4));
            this.imageView.setPreserveRatio(false);

            this.imageView.setStyle(" -fx-border-radius: 10 10 10 10");
            this.imageView.setStyle(" -fx-background-radius: 10 10 10 10");

            this.myBoxImagem.setStyle(" -fx-background-color:   #24B6F7 ! important;");
            this.btnEditar.setStyle(" -fx-background-color:   #24B6F7 ! important;");
            this.btnView.setStyle(" -fx-background-color:   #F0AD4E ! important;");
            this.btnEliminar.setStyle(" -fx-background-color:   #d9534f ! important;");

            this.myBox.getChildren().addAll(btnView, btnEditar, btnEliminar);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle(" -fx-cursor: hand;");

            evento();
        }

        private void evento() {

            btnEditar.setOnAction((ActionEvent event) -> {
                try {
                    FormeProdutoController.myProduct = ItemLista.this.entity;
                    Util.frameDialog("res/fxml/produtos/formeProduto.fxml");

                    //
                } catch (IOException ex) {
                    Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            btnEliminar.setOnAction((ActionEvent event) -> {

                Util.opeConnection();
                Util.enti.getTransaction().begin();
                Product myEntity = new ProductJpaController(Util.emf).findProduct(this.id);
                myEntity.setState(-1);
                Product remEntity = Util.enti.merge(myEntity);

                Util.enti.getTransaction().commit();
                myClient.caregarLista();

                TrayNotification tray = new TrayNotification("Produto: ",
                        "Foi Removido no sistema... ",
                        NotificationType.SUCCESS);
                //// com.developer.res.css.img
                tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                tray.setRectangleFill(Paint.valueOf("Gray"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
                tray.showAndWait();

            });

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

        public String getFaturamento() {
            return faturamento;
        }

        public String getQuantidade() {
            return quantidade;
        }

        public String getEntrada() {
            return entrada;
        }

        public String getSaida() {
            return saida;
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

        public ProdutosController getMyClient() {
            return myClient;
        }

        public Product getEntity() {
            return entity;
        }

        public VBox getMyBoxImagem() {
            return myBoxImagem;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public String getUnitario() {
            return unitario;
        }

        public String getSerialN() {
            return serialN;
        }

    }

    private ObservableList<ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComandss = "SELECT p FROM Product p WHERE"
                + " p.state=:state order by p.id desc";

        Query querys = Util.enti.createQuery(queryComandss, Product.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        querys.setParameter("state", 0);

        List<Product> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        for (Product obj : lista) {

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

            System.out.println(Util.muedaLocal("" + obj.getEntradada()));
            olis.add(new ItemLista(
                    obj.getId(), i,
                    obj.getProdutTypeid().toString() + ", "
                    + (obj.getNivel3Id() == null ? " " : " " + obj.getNivel3Id()) + " "
                    + (obj.getNivel3Id() == null ? " " : (obj.getNivel3Id().getNivel2Id() == null ? " "
                    : " " + obj.getNivel3Id().getNivel2Id())) + " ",
                    obj.getPartNamber() == null ? "não foi inserido o nº de referencia" : obj.getPartNamber(),
                    "" + Util.muedaLocalT("" + obj.getFaturacao()),
                    "" + obj.getQuantidAtual(),
                    "" + Util.muedaLocalT("" + obj.getEntradada()),
                    "" + Util.muedaLocalT("" + obj.getSaidas()),
                    new HBox(5), new JFXButton("", iconPrints3), new JFXButton("", iconPrints2), new JFXButton("", iconPrints),
                    this, obj,
                    new VBox(3),
                    obj.getPhotoList().isEmpty() ? new ImageView() : new ImageView(new Image("file:/" + obj.getPhotoList().get(0).toString(), 30, 30, false, false)),
                    Util.muedaLocal("" + obj.getPrecounitario())
            ));
        }

        return olis;
    }
}
