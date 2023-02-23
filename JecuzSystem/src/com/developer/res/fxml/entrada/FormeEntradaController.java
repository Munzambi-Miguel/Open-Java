/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.entrada;

import com.developer.Main;
import com.developer.java.controller.MovementJpaController;
import com.developer.java.controller.ProductJpaController;
import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Entity;
import com.developer.java.entity.Movement;

import com.developer.java.entity.Product;
import static com.developer.res.fxml.clientes.FormeClientController.popConfig;
import com.developer.res.fxml.fornecimento.FormeFornecedorController;
import com.developer.util.SelectorList;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
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
public class FormeEntradaController implements Initializable {

    @FXML
    private BorderPane myDashboard;
    @FXML
    private BorderPane myBorderVender;
    @FXML
    private JFXTextField produto_txtbuscar;
    @FXML
    private Text nome_label;
    @FXML
    private Text categoria_label1;
    @FXML
    private Text categoria_label2;
    @FXML
    private Text categoria_label3;
    @FXML
    private BorderPane myBorderAjuste;

    private Product myProduct;
    @FXML
    private JFXTextField quantidade_txt;
    @FXML
    private JFXTextField procoCompra_txt;
    @FXML
    private JFXTextField persentagem_txt;
    @FXML
    private JFXTextArea serial_txt;
    @FXML
    private JFXDatePicker dataFabricacao_text;
    @FXML
    private JFXDatePicker dataespiracao_text;
    @FXML
    private HTMLEditor information_edotor;
    @FXML
    private JFXTabPane myTabPane;
    @FXML
    private JFXButton btn_guardar;
    @FXML
    private JFXComboBox cmb_forncedor;
    @FXML
    private JFXButton btn_popFornecedor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        btn_guardar.setText("SEGUINTE");
        selectorFornecedor();
        validation();

        for (Node node : myDashboard.getChildren()) {
            node.setOnMouseMoved((e) -> {

                if (popConfig.isShowing()) {
                    popConfig.hide();
                }
            });
        }
        
       
    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

    @FXML
    private void getObjectiLooking(ActionEvent event) {
        getLooking();
    }

    public void getLooking() {

        try {
            Util.opeConnection();
            Util.enti.getTransaction().begin();
            // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

            String queryComandss = "SELECT p FROM Product p WHERE"
                    + " p.state=:state AND p.partNamber=:partNamber";

            Query querys = Util.enti.createQuery(queryComandss, Product.class).setHint(QueryHints.REFRESH, true);

            querys.setParameter("state", 0);
            querys.setParameter("partNamber", this.produto_txtbuscar.getText().trim());
            //querys.setParameter("desiganation", this.produto_txtbuscar.getText().trim());
// 
            this.myProduct = (Product) querys.getSingleResult();

            this.nome_label.setText(this.myProduct.getProdutTypeid().getDesignation());
            if (this.myProduct.getNivel3Id() != null) {
                this.categoria_label1.setText(this.myProduct.getNivel3Id().toString());
                this.categoria_label2.setText(this.myProduct.getNivel3Id().getNivel2Id().toString());
                this.categoria_label3.setText(this.myProduct.getNivel3Id().getNivel2Id().getNivel1Id().toString());
            }

            Util.enti.getTransaction().commit();
            Util.emf.close();

        } catch (Exception e) {
            // e.printStackTrace();
        }

        if (this.myProduct == null) {

            TrayNotification tray = new TrayNotification("Produto: ",
                    "Não encontrado Verifique a designação ou a Referencia... ",
                    NotificationType.ERROR);
            //// com.developer.res.css.img
            //tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();
        }

    }

    @FXML
    private void keyEnter(KeyEvent e) {

        if (e.getCode() == KeyCode.ENTER) {
            getLooking();
        }
    }

    @FXML
    private void darEntradaProduto(ActionEvent event) throws Exception {
        save();
    }

    public void save() throws NonexistentEntityException, Exception {

        try {

            if (validationForme() && (myTabPane.getSelectionModel().getSelectedIndex() != 0)) {

                Util.opeConnection();
                Util.enti.getTransaction().begin();

                // Inserindo o mevimento de entrada no stock
                Movement m = new Movement();

                m.setQuantAtual(Integer.parseInt(this.quantidade_txt.getText().trim()));
                m.setQuantidade(Integer.parseInt(this.quantidade_txt.getText().trim()));

                m.setProductId(myProduct);

                m.setDataInsert(new Date());
                m.setInformacao(this.information_edotor.getHtmlText());

                m.setDebito(BigDecimal.valueOf(Double.parseDouble(Util.muedaSendText(this.procoCompra_txt.getText()))));
                m.setSerialNamber(this.serial_txt.getText().trim());

                if (!this.persentagem_txt.getText().isEmpty()) {
                    m.setPercentagemImposto(BigDecimal.valueOf(Double.parseDouble(this.persentagem_txt.getText().trim())));
                }
                
                

                if (this.dataFabricacao_text.getValue() != null) {
                    m.setDataFabrica(Util.dataFormat("" + this.dataFabricacao_text.getValue()));
                }

                m.setFornedorId((Entity) cmb_forncedor.getSelectionModel().getSelectedItem());
                if (this.dataespiracao_text.getValue() != null) {
                    m.setDataCaducidade(Util.dataFormat("" + this.dataespiracao_text.getValue()));
                }

                m.setAutenticationId(Util.auts);
                
                new MovementJpaController(Util.emf).create(m);

                Double valor = Double.parseDouble("" + myProduct.getEntradada()) + Double.parseDouble("" + m.getDebito());

                myProduct.setEntradada(BigDecimal.valueOf(valor));
                Util.enti.merge(myProduct);
                // new ProductJpaController(Util.emf).edit(myProduct);

                Util.enti.getTransaction().commit();
                Util.emf.close();

                EntradaStockController.instance.caregarLista();
                this.dataespiracao_text.getScene().getWindow().hide();

                TrayNotification tray = new TrayNotification("Movimento e Entrada: ",
                        "Foi realizado com sucesso!... ",
                        NotificationType.SUCCESS);
                //// com.developer.res.css.img
                tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                tray.setRectangleFill(Paint.valueOf("Gray"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
                tray.showAndWait();

            } else {

                if (validationForme()) {
                    myTabPane.getSelectionModel().selectLast();
                    btn_guardar.setText("GUARDAR");
                }

            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    public void validation() {

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Porfavor digite a quantidade!");
        validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.quantidade_txt.getValidators().add(validator);
        this.quantidade_txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.quantidade_txt.validate();
              
            }
        });

        RequiredFieldValidator validatorProcoCompre = new RequiredFieldValidator();
        validatorProcoCompre.setMessage("Porfavor digite o preço de compra!");
        validatorProcoCompre.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.procoCompra_txt.getValidators().add(validatorProcoCompre);
        this.procoCompra_txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.procoCompra_txt.validate();
            }

            try {
                String mueda = this.procoCompra_txt.getText().trim();
                this.procoCompra_txt.setText(Util.muedaLocal(mueda));
            } catch (Exception e) {
            }

        });

        RequiredFieldValidator validatorNumber = new RequiredFieldValidator();
        validatorNumber.setMessage("Porfavor digite o numero da Serie da Caixa!");
        validatorNumber.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.serial_txt.getValidators().add(validatorNumber);
        this.serial_txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.serial_txt.validate();
            }
        });

        RequiredFieldValidator validatorAS = new RequiredFieldValidator();
        validatorAS.setMessage("Certifique-se que encontrou o produto!");
        validatorAS.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.produto_txtbuscar.getValidators().add(validatorAS);
        this.serial_txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.produto_txtbuscar.validate();
            }
        });
    }

    public boolean validationForme() {

        boolean flag = true;

        if (this.produto_txtbuscar.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Certifique-se que encontrou o produto!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.produto_txtbuscar.getValidators().add(validator);
            this.produto_txtbuscar.validate();

            flag = false;
        }

        if (this.myProduct == null) {
            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Certifique-se que encontrou o produto!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.produto_txtbuscar.getValidators().add(validator);
            this.produto_txtbuscar.validate();

            flag = false;
        }

        if (this.quantidade_txt.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite a quantidade!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.quantidade_txt.getValidators().add(validator);
            this.quantidade_txt.validate();

            flag = false;

        }

        if (this.procoCompra_txt.getText().isEmpty()) {
            RequiredFieldValidator validatorProcoCompre = new RequiredFieldValidator();
            validatorProcoCompre.setMessage("Porfavor digite o preço de compra!");
            validatorProcoCompre.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.procoCompra_txt.getValidators().add(validatorProcoCompre);
            this.procoCompra_txt.validate();
            flag = false;
        }

        if (this.serial_txt.getText().isEmpty()) {
            RequiredFieldValidator validatorProcoCompre = new RequiredFieldValidator();
            validatorProcoCompre.setMessage("Porfavor digite o preço de compra!");
            validatorProcoCompre.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.serial_txt.getValidators().add(validatorProcoCompre);
            this.serial_txt.validate();
            flag = false;
        }

        return flag;
    }

    @FXML
    private void clossingView(MouseEvent event) {

        this.categoria_label1.getScene().getWindow().hide();
    }

    @FXML
    private void callingPopFornecedor(ActionEvent event) {

        if (popConfig.isShowing()) {
            popConfig.hide();

        } else {
            popLoad();
            FormeFornecedorController.myComboBox = cmb_forncedor;
            popConfig.show(this.btn_popFornecedor, 0);

            //popConfig.getRoot().setFocusTraversable(true);
        }

    }

    // popo
    public static final PopOver popConfig = new PopOver();
    private Parent popContent;

    private void popLoad() {
        try {

            //// com.developer.res.fxml.tableInterative
            popContent = Util.getNode("res/fxml/fornecimento/formeFornecedor.fxml");
            popConfig.setContentNode(popContent);
            popConfig.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
            popConfig.setCornerRadius(5);

        } catch (IOException e) {
            //e.printStackTrace();
        }

    }

    public void selectorFornecedor() {

        this.cmb_forncedor.setItems(SelectorList.setListFornecedor());
    }

}
