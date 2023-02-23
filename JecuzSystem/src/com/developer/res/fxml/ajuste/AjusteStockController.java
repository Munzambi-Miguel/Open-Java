/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.ajuste;

import com.developer.util.Util;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class AjusteStockController implements Initializable {

    @FXML
    private BorderPane myDashboard;
    private SplitPane mySplitPane;
    @FXML
    private BorderPane myBorderVender;
    @FXML
    private BorderPane myBorderAjuste;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            //    com.developer.res.fxml.ajuste

            Util.addViewPage(myBorderVender, "res/fxml/ajuste/produtosMontra.fxml");
            Util.addViewPage(myBorderAjuste, "res/fxml/ajuste/produtosRetirados.fxml");
        } catch (IOException ex) {
            Logger.getLogger(AjusteStockController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void openModal(ActionEvent event) {
        mySplitPane.setDividerPositions(100);
    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

    private void verAjustes(ActionEvent event) {
        mySplitPane.setDividerPositions(0);

    }


    private void novoAjuste(MouseEvent event) {
        mySplitPane.setDividerPositions(100);
    }

}
