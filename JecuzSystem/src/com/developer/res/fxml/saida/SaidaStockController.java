/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.saida;

import com.developer.java.entity.Movement;
import com.developer.res.fxml.clientes.ClienteController;
import com.developer.res.fxml.fornecimento.FornecedorController;
import com.developer.res.fxml.produtos.ProdutosController;
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
public class SaidaStockController implements Initializable {

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
    private TableColumn tc_order;
    @FXML
    private TableColumn tc_designacao;
    @FXML
    private TableColumn tc_quantidade;

    @FXML
    private TableColumn tc_imposto;
    @FXML
    private TableColumn tc_validade;
    @FXML
    private TableColumn tc_nuserial;

    @FXML
    private TableColumn tc_acao;

    private final int QUANTIDADE_PAGINA = 17;
    @FXML
    private TableColumn tc_unidade;
    @FXML
    private TableColumn tc_total;
    @FXML
    private TableColumn tc_diferença;

    public static SaidaStockController instance;
    @FXML
    private TableColumn tc_descont;
    @FXML
    private TableColumn tc_tdesconto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        caregarLista();
        instance = this;
    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

    @FXML
    private void openModal(ActionEvent event) throws IOException, InterruptedException {
        Util.frameDialog("res/fxml/saida/formeSaidaStock.fxml");
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
        tc_imposto.setCellValueFactory(
                new PropertyValueFactory("percentage")
        );
        tc_validade.setCellValueFactory(
                new PropertyValueFactory("validade")
        );
        tc_nuserial.setCellValueFactory(
                new PropertyValueFactory("n_serial")
        );
        tc_unidade.setCellValueFactory(
                new PropertyValueFactory("unidade")
        );

        tc_diferença.setCellValueFactory(
                new PropertyValueFactory("diferenca")
        );
        tc_descont.setCellValueFactory(
                new PropertyValueFactory("desconto")
        );
        tc_tdesconto.setCellValueFactory(
                new PropertyValueFactory("totalDesconto")
        );

        tc_total.setCellValueFactory(
                new PropertyValueFactory("total")
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
                + " m.movimentType=1 and m.state=0 ";

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
                + " m.movimentType=1 and m.state=:state order by m.id desc";

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

            olis.add(new ItemLista(
                    obj.getId(), i,
                    obj.getProductId() != null ? obj.toString()
                    : "Elemento de entrada não identificado",
                    obj.getQuantAtual() < 0 ? 0 : obj.getQuantAtual(),
                    Util.muedaLocalT("" + obj.getCredito()),
                    obj.getPercentagemImposto() == null ? "0 %" : obj.getPercentagemImposto() + " %",
                    obj.getDataFabrica() != null && obj.getDataCaducidade() != null ? "Fab "
                    + Util.dataFormat(obj.getDataFabrica()) + "/ Exp "
                    + Util.dataFormat(obj.getDataCaducidade()) : "Não foi inserido a validade ... ",
                    obj.getSerialNamber(),
                    Util.muedaLocalT(""
                            + new BigDecimal("" + obj.getCredito())
                                    .multiply(new BigDecimal("" + (obj.getQuantidade() - obj.getQuantAtual()))
                                    )
                    ),
                    obj.getQuantAtual(),
                    calcularImposto(obj),
                    new HBox(5), new JFXButton("", iconPrints3), new JFXButton("", iconPrints2), new JFXButton("", iconPrints),
                    this, obj,
                    obj.getDesconto() == null ? Util.muedaLocalT("0") : Util.muedaLocalT("" + obj.getDesconto()),
                    calcularImpostoDesconto(obj)
            )
            );
        }

        return olis;
    }

    public final class ItemLista {

        private final int id;
        private final int ordem;
        private final String designacao;
        private final String validade;
        private final String n_serial;
        private final int quantidade;
        private final String unidade;
        private final String percentage;
        private final String total;
        private final String diferenca;
        private final int atualQuantidade;
        private final HBox myBox;
        private final JFXButton btnEliminar;
        private final JFXButton btnEditar;
        private final JFXButton btnView;
        private Movement mymovement;
        private SaidaStockController myInstances;
        private final String desconto;
        private final String totalDesconto;

        public ItemLista(int id, int ordem,
                String designacao,
                int quantidade,
                String unidade,
                String percentage,
                String validade,
                String n_serial,
                String diferenca,
                int atualQuantidade,
                String total,
                HBox myBox, JFXButton btnEliminar,
                JFXButton btnEditar, JFXButton btnView,
                SaidaStockController myInstances, Movement mymovement,
                String desconto,
                String totalDesconto
        ) {
            this.id = id;
            this.ordem = ordem;
            this.designacao = designacao;
            this.validade = validade;
            this.n_serial = n_serial;
            this.quantidade = quantidade;
            this.unidade = unidade;
            this.percentage = percentage;
            this.total = total;
            this.desconto = desconto;
            this.totalDesconto = totalDesconto;
            this.diferenca = diferenca;
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

        public int getId() {
            return id;
        }

        public int getOrdem() {
            return ordem;
        }

        public String getDesignacao() {
            return designacao;
        }

        public String getValidade() {
            return validade;
        }

        public String getN_serial() {
            return n_serial;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public String getUnidade() {
            return unidade;
        }

        public String getPercentage() {
            return percentage;
        }

        public String getTotal() {
            return total;
        }

        public String getDiferenca() {
            return diferenca;
        }

        public int getAtualQuantidade() {
            return atualQuantidade;
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

        public SaidaStockController getMyInstances() {
            return myInstances;
        }

        public String getDesconto() {
            return desconto;
        }

        public String getTotalDesconto() {
            return totalDesconto;
        }

    }

    /**
     *
     * @param obj
     * @return mudar com ou sem inposto essa função tem a finalidade de calcular
     * o valor coso tenha imposto ou não vejamos as instruções a baixo
     *
     */
    public String calcularImposto(Movement obj) {

        if ((obj.getPercentagemImposto()) != null) {

            return Util.muedaLocalT(""
                    + new BigDecimal(
                            "" + obj.getCredito()).add(
                            new BigDecimal("" + obj.getCredito()).multiply(
                                    new BigDecimal("" + obj.getPercentagemImposto()).divide(
                                            BigDecimal.valueOf(100)
                                    )
                            )
                    ).multiply(
                            new BigDecimal("" + obj.getQuantAtual())
                    )
            );
        } else {

            return Util.muedaLocalT("" + new BigDecimal("" + obj.getCredito())
                    .multiply(new BigDecimal("" + obj.getQuantAtual())));
        }

    }

    public String calcularImpostoDesconto(Movement obj) {

        if ((obj.getPercentagemImposto()) != null && obj.getDesconto() != null) {

            return Util.muedaLocalT(""
                    + new BigDecimal(
                            "" + (new BigDecimal("" + obj.getCredito())
                                    .subtract(new BigDecimal("" + obj.getDesconto())))).add(
                            new BigDecimal("" + (new BigDecimal("" + obj.getCredito())
                                    .subtract(new BigDecimal("" + obj.getDesconto())))).multiply(
                                    new BigDecimal("" + obj.getPercentagemImposto()).divide(
                                            BigDecimal.valueOf(100)
                                    )
                            )
                    ).multiply(
                            new BigDecimal("" + obj.getQuantAtual())
                    )
            );
        } else {

            return Util.muedaLocalT("" + new BigDecimal("" + obj.getCredito())
                    .multiply(new BigDecimal("" + obj.getQuantAtual())));
        }

    }

}
