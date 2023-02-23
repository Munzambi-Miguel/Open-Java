/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.faturacao;

import com.developer.java.entity.Fatura;
import com.developer.java.entity.Item;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class DevolucaoController implements Initializable {

    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn tc_ordem;
    @FXML
    private TableColumn tc_operador;
    @FXML
    private TableColumn tc_cliente;
    @FXML
    private TableColumn tc_fatura;
    @FXML
    private TableColumn tc_datahora;
    @FXML
    private TableColumn tc_acao;
    @FXML
    private TableColumn tc_item;
    @FXML
    private TableColumn tc_quantidade;
    @FXML
    private TableColumn tc_unidade;
    @FXML
    private TableColumn tc_subtotal;

    private final int QUANTIDADE_PAGINA = 13;
    
    public static DevolucaoController myInstace;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO vamos listar os item e vamos permitir que aja devolução dos item 
        caregarLista();
        
        myInstace = this;
    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

    @FXML
    private void openModal(ActionEvent event) throws IOException, InterruptedException {
         Util.frameDialog("res/fxml/faturacao/devolucaoItens.fxml");
    }

    /**
     * Neste metodo vamos caregar os elementos da tabelas e associalos ao um
     * determinado objecto
     */
    public void caregarLista() {

        tc_ordem.setCellValueFactory(
                new PropertyValueFactory("ordem")
        );
        tc_operador.setCellValueFactory(
                new PropertyValueFactory("operador")
        );

        tc_cliente.setCellValueFactory(
                new PropertyValueFactory("cliente")
        );
        tc_fatura.setCellValueFactory(
                new PropertyValueFactory("fatura")
        );
        tc_item.setCellValueFactory(
                new PropertyValueFactory("item")
        );
        tc_quantidade.setCellValueFactory(
                new PropertyValueFactory("quatidade")
        );

        tc_unidade.setCellValueFactory(
                new PropertyValueFactory("unidade")
        );
        tc_subtotal.setCellValueFactory(
                new PropertyValueFactory("subtotal")
        );
        tc_datahora.setCellValueFactory(
                new PropertyValueFactory("data")
        );

        tc_acao.setCellValueFactory(
                new PropertyValueFactory("myBox")
        );

        paginacao.setPageFactory((Integer pagina) -> {
            atualizarGrade(pagina);
            return tableView;
        });

    }

    /**
     * Nestá função vamos determinar a nossa pagnação isso no elemento fxml da
     * pagnação
     *
     * isso é possivel fazendo uma consulta no banco e retornar a quantidade de
     * elemento na nossa lista
     *
     * @param pagina
     */
    private void atualizarGrade(Integer pagina) {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        String queryComands = "SELECT p FROM Item p WHERE"
                + " p.state=10 order by p.id desc";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        List<Fatura> lista = querys.getResultList();

        if (lista.isEmpty()) {
            paginacao.setPageCount(1);
        }
        Util.enti.getTransaction().commit();
        Util.emf.close();

        try {
            paginacao.setPageCount((int) Math.ceil(((double) lista.size()) / QUANTIDADE_PAGINA));
            tableView.setItems(listar(QUANTIDADE_PAGINA, pagina));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Vamos criar o nosso objecto interno com a finalidade de apresentarmos os
     * textos na tabela
     */
    private ObservableList<ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComandss = "SELECT p FROM Item p WHERE"
                + " p.state=:state order by p.id desc";

        Query querys = Util.enti.createQuery(queryComandss, Fatura.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        querys.setParameter("state", -10);

        List<Item> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        for (Item obj : lista) {

            i++;
           
            FontAwesomeIconView iconPrints2 = new FontAwesomeIconView();

         
            iconPrints2.setGlyphName("ANGLE_DOUBLE_LEFT");

           
            iconPrints2.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

            olis.add(new ItemLista(
                    obj.getId(), i,
                    obj.getFaturaId().getFuncionarioId().getUsername(),
                    obj.getFaturaId().getCliente().isEmpty()?"#Cliente não identificado":obj.getFaturaId().getCliente(),
                    obj.getFaturaId().getFatura(),
                    obj.getProduto(),
                    ""+obj.getQuantity(),
                    Util.muedaLocalT(""+obj.getProductId().getPrecounitario()) ,
                    Util.muedaLocalT(""+obj.getProductId().getPrecounitario().multiply(BigDecimal.valueOf(obj.getQuantity()))),
                    Util.dateHoraFormat(obj.getDataUpdate()),
                    new HBox(3),
                    new JFXButton("", iconPrints2)
            )
            );
        }

        return olis;
    }

    public final class ItemLista {

        private final int id;
        private final int ordem;
        private final String operador;
        private final String cliente;
        private final String fatura;
        private final String item;
        private final String quatidade;
        private final String unidade;
        private final String subtotal;
        private final String data;
        private final HBox myBox;
        private final JFXButton btnTransferir;

        private DevolucaoController instace;
        private Item entity;

        public ItemLista(int id, int ordem, String operador, String cliente,
                String fatura, String item, String quatidade, String unidade,
                String subtotal, String data, HBox myBox,
                JFXButton btnTransferir) {
            this.id = id;
            this.ordem = ordem;
            this.operador = operador;
            this.cliente = cliente;
            this.fatura = fatura;
            this.item = item;
            this.quatidade = quatidade;
            this.unidade = unidade;
            this.subtotal = subtotal;
            this.data = data;
            this.myBox = myBox;
            this.btnTransferir = btnTransferir;

            this.btnTransferir.setStyle(" -fx-background-color:   #24B6F7 ! important;");

            this.myBox.getChildren().addAll(btnTransferir);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle(" -fx-cursor: hand;");

            evento();

        }

        public int getId() {
            return id;
        }

        public int getOrdem() {
            return ordem;
        }

        public String getOperador() {
            return operador;
        }

        public String getCliente() {
            return cliente;
        }

        public String getFatura() {
            return fatura;
        }

        public String getItem() {
            return item;
        }

        public String getQuatidade() {
            return quatidade;
        }

        public String getUnidade() {
            return unidade;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public String getData() {
            return data;
        }

        public HBox getMyBox() {
            return myBox;
        }

        public JFXButton getBtnTransferir() {
            return btnTransferir;
        }

        public DevolucaoController getInstace() {
            return instace;
        }

        public Item getEntity() {
            return entity;
        }

        private void evento() {
            //Qui vamos colocar os eventos para a listagem de informação    
            
        }

    }

}
