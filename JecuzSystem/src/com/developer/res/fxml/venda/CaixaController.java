/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.venda;

import com.developer.Main;
import com.developer.java.controller.CaixaJpaController;
import com.developer.java.controller.CaixaMovementJpaController;
import com.developer.java.entity.Autentication;
import com.developer.java.entity.Caixa;
import com.developer.java.entity.CaixaMovement;
import com.developer.res.fxml.dashboad.MainController;
import com.developer.util.IdentifyEntity;
import com.developer.util.MeusFicheiros;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
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
public class CaixaController implements Initializable {

    @FXML
    private Text textInforme;
    @FXML
    private JFXTextField txt_operador;
    @FXML
    private JFXTextField txt_saldoInicial;
    @FXML
    private JFXTextField txt_Administrador;
    @FXML
    private JFXPasswordField txt_senha;
    @FXML
    private JFXButton guardar;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.txt_operador.setText(Util.auts.getUsername());
        this.txt_operador.setFocusTraversable(false);
        this.txt_saldoInicial.setFocusTraversable(true);
        interativeValidation();

        this.txt_saldoInicial.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                String saldo = txt_saldoInicial.getText().trim();
                if (!saldo.isEmpty()) {
                    txt_saldoInicial.setText(Util.muedaLocalT(saldo));
                }
            }
        });
    }

    @FXML
    private void clossingView(MouseEvent event) {
        this.textInforme.getScene().getWindow().hide();
    }

    @FXML
    private void novaOperacaoCaixa(ActionEvent event) throws Exception {

        if (validation()) {

            salvar();

        }
    }

    public boolean validation() {
        boolean flag = true;

        if (this.txt_saldoInicial.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("campo obrigatório!...");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_saldoInicial.getValidators().add(validator);
            this.txt_saldoInicial.validate();
            flag = false;
        }
        if (this.txt_Administrador.getText().isEmpty()) {
            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("campo obrigatório!...");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_Administrador.getValidators().add(validator);
            this.txt_Administrador.validate();

            flag = false;
        }
        if (this.txt_senha.getText().isEmpty()) {
            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("campo obrigatório!...");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_senha.getValidators().add(validator);
            this.txt_senha.validate();

            flag = false;
        }

        return flag;
    }

    public void interativeValidation() {

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Campo obrigatório!");
        validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());

        this.txt_Administrador.getValidators().add(validator);
        this.txt_Administrador.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.txt_Administrador.validate();

            }
        });

        this.txt_senha.getValidators().add(validator);
        this.txt_senha.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.txt_senha.validate();
            }
        });
        this.txt_saldoInicial.getValidators().add(validator);
        this.txt_saldoInicial.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.txt_saldoInicial.validate();

            }
        });

    }

    private void salvar() throws IOException {

        Util.opeConnection();
        Util.enti.getTransaction().begin();

        Autentication admin = IdentifyEntity.validation(this.txt_Administrador.getText().trim(),
                this.txt_senha.getText().trim()
        );

        if (admin != null) {

            Caixa caixa = new Caixa();

            caixa.setAutenticationId(Util.auts);

            Autentication autAdim = IdentifyEntity.validation(
                    this.txt_Administrador.getText().trim(), this.txt_senha.getText().trim()
            );

            if (autAdim == null) {

                TrayNotification tray = new TrayNotification("Abertura do Caixa: ",
                        "Falhou pois a senha ou Pass do administrador é incompativel verifique suas credencias!... ",
                        NotificationType.ERROR);
                tray.setRectangleFill(Paint.valueOf("Gray"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
                tray.showAndWait();

                return;
            }

           // caixa.setAdministrador(autAdim);
           // caixa.setDataHoraAbertura(new Date());

            CaixaMovement cm = new CaixaMovement();

            cm.setCaixaId(caixa);
            cm.setCorrentValue(BigDecimal.ZERO);
            cm.setInitialValue(Util.muedaLocalTRecerc(this.txt_saldoInicial.getText().trim()));
            cm.setInformacao(
                    "Operação de abertura do caixa"
                    + "Funcionario de Open.Caixa: " + caixa.getAutenticationId().getEntityid().toString()
                  //  + "Funcionario Administrador: " + caixa.getAdministrador().getEntityid().toString()
                  ///  + "Tarefa efetuada data e hora _" + caixa.getDataHoraAbertura()
            );

            new CaixaJpaController(Util.emf).create(caixa);
            new CaixaMovementJpaController(Util.emf).create(cm);

            if (MeusFicheiros.vazio(Util.openCaixa)) {
                MeusFicheiros.gravarDados(caixa, Util.openCaixa);

                TrayNotification tray = new TrayNotification("Abertura do Caixa: ",
                        "Foi realizado com sucesso!... ",
                        NotificationType.SUCCESS);
                tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                tray.setRectangleFill(Paint.valueOf("Gray"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
                tray.showAndWait();
                
                Util.addViewPage(MainController.myStaticBorder, "res/fxml/venda/vendaView.fxml");
            }

        } else {

            TrayNotification tray = new TrayNotification("Abertura do Caixa: ",
                    "Falhou pois a senha ou Pass do administrador é incompativel verifique suas credencias!... ",
                    NotificationType.ERROR);
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();
        }

        Util.enti.getTransaction().commit();
        Util.emf.close();
        this.textInforme.getScene().getWindow().hide();
    }

}
