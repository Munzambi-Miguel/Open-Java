/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.venda;

import com.developer.Main;
import com.developer.java.controller.AberturaDiaJpaController;
import com.developer.java.controller.CaixaMovementJpaController;
import com.developer.java.controller.FaturaJpaController;
import com.developer.java.controller.ItemJpaController;
import com.developer.java.entity.AberturaDia;
import com.developer.java.entity.CaixaMovement;
import com.developer.java.entity.Fatura;
import com.developer.java.entity.Item;
import static com.developer.res.fxml.venda.VendaViewController.myList;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
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
public class FinalizarVendaController implements Initializable {

    @FXML
    private BorderPane myDashboard;
    @FXML
    private Text textInforme;
    @FXML
    private JFXTextField tota_apagar;
    @FXML
    private JFXTextField cliente_nome;
    @FXML
    private JFXComboBox tipo_pagamento;
    @FXML
    private JFXTextField valor_entrege;
    @FXML
    private JFXTextField num_fatura;
    @FXML
    private JFXButton addCar_btn;
    @FXML
    private TableColumn tc_item;
    @FXML
    private TableColumn tc_quantidade;
    @FXML
    private TableColumn tc_preco;
    @FXML
    private TableColumn tc_total;
    @FXML
    private Text infor_troco;
    @FXML
    private TableView tableView;
    @FXML
    private HBox barcodeImage;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        aux();
    }

    public void aux() {

        propietyTableView();
        sumTotal();

        tipo_pagamento.setValue("NUMERARIO");
        
        this.valor_entrege.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                try {
                    this.valor_entrege.setText(Util.muedaLocalT(
                            this.valor_entrege.getText().replace(" ", "")
                    ));
                } catch (Exception e) {
                }

            }
        });

        this.valor_entrege.setOnKeyPressed((event) -> {

            if (event.getCode() == KeyCode.ENTER) {
                this.valor_entrege.setText(Util.muedaLocalT(
                        this.valor_entrege.getText()
                ));
            }
        });

        Util.opeConnection();
        Util.enti.getTransaction().begin();
        if (!new FaturaJpaController(Util.emf).findFaturaEntities().isEmpty()) {
            this.num_fatura.setText((Util.dataFormat(new Date()).replace("-", "")
                    + "#FACTN" + (new FaturaJpaController(Util.emf).findFaturaEntities().get(
                            new FaturaJpaController(Util.emf).findFaturaEntities().size() - 1).getId() + 1))
            );
        } else {
            this.num_fatura.setText((Util.dataFormat(new Date()).replace("-", "")
                    + "#FACTN" + 1)
            );
        }

        Util.enti.getTransaction().commit();
        Util.emf.close();

        this.valor_entrege.setOnKeyReleased((event) -> {
            if (!tipo_pagamento.getSelectionModel().getSelectedItem().equals("MULTCAIXA")) {
                try {
                    BigDecimal valor = Util.muedaLocalTRecerc(this.tota_apagar.getText());
                    this.infor_troco.setText("Troco: "
                            + Util.muedaLocalT("" + new BigDecimal(this.valor_entrege.getText()).subtract(valor)
                            ));
                } catch (Exception as) {

                } finally {
                }
            }

        });

        List<Label> listNode = new ArrayList();
        try {
            String name = this.num_fatura.getText();
            for (int i = 0; i < name.length(); i++) {
                Label e = new Label("" + name.charAt(i));
                listNode.add(e);
            }

            this.barcodeImage.getChildren().addAll(listNode);
        } catch (Exception e) {
        }

        tipo_pagamento.setItems(FXCollections.observableArrayList("NUMERARIO", "MULTCAIXA"));
    }

    @FXML
    private void clossingView(MouseEvent event) {
        this.myDashboard.getScene().getWindow().hide();
    }

    public void propietyTableView() {

        tc_item.setCellValueFactory(
                new PropertyValueFactory("itemName")
        );
        tc_preco.setCellValueFactory(
                new PropertyValueFactory("preco")
        );

        tc_quantidade.setCellValueFactory(
                new PropertyValueFactory("quantidade")
        );
        tc_total.setCellValueFactory(
                new PropertyValueFactory("total")
        );

        this.tableView.setItems(VendaViewController.myList);

    }

    public void sumTotal() {

        Double var = 0.0;
        for (ItemCar itemCar : myList) {
            var += Double.parseDouble("" + Util.muedaLocalTRecerc(itemCar.getTotal()));
        }

        this.tota_apagar.setText(
                Util.muedaLocalT("" + var)
        );

    }

    @FXML
    private void finalizarFaturaMetodo(ActionEvent event) {

        ///  --[EL Info]: connection: 2019-11-04 23:12:12.114 --ServerSession(976187709)        
        gerarFatura();
    }

    public void gerarFatura() {

        try {
            Util.opeConnection();
            Util.enti.getTransaction().begin();

            Fatura f = new Fatura();
            f.setFatura(this.num_fatura.getText());
            f.setCliente(this.cliente_nome.getText().trim());
            f.setDataInsert(new Date());
            f.setTotal(Util.muedaLocalTRecerc(this.tota_apagar.getText()));
            try {

                f.setTroco(Util.muedaLocalTRecerc(this.infor_troco.getText().replace("Troco: ", "")));
            } catch (Exception e) {
                f.setTroco(BigDecimal.ZERO);
            }

            if (tipo_pagamento.getSelectionModel().getSelectedItem().equals("MULTCAIXA")) {
                f.setValorPago(Util.muedaLocalTRecerc(this.tota_apagar.getText()));
                f.setNumEtiqueta(this.valor_entrege.getText());
                f.setTipoPagamento(true);
            } else {
                f.setValorPago(Util.muedaLocalTRecerc(this.valor_entrege.getText()));
                f.setTipoPagamento(false);
            }

            f.setNumEtiqueta(this.num_fatura.getText());

            f.setFuncionarioId(Util.auts);
            new FaturaJpaController(Util.emf).create(f);

            for (ItemCar itemCar : myList) {
                Item itemDoCarry = new Item();

                itemDoCarry.setFaturaId(f);
                itemDoCarry.setProductId(itemCar.getProduct());

                itemDoCarry.setQuantity(itemCar.getQuantidade());

                itemDoCarry.setProduto(itemCar.toString());
                itemDoCarry.setTotal(itemCar.getTotal());

                itemCar.getProduct().setQuantidAtual((itemCar.getProduct().getQuantidAtual() - itemCar.getQuantidade()));
                itemCar.getProduct().setFaturacao(Util.muedaLocalTRecerc(itemCar.getTotal()));

                Util.enti.merge(itemCar.getProduct());
                try {
                    new ItemJpaController(Util.emf).create(itemDoCarry);
                } catch (Exception e) {
                    TrayNotification tray = new TrayNotification("CODIGO DE ERRO 262: ",
                            "Não Tem Acesso ao sistema nem uma informação"
                            + "erro encontrado na connexão\n Inserção corrompida; consulte o suporte www.uteka.jecuz.com\n\n",
                            NotificationType.ERROR);
                    //// com.developer.res.css.img
                    // tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                    // tray.setRectangleFill(Paint.valueOf("#000"));
                    tray.setAnimationType(AnimationType.POPUP);
                    tray.showAndDismiss(Duration.seconds(3));
                    tray.showAndWait();
                }

            }

            int num = new FaturaJpaController(Util.emf).findFaturaEntities().get(new FaturaJpaController(Util.emf).findFaturaEntities().size() - 1).getId();
            AberturaDia abs = new AberturaDiaJpaController(Util.emf).findAberturaDia("" + InetAddress.getLocalHost().getHostAddress());
            CaixaMovement movcaixa = new CaixaMovement();

            movcaixa.setCaixaId(
                    abs.getCaixaMovementId().getCaixaId()
            );

            movcaixa.setCorrentValue(f.getTotal());
            movcaixa.setInitialValue(
                    abs.getCaixaMovementId().getInitialValue()
            );
            movcaixa.setDataInsert(new Date());

            movcaixa.setAutenticationId(Util.auts);

            movcaixa.setTipoMovimento(20);

            movcaixa.setInformacao(
                    "Operação de Faturacao no caixa "
                    + "Tarefa efetuada data e hora_ " + movcaixa.getDataInsert()
            );

            movcaixa.setFaturaId(f);

            new CaixaMovementJpaController(Util.emf).create(movcaixa);

            printJasp(new FaturaJpaController(Util.emf).findFaturaEntities()
                    .get(new FaturaJpaController(Util.emf).findFaturaEntities().size() - 1)
            );

            TrayNotification tray = new TrayNotification("Faturação: ",
                    "Finalizado com sucesso Aguarde a impressão... ",
                    NotificationType.SUCCESS);
            //// com.developer.res.css.img
            tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();

            myList.clear();

            this.myDashboard.getScene().getWindow().hide();

            Util.enti.getTransaction().commit();
            Util.emf.close();
        } catch (Exception e) {

            e.printStackTrace();
            Util.enti.getTransaction().rollback();
        }

    }

    private void printJasp(Fatura f) {

        try {
            URL path = MyRiports.class.getResource("jasperFile/factura_termica.jasper");
            HashMap map = new HashMap();

            map.put("id", "" + f.getId());
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

    @FXML
    private void tipoVenda(ActionEvent event) {

        if (tipo_pagamento.getSelectionModel().getSelectedItem().equals("MULTCAIXA")) {
            valor_entrege.setPromptText("Nº de Recibo do TPA * .");
        } else {
            valor_entrege.setPromptText("Valor Entrege * .");
        }
    }

}
