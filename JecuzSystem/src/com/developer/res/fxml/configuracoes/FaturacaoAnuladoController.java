/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.configuracoes;

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
public class FaturacaoAnuladoController implements Initializable {

    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private TableView<?> tableView;
    @FXML
    private TableColumn<?, ?> TebleColumnOrder;
    @FXML
    private TableColumn<?, ?> TableColumnName;
    @FXML
    private TableColumn<?, ?> TableColumnNIdem;
    @FXML
    private TableColumn<?, ?> TableColumnProvincia;
    @FXML
    private TableColumn<?, ?> TableColumnZona;
    @FXML
    private TableColumn<?, ?> TableColumnConatact;
    @FXML
    private TableColumn<?, ?> TableColumnAccao;
    @FXML
    private Pagination paginacao;

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
    private void openModal(ActionEvent event) {
    }
    
}
