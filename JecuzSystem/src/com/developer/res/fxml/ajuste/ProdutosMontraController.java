/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.ajuste;

import com.developer.Main;
import com.developer.java.controller.ProductJpaController;
import com.developer.java.entity.Movement;
import com.developer.java.entity.Product;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.math.BigDecimal;
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
public class ProdutosMontraController implements Initializable {

    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn tc_ordem;
    @FXML
    private TableColumn tc_produto;
    @FXML
    private TableColumn tc_quantidade;
    @FXML
    private TableColumn tc_preco;
    @FXML
    private TableColumn tc_total;
    @FXML
    private TableColumn tc_acao;
    @FXML
    private TableColumn tc_imposto;
    @FXML
    private TableColumn tc_comImposto;

    private final int QUANTIDADE_PAGINA = 13;
    
    public static ProdutosMontraController instance;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        caregarLista();
        instance = this;
    }

    public void caregarLista() {

        tc_ordem.setCellValueFactory(
                new PropertyValueFactory("imageView")
        );
        tc_produto.setCellValueFactory(
                new PropertyValueFactory("produto")
        );

        tc_quantidade.setCellValueFactory(
                new PropertyValueFactory("quantidade")
        );
        tc_preco.setCellValueFactory(
                new PropertyValueFactory("procoUnitario")
        );
        tc_imposto.setCellValueFactory(
                new PropertyValueFactory("imposto")
        );
        tc_total.setCellValueFactory(
                new PropertyValueFactory("total")
        );
        tc_comImposto.setCellValueFactory(
                new PropertyValueFactory("totalImposto")
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

        String queryComands = "SELECT m FROM Movement m WHERE"
                + " m.movimentType=1 and m.state=0 GROUP BY m.productId";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        List<Movement> lista = querys.getResultList();

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

    private ObservableList<ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComandss = "SELECT m "
                + "FROM Movement m WHERE"
                + " m.movimentType=1 and m.state=0  GROUP BY m.productId";

        Query querys = Util.enti.createQuery(queryComandss, Movement.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        List<Movement> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        
        System.out.println(olis);
        for (Movement obj : lista) {

            i++;
            FontAwesomeIconView iconPrints = new FontAwesomeIconView();
            FontAwesomeIconView iconPrints2 = new FontAwesomeIconView();
            FontAwesomeIconView iconPrints3 = new FontAwesomeIconView();

            iconPrints.setGlyphName("EYE");
            iconPrints2.setGlyphName("ANGLE_DOUBLE_RIGHT");
            iconPrints3.setGlyphName("TRASH_ALT");

            iconPrints.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));
            iconPrints2.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));
            iconPrints3.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

            olis.add(new ItemLista(
                    obj.getId(),
                    i,
                    obj.getProductId().toString(),
                    "" + obj.getProductId().getQuantidAtual(),
                    "" + Util.muedaLocalT("" + obj.getProductId().getPrecounitario()),
                    "" + obj.getPercentagemImposto(),
                    "" + Util.muedaLocalT("" + obj.getCredito().multiply(BigDecimal.valueOf(obj.getProductId().getQuantidAtual()))),
                    ""+ Util.calcularImposto(obj),
                    new HBox(5),
                    new JFXButton("", iconPrints3), new JFXButton("", iconPrints2), new JFXButton("", iconPrints),
                    this,
                    obj,
                    new VBox(3),
                    obj.getProductId().getPhotoList().isEmpty()
                    ? new ImageView()
                    : new ImageView(new Image("file:/" + obj.getProductId().getPhotoList()
                            .get(0).toString(), 30, 30, false, false))
            ));
        }

        return olis;
    }

    public final class ItemLista {

        private final int id;
        private final int index;
        private final String produto;
        private final String quantidade;
        private final String procoUnitario;
        private final String imposto;
        private final String total;
        private final String totalImposto;
        private final HBox myBox;
        private final JFXButton btnEliminar;
        private final JFXButton btnEditar;
        private final JFXButton btnView;
        private ProdutosMontraController myInstance;
        private Movement entity;
        private final VBox myBoxImagem;
        private final ImageView imageView;

        public ItemLista(
                int id,
                int index,
                String produto,
                String quantidade,
                String procoUnitario,
                String imposto,
                String total,
                String totalImposto,
                HBox myBox, JFXButton btnEliminar,
                JFXButton btnEditar, JFXButton btnView,
                ProdutosMontraController myInstance,
                Movement entity,
                VBox myBoxImagem, ImageView imageView) {
            this.id = id;
            this.index = index;
            this.produto = produto;
            this.quantidade = quantidade;
            this.procoUnitario = procoUnitario;
            this.imposto = imposto;
            this.total = total;
            this.totalImposto = totalImposto;
            this.myBox = myBox;
            this.btnEliminar = btnEliminar;
            this.btnEditar = btnEditar;
            this.myInstance = myInstance;
            this.entity = entity;
            this.btnView = btnView;
            this.myBoxImagem = myBoxImagem;
            this.imageView = imageView;

            this.imageView.setClip(null);
            this.imageView.setCursor(Cursor.HAND);

            // -fx-border-radius: 10 10 10 10;
            //-fx-background-radius: 10 10 10 10;
            if (entity.getProductId().getPhotoList().isEmpty()) {
                this.imageView.setImage(new Image(Main.class.getResourceAsStream("res/img/information.png"), 30, 30, true, true));
            } else {
               this.imageView.setImage(new Image("file:" + Util.configs.getDirector() + "\\" + entity.getProductId().getPhotoList().get(0).toString(), 30, 30, true, true));
                System.out.println(entity.getProductId().getPhotoList().get(0).toString());
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
                    // com.developer.res.fxml.ajuste
                    FormAjusteController.myProduct = ItemLista.this.entity.getProductId();
                    Util.frameDialog("res/fxml/ajuste/formAjuste.fxml");
                } catch (IOException ex) {
                    Logger.getLogger(ProdutosMontraController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProdutosMontraController.class.getName()).log(Level.SEVERE, null, ex);
                }
               
            });

            btnEliminar.setOnAction((ActionEvent event) -> {

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

        public String getProduto() {
            return produto;
        }

        public String getQuantidade() {
            return quantidade;
        }

        public String getProcoUnitario() {
            return procoUnitario;
        }

        public String getImposto() {
            return imposto;
        }

        public String getTotal() {
            return total;
        }

        public String getTotalImposto() {
            return totalImposto;
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

        public ProdutosMontraController getMyInstance() {
            return myInstance;
        }

        public Movement getEntity() {
            return entity;
        }

        public VBox getMyBoxImagem() {
            return myBoxImagem;
        }

        public ImageView getImageView() {
            return imageView;
        }

    }
}
