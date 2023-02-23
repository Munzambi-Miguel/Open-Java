/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.tabelas;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class TipoClienteViewController implements Initializable {

    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableView<?> tableView;
    @FXML
    private TableColumn<?, ?> tc_ordem;
    @FXML
    private TableColumn<?, ?> tc_designacao;
    @FXML
    private TableColumn<?, ?> tc_acao;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void openModal(ActionEvent event) {
    }
    
}
