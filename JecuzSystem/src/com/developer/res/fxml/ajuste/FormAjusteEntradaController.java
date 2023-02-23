/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.ajuste;

import com.developer.Main;
import com.developer.java.controller.MovementJpaController;
import com.developer.java.entity.Movement;
import com.developer.java.entity.Product;

import com.developer.util.Util;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class FormAjusteEntradaController implements Initializable {

    @FXML
    private BorderPane myBorderPane;
    @FXML
    private Text textInforme;
    @FXML
    private JFXTextField txt_produto;
    @FXML
    private Text lb_produto;
    @FXML
    private Text lb_categoria;
    @FXML
    private Text lb_categoria2;
    @FXML
    private Text lb_categoria3;
    @FXML
    private Text lb_categoria4;
    @FXML
    private JFXTextField txt_quantidade;
    @FXML
    private JFXTextArea txt_detalhes;

    public static Product myProduct;
    public static Movement myMovement;
    @FXML
    private JFXTextField txt_snEntrada;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        txt_quantidade.setMouseTransparent(true);
        txt_quantidade.setText("" + myMovement.getQuantidade());
        carregarObjs();
        validation();

    }

    @FXML
    private void guardarSaidaEmLote(ActionEvent event) {

        if (validationForme()) {

            try {
                Util.opeConnection();
                Util.enti.getTransaction().begin();

                Movement mc = new Movement();

                mc.setMovimentType(15);
                mc.setAutenticationId(Util.auts);
                mc.setDataInsert(new Date());
                mc.setQuantidade(Integer.parseInt(this.txt_quantidade.getText().trim()));
                mc.setQuantAtual(Integer.parseInt(this.txt_quantidade.getText().trim()));
                mc.setInformacao(this.txt_detalhes.getText().trim());
                mc.setProductId(myProduct);
                mc.setSerialNamber(this.txt_snEntrada.getText().trim());

                new MovementJpaController(Util.emf).create(mc);

              //  int qt = myProduct.getQuantidAtual() + Integer.parseInt(this.txt_quantidade.getText().trim());

                Movement mov = myMovement;

                mov.setMovimentType(14);
                Util.enti.merge(mov);

               // myProduct.setQuantidAtual(qt);
               // Util.enti.merge(myProduct);

                Util.enti.getTransaction().commit();
                Util.emf.close();

                ProdutosMontraController.instance.caregarLista();
                ProdutosRetiradosController.instance.caregarLista();
                this.myBorderPane.getScene().getWindow().hide();

                TrayNotification tray = new TrayNotification("Produto: ",
                        "Saida efectuado com sucesso... ",
                        NotificationType.SUCCESS);
                //// com.developer.res.css.img
                tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                tray.setRectangleFill(Paint.valueOf("Gray"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
                tray.showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    @FXML
    private void clossingView(MouseEvent event) {
        this.myBorderPane.getScene().getWindow().hide();
    }

    public void carregarObjs() {
        this.txt_produto.setText(myProduct.toString());
        this.lb_produto.setText(myProduct.getProdutTypeid().getDesignation());
        this.lb_categoria.setText(myProduct.getNivel3Id().getDesignation());
        this.lb_categoria2.setText(myProduct.getNivel3Id().getNivel2Id().getDesignation());
        this.lb_categoria3.setText(myProduct.getNivel3Id().getNivel2Id().getNivel1Id().getDesignation());
        this.lb_categoria4.setText("Qt. " + myProduct.getQuantidAtual());
    }

    public void validation() {

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("campo obrigatorio!");
        validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.txt_quantidade.getValidators().add(validator);
        this.txt_quantidade.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.txt_quantidade.validate();

            }
        });

        RequiredFieldValidator validatorProcoCompre = new RequiredFieldValidator();
        validatorProcoCompre.setMessage("campo obrigatorio!");
        validatorProcoCompre.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.txt_detalhes.getValidators().add(validatorProcoCompre);
        this.txt_detalhes.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.txt_detalhes.validate();
            }

        });

        RequiredFieldValidator validatorProcoCompres = new RequiredFieldValidator();
        validatorProcoCompres.setMessage("campo obrigatorio!");
        validatorProcoCompres.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.txt_snEntrada.getValidators().add(validatorProcoCompres);
        this.txt_snEntrada.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.txt_snEntrada.validate();
            }

        });

    }

    public boolean validationForme() {

        boolean flag = true;

        if (this.txt_quantidade.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Certifique-se que encontrou o produto!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_quantidade.getValidators().add(validator);
            this.txt_quantidade.validate();

            flag = false;
        }

        if (this.txt_detalhes.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite a quantidade!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_detalhes.getValidators().add(validator);
            this.txt_detalhes.validate();

            flag = false;

        }
        if (this.txt_snEntrada.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite a quantidade!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_snEntrada.getValidators().add(validator);
            this.txt_snEntrada.validate();

            flag = false;

        }

        return flag;
    }

}
