/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.faturacao;

import com.developer.java.controller.AberturaDiaJpaController;
import com.developer.java.controller.CaixaMovementJpaController;
import com.developer.java.entity.AberturaDia;
import com.developer.java.entity.Caixa;
import com.developer.java.entity.CaixaMovement;
import com.developer.util.Util;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class OpenCaixaMovimentController implements Initializable {

    @FXML
    private JFXTextField tx_userInstance;
    @FXML
    private JFXTextField txt_valorInicial;
    @FXML
    private BorderPane myLayout;

    public static Caixa myCaixa;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        instanceValidation();
        this.tx_userInstance.setText(Util.auts.getUsername());
        try {
            txt_valorInicial.setOnKeyPressed((event) -> {
                if (event.getCode() == KeyCode.ENTER) {
                    String saldo = txt_valorInicial.getText().trim();
                    txt_valorInicial.setText(Util.muedaLocalT(saldo));
                }
            });
        } catch (Exception e) {
        }

    }

    @FXML
    private void closingIFclicked(MouseEvent event) {
        this.myLayout.getScene().getWindow().hide();
    }

    @FXML
    private void openCaixa(ActionEvent event) {
        if (validation()) {

            Util.opeConnection();
            Util.enti.getTransaction().begin();

            CaixaMovement myCaixaMoviment = new CaixaMovement();

            myCaixaMoviment.setAutenticationId(Util.auts);
            myCaixaMoviment.setCaixaId(myCaixa);
            myCaixaMoviment.setDataInsert(new Date());
            myCaixaMoviment.setTipoMovimento(10);
            myCaixaMoviment.setInitialValue(Util.muedaLocalTRecerc(this.txt_valorInicial.getText().trim()));
            //myCaixaMoviment.set
            myCaixaMoviment.setCorrentValue(BigDecimal.ZERO);
            myCaixaMoviment.setInformacao(
                    "Operação de abertura do caixa no " + Util.dateHoraFormat(new Date())
                    + "Funcionario " + Util.auts.getUsername()
            );
            new CaixaMovementJpaController(Util.emf).create(myCaixaMoviment);
            
            AberturaDia abd = new AberturaDia();
            
            abd.setCaixaMovementId(myCaixaMoviment);
            abd.setNumCaixa(myCaixa.getMaquinaId().getIp());
            abd.setDiaCorrent(new Date());
            
            try {
                new AberturaDiaJpaController(Util.emf).create(abd);
            } catch (Exception ex) {
                
            }

            myCaixa.setState(1);
            Util.enti.merge(myCaixa);

            Util.enti.getTransaction().commit();
            Util.emf.close();

            this.myLayout.getScene().getWindow().hide();

        }

    }

    public void instanceValidation() {

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Campo obrigatório!");
        this.txt_valorInicial.getValidators().add(validator);
        this.txt_valorInicial.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.txt_valorInicial.validate();
            }
        });
    }

    public boolean validation() {
        boolean flag = true;
        if (this.txt_valorInicial.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("campo obrigatório!...");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_valorInicial.getValidators().add(validator);
            this.txt_valorInicial.validate();
            flag = false;
        }

        return flag;
    }

}
