/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.funcionario;

import application.EditText;
import com.developer.Main;
import com.developer.java.controller.AddressJpaController;
import com.developer.java.controller.AutenticationJpaController;
import com.developer.java.controller.ContactJpaController;
import com.developer.java.controller.EntityJpaController;
import com.developer.java.controller.PhotoJpaController;
import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Address;
import com.developer.java.entity.Autentication;
import com.developer.java.entity.City;
import com.developer.java.entity.Client;
import com.developer.java.entity.Contact;
import com.developer.java.entity.Entity;
import com.developer.java.entity.Grup;
import com.developer.java.entity.Photo;
import com.developer.res.fxml.clientes.FormeClientController;
import com.developer.res.fxml.editorImagem.EditarImagemViewController;
import com.developer.res.fxml.tableInterative.TableInterativeController;
import com.developer.util.AES;

import com.developer.util.SelectorList;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javax.persistence.Query;
import org.controlsfx.control.PopOver;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class FormeFuncionarioController implements Initializable {

    @FXML
    private BorderPane formularioBorderPane;
    @FXML
    private Text textInforme;
    @FXML
    private TextField idIdent;
    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private JFXTextField numeroIdentificacao;
    @FXML
    private JFXTextField nomecliente;
    private JFXComboBox tipoCliente;
    @FXML
    private JFXComboBox cidadeCliente;
    @FXML
    private JFXTextArea endereco;
    @FXML
    private JFXTextField fax;
    @FXML
    private JFXTextField telefone;
    @FXML
    private JFXTextField telefone2;
    @FXML
    private JFXTextArea email;
    @FXML
    private JFXProgressBar progressBar1;
    public static Entity myentity;
    @FXML
    private EditText passwordText;
    @FXML
    private FontAwesomeIconView showPassord1;
    @FXML
    private BorderPane formularioBorderPane1;
    @FXML
    private TextField idIdent1;
    @FXML
    private VBox vBoxImagemProduto;
    @FXML
    private ToggleGroup Genero;
    @FXML
    private JFXButton btn_cidade;

    public static final PopOver popConfig = new PopOver();
    private Parent popContent;

    private FileChooser fileChooser = new FileChooser();
    File selectedFile = null;
    @FXML
    private EditText username;
    @FXML
    private JFXCheckBox insert;
    @FXML
    private JFXCheckBox update;
    @FXML
    private JFXCheckBox eliminacao;
    @FXML
    private JFXCheckBox acessoPrint;
    @FXML
    private JFXComboBox<Grup> grupTrabalho;
    @FXML
    private JFXTabPane mytablePanes;

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
        upLoadSeletor();

        if (myentity != null) {
            this.setMyentity(myentity);
        }

        for (Node node : formularioBorderPane.getChildren()) {
            node.setOnMouseMoved((e) -> {

                if (popConfig.isShowing()) {
                    popConfig.hide();
                }
            });
        }

        // group.getSelectedToggle().getUserData().toString()
        //System.out.println();
        //Genero.getToggles().get(2).setSelected(true);
        myIsntance = this;
    }

    private void fecharFormulario(ActionEvent event) {
        ((BorderPane) formularioBorderPane.getParent()).getChildren().remove(2);
    }

    @FXML
    private void guardarCliente(ActionEvent event) throws IOException, NonexistentEntityException {

        if (this.myentity == null) {

            salvar();
        } else {
            alterar();
        }

    }

    public void salvar() throws IOException {

        if (validation2()) {

            if (mytablePanes.getSelectionModel().getSelectedIndex() == 0) {
                mytablePanes.getSelectionModel().selectNext();

                return;
            }

            try {

                Util.opeConnection();
                Util.enti.getTransaction().begin();

                Entity entity = new Entity();

                entity.setName(this.nomecliente.getText().trim());
                entity.setEntityType(2);
                if (!this.numeroIdentificacao.getText().isEmpty()) {
                    entity.setNIdetification(this.numeroIdentificacao.getText().trim());
                }
                entity.setDataInsert(new Date());

                entity.setGenre(Genero.getToggles().indexOf(Genero.selectedToggleProperty().get()));

                entity.setDividad(BigDecimal.ZERO);
                entity.setCompra(BigDecimal.ZERO);

                new EntityJpaController(Util.emf).create(entity);

                // Getting information of de contact from my entintyes
                if (!this.fax.getText().isEmpty()) {
                    Contact myContact = new Contact();
                    myContact.setTypeContact(1);
                    myContact.setDescription(this.fax.getText().trim());
                    myContact.setEntityId(entity);
                    new ContactJpaController(Util.emf).create(myContact);
                }

                if (!this.telefone.getText().isEmpty()) {
                    Contact myContactNumber = new Contact();
                    myContactNumber.setTypeContact(2);
                    myContactNumber.setDescription(this.telefone.getText().trim());
                    myContactNumber.setEntityId(entity);
                    new ContactJpaController(Util.emf).create(myContactNumber);
                }

                if (!this.telefone2.getText().isEmpty()) {

                    Contact myContactNumber2 = new Contact();
                    myContactNumber2.setTypeContact(4);
                    myContactNumber2.setDescription(this.telefone2.getText().trim());
                    myContactNumber2.setEntityId(entity);
                    new ContactJpaController(Util.emf).create(myContactNumber2);
                }

                if (!this.email.getText().isEmpty()) {

                    Contact myContactNumber3 = new Contact();
                    myContactNumber3.setTypeContact(3);
                    myContactNumber3.setDescription(this.email.getText().trim());
                    myContactNumber3.setEntityId(entity);
                    new ContactJpaController(Util.emf).create(myContactNumber3);
                }

                if (cidadeCliente.getSelectionModel().getSelectedItem() != null) {
                    Address address = new Address();
                    address.setCityid((City) this.cidadeCliente.getSelectionModel().getSelectedItem());

                    address.setEntityId(entity);

                    if (!this.endereco.getText().isEmpty()) {
                        address.setDecripton(this.endereco.getText().trim());
                    } else {
                        address.setDecripton("... ");
                    }

                    new AddressJpaController(Util.emf).create(address);
                }

                if (selectedFile != null) {
                    //EditarImagemViewController.newPath;

                    Photo p = new Photo();
                    Util.newPathImagem(new File(EditarImagemViewController.newPath), new File(EditarImagemViewController.newPath).getName());
                    p.setPath(new File(EditarImagemViewController.newPath).getName());
                    p.setEntityId(entity);
                    p.setTypePhoty(3);
                    new PhotoJpaController(Util.emf).create(p);
                    new File(EditarImagemViewController.newPath).delete();
                }

                Autentication aut = new Autentication();

                aut.setUsername(this.username.getText().trim());
                aut.setPasssword(AES.encrypt(AES.encrypt(this.passwordText.getText().trim(), Util.key), Util.key));
                aut.setEntityid(entity);
                aut.setState(0);

                if (this.insert.isCache() && this.update.isCache() && this.eliminacao.isCache() && this.acessoPrint.isCache()) {
                    aut.setAutenticationType(4);
                }
                if (this.insert.isCache() && this.update.isCache() && this.eliminacao.isCache()) {
                    aut.setAutenticationType(3);
                }

                if (this.insert.isCache() && this.update.isCache()) {
                    aut.setAutenticationType(2);
                }
                if (this.insert.isCache()) {
                    aut.setAutenticationType(1);
                }

                aut.setGrupId((Grup) this.grupTrabalho.getSelectionModel().getSelectedItem());

                new AutenticationJpaController(Util.emf).create(aut);

                Util.enti.getTransaction().commit();
                Util.emf.close();

                try {
                    FuncionarioController.myIsntance.caregarLista();
                } catch (Exception e) {
                }

                this.email.getScene().getWindow().hide();

                TrayNotification tray = new TrayNotification("Cliente: " + entity.getName(),
                        "Acabou de alterar o cliente no sistema... ",
                        NotificationType.SUCCESS);
                //// com.developer.res.css.img
                tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                tray.setRectangleFill(Paint.valueOf("Gray"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));

                tray.showAndWait();

            } catch (Exception e) {

            }
        }

    }

    private void alterar() throws NonexistentEntityException, IOException {

        if (mytablePanes.getSelectionModel().getSelectedIndex() == 0) {
            mytablePanes.getSelectionModel().selectNext();

            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmando...");
        List<Contact> lisConta = FormeFuncionarioController.myentity.getContactList();
        alert.setContentText(
                "Setifique de que Pretendes Alteras as Informações"
                + "\n - " + FormeFuncionarioController.myentity.getName()
        );
        alert.setHeaderText(null);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            Util.opeConnection();
            Util.enti.getTransaction().begin();

            FormeFuncionarioController.myentity.setName(this.nomecliente.getText().trim());
            if (!this.numeroIdentificacao.getText().isEmpty()) {
                FormeFuncionarioController.myentity.setNIdetification(this.numeroIdentificacao.getText().trim());
            }

            FormeFuncionarioController.myentity.setGenre(Genero.getToggles().indexOf(Genero.selectedToggleProperty().get()));
            Entity obj = Util.enti.merge(FormeFuncionarioController.myentity);

            Util.enti.getTransaction().commit();
            Util.emf.close();

            Util.opeConnection();
            Util.enti.getTransaction().begin();
            for (Contact cont : lisConta) {

                //Contact conts = Util.enti.merge(cont);
                //Util.enti.remove(conts);
                new ContactJpaController(Util.emf).destroy(cont.getId());
            }

            if (!this.fax.getText().isEmpty()) {
                Contact myContact = new Contact();
                myContact.setTypeContact(1);
                myContact.setDescription(this.fax.getText().trim());
                myContact.setEntityId(FormeFuncionarioController.myentity);
                new ContactJpaController(Util.emf).create(myContact);
            }

            if (!this.telefone.getText().isEmpty()) {
                Contact myContactNumber = new Contact();
                myContactNumber.setTypeContact(2);
                myContactNumber.setDescription(this.telefone.getText().trim());
                myContactNumber.setEntityId(FormeFuncionarioController.myentity);
                new ContactJpaController(Util.emf).create(myContactNumber);
            }

            if (!this.telefone2.getText().isEmpty()) {

                Contact myContactNumber2 = new Contact();
                myContactNumber2.setTypeContact(4);
                myContactNumber2.setDescription(this.telefone2.getText().trim());
                myContactNumber2.setEntityId(FormeFuncionarioController.myentity);
                new ContactJpaController(Util.emf).create(myContactNumber2);
            }

            if (!this.email.getText().isEmpty()) {

                Contact myContactNumber3 = new Contact();
                myContactNumber3.setTypeContact(3);
                myContactNumber3.setDescription(this.email.getText().trim());
                myContactNumber3.setEntityId(FormeFuncionarioController.myentity);
                new ContactJpaController(Util.emf).create(myContactNumber3);
            }

            if (selectedFile != null) {

                if (!FormeFuncionarioController.myentity.getPhotoList().isEmpty()) {

                    new File(FormeFuncionarioController.myentity.getPhotoList().get(0).toString()).delete();

                    new PhotoJpaController(Util.emf).destroy(FormeFuncionarioController.myentity.getPhotoList().get(0).getId());

                    Photo p = new Photo();
                    Util.newPathImagem(new File(EditarImagemViewController.newPath), new File(EditarImagemViewController.newPath).getName());
                    p.setPath(new File(EditarImagemViewController.newPath).getName());
                    p.setEntityId(FormeClientController.myentity);
                    p.setTypePhoty(3);

                    System.out.println("Nome da Imagem: " + new File(EditarImagemViewController.newPath).getName());
                    new PhotoJpaController(Util.emf).create(p);

                    new File(EditarImagemViewController.newPath).delete();

                    System.out.println("estive qui a verificar");
                } else {
                    Photo p = new Photo();
                    Util.newPathImagem(new File(EditarImagemViewController.newPath), new File(EditarImagemViewController.newPath).getName());
                    p.setPath(new File(EditarImagemViewController.newPath).getName());
                    p.setEntityId(FormeFuncionarioController.myentity);
                    System.out.println("Nome da Imagem: " + new File(EditarImagemViewController.newPath).getName());
                    p.setTypePhoty(3);
                    new PhotoJpaController(Util.emf).create(p);
                }

            } else {

            }

            if (cidadeCliente.getSelectionModel().getSelectedItem() != null) {
                Address address = new Address();
                address.setCityid((City) this.cidadeCliente.getSelectionModel().getSelectedItem());
                address.setEntityId(myentity);
                if (!this.endereco.getText().isEmpty()) {
                    address.setDecripton(this.endereco.getText().trim());
                } else {
                    address.setDecripton("... ");
                }
                new AddressJpaController(Util.emf).create(address);
            }

            if (FormeFuncionarioController.myentity.getAutenticationList().isEmpty()) {

                Autentication aut = new Autentication();

                aut.setUsername(this.username.getText().trim());
                aut.setPasssword(AES.encrypt(AES.encrypt(this.passwordText.getText().trim(), Util.key), Util.key));
                aut.setEntityid(FormeFuncionarioController.myentity);
                aut.setState(0);

                if (this.insert.isCache() && this.update.isCache() && this.eliminacao.isCache() && this.acessoPrint.isCache()) {
                    aut.setAutenticationType(4);
                }
                if (this.insert.isCache() && this.update.isCache() && this.eliminacao.isCache()) {
                    aut.setAutenticationType(3);
                }

                if (this.insert.isCache() && this.update.isCache()) {
                    aut.setAutenticationType(2);
                }
                if (this.insert.isCache()) {
                    aut.setAutenticationType(1);
                }

                aut.setGrupId((Grup) this.grupTrabalho.getSelectionModel().getSelectedItem());

                new AutenticationJpaController(Util.emf).create(aut);

            } else {

                Autentication aut = FormeFuncionarioController.myentity.getAutenticationList().get(0);

                aut.setUsername(this.username.getText().trim());
                aut.setPasssword(AES.encrypt(AES.encrypt(this.passwordText.getText().trim(), Util.key), Util.key));
                aut.setEntityid(FormeFuncionarioController.myentity);
                aut.setState(0);

                if (this.insert.isSelected()) {
                    aut.setAutenticationType(1);
                }

                if (this.insert.isSelected() && this.update.isSelected()) {
                    aut.setAutenticationType(2);
                }

                if (this.insert.isSelected() && this.update.isSelected() && this.eliminacao.isSelected()) {
                    aut.setAutenticationType(3);
                }

                if (this.insert.isSelected() && this.update.isSelected() && this.eliminacao.isSelected() && this.acessoPrint.isSelected()) {
                    aut.setAutenticationType(4);
                }

                Util.opeConnection();
                Util.enti.getTransaction().begin();

                aut.setGrupId((Grup) this.grupTrabalho.getSelectionModel().getSelectedItem());
                Util.enti.merge(aut);
                //  new AutenticationJpaController(Util.emf).create(aut);
                Util.enti.getTransaction().commit();
                Util.emf.close();
            }

            TrayNotification tray = new TrayNotification("Cliente: " + obj.getName(),
                    "Acabou de alterar o cliente no sistema... ",
                    NotificationType.SUCCESS);
            //// com.developer.res.css.img
            tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();
            FuncionarioController.myInstace.caregarLista();

            this.textInforme.getScene().getWindow().hide();

        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    public void upLoadSeletor() {

        this.cidadeCliente.setItems(SelectorList.citySelector());
        this.grupTrabalho.setItems(SelectorList.grupSelector());

    }

    public static FormeFuncionarioController getInstance() {
        return myIsntance;
    }

    public static FormeFuncionarioController myIsntance;

    @FXML
    private void showPassword(MouseEvent event) {

        String text = "";

        if (passwordText.getPasswordField()) {
            text = passwordText.getText().trim();
            passwordText.setPasswordField(false);
            if (!text.isEmpty()) {
                passwordText.setText(text);
            }
            // EYE_SLASH
            showPassord1.setGlyphName("EYE_SLASH");
        } else {
            text = passwordText.getText().trim();
            passwordText.setPasswordField(true);
            if (!text.isEmpty()) {
                passwordText.setText(text);
            }

            // EYE
            showPassord1.setGlyphName("EYE");
        }
    }

    @FXML
    private void callingHome(ActionEvent event) {

    }

    @FXML
    private void clossingView(MouseEvent event) {
        this.formularioBorderPane.getScene().getWindow().hide();
    }

    @FXML
    private void newCidadeCalling(ActionEvent event) {
        if (popConfig.isShowing()) {
            popConfig.hide();

        } else {
            popLoad();
            TableInterativeController.myCity = new City();
            TableInterativeController.myTipocliente = null;
            TableInterativeController.myComboBox = this.cidadeCliente;
            popConfig.show(this.btn_cidade, 0);

            //popConfig.getRoot().setFocusTraversable(true);
        }
    }

    public boolean validation2() {

        boolean flag = true;
        ///  Validation from my Client Entityes
        if (this.nomecliente.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite o seu nome!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.nomecliente.getValidators().add(validator);
            this.nomecliente.validate();
            flag = false;
        }

        if (this.telefone.getText().isEmpty()) {
            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite o seu numero!");

            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.telefone.getValidators().add(validator);
            this.telefone.validate();
            flag = false;
        }

        return flag;
    }

    public boolean validation() {

        boolean flag = true;
        ///  Validation from my Client Entityes
        if (this.nomecliente.getText().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite o seu nome!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.nomecliente.getValidators().add(validator);
            this.nomecliente.focusedProperty().addListener((o, oldVal, newVal) -> {
                if (!newVal) {
                    this.nomecliente.validate();
                }
            });

            flag = false;
        }

        if (this.telefone.getText().isEmpty()) {
            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite o seu numero!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.telefone.getValidators().add(validator);
            this.telefone.focusedProperty().addListener((o, oldVal, newVal) -> {
                if (!newVal) {
                    this.telefone.validate();
                }
            });
            flag = false;
        }

        return flag;
    }

    public void setMyentity(Entity myentity) {
        FormeFuncionarioController.myentity = myentity;

        this.textInforme.setText("Atualização");

        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComands = "SELECT en FROM Client en WHERE"
                + " en.entityId =:entityId";

        Query querys = Util.enti.createQuery(queryComands, Client.class);

        try {
            Genero.getToggles().get(myentity.getGenre()).setSelected(true);
        } catch (Exception e) {
            TrayNotification tray = new TrayNotification("CODIGO DE ERRO 697: ",
                    "Não foi encontrado o genero da entidade \nVerifique se essa entidade "
                    + "tem de ser inserido o Genero \nTemos uma lista na Janela de entidades...\n\n",
                    NotificationType.ERROR);
            //// com.developer.res.css.img
            // tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();

            return;

        }

        querys.setParameter("entityId", myentity);

        Util.enti.getTransaction().commit();
        Util.emf.close();

        this.nomecliente.setText(myentity.getName());
        if (myentity.getNIdetification() != null) {
            this.numeroIdentificacao.setText(myentity.getNIdetification());
        }

        for (Contact contact : myentity.getContactList()) {

            if (contact.getTypeContact() == 1) {
                this.fax.setText(contact.getDescription());
            }

            if (contact.getTypeContact() == 2) {
                this.telefone.setText(contact.getDescription());
            }

            if (contact.getTypeContact() == 4) {
                this.telefone2.setText(contact.getDescription());
            }

            if (contact.getTypeContact() == 3) {
                this.email.setText(contact.getDescription());
            }
        }

        if (!myentity.getAddressList().isEmpty()) {
            this.endereco.setText(myentity.getAddressList().get(myentity.getAddressList().size() - 1).getDecripton());
            this.cidadeCliente.setValue(myentity.getAddressList().get(myentity.getAddressList().size() - 1).getCityid());
        }

        if (!myentity.getAutenticationList().isEmpty()) {
            this.username.setText(myentity.getAutenticationList().get(0).getUsername());
            // this.passwordText.setText(myentity.getAutenticationList().get(0).getPasssword());
            try {
                this.passwordText.setText(AES.decrypt(AES.decrypt(myentity.getAutenticationList().get(0).getPasssword(), Util.key), Util.key));
            } catch (Exception e) {
            }

            if (myentity.getAutenticationList().get(0).getAutenticationType() == 4) {
                this.insert.setSelected(true);
                this.update.setSelected(true);
                this.eliminacao.setSelected(true);
                this.acessoPrint.setSelected(true);
            }
            if (myentity.getAutenticationList().get(0).getAutenticationType() == 3) {
                this.insert.setSelected(true);
                this.update.setSelected(true);
                this.eliminacao.setSelected(true);
            }
            if (myentity.getAutenticationList().get(0).getAutenticationType() == 2) {
                this.insert.setSelected(true);
                this.update.setSelected(true);
            }
            if (myentity.getAutenticationList().get(0).getAutenticationType() == 1) {
                this.insert.setSelected(true);
            }

            this.grupTrabalho.setValue(myentity.getAutenticationList().get(0).getGrupId());
        }

        idIdent.setVisible(false);
        this.idIdent.setText("(( " + myentity.getId() + " ))");

        if (!myentity.getPhotoList().isEmpty()) {

            //   System.out.println("i am year ");
            Image images = new Image("file:" + Util.caminhoImagem + "\\" + myentity.getPhotoList().get(0).toString(), 180, 180, true, true);

            ImageView imageView = new ImageView(images);
            imageView.setFitWidth(180);
            imageView.setFitHeight(180);

            imageView.setPreserveRatio(true);
            this.vBoxImagemProduto.getChildren().clear();
            this.vBoxImagemProduto.getChildren().add(imageView);

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

    @FXML
    private void setImagemEventMause(MouseEvent event) throws IOException, InterruptedException {

        // if (cmb_marca.getSelectionModel().getSelectedItem() == null) {
        // return;
        //}
        selectedFile = fileChooser.showOpenDialog(Main.instanceStage);
        if (selectedFile != null) {

            //com.developer.res.fxml.editorImagem
            EditarImagemViewController.selectedFile = selectedFile;
            //  EditarImagemViewController.nameFile = "Produto" + cmb_marca.getSelectionModel().getSelectedItem().toString();
            Util.frameDialog2("res/fxml/editorImagem/editarImagemView.fxml");

            Image image = new Image("file:" + EditarImagemViewController.newPath, 180, 180, true, true);

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(180);
            imageView.setFitHeight(180);

            imageView.setPreserveRatio(true);
            this.vBoxImagemProduto.getChildren().clear();
            this.vBoxImagemProduto.getChildren().add(imageView);

            System.out.println(selectedFile.toURI().toString());
        }

    }

}
