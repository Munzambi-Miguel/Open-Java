/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.faturacao;

import com.developer.java.controller.AberturaDiaJpaController;
import com.developer.java.controller.CaixaMovementJpaController;
import com.developer.java.entity.AberturaDia;
import com.developer.java.entity.Caixa;
import com.developer.java.entity.CaixaMovement;
import com.developer.java.entity.Product;
import com.developer.util.Util;
import com.jfoenix.controls.JFXToggleButton;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.layout.HBox;
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class CaixaController implements Initializable {

    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private BorderPane myBorderPaneInternar;

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn tc_caixaNum;
    @FXML
    private TableColumn tc_funcionario;
    @FXML
    private TableColumn tc_ipMaquina;
    @FXML
    private TableColumn tc_tipo;
    @FXML
    private TableColumn tc_valor;
    @FXML
    private TableView tableViewCaixa;
    @FXML
    private TableColumn tc_caixaNumCaixa;
    @FXML
    private TableColumn tc_stadoCaixa;
    @FXML
    private TableColumn tc_acaoCaixa;
    @FXML
    private TableColumn tc_data;
    @FXML
    private Pagination paginacaoCaixas;

    private final int QUANTIDADE_PAGINA = 13;
    private final int QUANTIDADE_PAGINA2 = 18;

    public static CaixaController instance;
    @FXML
    private Pagination paginacaoHistory;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        caregarLista();
        caregarListaHist();
        instance = this;

    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

    @FXML
    private void openModal(ActionEvent event) throws IOException, InterruptedException {
        //com.developer.res.fxml.faturacao
        Util.frameDialog("res/fxml/faturacao/formeCaixa.fxml");
    }

    public void caregarLista() {

        tc_caixaNum.setCellValueFactory(
                new PropertyValueFactory("index")
        );
        tc_caixaNumCaixa.setCellValueFactory(
                new PropertyValueFactory("desigancao")
        );

        tc_stadoCaixa.setCellValueFactory(
                new PropertyValueFactory("estado")
        );

        tc_acaoCaixa.setCellValueFactory(
                new PropertyValueFactory("myBox")
        );

        paginacaoCaixas.setPageFactory((Integer pagina) -> {
            atualizarGrade(pagina);
            return tableViewCaixa;
        });

    }

    public final class ItemLista {

        private final int id;
        private final int index;
        private final String desigancao;
        private final String estado;
        private final HBox myBox;
        private final JFXToggleButton btnToggle;
        private CaixaController mycaixaTable;
        private Caixa entity;

        public ItemLista(int id, int index, String desigancao, String estado,
                HBox myBox, JFXToggleButton btnToggle,
                CaixaController mycaixaTable, Caixa entity) {
            this.id = id;
            this.index = index;
            this.desigancao = desigancao;
            this.estado = estado;
            this.myBox = myBox;
            this.btnToggle = btnToggle;
            this.mycaixaTable = mycaixaTable;
            this.entity = entity;

            this.myBox.getChildren().addAll(btnToggle);
            event();
        }

        public int getId() {
            return id;
        }

        public int getIndex() {
            return index;
        }

        public String getDesigancao() {
            return desigancao;
        }

        public String getEstado() {
            return estado;
        }

        public HBox getMyBox() {
            return myBox;
        }

        public JFXToggleButton getBtnToggle() {
            return btnToggle;
        }

        public CaixaController getMycaixaTable() {
            return mycaixaTable;
        }

        public Caixa getEntity() {
            return entity;
        }

        private void event() {

            // Vamos criar aberturas dos movimento do Caixa
            btnToggle.setOnAction((event) -> {

                if (this.btnToggle.isSelected()) {
                    try {

                        OpenCaixaMovimentController.myCaixa = this.entity;
                        Util.frameDialog("res/fxml/faturacao/openCaixaMoviment.fxml");
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(CaixaController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {

                    Util.opeConnection();
                    Util.enti.getTransaction().begin();

                    CaixaMovement myCaMov = new CaixaMovement();

                    myCaMov.setAutenticationId(Util.auts);
                    myCaMov.setCaixaId(this.entity);
                    myCaMov.setDataInsert(new Date());
                    myCaMov.setTipoMovimento(11);

                    myCaMov.setInitialValue(BigDecimal.ZERO);
                    myCaMov.setCorrentValue(BigDecimal.ZERO);

                    myCaMov.setInformacao(
                            "Operação de fecho do caixa no " + Util.dateHoraFormat(new Date())
                            + "Funcionario " + Util.auts.getUsername()
                    );
                    new CaixaMovementJpaController(Util.emf).create(myCaMov);

                    this.entity.setState(0);
                    Util.enti.merge(entity);

                    AberturaDia abs = new AberturaDiaJpaController(Util.emf).findAberturaDia(this.entity.getMaquinaId().getIp());
                    AberturaDia abss = Util.enti.merge(abs);
                    Util.enti.remove(abss);

                    Util.enti.getTransaction().commit();
                    Util.emf.close();
                }

                mycaixaTable.caregarLista();
                mycaixaTable.caregarListaHist();
            });
        }

    }

    private void atualizarGrade(Integer pagina) {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        String queryComands = "SELECT p FROM Caixa p"
                + " ";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        List<Caixa> lista = querys.getResultList();

        if (lista.isEmpty()) {
            paginacaoCaixas.setPageCount(1);
        }
        Util.enti.getTransaction().commit();
        Util.emf.close();

        try {
            paginacaoCaixas.setPageCount((int) Math.ceil(((double) lista.size()) / QUANTIDADE_PAGINA));
            tableViewCaixa.setItems(listar(QUANTIDADE_PAGINA, pagina));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObservableList<ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComandss = "SELECT p FROM Caixa p"
                + "  ";

        Query querys = Util.enti.createQuery(queryComandss, Product.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        List<Caixa> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        for (Caixa obj : lista) {

            JFXToggleButton toggle = new JFXToggleButton();
            toggle.setMaxHeight(10);
            toggle.setMinHeight(0);
            if (obj.getState() == 1) {
                toggle.setSelected(true);
            } else {
                toggle.setSelected(false);
            }

            i++;
            olis.add(
                    new ItemLista(obj.getId(), i, "Caixa nº " + obj.getMaquinaId().getNumero(),
                            "" + (obj.getState() == 0 ? "desligado" : "ligado"),
                            new HBox(3), toggle, this, obj)
            );
        }

        return olis;
    }

    //-----------------------------------------------------------------------------------------
    public void caregarListaHist() {

        tc_caixaNum.setCellValueFactory(
                new PropertyValueFactory("caixanum")
        );
        tc_funcionario.setCellValueFactory(
                new PropertyValueFactory("funcionario")
        );

        tc_ipMaquina.setCellValueFactory(
                new PropertyValueFactory("ipmaquina")
        );

        tc_tipo.setCellValueFactory(
                new PropertyValueFactory("tipo")
        );

        tc_valor.setCellValueFactory(
                new PropertyValueFactory("valor")
        );

        tc_data.setCellValueFactory(
                new PropertyValueFactory("data")
        );

        paginacaoHistory.setPageFactory((Integer paginas) -> {
            atualizarGradeS(paginas);
            return tableView;
        });

    }

    public final class historico {

        private final String caixanum;
        private final String funcionario;
        private final String ipmaquina;
        private final String tipo;
        private final String valor;
        private final String data;

        public historico(String caixanum, String funcionario,
                String ipmaquina, String tipo, String valor, String data) {
            this.caixanum = caixanum;
            this.funcionario = funcionario;
            this.ipmaquina = ipmaquina;
            this.tipo = tipo;
            this.valor = valor;
            this.data = data;
        }

        public String getCaixanum() {
            return caixanum;
        }

        public String getFuncionario() {
            return funcionario;
        }

        public String getIpmaquina() {
            return ipmaquina;
        }

        public String getTipo() {
            return tipo;
        }

        public String getValor() {
            return valor;
        }

        public String getData() {
            return data;
        }

    }

    private void atualizarGradeS(Integer paginas) {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        String queryComands = "SELECT p FROM CaixaMovement p"
                + " WHERE p.tipoMovimento=10 OR p.tipoMovimento=11 order by p.id desc";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        List<CaixaMovement> lista = querys.getResultList();

        if (lista.isEmpty()) {
            paginacaoHistory.setPageCount(1);
        }
        Util.enti.getTransaction().commit();
        Util.emf.close();

        try {
            paginacaoHistory.setPageCount((int) Math.ceil(((double) lista.size()) / QUANTIDADE_PAGINA2));
            tableView.setItems(listarHis(QUANTIDADE_PAGINA2, paginas));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObservableList<historico> listarHis(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComandss = "SELECT p FROM CaixaMovement p"
                + " WHERE p.tipoMovimento=10 OR p.tipoMovimento=11 order by p.id desc";

        Query querys = Util.enti.createQuery(queryComandss, CaixaMovement.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        List<CaixaMovement> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        for (CaixaMovement obj : lista) {

            JFXToggleButton toggle = new JFXToggleButton();
            toggle.setMaxHeight(10);
            toggle.setMinHeight(0);

            i++;
            olis.add(new historico(
                    "Caixa nº " + obj.getCaixaId().getMaquinaId().getNumero(),
                    "" + obj.getAutenticationId().getUsername(),
                    "" + obj.getCaixaId().getMaquinaId().getIp(),
                    "" + (obj.getTipoMovimento() == 10 ? "Operação de abertura" : "Fecho do caixa"),
                    "" + Util.muedaLocalT("" + obj.getInitialValue()),
                    "" + Util.dateHoraFormat(obj.getDataInsert())
            ));

        }

        return olis;
    }

}
