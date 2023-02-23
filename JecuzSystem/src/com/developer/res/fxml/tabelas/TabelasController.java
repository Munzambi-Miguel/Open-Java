/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.tabelas;

import com.developer.util.Util;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class TabelasController implements Initializable {

    @FXML
    private BorderPane myDashboard;
    @FXML
    private BorderPane myBorderTabelasProduto;
    @FXML
    private VBox mHBPtables;
    @FXML
    private BorderPane myBorderTabelasGerais;
    @FXML
    private VBox myVBTableDe;
    @FXML
    private HBox myHBoxGTable;
    @FXML
    private VBox myTablesEx;

    //private HBox myHBoxGTable;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            // com.developer.res.fxml.tabelas

            mHBPtables.getChildren().add(Util.getNode("res/fxml/tabelas/nivel1Tabela.fxml"));
            mHBPtables.getChildren().add(Util.getNode("res/fxml/tabelas/nivel2Tabela.fxml"));
            mHBPtables.getChildren().add(Util.getNode("res/fxml/tabelas/paisView.fxml"));
            myVBTableDe.getChildren().add(Util.getNode("res/fxml/tabelas/nivel3Tabela.fxml"));
            myVBTableDe.getChildren().add(Util.getNode("res/fxml/tabelas/pecoProdutos.fxml"));
            myVBTableDe.getChildren().add(Util.getNode("res/fxml/tabelas/CidadeView.fxml"));
            myTablesEx.getChildren().add(Util.getNode("res/fxml/tabelas/unidades.fxml"));
            myTablesEx.getChildren().add(Util.getNode("res/fxml/tabelas/marcas.fxml"));
            myTablesEx.getChildren().add(Util.getNode("res/fxml/tabelas/photoView.fxml"));
     
        } catch (IOException ex) {
            Logger.getLogger(TabelasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

}
