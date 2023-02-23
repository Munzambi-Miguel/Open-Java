/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.inicio;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class BottonViewsController implements Initializable {

    @FXML
    private Pagination paginacao;
    @FXML
    private TableView<?> tableView;
    @FXML
    private TableColumn<?, ?> TebleColumnOrder;
    @FXML
    private TableColumn<?, ?> TableColumndesignacao;
    @FXML
    private TableColumn<?, ?> TableColumnQuantidade;
    @FXML
    private TableColumn<?, ?> TableColumnSaida;
    @FXML
    private TableColumn<?, ?> TableColumnUnitario;
    @FXML
    private TableColumn<?, ?> TableColumnAccao;

    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
