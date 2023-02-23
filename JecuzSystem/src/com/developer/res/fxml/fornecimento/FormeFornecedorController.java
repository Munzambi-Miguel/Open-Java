/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.fornecimento;

import com.developer.Main;
import com.developer.java.controller.AddressJpaController;
import com.developer.java.controller.ContactJpaController;
import com.developer.java.controller.EntityJpaController;

import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Address;
import com.developer.java.entity.City;
import com.developer.java.entity.Contact;
import com.developer.java.entity.Entity;
import static com.developer.res.fxml.clientes.FormeClientController.myentity;
import static com.developer.res.fxml.clientes.FormeClientController.popConfig;
import com.developer.res.fxml.tableInterative.TableInterativeController;

import com.developer.util.SelectorList;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
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
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class FormeFornecedorController implements Initializable {

    @FXML
    private BorderPane formularioBorderPane;
    @FXML
    private Text textInforme;
    @FXML
    private TextField idIdent;
    @FXML
    private JFXProgressBar progressBar;

    private static FormeFornecedorController instance;

    // Informações da entidade
    public static Entity myentity;

    @FXML
    private JFXTextField numero_txt;
    @FXML
    private JFXTextField designacao_txt;
    @FXML
    private JFXComboBox cidade_comb;
    @FXML
    private JFXTextArea endereco_texA;
    @FXML
    private JFXTextField fax_text;
    @FXML
    private JFXTextField telefone_txt;
    @FXML
    private JFXTextField telefone_txt2;
    @FXML
    private JFXTextArea email_txtA;

    public static final PopOver popConfig = new PopOver();
    private Parent popContent;
    @FXML
    private JFXButton btn_cidade;

    public static JFXComboBox myComboBox;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        validation();
        selector();
        instance = this;

        if (myentity != null) {
            this.setMyentity(myentity);
        }

        for (Node node : formularioBorderPane.getChildren()) {
            node.setOnMouseMoved((e) -> {

                if (popConfig.isShowing()) {
                    popConfig.hide();
                    myComboBox = null;
                }
            });
        }

    }

    private void fecharFormulario(ActionEvent event) {
        ((BorderPane) formularioBorderPane.getParent()).getChildren().remove(2);
    }

    @FXML
    private void guardarFornecedor(ActionEvent event) throws IOException, NonexistentEntityException {
        if (FormeFornecedorController.myentity == null) {
            if (validation2()) {
                salvar();
            }
        } else {
            alterar();
        }
    }

    public void salvar() throws IOException {

        Util.opeConnection();
        Util.enti.getTransaction().begin();

        Entity entity = new Entity();
        entity.setName(designacao_txt.getText().trim());
        entity.setNIdetification(numero_txt.getText().trim());
        entity.setDataInsert(new Date());

        entity.setEntityType(3);

        entity.setCompra(BigDecimal.ZERO);

        new EntityJpaController(Util.emf).create(entity);

        // Getting information of de contact from my entintyes
        if (!this.fax_text.getText().isEmpty()) {
            Contact myContact = new Contact();
            myContact.setTypeContact(1);
            myContact.setDescription(this.fax_text.getText().trim());
            myContact.setEntityId(entity);
            new ContactJpaController(Util.emf).create(myContact);
        }

        if (!this.telefone_txt.getText().isEmpty()) {
            Contact myContactNumber = new Contact();
            myContactNumber.setTypeContact(2);
            myContactNumber.setDescription(this.telefone_txt.getText().trim());
            myContactNumber.setEntityId(entity);
            new ContactJpaController(Util.emf).create(myContactNumber);
        }

        if (!this.telefone_txt2.getText().isEmpty()) {

            Contact myContactNumber2 = new Contact();
            myContactNumber2.setTypeContact(4);
            myContactNumber2.setDescription(this.telefone_txt2.getText().trim());
            myContactNumber2.setEntityId(entity);
            new ContactJpaController(Util.emf).create(myContactNumber2);
        }

        if (!this.email_txtA.getText().isEmpty()) {

            Contact myContactNumber3 = new Contact();
            myContactNumber3.setTypeContact(3);
            myContactNumber3.setDescription(this.email_txtA.getText().trim());
            myContactNumber3.setEntityId(entity);
            new ContactJpaController(Util.emf).create(myContactNumber3);
        }

        if (cidade_comb.getSelectionModel().getSelectedItem() != null) {
            Address address = new Address();
            address.setCityid((City) this.cidade_comb.getSelectionModel().getSelectedItem());
            address.setEntityId(entity);
            if (!this.endereco_texA.getText().isEmpty()) {
                address.setDecripton(this.endereco_texA.getText().trim());
            } else {
                address.setDecripton("... ");
            }
            new AddressJpaController(Util.emf).create(address);
        }

        Util.enti.getTransaction().commit();
        Util.emf.close();

        if (FornecedorController.myIsntance != null) {
            FornecedorController.myIsntance.caregarLista();
        }

        TrayNotification tray = new TrayNotification("Fornecedor: " + entity.getName(),
                "Acabou de adicionar o novo fornecedor no sistema... ",
                NotificationType.SUCCESS);
        //// com.developer.res.css.img
        tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
        tray.setRectangleFill(Paint.valueOf("Gray"));
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(Duration.seconds(3));
        this.textInforme.getScene().getWindow().hide();
        tray.showAndWait();

        if (myComboBox != null) {
            myComboBox.setItems(SelectorList.setListFornecedor());
            myComboBox.setValue(entity);

            myComboBox = null;
        }

    }

    private void alterar() throws NonexistentEntityException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmando...");
        List<Contact> lisConta = FormeFornecedorController.myentity.getContactList();
        alert.setContentText(
                "Setifique de que Pretendes Alteras as Informações"
                + "\n - " + FormeFornecedorController.myentity.getName()
        );
        alert.setHeaderText(null);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            Util.opeConnection();
            Util.enti.getTransaction().begin();

            FormeFornecedorController.myentity.setName(this.designacao_txt.getText().trim());
            if (!this.numero_txt.getText().isEmpty()) {
                FormeFornecedorController.myentity.setNIdetification(this.numero_txt.getText().trim());
            }

            Entity obj = Util.enti.merge(FormeFornecedorController.myentity);

            Util.enti.getTransaction().commit();
            Util.emf.close();

            Util.opeConnection();
            Util.enti.getTransaction().begin();
            for (Contact cont : lisConta) {

                //Contact conts = Util.enti.merge(cont);
                //Util.enti.remove(conts);
                new ContactJpaController(Util.emf).destroy(cont.getId());
            }

            if (!this.fax_text.getText().isEmpty()) {
                Contact myContact = new Contact();
                myContact.setTypeContact(1);
                myContact.setDescription(this.fax_text.getText().trim());
                myContact.setEntityId(FormeFornecedorController.myentity);
                new ContactJpaController(Util.emf).create(myContact);
            }

            if (!this.telefone_txt.getText().isEmpty()) {
                Contact myContactNumber = new Contact();
                myContactNumber.setTypeContact(2);
                myContactNumber.setDescription(this.telefone_txt.getText().trim());
                myContactNumber.setEntityId(FormeFornecedorController.myentity);
                new ContactJpaController(Util.emf).create(myContactNumber);
            }

            if (!this.telefone_txt2.getText().isEmpty()) {

                Contact myContactNumber2 = new Contact();
                myContactNumber2.setTypeContact(4);
                myContactNumber2.setDescription(this.telefone_txt2.getText().trim());
                myContactNumber2.setEntityId(FormeFornecedorController.myentity);
                new ContactJpaController(Util.emf).create(myContactNumber2);
            }

            if (!this.email_txtA.getText().isEmpty()) {

                Contact myContactNumber3 = new Contact();
                myContactNumber3.setTypeContact(3);
                myContactNumber3.setDescription(this.email_txtA.getText().trim());
                myContactNumber3.setEntityId(FormeFornecedorController.myentity);
                new ContactJpaController(Util.emf).create(myContactNumber3);
            }

            if (cidade_comb.getSelectionModel().getSelectedItem() != null) {
                Address address = new Address();
                address.setCityid((City) this.cidade_comb.getSelectionModel().getSelectedItem());
                address.setEntityId(FormeFornecedorController.myentity);
                if (!this.endereco_texA.getText().isEmpty()) {
                    address.setDecripton(this.endereco_texA.getText().trim());
                } else {
                    address.setDecripton("... ");
                }
                new AddressJpaController(Util.emf).create(address);
            }

            Util.enti.getTransaction().commit();
            Util.emf.close();

            FornecedorController.myIsntance.caregarLista();

            TrayNotification tray = new TrayNotification("Fornecedor: " + obj.getName(),
                    "Acabou de alterar o fornecedor no sistema... ",
                    NotificationType.SUCCESS);
            //// com.developer.res.css.img
            tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();

            FormeFornecedorController.myentity = null;
            this.textInforme.getScene().getWindow().hide();

        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    public void setMyentity(Entity myentitys) {

        this.textInforme.setText("Atualização");

        this.designacao_txt.setText(myentitys.getName());
        this.numero_txt.setText(myentitys.getNIdetification());

        // this.cidade_comb.setValue(myentitys.getAddressId().getCityid());
        //   this.endereco_texA.setText(myentitys.getAddressId().getDecripton());
        for (Contact contact : myentitys.getContactList()) {

            if (contact.getTypeContact() == 1) {
                this.fax_text.setText(contact.getDescription());
            }

            if (contact.getTypeContact() == 2) {
                this.telefone_txt.setText(contact.getDescription());
            }

            if (contact.getTypeContact() == 4) {
                this.telefone_txt2.setText(contact.getDescription());
            }

            if (contact.getTypeContact() == 3) {
                this.email_txtA.setText(contact.getDescription());
            }
        }

        idIdent.setVisible(false);

        this.idIdent.setText("(( " + myentitys.getId() + " ))");
    }

    public void validation() {

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Porfavor digite a designação!");
        validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.designacao_txt.getValidators().add(validator);
        this.designacao_txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.designacao_txt.validate();
            }
        });

        RequiredFieldValidator validators = new RequiredFieldValidator();
        validators.setMessage("Porfavor digite o numero de telefone!");
        validators.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                .glyph(FontAwesomeIcon.WARNING)
                .size("1em")
                .styleClass("error")
                .build());
        this.telefone_txt.getValidators().add(validators);
        this.telefone_txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                this.telefone_txt.validate();
            }
        });

    }

    public boolean validation2() {

        boolean flag = true;

        if (this.designacao_txt.getText().isEmpty()) {
            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite a designação!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.designacao_txt.getValidators().add(validator);
            this.designacao_txt.validate();
            flag = false;
        }

        if (this.telefone_txt.getText().isEmpty()) {
            RequiredFieldValidator validators = new RequiredFieldValidator();
            validators.setMessage("Porfavor digite o numero de telefone!");
            validators.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.telefone_txt.getValidators().add(validators);
            this.telefone_txt.validate();
            flag = false;
        }
        return flag;

    }

    public void selector() {

        cidade_comb.setItems(SelectorList.citySelector());
    }

    @FXML
    private void clossingView(MouseEvent event) {

        this.email_txtA.getScene().getWindow().hide();
    }

    @FXML
    private void newCidadeCalling(ActionEvent event) {
        if (popConfig.isShowing()) {
            popConfig.hide();

        } else {
            popLoad();
            TableInterativeController.myCity = new City();
            TableInterativeController.myTipocliente = null;
            TableInterativeController.myComboBox = this.cidade_comb;
            popConfig.show(this.btn_cidade, 0);

            //popConfig.getRoot().setFocusTraversable(true);
        }
    }

    private void popLoad() {
        try {

            //// com.developer.res.fxml.tableInterative
            popContent = Util.getNode("res/fxml/tableInterative/tableInterative.fxml");
            popConfig.setContentNode(popContent);
            popConfig.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
            popConfig.setCornerRadius(5);

        } catch (IOException e) {
            //e.printStackTrace();
        }

    }
}
