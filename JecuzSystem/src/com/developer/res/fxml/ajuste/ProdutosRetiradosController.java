/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.ajuste;

import com.developer.Main;
import com.developer.java.entity.Movement;
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
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class ProdutosRetiradosController implements Initializable {

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
    private TableColumn tc_datahora;
    @FXML
    private TableColumn tc_funcionario;
    @FXML
    private TableColumn tc_informacao;
    @FXML
    private TableColumn tc_accao;

    private final int QUANTIDADE_PAGINA = 13;

    public static ProdutosRetiradosController instance;

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

        tc_datahora.setCellValueFactory(
                new PropertyValueFactory("datahora")
        );
        tc_funcionario.setCellValueFactory(
                new PropertyValueFactory("funcionario")
        );
        tc_informacao.setCellValueFactory(
                new PropertyValueFactory("informacao")
        );

        tc_accao.setCellValueFactory(
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
                + " m.movimentType=11 and m.state=0";

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

        String queryComands = "SELECT m FROM Movement m WHERE"
                + " m.movimentType=11 and m.state=0 ";

        Query querys = Util.enti.createQuery(queryComands, Movement.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        List<Movement> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();

        System.out.println(olis);
        for (Movement obj : lista) {

            i++;
            FontAwesomeIconView iconPrints3 = new FontAwesomeIconView();

            iconPrints3.setGlyphName("ANGLE_DOUBLE_LEFT");

            iconPrints3.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

            olis.add(new ItemLista(
                    obj.getId(),
                    i,
                    obj.getProductId().toString(),
                    "" + obj.getQuantidade(),
                    "" + Util.dateHoraFormat(obj.getDataInsert()),
                    "" + (obj.getAutenticationId() == null ? "Funcionário não identificado ..." : obj.getAutenticationId().getEntityid().getName()),
                    "" + (obj.getInformacao() == null ? "informação não inserida no sistema ..." : obj.getInformacao()),
                    new HBox(5),
                    new JFXButton("", iconPrints3),
                    this,
                    obj,
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
        private final String datahora;
        private final String funcionario;
        private final String informacao;
        private final HBox myBox;
        private final JFXButton btnEliminar;
        private ProdutosRetiradosController myInstance;
        private Movement entity;
        private final ImageView imageView;

        public ItemLista(
                int id,
                int index,
                String produto,
                String quantidade,
                String datahora,
                String funcionario,
                String informacao,
                HBox myBox, JFXButton btnEliminar,
                ProdutosRetiradosController myInstance,
                Movement entity,
                ImageView imageView) {
            this.id = id;
            this.index = index;
            this.produto = produto;
            this.quantidade = quantidade;
            this.datahora = datahora;
            this.funcionario = funcionario;
            this.informacao = informacao;
            this.myBox = myBox;
            this.btnEliminar = btnEliminar;
            this.entity = entity;
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

            this.btnEliminar.setStyle(" -fx-background-color:   #d9534f ! important;");

            this.myBox.getChildren().addAll(btnEliminar);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle(" -fx-cursor: hand;");

            evento();
        }

        private void evento() {

            btnEliminar.setOnAction((ActionEvent event) -> {
                try {
                    // com.developer.res.fxml.ajuste
                    FormAjusteEntradaController.myMovement = this.entity;
                    FormAjusteEntradaController.myProduct = this.entity.getProductId();

                    Util.frameDialog("res/fxml/ajuste/formAjusteEntrada.fxml");
                } catch (IOException ex) {
                    Logger.getLogger(ProdutosMontraController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProdutosMontraController.class.getName()).log(Level.SEVERE, null, ex);
                }

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

        public String getDatahora() {
            return datahora;
        }

        public String getFuncionario() {
            return funcionario;
        }

        public String getInformacao() {
            return informacao;
        }

        public HBox getMyBox() {
            return myBox;
        }

        public JFXButton getBtnEliminar() {
            return btnEliminar;
        }

        public ProdutosRetiradosController getMyInstance() {
            return myInstance;
        }

        public Movement getEntity() {
            return entity;
        }

        public ImageView getImageView() {
            return imageView;
        }

    }

}
