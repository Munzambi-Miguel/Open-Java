/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.dashboad;

import com.developer.util.MeusFicheiros;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class ConfiguracaoViewController implements Initializable {

    @FXML
    private VBox options;
    @FXML
    private JFXButton btn_theme;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void altTheme(MouseEvent event) {
    }

    @FXML
    private void encerar(ActionEvent event) {

        new File(Util.path).delete();
        this.options.getScene().getWindow().hide();
        System.exit(0);
    }

}
