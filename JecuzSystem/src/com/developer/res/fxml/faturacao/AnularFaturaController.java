/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.faturacao;

import com.developer.java.entity.Fatura;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
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
public class AnularFaturaController implements Initializable {

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
    private TableColumn tc_operador;
    @FXML
    private TableColumn tc_cliente;
    @FXML
    private TableColumn tc_fatura;
    @FXML
    private TableColumn tc_total;
    @FXML
    private TableColumn tc_entrege;
    @FXML
    private TableColumn tc_troco;
    @FXML
    private TableColumn tc_tipo;
    @FXML
    private TableColumn tc_datahora;
    @FXML
    private TableColumn tc_acao;

    private final int QUANTIDADE_PAGINA = 13;
    
    public static AnularFaturaController instace;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        caregarLista();
        instace = this;
    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

    @FXML
    private void openModal(ActionEvent event) throws IOException, InterruptedException {

        // com.developer.res.fxml.faturacao
        Util.frameDialog("res/fxml/faturacao/formeAnulamentoFactura.fxml");
    }

    public void caregarLista() {

        tc_ordem.setCellValueFactory(
                new PropertyValueFactory("index")
        );
        tc_operador.setCellValueFactory(
                new PropertyValueFactory("operador")
        );

        tc_cliente.setCellValueFactory(
                new PropertyValueFactory("cliente")
        );
        tc_fatura.setCellValueFactory(
                new PropertyValueFactory("fatura")
        );
        tc_total.setCellValueFactory(
                new PropertyValueFactory("total")
        );
        tc_entrege.setCellValueFactory(
                new PropertyValueFactory("entrege")
        );

        tc_troco.setCellValueFactory(
                new PropertyValueFactory("troco")
        );
        tc_tipo.setCellValueFactory(
                new PropertyValueFactory("tipo")
        );
        tc_datahora.setCellValueFactory(
                new PropertyValueFactory("data")
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

        String queryComands = "SELECT p FROM Fatura p WHERE"
                + " p.state=-10 order by p.id desc";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        List<Fatura> lista = querys.getResultList();

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

        String queryComandss = "SELECT p FROM Fatura p WHERE"
                + " p.state=:state order by p.id desc";

        Query querys = Util.enti.createQuery(queryComandss, Fatura.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        querys.setParameter("state", -10);

        List<Fatura> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        for (Fatura obj : lista) {

            i++;
            FontAwesomeIconView iconPrints = new FontAwesomeIconView();
            FontAwesomeIconView iconPrints2 = new FontAwesomeIconView();

            iconPrints.setGlyphName("EYE");
            iconPrints2.setGlyphName("ANGLE_DOUBLE_LEFT");

            iconPrints.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));
            iconPrints2.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

            olis.add(new ItemLista(
                    obj.getId(), i, obj.getAdmin().getUsername(), obj.getCliente(), obj.getFatura(),
                    Util.muedaLocalT(obj.getTotal() + ""), Util.muedaLocalT(obj.getValorPago() + ""),
                    Util.muedaLocalT(obj.getTroco() + ""), obj.getTipoPagamento() ? "Multicaixa" : "Numerario",
                    Util.dateHoraFormat(obj.getDataInsert()),
                    new HBox(3),
                    new JFXButton("", iconPrints2),
                    new JFXButton("", iconPrints)
            )
            );
        }

        return olis;
    }

    public final class ItemLista {

        private final int id;
        private final int index;
        private final String operador;
        private final String cliente;
        private final String fatura;
        private final String total;
        private final String entrege;
        private final String troco;
        private final String tipo;
        private final String data;
        private final HBox myBox;
        private final JFXButton btnEditar;
        private final JFXButton btnView;
        private AnularFaturaController instace;
        private Fatura entity;

        public ItemLista(int id, int index, String operador, String cliente,
                String fatura, String total, String entrege,
                String troco, String tipo, String data,
                HBox myBox, JFXButton btnEditar, JFXButton btnView) {
            this.id = id;
            this.index = index;
            this.operador = operador;
            this.cliente = cliente;
            this.fatura = fatura;
            this.total = total;
            this.entrege = entrege;
            this.troco = troco;
            this.tipo = tipo;
            this.data = data;
            this.myBox = myBox;
            this.btnEditar = btnEditar;
            this.btnView = btnView;

            this.btnEditar.setStyle(" -fx-background-color:   #24B6F7 ! important;");
            this.btnView.setStyle(" -fx-background-color:   #F0AD4E ! important;");

            this.myBox.getChildren().addAll(btnView, btnEditar);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle(" -fx-cursor: hand;");

            evento();

        }

        private void evento() {

            btnEditar.setOnAction((ActionEvent event) -> {

            });
        }

        public int getId() {
            return id;
        }

        public int getIndex() {
            return index;
        }

        public String getOperador() {
            return operador;
        }

        public String getCliente() {
            return cliente;
        }

        public String getFatura() {
            return fatura;
        }

        public String getTotal() {
            return total;
        }

        public String getEntrege() {
            return entrege;
        }

        public String getTroco() {
            return troco;
        }

        public String getTipo() {
            return tipo;
        }

        public String getData() {
            return data;
        }

        public HBox getMyBox() {
            return myBox;
        }

        public JFXButton getBtnEditar() {
            return btnEditar;
        }

        public JFXButton getBtnView() {
            return btnView;
        }

        public AnularFaturaController getInstace() {
            return instace;
        }

        public Fatura getEntity() {
            return entity;
        }

    }

}
