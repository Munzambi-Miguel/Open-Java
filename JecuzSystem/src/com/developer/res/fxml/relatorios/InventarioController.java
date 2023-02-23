/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.relatorios;

import com.developer.java.entity.CaixaMovement;
import com.developer.java.entity.Item;
import com.developer.java.entity.Movement;
import com.developer.java.entity.Product;
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
public class InventarioController implements Initializable {

    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private TableView tableView;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableColumn tc_SN;
    @FXML
    private TableColumn tc_Item;
    @FXML
    private TableColumn tc_entrada;
    @FXML
    private TableColumn tc_saida;
    @FXML
    private TableColumn tc_venda;
    @FXML
    private TableColumn tc_data;
    @FXML
    private TableColumn tc_quantidade;
    @FXML
    private TableColumn tc_acao;
    private final int QUANTIDADE_PAGINA = 13;

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

    /**
     * Nesta area vamos nos basear nas movimentações dos produtos em movimento
     * no nosso sistema bem como as sua alteração de acordo o dia sema mes e ano
     * veja a listagem
     */
    public void caregarLista() {

        tc_SN.setCellValueFactory(
                new PropertyValueFactory("serial")
        );
        tc_Item.setCellValueFactory(
                new PropertyValueFactory("item")
        );
        tc_entrada.setCellValueFactory(
                new PropertyValueFactory("entrada")
        );

        tc_saida.setCellValueFactory(
                new PropertyValueFactory("saida")
        );
        tc_venda.setCellValueFactory(
                new PropertyValueFactory("venda")
        );
        tc_data.setCellValueFactory(
                new PropertyValueFactory("data")
        );
        tc_quantidade.setCellValueFactory(
                new PropertyValueFactory("quantidade")
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

        String queryComands = "SELECT p FROM Product p"
                + " WHERE p.state=0 order by p.id desc";

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

    private ObservableList<RelatorioMovimentoController.ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        String queryComands = "SELECT p FROM Product p"
                + " WHERE p.state=0 order by p.id desc";

        Query querys = Util.enti.createQuery(queryComands, Product.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        List<Product> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();

        for (Product obj : lista) {

            i++;
            FontAwesomeIconView iconPrints = new FontAwesomeIconView();
            FontAwesomeIconView iconPrints2 = new FontAwesomeIconView();

            iconPrints.setGlyphName("EYE");
            iconPrints2.setGlyphName("ANGLE_DOUBLE_LEFT");

            iconPrints.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));
            iconPrints2.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

            olis.add(new ItemLista(
                    obj.getId(),
                    obj.getPartNamber(),
                    obj.toString(),
                    "" + movimentoEntrada(obj),
                    "" + movimentoSaida(obj),
                    "" + quatidadeItens(obj), // Quantidade de Item 
                    "" + movimentoVenda(obj), // Total de Venda
                    "" + obj.getInsertData(),
                    new HBox(3),
                    new JFXButton("", iconPrints2),
                    new JFXButton("", iconPrints),
                    this)
            );
        }

        return olis;
    }

    public final class ItemLista {

        private final Integer id;
        private final String serial;
        private final String item;
        private final String entrada;
        private final String saida;
        private final String quantidade;
        private final String venda;
        private final String data;
        private final HBox myBox;
        private final JFXButton print;
        private final JFXButton btnView;
        private InventarioController instace;

        public ItemLista(Integer id, String serial, String item, String entrada,
                String saida, String quantidade, String venda, String data,
                HBox myBox, JFXButton print, JFXButton btnView, InventarioController instace) {
            this.id = id;
            this.serial = serial;
            this.item = item;
            this.entrada = entrada;
            this.saida = saida;
            this.quantidade = quantidade;
            this.venda = venda;
            this.data = data;
            this.myBox = myBox;
            this.print = print;
            this.btnView = btnView;
            this.instace = instace;

            this.print.setStyle(" -fx-background-color:   #24B6F7 ! important;");
            this.btnView.setStyle(" -fx-background-color:   #F0AD4E ! important;");

            this.myBox.getChildren().addAll(btnView, print);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle(" -fx-cursor: hand;");

            evento();
        }

        public Integer getId() {
            return id;
        }

        public String getSerial() {
            return serial;
        }

        public String getItem() {
            return item;
        }

        public String getEntrada() {
            return entrada;
        }

        public String getSaida() {
            return saida;
        }

        public String getQuantidade() {
            return quantidade;
        }

        public String getVenda() {
            return venda;
        }

        public String getData() {
            return data;
        }

        public HBox getMyBox() {
            return myBox;
        }

        public JFXButton getPrint() {
            return print;
        }

        public JFXButton getBtnView() {
            return btnView;
        }

        public InventarioController getInstace() {
            return instace;
        }

        private void evento() {

        }

    }

    private BigDecimal movimentoEntrada(Product prod) {

        BigDecimal totalEntrada = BigDecimal.valueOf(0);

        for (Movement movEntr : prod.getMovementList()) {

            if (movEntr.getMovimentType() == 0) {
                totalEntrada = movEntr.getDebito().add(totalEntrada);
            }
        }

        return totalEntrada;
    }

    private BigDecimal movimentoSaida(Product prod) {

        BigDecimal totalSaida = BigDecimal.valueOf(0);
        int quantidade = 0;

        for (Movement movSaida : prod.getMovementList()) {

            if (movSaida.getMovimentType() == 1) {
                totalSaida = movSaida.getCredito().add(totalSaida);
                quantidade += movSaida.getQuantidade();
            }
        }

        totalSaida = totalSaida.multiply(BigDecimal.valueOf(quantidade));

        return totalSaida;
    }

    private BigDecimal movimentoVenda(Product prod) {

        BigDecimal totalVenda = BigDecimal.valueOf(0);
        try {
            for (Item movVenda : prod.getItemList()) {

                if (movVenda.getState() == 0) {
                    totalVenda = totalVenda.add(Util.muedaTs(movVenda.getTotal()));
                }
            }
        } catch (Exception e) {
            return BigDecimal.valueOf(0);
        }

        return totalVenda;
    }

    private Double quatidadeItens(Product prod) {
        double quantidades = 0;
        try {

            for (Item movVenda : prod.getItemList()) {

                if (movVenda.getState() == 0) {
                    quantidades += movVenda.getQuantity();
                }
            }
        } catch (Exception e) {
            return 0.0;
        }

        return quantidades;
    }

}
