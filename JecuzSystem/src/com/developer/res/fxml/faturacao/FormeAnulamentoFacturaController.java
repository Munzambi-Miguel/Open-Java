/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.faturacao;

import com.developer.Main;
import com.developer.java.entity.Fatura;
import com.developer.java.entity.Item;
import com.developer.java.entity.Movement;
import com.developer.java.entity.Product;
import com.developer.util.Util;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.Date;
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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class FormeAnulamentoFacturaController implements Initializable {

    @FXML
    private BorderPane myBorderPane;
    @FXML
    private Text textInforme;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableView tableView;
    @FXML
    private JFXTextField txt_numFaura;
    @FXML
    private Text lb_nomeCliente;
    @FXML
    private Text lb_tipoPagamento;
    @FXML
    private Text lb_valorPago;
    @FXML
    private Text lb_troco;
    @FXML
    private Text lb_totalApagar;
    @FXML
    private Text lb_operador;

    Fatura fatura;
    @FXML
    private TableColumn tc_quantidade;
    @FXML
    private TableColumn tc_item;
    @FXML
    private TableColumn tc_preco;

    public final int QUANTIDADE_PAGINA = 10;
    @FXML
    private JFXTextArea txt_motivoAnulamento;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        validation();
    }

    @FXML
    private void clossingView(MouseEvent event) {
        this.myBorderPane.getScene().getWindow().hide();
    }

    @FXML
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

                lb_operador.setText(fatura.getFuncionarioId().getUsername());

                Util.enti.getTransaction().commit();
                Util.emf.close();

                caregarItem();
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
        tc_preco.setCellValueFactory(
                new PropertyValueFactory("proco")
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
                + " m.faturaId=:faturaId";

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
            ex.printStackTrace();
        }
    }

    private ObservableList<ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComands = "SELECT m FROM Item m WHERE"
                + " m.faturaId=:faturaId";

        Query querys = Util.enti.createQuery(queryComands, Item.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));
        querys.setParameter("faturaId", fatura);

        List<Item> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        for (Item obj : lista) {

            olis.add(new ItemLista(
                    obj.getQuantity(),
                    obj.getProduto(),
                    obj.getTotal()
            ));
        }

        return olis;
    }

    public final class ItemLista {

        private final int quantidade;
        private final String nameItem;
        private final String proco;

        public ItemLista(int quantidade, String nameItem, String proco) {
            this.quantidade = quantidade;
            this.nameItem = nameItem;
            this.proco = proco;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public String getNameItem() {
            return nameItem;
        }

        public String getProco() {
            return proco;
        }

    }

    public void validation() {

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

        RequiredFieldValidator validatorProcoCompre = new RequiredFieldValidator();
        validatorProcoCompre.setMessage("Campo obrigatorio de prienchimento!");
        validatorProcoCompre.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.txt_motivoAnulamento.getValidators().add(validatorProcoCompre);
        this.txt_motivoAnulamento.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.txt_motivoAnulamento.validate();
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

}
