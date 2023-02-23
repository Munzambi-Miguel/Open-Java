/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.faturacao;

import com.developer.Main;
import com.developer.java.controller.CaixaJpaController;
import com.developer.java.entity.Caixa;
import com.developer.java.entity.Maquina;
import com.developer.util.SelectorList;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
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
public class FormeCaixaController implements Initializable {

    @FXML
    private Text textInforme;
   
    @FXML
    private JFXTextField txt_Administrador;
    @FXML
    private JFXButton guardar;
    @FXML
    private BorderPane myBorderInform;
    @FXML
    private JFXComboBox<Maquina> comb_nuMaquina;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        interativeValidation();
        this.txt_Administrador.setText(Util.auts.getUsername());
      

        select();
    }

    @FXML
    private void clossingView(MouseEvent event) {
        this.myBorderInform.getScene().getWindow().hide();
    }

    @FXML
    private void novaOperacaoCaixa(ActionEvent event) throws IOException {
        if (validation()) {

            salvar();

        }

    }

    public void select() {

        comb_nuMaquina.setItems(SelectorList.maquiSelector());
    }

    public boolean validation() {
        boolean flag = true;

        
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

      

    }

    private void salvar() throws IOException {

        Util.opeConnection();
        Util.enti.getTransaction().begin();

        try {

            Caixa caixa = new Caixa();
            caixa.setAutenticationId(Util.auts);
            caixa.setDataInsert(new Date());

            caixa.setMaquinaId(this.comb_nuMaquina.getSelectionModel().getSelectedItem());
            new CaixaJpaController(Util.emf).create(caixa);

            TrayNotification tray = new TrayNotification("A Criação do Caixa: ",
                    "Foi realizado com sucesso!... ",
                    NotificationType.SUCCESS);
            tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();

            Maquina as = this.comb_nuMaquina.getSelectionModel().getSelectedItem();

            as.setState(0);
            Util.enti.merge(as);

        } catch (Exception e) {
            TrayNotification tray = new TrayNotification("A Criação do Caixa: ",
                    "Falhou a operação de Criação do Caixa!... ",
                    NotificationType.ERROR);

            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();
        }
        Util.enti.getTransaction().commit();
        Util.emf.close();

        this.textInforme.getScene().getWindow().hide();
        CaixaController.instance.caregarLista();
    }

}
