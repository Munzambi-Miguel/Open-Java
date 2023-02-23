/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.venda;

import com.developer.java.entity.Product;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
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
public class VendaViewController implements Initializable {

    @FXML
    private BorderPane myBorderVenda;
    @FXML
    private TitledPane containerSelectItem;
    @FXML
    private Label totalAvendaLab;
    public static Label totalVendaLabelStatic;

    @FXML
    private BorderPane myInternalViewVendaBorder;
    @FXML
    private JFXTextField buscar_produto;
    public static JFXComboBox buscarProduto;

    private Product myPro;

    public static ObservableList<ItemCar> myList = FXCollections.observableArrayList();
    @FXML
    private TableColumn tc_item;
    @FXML
    private TableColumn tc_proco;
    @FXML
    private TableColumn tc_quantidade;
    @FXML
    private TableColumn tc_total;
    @FXML
    private TableColumn tc_acao;

    @FXML
    private TableView<ItemCar> tableView;
    public static TableView<ItemCar> myTableView;

    @FXML
    private JFXButton btn_faturar;
    @FXML
    private Text informacao_total;

    public static Text informacaototalInstance;
    @FXML
    private Label userNames;
    public static Label userNamesStatic;
    @FXML
    private JFXButton btn_busca;
    @FXML
    private VBox vbLateral;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Dimension Tela = Toolkit.getDefaultToolkit().getScreenSize();

        if (Tela.height >= 900) {
            myInternalViewVendaBorder.setMaxHeight(myInternalViewVendaBorder.USE_COMPUTED_SIZE);
        }

        informacaototalInstance = this.informacao_total;

        try {
            this.caregarLista();

        } catch (Exception e) {
        }

        buscar_produto.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (!this.buscar_produto.getText().isEmpty()) {
                    addingCar(this.buscar_produto.getText());
                }
            }

        });
        myTableView = this.tableView;

        this.buscar_produto.setFocusTraversable(true);
        this.btn_busca.setFocusTraversable(true);

    }

    @FXML
    private void selecioneItem(ActionEvent event) {
        if (!this.buscar_produto.getText().isEmpty()) {
            addingCar(this.buscar_produto.getText());
        }
    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

    private void addingCar(Object os) {

        try {
            // com.developer.res.fxml.venda
            if (!this.buscar_produto.getText().isEmpty()) {
                if (this.getProduts(this.buscar_produto.getText()) != null) {

                    this.buscar_produto.clear();
                    this.buscar_produto.setFocusTraversable(false);
                    this.tableView.setFocusTraversable(true);

                    Util.frameDialog("res/fxml/venda/ProductView.fxml");
                }
            }

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public Product getProduts(String os) {

        try {

            Product pro = null;
            Util.opeConnection();
            Util.enti.getTransaction().begin();

            String queryComands = "SELECT en FROM Product en WHERE"
                    + " en.state=0 and en.quantidAtual>0 and en.partNamber=:partNamber";

            Query querys = Util.enti.createQuery(queryComands, Product.class).setHint(QueryHints.REFRESH, true);
            querys.setParameter("partNamber", os);

            pro = (Product) querys.getSingleResult();
            ProductViewController.obj = pro;
            Util.enti.getTransaction().commit();
            Util.emf.close();
            return pro;

        } catch (Exception e) {

            TrayNotification tray = new TrayNotification("Codigo Infalido: ",
                    "Verifque a existência desté produto no stock ou montra\n"
                    + "Veja que não atingio o estado crítico!... \n\n ",
                    NotificationType.ERROR);
            //tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();

            return null;
        }
    }

    public void caregarLista() {

        tc_item.setCellValueFactory(
                new PropertyValueFactory("itemName")
        );
        tc_proco.setCellValueFactory(
                new PropertyValueFactory("preco")
        );

        tc_quantidade.setCellValueFactory(
                new PropertyValueFactory("quantidade")
        );
        tc_total.setCellValueFactory(
                new PropertyValueFactory("total")
        );
        tc_acao.setCellValueFactory(
                new PropertyValueFactory("myBox")
        );

        try {
            tableView.setItems(myList);
        } catch (Exception e) {
        }

    }

    @FXML
    private void faturarJA(ActionEvent event) {
        if (!myList.isEmpty()) {
            try {
                Util.frameDialog("res/fxml/venda/finalizarVenda.fxml");
            } catch (IOException | InterruptedException ex) {
                TrayNotification tray = new TrayNotification("CODIGO DE ERRO 231: ",
                        "Não Tem Acesso ao sistema nem uma informação"
                        + "erro encontrado na Janela\n Ficheiro corrompido consulte o suporte www.uteka.jecuz.com\n\n",
                        NotificationType.ERROR);
                //// com.developer.res.css.img
                // tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                // tray.setRectangleFill(Paint.valueOf("#000"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
                tray.showAndWait();
            }
        } else {

        }
    }
    private static BigDecimal tatal = new BigDecimal("0");

    public static void sumTotal() {

        Double var = 0.0;
        for (ItemCar itemCar : myList) {

            var += Double.parseDouble("" + Util.muedaLocalTRecerc(itemCar.getTotal()));

            System.out.println(" ");
        }

        VendaViewController.informacaototalInstance.setText(
                "Total: " + Util.muedaLocalT("" + var)
        );

    }

    @FXML
    private void ferramentaBusca(ActionEvent event) throws Exception {
        // com.developer.res.fxml.venda
        Util.frameDialog("res/fxml/venda/produtAddCar.fxml");

    }

}
