/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.tableInterative;

import com.developer.java.controller.CityJpaController;
import com.developer.java.controller.PaisJpaController;
import com.developer.java.controller.TipoclienteJpaController;
import com.developer.java.entity.City;
import com.developer.java.entity.Pais;
import com.developer.java.entity.Tipocliente;
import com.developer.util.SelectorList;
import com.developer.util.Util;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class TableInterativeController implements Initializable {

    @FXML
    private VBox vBoxLayout;
    @FXML
    private JFXTextField txt_designacao;
    @FXML
    private VBox vbInforme;
    @FXML
    private Label information;

    public static JFXComboBox myComboBox;

    public static City myCity;

    public static Tipocliente myTipocliente;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        this.txt_designacao.setOnKeyPressed((j) -> {
            if (j.getCode() == KeyCode.ENTER) {
                guardar();
            }

            if (j.getCode() == KeyCode.ESCAPE) {
                this.txt_designacao.getScene().getWindow().hide();
            }
        });
    }

    @FXML
    private void guardarInformacao(ActionEvent event) {
        guardar();
    }

    boolean validation() {

        if (this.txt_designacao.getText().trim().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite a Designação!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_designacao.getValidators().add(validator);

            this.txt_designacao.focusedProperty().addListener((o, oldVal, newVal) -> {
                if (!newVal) {
                    this.txt_designacao.validate();
                }

                this.vbInforme.setVisible(false);
            });
            this.txt_designacao.validate();

            return false;

        }

        return true;
    }

    private void guardar() {
        if (validation()) {
            Util.opeConnection();
            Util.enti.getTransaction().begin();

            // registrar cada item de tipos tabelas
            if (myCity != null) {

                myCity.setDescription(this.txt_designacao.getText().trim());
               

                myCity.setPaisId(new PaisJpaController(Util.emf).findPais(1));
                new CityJpaController(Util.emf).create(myCity);
                myComboBox.setItems(SelectorList.citySelector());
                myComboBox.setValue(myCity);

                this.txt_designacao.getScene().getWindow().hide();
            }

            if (myTipocliente != null) {

                myTipocliente.setDesignation(this.txt_designacao.getText().trim());
                new TipoclienteJpaController(Util.emf).create(myTipocliente);
                myComboBox.setItems(SelectorList.tipoCLiente());
                myComboBox.setValue(myTipocliente);
                this.txt_designacao.getScene().getWindow().hide();
            }

            if (Util.emf.isOpen()) {
                Util.emf.close();
            }

        }
    }

}
