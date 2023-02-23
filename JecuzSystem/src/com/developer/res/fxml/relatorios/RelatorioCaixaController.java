/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.relatorios;

import com.developer.java.entity.CaixaMovement;
import com.developer.util.Util;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class RelatorioCaixaController implements Initializable {

    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private TableView tableView;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableColumn tc_ordem;
    @FXML
    private TableColumn tc_caixa;
    @FXML
    private TableColumn tc_abertura;
    @FXML
    private TableColumn tc_saldo;
    @FXML
    private TableColumn tc_cliente;
    @FXML
    private TableColumn tc_compra;
    @FXML
    private TableColumn tc_fecho;
    @FXML
    private TableColumn tc_corrente;
    @FXML
    private TableColumn tc_saldofinal;

    private final int QUANTIDADE_PAGINA = 13;
    @FXML
    private TableColumn tc_caoCaixa;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        caregarLista();
    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

    @FXML
    private void openModal(ActionEvent event) {
    }

    public void caregarLista() {

        tc_ordem.setCellValueFactory(
                new PropertyValueFactory("index")
        );
        tc_caixa.setCellValueFactory(
                new PropertyValueFactory("caixa")
        );
        tc_abertura.setCellValueFactory(
                new PropertyValueFactory("abertura")
        );

        tc_saldo.setCellValueFactory(
                new PropertyValueFactory("saldoInicial")
        );
        tc_cliente.setCellValueFactory(
                new PropertyValueFactory("cliente")
        );
        tc_compra.setCellValueFactory(
                new PropertyValueFactory("compra")
        );
        tc_fecho.setCellValueFactory(
                new PropertyValueFactory("fecho")
        );
        tc_corrente.setCellValueFactory(
                new PropertyValueFactory("corrent")
        );
        tc_saldofinal.setCellValueFactory(
                new PropertyValueFactory("saldoFinal")
        );

        tc_caoCaixa.setCellValueFactory(
                new PropertyValueFactory("numCaixa")
        );

        paginacao.setPageFactory((Integer pagina) -> {
            atualizarGrade(pagina);
            return tableView;
        });

    }

    private void atualizarGrade(Integer pagina) {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        String queryComands = "SELECT p FROM CaixaMovement p"
                + " WHERE p.tipoMovimento=20 OR p.tipoMovimento=21 order by p.id desc";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        List<CaixaMovement> lista = querys.getResultList();

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

    private ObservableList<RelatorioMovimentoController.ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        String queryComands = "SELECT p FROM CaixaMovement p "
                + " WHERE p.tipoMovimento=20 OR p.tipoMovimento=21 order by p.id desc";

        Query querys = Util.enti.createQuery(queryComands, CaixaMovement.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        List<CaixaMovement> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();

        for (CaixaMovement obj : lista) {

            i++;
            FontAwesomeIconView iconPrints = new FontAwesomeIconView();
            iconPrints.setGlyphName("EYE");

            iconPrints.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

            olis.add(new ItemLista(
                    obj.getId(),
                    i,
                    "" + obj.getFaturaId().getFuncionarioId().getUsername(),
                    Util.dateHoraFormat(obj.getDataInsert()),
                    Util.muedaLocalT("" + obj.getInitialValue()),
                    "" + (obj.getFaturaId() == null ? "-- / --" : obj.getFaturaId().getCliente()),
                    "" + (obj.getFaturaId() == null ? "-- / --" : "+ " + Util.muedaLocalT("" + obj.getFaturaId().getTotal())),
                    "" + (obj.getTipoMovimento() == 20? "Faturação":(obj.getTipoMovimento() == 21?"Logout":
                            (obj.getTipoMovimento() == 22?"Pausa":(obj.getTipoMovimento() == 22?"Entrada":"")))  ),
                    "+ " + Util.muedaLocalT("" + obj.getCorrentValue()),
                    "+ " + Util.muedaLocalT("" + obj.getCorrentValue()),
                    "Caixa nº " + obj.getCaixaId().getMaquinaId().getNumero())
            );
        }

        return olis;
    }

    public final class ItemLista {

        private final int id;
        private final int index;
        private final String caixa;
        private final String abertura;
        private final String saldoInicial;
        private final String cliente;
        private final String compra;
        private final String fecho;
        private final String corrent;
        private final String saldoFinal;
        private final String numCaixa;

        public ItemLista(int id, int index, String caixa,
                String abertura, String saldoInicial,
                String cliente, String compra, String fecho,
                String corrent, String saldoFinal, String numCaixa
        
        ) {
            this.id = id;
            this.index = index;
            this.caixa = caixa;
            this.abertura = abertura;
            this.saldoInicial = saldoInicial;
            this.cliente = cliente;
            this.compra = compra;
            this.fecho = fecho;
            this.corrent = corrent;
            this.saldoFinal = saldoFinal;
            this.numCaixa = numCaixa;

        }

        public int getId() {
            return id;
        }

        public int getIndex() {
            return index;
        }

        public String getCaixa() {
            return caixa;
        }

        public String getAbertura() {
            return abertura;
        }

        public String getSaldoInicial() {
            return saldoInicial;
        }

        public String getCliente() {
            return cliente;
        }

        public String getCompra() {
            return compra;
        }

        public String getFecho() {
            return fecho;
        }

        public String getCorrent() {
            return corrent;
        }

        public String getSaldoFinal() {
            return saldoFinal;
        }

        public String getNumCaixa() {
            return numCaixa;
        }

    }
}
