/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.entrada;

import com.developer.java.entity.Movement;

import com.developer.res.fxml.clientes.ClienteController;
import com.developer.res.fxml.fornecimento.FornecedorController;
import com.developer.res.fxml.produtos.ProdutosController;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class EntradaStockController implements Initializable {

    @FXML
    private BorderPane myDashboard;
    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn tc_designacao;
    @FXML
    private TableColumn tc_quantidade;
    @FXML
    private TableColumn tc_custo;
    @FXML
    private TableColumn tc_imposto;
    @FXML
    private TableColumn tc_validade;
    @FXML
    private TableColumn tc_nuserial;
    @FXML
    private TableColumn tc_dataInsert;
    @FXML
    private TableColumn tc_acao;
    @FXML
    private TableColumn tc_order;

    private final int QUANTIDADE_PAGINA = 17;
    @FXML
    private TableColumn tc_quantidadeAtual;

    public static EntradaStockController instance;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        instance = this;
        caregarLista();
    }

    @FXML
    private void openModal(ActionEvent event) throws IOException, InterruptedException {

        // com.developer.res.fxml.entrada
        Util.frameDialog("res/fxml/entrada/formeEntrada.fxml");
    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

    private void verEntradas(ActionEvent event) {

    }

    public void caregarLista() {

        tc_order.setCellValueFactory(
                new PropertyValueFactory("ordem")
        );
        tc_designacao.setCellValueFactory(
                new PropertyValueFactory("designacao")
        );
        tc_quantidade.setCellValueFactory(
                new PropertyValueFactory("quantidade")
        );
        tc_custo.setCellValueFactory(
                new PropertyValueFactory("custo")
        );
        tc_imposto.setCellValueFactory(
                new PropertyValueFactory("percentage")
        );
        tc_validade.setCellValueFactory(
                new PropertyValueFactory("validade")
        );
        tc_nuserial.setCellValueFactory(
                new PropertyValueFactory("n_serial")
        );
        tc_dataInsert.setCellValueFactory(
                new PropertyValueFactory("dataInsert")
        );
        tc_quantidadeAtual.setCellValueFactory(
                new PropertyValueFactory("atualQuantidade")
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
                + " m.movimentType=0 OR  m.movimentType=15 and m.state=0 ";

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

    private ObservableList<FornecedorController.ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComandss = "SELECT m FROM Movement m WHERE"
                + " m.movimentType=0 OR  m.movimentType=15 and m.state=:state order by m.id desc";

        Query querys = Util.enti.createQuery(queryComandss, Movement.class).setHint(QueryHints.REFRESH, true).setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        querys.setParameter("state", 0);

        List<Movement> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        for (Movement obj : lista) {

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

            if (obj.getDataFabrica() == null && obj.getDataCaducidade() == null) {
                tc_validade.setStyle("-fx-alignment:  CENTER_LEFT  ! important");
            } else {
                tc_validade.setStyle("-fx-alignment:  CENTER_RIGHT  ! important");
            }

            olis.add(new ItemLista(
                    obj.getId(), i,
                    obj.getProductId() != null ? obj.toString() : "Elemento de entrada não identificado",//+ ,
                    obj.getQuantidade() < 0 ? 0 : obj.getQuantidade(),
                    obj.getDebito() != null ? Util.muedaLocal("" + obj.getDebito()) : "0,00 Akz",
                    obj.getPercentagemImposto() != null ? obj.getPercentagemImposto() + " %" : "0.0 %",
                    obj.getDataFabrica() != null && obj.getDataCaducidade() != null ? "Fab " + Util.dataFormat(obj.getDataFabrica()) + "/ Exp "
                    + Util.dataFormat(obj.getDataCaducidade()) : "Fab " +Util.dataFormat(new Date())+"/ Exp " +Util.dataFormat(new Date()),
                    obj.getSerialNamber().isEmpty() ? "Não tem nº Serial..." : obj.getSerialNamber(),
                    Util.dataFormat(obj.getDataInsert()),
                    obj.getQuantAtual() == null ? 0 : obj.getQuantAtual(),
                    new HBox(5), new JFXButton("", iconPrints3), new JFXButton("", iconPrints2), new JFXButton("", iconPrints),
                    this, obj
            ));
        }

        return olis;
    }

    public final class ItemLista {

        private final int id;
        private final int ordem;
        private final String designacao;
        private final int quantidade;
        private final String custo;
        private final String percentage;
        private final String validade;
        private final String n_serial;
        private final String dataInsert;
        private final int atualQuantidade;
        private final HBox myBox;
        private final JFXButton btnEliminar;
        private final JFXButton btnEditar;
        private final JFXButton btnView;
        private Movement mymovement;
        private EntradaStockController myInstances;

        public ItemLista(int id, int ordem, String designacao, int quantidade, String custo, String percentage, String validade,
                String n_serial, String dataInsert, int atualQuantidade, HBox myBox, JFXButton btnEliminar,
                JFXButton btnEditar, JFXButton btnView, EntradaStockController myInstances, Movement mymovement) {
            this.id = id;
            this.ordem = ordem;
            this.designacao = designacao;
            this.quantidade = quantidade;
            this.custo = custo;
            this.percentage = percentage;
            this.validade = validade;
            this.n_serial = n_serial;
            this.dataInsert = dataInsert;
            this.myBox = myBox;
            this.btnEliminar = btnEliminar;
            this.btnEditar = btnEditar;
            this.btnView = btnView;
            this.mymovement = mymovement;
            this.myInstances = myInstances;
            this.atualQuantidade = atualQuantidade;

            this.btnEditar.setStyle(" -fx-background-color:   #24B6F7 ! important;");
            this.btnView.setStyle(" -fx-background-color:   #F0AD4E ! important;");
            this.btnEliminar.setStyle(" -fx-background-color:   #d9534f ! important;");

            this.myBox.getChildren().addAll(btnView, btnEditar, btnEliminar);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle(" -fx-cursor: hand;");

            evento();
        }

        public int getId() {
            return id;
        }

        public int getOrdem() {
            return ordem;
        }

        public String getDesignacao() {
            return designacao;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public String getCusto() {
            return custo;
        }

        public String getPercentage() {
            return percentage;
        }

        public String getValidade() {
            return validade;
        }

        public String getN_serial() {
            return n_serial;
        }

        public String getDataInsert() {
            return dataInsert;
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

        public Movement getMymovement() {
            return mymovement;
        }

        public EntradaStockController getMyInstances() {
            return myInstances;
        }

        public int getAtualQuantidade() {
            return atualQuantidade;
        }

        private void evento() {

            btnEditar.setOnAction((ActionEvent event) -> {
                try {

                    Util.frameDialog("res/fxml/produtos/formeProduto.fxml");

                    //
                } catch (IOException ex) {
                    Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            btnEliminar.setOnAction((ActionEvent event) -> {

                /*  Util.opeConnection();
               // Util.enti.getTransaction().begin();
                Product myEntity = new ProductJpaController(Util.emf).findProduct(this.id);
                myEntity.setState(-1);
                Product remEntity = Util.enti.merge(myEntity);

                Util.enti.getTransaction().commit();
                Util.emf.close();

                TrayNotification tray = new TrayNotification("Produto: ",
                        "Foi Removido no sistema... ",
                        NotificationType.SUCCESS);
                //// com.developer.res.css.img
                tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                tray.setRectangleFill(Paint.valueOf("Gray"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
                tray.showAndWait();*/
            });

        }

    }

}
