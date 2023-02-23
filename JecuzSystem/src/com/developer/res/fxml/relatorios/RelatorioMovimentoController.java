/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.relatorios;

import com.developer.Main;
import com.developer.java.entity.Fatura;
import com.developer.java.entity.Movement;
import com.developer.res.fxml.ajuste.ProdutosMontraController;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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
public class RelatorioMovimentoController implements Initializable {

    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private TableView tableView;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableColumn tc_ordem;
    @FXML
    private TableColumn tc_movimento;
    @FXML
    private TableColumn tc_saldo;
    @FXML
    private TableColumn tc_imposto;
    @FXML
    private TableColumn tc_dataHora;
    @FXML
    private TableColumn tc_entidade;
    @FXML
    private TableColumn tc_acao;
    @FXML
    private TableColumn tc_serialNumber;

    private final int QUANTIDADE_PAGINA = 13;
    @FXML
    private TableColumn tc_produto;

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
    private void callingHome(ActionEvent event) {
    }

    @FXML
    private void openModal(ActionEvent event) {
    }

    public void caregarLista() {

        tc_ordem.setCellValueFactory(
                new PropertyValueFactory("index")
        );
        tc_produto.setCellValueFactory(
                new PropertyValueFactory("produto")
        );
        tc_movimento.setCellValueFactory(
                new PropertyValueFactory("tipoMovimento")
        );

        tc_saldo.setCellValueFactory(
                new PropertyValueFactory("saldo")
        );
        tc_imposto.setCellValueFactory(
                new PropertyValueFactory("imposto")
        );
        tc_dataHora.setCellValueFactory(
                new PropertyValueFactory("dataHora")
        );
        tc_entidade.setCellValueFactory(
                new PropertyValueFactory("entidade")
        );
        tc_serialNumber.setCellValueFactory(
                new PropertyValueFactory("serialNumber")
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
                + " m.state=0 AND m.movimentType=0 OR m.movimentType=1 OR m.movimentType=15";

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
                + " m.state=0 AND m.movimentType=0 OR m.movimentType=1 OR m.movimentType=15";

        Query querys = Util.enti.createQuery(queryComands, Movement.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        List<Movement> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();

        for (Movement obj : lista) {

            i++;
            FontAwesomeIconView iconPrints = new FontAwesomeIconView();
            iconPrints.setGlyphName("EYE");

            iconPrints.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

            
            try {
                olis.add(
                        new ItemLista(
                                obj.getId(),
                                i,
                                "" + obj.getProductId(),
                                "" + (obj.getMovimentType() == 0 ? "( Stock ) - Movimento de Entrada " :
                                       ( obj.getMovimentType() == 1 ? "( Stock ) - Movimento de Saida ":
                                        ( obj.getMovimentType() == 15 ? "( Stock ) - Movimento de Transferncia ":"" ))),
                                "" + (obj.getCredito() != null ? "+ " + Util.muedaLocalT("" + obj.getCredito().multiply(BigDecimal.valueOf(obj.getQuantidade()))) :
                                      ( obj.getDebito()==null?"-- / --": ("- " + Util.muedaLocalT("" + obj.getDebito())))),
                                "" + (obj.getPercentagemImposto() == null ? "-- / --" : obj.getPercentagemImposto() + " %"),
                                "" + Util.dateHoraFormat(obj.getDataInsert()),
                                "" + (obj.getAutenticationId() == null ? "-- / --" : obj.getAutenticationId().getEntityid()),
                                "" + (obj.getSerialNamber() == null ? "-- / --" : obj.getSerialNamber()),
                                new HBox(3),
                                new JFXButton("", iconPrints)
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(obj+", "+obj.getCredito()+" tipo "+obj.getMovimentType());
            }
        }

        return olis;
    }

    public final class ItemLista {

        private final int id;
        private final int index;
        private final String produto;
        private final String tipoMovimento;
        private final String saldo;
        private final String imposto;
        private final String dataHora;
        private final String entidade;
        private final String serialNumber;
        private final HBox myBox;
        private final JFXButton viewButton;
        private RelatorioMovimentoController myInstance;
        private Movement entity;

        public ItemLista(int id, int index, String produto, String tipoMovimento,
                String saldo, String imposto, String dataHora,
                String entidade, String serialNumber, HBox myBox, JFXButton viewButton
        ) {
            this.id = id;
            this.index = index;
            this.produto = produto;
            this.tipoMovimento = tipoMovimento;
            this.saldo = saldo;
            this.imposto = imposto;
            this.dataHora = dataHora;
            this.entidade = entidade;
            this.serialNumber = serialNumber;
            this.myBox = myBox;
            this.viewButton = viewButton;

            this.viewButton.setStyle(" -fx-background-color:  #24B6F7  ! important;");

            this.myBox.getChildren().addAll(viewButton);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle(" -fx-cursor: hand;");

            evento();
        }

        private void evento() {

            viewButton.setOnAction((ActionEvent event) -> {

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

        public String getTipoMovimento() {
            return tipoMovimento;
        }

        public String getSaldo() {
            return saldo;
        }

        public String getImposto() {
            return imposto;
        }

        public String getDataHora() {
            return dataHora;
        }

        public String getEntidade() {
            return entidade;
        }

        public HBox getMyBox() {
            return myBox;
        }

        public JFXButton getViewButton() {
            return viewButton;
        }

        public RelatorioMovimentoController getMyInstance() {
            return myInstance;
        }

        public Movement getEntity() {
            return entity;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public String getProduto() {
            return produto;
        }

    }

}
