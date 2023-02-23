/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.faturacao;

import com.developer.Main;
import com.developer.java.entity.Fatura;
import com.developer.java.entity.Item;
import com.developer.java.entity.Product;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javax.persistence.Query;
import org.controlsfx.control.PopOver;
import org.eclipse.persistence.config.QueryHints;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class DevolucaoItensController implements Initializable {

    @FXML
    private BorderPane myBorderPane;
    @FXML
    private Text textInforme;
    @FXML
    private JFXTextField txt_numFaura;
    @FXML
    private Text lb_tipoPagamento;
    @FXML
    private Text lb_valorPago;
    @FXML
    private Text lb_troco;
    @FXML
    private Text lb_totalApagar;
    @FXML
    private Text lb_nomeCliente;
    @FXML
    private Text lb_operador;
    private JFXTextArea txt_motivoAnulamento;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn tc_quantidade;
    @FXML
    private TableColumn tc_item;

    Fatura fatura;
    private final int QUANTIDADE_PAGINA = 13;

    @FXML
    private TableColumn tc_quantidadeAtirar;
    @FXML
    private TableColumn tc_accao;

    // vamos declarar variaveis para poup view 
    public static final PopOver popConfig = new PopOver();
    private Parent popContent;

    public static DevolucaoItensController myInstance;
    @FXML
    private JFXTextField txt_contactoTelefone;
    @FXML
    private JFXTextField txt_clienteNome;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        validation();
        myInstance = this;
    }

    @FXML
    private void clossingView(MouseEvent event) {
        this.myBorderPane.getScene().getWindow().hide();
    }

    private void guardarSaidaEmLote(ActionEvent event) {

        /*
            Vamos criar a operação para o anulamento da factura
            bem como algumas logicas de negocio
            Primeira para melhor segurança anivel do sistema
            o programa não deve mostrar faturas fora do dia de inserção
        
         */
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        fatura.setAdmin(Util.auts);
        fatura.setInformacao(
                " - Factura anulado pelo Sr.: " + Util.auts.getUsername()
                + " - Data de Anulamento.: " + Util.dateHoraFormat(new Date())
                + " - "
                + "" + this.txt_motivoAnulamento.getText().trim()
        );

        fatura.setState(-10);

        for (Item item : fatura.getItemList()) {

            Product pd = item.getProductId();
            int quantidade = item.getQuantity() + pd.getQuantidAtual();
            pd.setQuantidAtual(quantidade);
            Util.enti.merge(pd);
        }

        Util.enti.merge(fatura);

        Util.enti.getTransaction().commit();
        Util.emf.close();

        TrayNotification tray = new TrayNotification("Anulamento da Factura: ",
                "o processo de anulamento foi realizado com sucesso!... ",
                NotificationType.ERROR);
        tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
        tray.setRectangleFill(Paint.valueOf("Gray"));
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(Duration.seconds(3));
        tray.showAndWait();
        AnularFaturaController.instace.caregarLista();

        this.myBorderPane.getScene().getWindow().hide();
    }

    public void getFatura() {

        if (validationForme()) {
            try {

                Util.opeConnection();
                Util.enti.getTransaction().begin();

                String queryComands = "SELECT m FROM Fatura m WHERE"
                        + " m.state=0 and m.fatura=:fatura";

                Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);

                querys.setParameter("fatura", this.txt_numFaura.getText().trim());

                fatura = (Fatura) querys.getSingleResult();

                lb_nomeCliente.setText(fatura.getCliente());
                lb_valorPago.setText(Util.muedaLocalT("" + fatura.getValorPago()));
                lb_troco.setText(Util.muedaLocalT("" + fatura.getTroco()));
                lb_totalApagar.setText(Util.muedaLocalT("" + fatura.getTotal()));
                lb_tipoPagamento.setText("" + (fatura.getTipoPagamento() ? "multicaixa" : "normal"));

                txt_clienteNome.setText(fatura.getCliente());

                lb_operador.setText(fatura.getFuncionarioId().getUsername());

                Util.enti.getTransaction().commit();
                Util.emf.close();

            } catch (Exception e) {

                TrayNotification tray = new TrayNotification("Busca da Factura: ",
                        "Não foi possivel encontrar a factura!... ",
                        NotificationType.ERROR);
                //tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                tray.setRectangleFill(Paint.valueOf("Gray"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
                tray.showAndWait();
            }

            caregarItem();
        }
    }

    @FXML
    private void opeFatura(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            getFatura();
        }
    }

    @FXML
    private void getFactura(ActionEvent event) {
        getFatura();
    }

    public void caregarItem() {
        caregarLista();
    }

    public void caregarLista() {

        tc_quantidade.setCellValueFactory(
                new PropertyValueFactory("quantidade")
        );
        tc_item.setCellValueFactory(
                new PropertyValueFactory("nameItem")
        );
        tc_quantidadeAtirar.setCellValueFactory(
                new PropertyValueFactory("tipoDevolucao")
        );
        tc_accao.setCellValueFactory(
                new PropertyValueFactory("myBox")
        );

        paginacao.setPageFactory((Integer pagina) -> {
            atualizarGrade(pagina);
            return tableView;
        });

    }

    private void atualizarGrade(Integer pagina) {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        String queryComands = "SELECT m FROM Item m WHERE"
                + " m.faturaId=:faturaId and m.state=0 ";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        querys.setParameter("faturaId", fatura);
        List<Item> lista = querys.getResultList();

        if (lista.isEmpty()) {
            paginacao.setPageCount(1);
        }
        Util.enti.getTransaction().commit();
        Util.emf.close();

        try {
            paginacao.setPageCount((int) Math.ceil(((double) lista.size()) / QUANTIDADE_PAGINA));
            tableView.setItems(listar(QUANTIDADE_PAGINA, pagina));
        } catch (Exception ex) {
        }
    }

    private ObservableList<ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComands = "SELECT m FROM Item m WHERE"
                + " m.faturaId=:faturaId and m.state=0 ";

        Query querys = Util.enti.createQuery(queryComands, Item.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));
        querys.setParameter("faturaId", fatura);

        List<Item> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        /**
         * Neste metodo vamos buscar as informação para listarmos na tabela os
         * tipos de item a serem devolvidos para os clientes no entantos vomos
         * especificar as informações de uma forma clara
         */

        ObservableList<String> listCom = FXCollections.observableArrayList("Danificado", "Bom Estado");
        for (Item obj : lista) {

            FontAwesomeIconView iconPrints2 = new FontAwesomeIconView();
            iconPrints2.setGlyphName("ANGLE_DOUBLE_LEFT");
            olis.add(new ItemLista(
                    // informação da quantidade do item 
                    obj.getQuantity(),
                    // informação do item ou nomeação do produto
                    obj.getProduto(),
                    //informação do tipo de devolução                   
                    new JFXComboBox<>(listCom),
                    new HBox(3), new JFXButton("", iconPrints2),
                    obj.getFaturaId(), obj
            ));
        }

        return olis;
    }

    public final class ItemLista {

        private final int quantidade;
        private final String nameItem;
        private final JFXComboBox<String> tipoDevolucao;
        private final HBox myBox;
        private final JFXButton btnDevolver;
        public final Fatura myFa;
        public final Item myIt;

        public ItemLista(int quantidade, String nameItem,
                JFXComboBox<String> tipoDevolucao,
                HBox myBox, JFXButton btnDevolver,
                Fatura myFa, Item myIt) {
            this.quantidade = quantidade;
            this.nameItem = nameItem;
            this.tipoDevolucao = tipoDevolucao;
            this.myBox = myBox;
            this.btnDevolver = btnDevolver;
            this.myFa = myFa;
            this.myIt = myIt;

            /// aqui vos inserir as informações para os botões de devolção 
            /// dos itens da fatura bem como algumas funcionalidades
            this.btnDevolver.setStyle(" -fx-background-color:   #24B6F7 ! important;");
            this.tipoDevolucao.setValue("Danificado");
            this.myBox.getChildren().addAll(btnDevolver);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle(" -fx-cursor: hand;");

            evento();
        }

        public int getQuantidade() {
            return quantidade;
        }

        public String getNameItem() {
            return nameItem;
        }

        public JFXComboBox<String> getTipoDevolucao() {
            return tipoDevolucao;
        }

        public HBox getMyBox() {
            return myBox;
        }

        public JFXButton getBtnDevolver() {
            return btnDevolver;
        }

        /**
         * Neste metodos vamos criar mecanismo para que o cliente possa devolver
         * os itens de atualizar a linha em que se fez a operação
         *
         * Para ser efetuado a devolução devemos saber o estado do produto ou
         * item a ser devolvido.
         *
         * No entanto para essa realização vamos definir assim Acabado e não
         * acabado ou util ou inutil; bom estado e denificado
         */
        public void evento() {

            try {
                btnDevolver.setOnAction((event) -> {
                    if (popConfig.isShowing()) {
                        popConfig.hide();

                    } else {

                        if (txt_clienteNome.getText().isEmpty() || txt_contactoTelefone.getText().isEmpty()) {

                            /**
                             * Pensamos em manter os campo de contacto e o nome
                             * do cliente obrigatorio
                             */
                            TrayNotification tray = new TrayNotification("Devolução de Item: ",
                                    "Não é possivel entrar, O nome do Cliente e o contacto é de caracter obrigatório... ",
                                    NotificationType.ERROR);
                            //// - com.developer.res.css.img
                            //// - tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                            tray.setRectangleFill(Paint.valueOf("Gray"));
                            tray.setAnimationType(AnimationType.POPUP);
                            tray.showAndDismiss(Duration.seconds(3));

                            /**
                             * mostrar as validações no cliente para ter a noção
                             * de que está a faltar o preenchimento dos campos
                             * bem como o nome e o contacto telefonico
                             */
                            if (txt_clienteNome.getText().trim().isEmpty()) {

                                RequiredFieldValidator validator = new RequiredFieldValidator();
                                validator.setMessage("Faltando o nome do cliente!");
                                validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                                        .glyph(FontAwesomeIcon.WARNING)
                                        .size("1em")
                                        .styleClass("error")
                                        .build());
                                txt_clienteNome.getValidators().add(validator);

                                txt_clienteNome.focusedProperty().addListener((o, oldVal, newVal) -> {
                                    if (!newVal) {
                                        txt_clienteNome.validate();
                                    }

                                });
                                txt_clienteNome.validate();

                            }

                            if (txt_contactoTelefone.getText().trim().isEmpty()) {

                                RequiredFieldValidator validator = new RequiredFieldValidator();
                                validator.setMessage("Faltando o contacto do cliente!");
                                validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                                        .glyph(FontAwesomeIcon.WARNING)
                                        .size("1em")
                                        .styleClass("error")
                                        .build());
                                txt_contactoTelefone.getValidators().add(validator);

                                txt_contactoTelefone.focusedProperty().addListener((o, oldVal, newVal) -> {
                                    if (!newVal) {
                                        txt_contactoTelefone.validate();
                                    }

                                });
                                txt_contactoTelefone.validate();

                            }

                        } else {
                            popLoad();

                            QTConfirmerController.myFa = this.myFa;
                            QTConfirmerController.myIt = this.myIt;
                            QTConfirmerController.tipoDevolucao = this.tipoDevolucao.getSelectionModel().getSelectedItem();
                            popConfig.show(this.btnDevolver, 0);
                        }

                    }
                });
            } catch (Exception e) {
                popConfig.hide();
            }

        }

    }

    private void popLoad() {
        try {

            //// com.developer.res.fxml.faturacao
            popContent = Util.getNode("res/fxml/faturacao/QTConfirmer.fxml");
            popConfig.setContentNode(popContent);
            popConfig.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
            popConfig.setCornerRadius(5);

        } catch (IOException e) {
            //e.printStackTrace();
        }

    }

    public void validation() {

        /**
         * Está função tem a finalidade de garantir as atualizações
         *
         */
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Porfavor digite o numero da Factura!");
        validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.txt_numFaura.getValidators().add(validator);
        this.txt_numFaura.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.txt_numFaura.validate();

            }
        });

        /**
         * Validanfo o nome do cliente
         */
        RequiredFieldValidator nomeCliente = new RequiredFieldValidator();
        nomeCliente.setMessage("Faltando o nome do Cliente!");
        nomeCliente.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.txt_clienteNome.getValidators().add(nomeCliente);
        this.txt_clienteNome.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.txt_clienteNome.validate();
            }
        });

        /**
         * Validando o contacto do cliente
         */
        RequiredFieldValidator contactoCliente = new RequiredFieldValidator();
        contactoCliente.setMessage("Faltando o contacto do Cliente!");
        contactoCliente.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.txt_contactoTelefone.getValidators().add(contactoCliente);
        this.txt_contactoTelefone.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.txt_contactoTelefone.validate();
            }
        });
    }

    public boolean validationForme() {

        boolean flag = true;

        if (this.txt_numFaura.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("campo obrigatorio de prienchimento");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_numFaura.getValidators().add(validator);
            this.txt_numFaura.validate();

            flag = false;
        }

        return flag;
    }

    public boolean validarText() {

        boolean flag = true;

        if (this.txt_motivoAnulamento.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Campo obrigatorio de prienchimento!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_motivoAnulamento.getValidators().add(validator);
            this.txt_motivoAnulamento.validate();

            flag = false;

        }

        return flag;
    }

    public JFXTextField getTxt_contactoTelefone() {
        return txt_contactoTelefone;
    }

    public JFXTextField getTxt_clienteNome() {
        return txt_clienteNome;
    }

}
