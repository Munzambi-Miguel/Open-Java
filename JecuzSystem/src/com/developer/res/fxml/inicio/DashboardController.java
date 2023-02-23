/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.inicio;

import com.developer.util.Util;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class DashboardController implements Initializable {

    @FXML
    private VBox myVBInternar;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO Lua
            // com.developer.res.fxml.inicio 
            myVBInternar.getChildren().add(Util.getNode("res/fxml/inicio/TopCont.fxml"));
            myVBInternar.getChildren().add(Util.getNode("res/fxml/inicio/CenterChart.fxml"));
           // myVBInternar.getChildren().add(Util.getNode("res/fxml/inicio/BottonViews.fxml"));
           
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void callingHome(ActionEvent event) {

    }

}
