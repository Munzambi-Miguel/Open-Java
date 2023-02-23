/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.saida;

import com.developer.Main;
import com.developer.java.controller.MovementJpaController;
import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Entity;
import com.developer.java.entity.Movement;

import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
public class FormeSaidaStockController implements Initializable {

    @FXML
    private BorderPane myDashboard;
    @FXML
    private JFXButton btn_guardar;
    @FXML
    private JFXTabPane myTabPane;
    @FXML
    private BorderPane myBorderVender;
    @FXML
    private Text nome_label;
    @FXML
    private Text categoria_label1;
    @FXML
    private Text categoria_label2;
    @FXML
    private Text categoria_label3;
    @FXML
    private JFXTextField quantidade_txt;
    @FXML
    private JFXTextField procoCompra_txt;
    @FXML
    private JFXTextField desconto_txt;
    @FXML
    private JFXTextField persentagem_txt;
    @FXML
    private JFXTextField entradas_txtbuscar;

    private Entity entidade;

    private Movement movement;

    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

    @FXML
    private void clossingView(MouseEvent event) {
        this.btn_guardar.getScene().getWindow().hide();
    }

    @FXML
    private void keyEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            this.getLooking();
        }
    }

    @FXML
    private void getObjectiLooking(ActionEvent event) {
        getLooking();
    }

    public void getLooking() {

        try {
            Util.opeConnection();
            Util.enti.getTransaction().begin();
            // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

            String queryComandss = "SELECT m FROM Movement m WHERE"
                    + " m.state=:state AND m.serialNamber=:sirialNumber and m.movimentType=0";

            Query querys = Util.enti.createQuery(queryComandss, Movement.class).setHint(QueryHints.REFRESH, true);

            querys.setParameter("state", 0);
            querys.setParameter("sirialNumber", this.entradas_txtbuscar.getText().trim());

// 
            this.movement = (Movement) querys.getSingleResult();

            this.nome_label.setText("Produto : " + this.movement.toString());
            this.categoria_label1.setText("Entrada : " + Util.dateHoraFormat(this.movement.getDataInsert()) + "");
            this.categoria_label2.setText("Compra : " + Util.muedaLocalT("" + this.movement.getDebito()));
            this.categoria_label3.setText("Existência : " + this.movement.getQuantAtual());

            Util.enti.getTransaction().commit();
            Util.emf.close();

        } catch (Exception e) {

        }

        if (this.movement == null) {

            TrayNotification tray = new TrayNotification("Movimento: ",
                    "Não encontrado Verifique a designação ou a Referencia... ",
                    NotificationType.ERROR);
            //// com.developer.res.css.img
            //tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();
        }

    }

    public boolean validation() {

        boolean flag = true;

        if (this.entradas_txtbuscar.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Certifique-se que encontrou o produto!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.entradas_txtbuscar.getValidators().add(validator);
            this.entradas_txtbuscar.validate();

            flag = false;
        }

        if (this.movement == null) {
            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Certifique-se que encontrou o produto!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.entradas_txtbuscar.getValidators().add(validator);
            this.entradas_txtbuscar.validate();

            flag = false;
        }

        if (this.quantidade_txt.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite a quantidade!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.quantidade_txt.getValidators().add(validator);
            this.quantidade_txt.validate();

            flag = false;

        }

        if (this.procoCompra_txt.getText().isEmpty()) {
            RequiredFieldValidator validatorProcoCompre = new RequiredFieldValidator();
            validatorProcoCompre.setMessage("Porfavor digite o preço de compra!");
            validatorProcoCompre.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.procoCompra_txt.getValidators().add(validatorProcoCompre);
            this.procoCompra_txt.validate();
            flag = false;
        }

        return flag;
    }

    @FXML
    private void darSaidaProduto(ActionEvent event) throws Exception {
        if (movement.getQuantAtual() - Integer.parseInt(this.quantidade_txt.getText()) < 0) {

            TrayNotification tray = new TrayNotification("Movimento de Saida: ",
                    "Movimento cancelado a quantidade excedeu o limite... ",
                    NotificationType.SUCCESS);
            //// com.developer.res.css.img
            tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();

            return;
        } else {
            save();
        }

    }

    public void save() throws NonexistentEntityException, Exception {

        try {

            Util.opeConnection();
            Util.enti.getTransaction().begin();

            // Inserindo o mevimento de entrada no stock
            Movement m = new Movement();

            m.setQuantAtual(Integer.parseInt(this.quantidade_txt.getText().trim()));
            m.setQuantidade(Integer.parseInt(this.quantidade_txt.getText().trim()));

            m.setProductId(movement.getProductId());

            m.setDataInsert(new Date());

            m.setCredito(BigDecimal.valueOf(Double.parseDouble(Util.muedaSendText(this.procoCompra_txt.getText()))));
            m.setSerialNamber(this.movement.getSerialNamber());

            if (!this.persentagem_txt.getText().isEmpty()) {
                m.setPercentagemImposto(BigDecimal.valueOf(Double.parseDouble(this.persentagem_txt.getText().trim())));
            }
            if (!this.desconto_txt.getText().isEmpty()) {
                m.setDesconto(BigDecimal.valueOf(Double.parseDouble(this.desconto_txt.getText().trim())));
            } else {
                m.setDesconto(BigDecimal.ZERO);
            }
            m.setDataFabrica(this.movement.getDataFabrica());
            m.setDataCaducidade(this.movement.getDataCaducidade());
            m.setFornedorId(this.movement.getFornedorId());

            m.setMovimentType(1);
            
            m.setAutenticationId(Util.auts);

            new MovementJpaController(Util.emf).create(m);

            Double valor = Double.parseDouble("" + this.movement.getProductId().getSaidas())
                    + (Double.parseDouble("" + creditoImposto(m)) * m.getQuantidade());

            this.movement.getProductId().setPrecounitario(this.creditoImposto(m));

            int quantidadeProduto = m.getQuantidade() + this.movement.getProductId().getQuantidAtual();

            this.movement.getProductId().setQuantidAtual(quantidadeProduto);
            this.movement.getProductId().setSaidas(BigDecimal.valueOf(valor));
            Util.enti.merge(this.movement.getProductId());

            int quantidadeAtual = this.movement.getQuantAtual() - Integer.parseInt(this.quantidade_txt.getText().trim());
            this.movement.setQuantAtual(quantidadeAtual);
            Util.enti.merge(this.movement);

            Util.enti.getTransaction().commit();
            Util.emf.close();

            SaidaStockController.instance.caregarLista();
            this.entradas_txtbuscar.getScene().getWindow().hide();

            TrayNotification tray = new TrayNotification("Movimento de Saida: ",
                    "Foi realizado com sucesso!... ",
                    NotificationType.SUCCESS);
            //// com.developer.res.css.img
            tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();

        } catch (NumberFormatException e) {
            Util.enti.getTransaction().rollback();

        }

    }

    public BigDecimal creditoImposto(Movement ms) {

        if (ms.getPercentagemImposto() != null) {
            return ms.getCredito().add(
                    ms.getCredito().multiply(
                            ms.getPercentagemImposto().divide(BigDecimal.valueOf(100)
                            )
                    )
            );
        }

        return ms.getCredito();
    }

}
