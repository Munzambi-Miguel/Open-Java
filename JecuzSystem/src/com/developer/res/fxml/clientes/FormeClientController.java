/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.clientes;

import com.developer.Main;
import com.developer.java.controller.AddressJpaController;
import com.developer.java.controller.ClientJpaController;
import com.developer.java.controller.ContactJpaController;
import com.developer.java.controller.EntityJpaController;
import com.developer.java.controller.PhotoJpaController;

import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Address;
import com.developer.java.entity.City;
import com.developer.java.entity.Client;
import com.developer.java.entity.Contact;
import com.developer.java.entity.Entity;
import com.developer.java.entity.Photo;

import com.developer.java.entity.Tipocliente;
import com.developer.res.fxml.editorImagem.EditarImagemViewController;
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
import javafx.scene.control.Alert.AlertType;
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
public class FormeClientController implements Initializable {

    @FXML
    private BorderPane formularioBorderPane;
    @FXML
    private JFXTextField numeroIdentificacao;
    @FXML
    private JFXTextField nomecliente;
    @FXML
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
    private TextField idIdent;
    @FXML
    private JFXProgressBar progressBar;

    public static FormeClientController instance;

    public static Entity myentity;

    private Client myclient;

    @FXML
    private Text textInforme;
    @FXML
    private VBox vBoxImagemProduto;

    private FileChooser fileChooser = new FileChooser();
    File selectedFile = null;
    @FXML
    private ToggleGroup Genero;
    @FXML
    private JFXButton btn_tipoClient;
    @FXML
    private JFXButton btn_cidade;

    public static final PopOver popConfig = new PopOver();
    private Parent popContent;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     *
     * Para pegar o index de cada radiobunto na ToggleGroup basta apenas usar
     * essa intrução
     * @ Genero.getToggles().indexOf(Genero.selectedToggleProperty().get())
     *
     * Para setar a posição tem de usar essa instrução a baixo
     * @ Genero.getToggles().get(2).setSelected(true);
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        //this.setMyentity(this.myentity);
        upLoadSeletor();
        validation();

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
        instance = this;
    }

    @FXML
    private void guardarCliente(ActionEvent event) throws IOException {

        //System.out.println(Genero.getToggles().indexOf(Genero.selectedToggleProperty().get()));
        try {
            if (FormeClientController.myentity == null) {
                salvar();
            } else {
                alterar();
            }
        } catch (NonexistentEntityException | IOException e) {
        }

    }

    public void salvar() throws IOException {

        if (validation2()) {

            try {

                Util.opeConnection();
                Util.enti.getTransaction().begin();

                Entity entity = new Entity();

                entity.setName(this.nomecliente.getText().trim());
                entity.setEntityType(1);
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

                Client myClients = new Client();
                myClients.setEntityId(entity);
                myClients.setTipoClienteid((Tipocliente) this.tipoCliente.getSelectionModel().getSelectedItem());
                myClients.setDefaultDescription("Cliente");

                new ClientJpaController(Util.emf).create(myClients);

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

                Util.enti.getTransaction().commit();
                Util.emf.close();

                ClienteController.myIsntance.caregarLista();

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

            } catch (IOException e) {

            }
        }

    }

    private void alterar() throws NonexistentEntityException, IOException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmando...");
        List<Contact> lisConta = FormeClientController.myentity.getContactList();
        alert.setContentText(
                "Setifique de que Pretendes Alteras as Informações"
                + "\n - " + FormeClientController.myentity.getName()
        );
        alert.setHeaderText(null);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            Util.opeConnection();
            Util.enti.getTransaction().begin();

            FormeClientController.myentity.setName(this.nomecliente.getText().trim());
            if (!this.numeroIdentificacao.getText().isEmpty()) {
                FormeClientController.myentity.setNIdetification(this.numeroIdentificacao.getText().trim());
            }

            FormeClientController.myentity.setGenre(Genero.getToggles().indexOf(Genero.selectedToggleProperty().get()));
            Entity obj = Util.enti.merge(FormeClientController.myentity);

            this.myclient.setTipoClienteid((Tipocliente) this.tipoCliente.getSelectionModel().getSelectedItem());

            Util.enti.merge(this.myclient);

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
                myContact.setEntityId(FormeClientController.myentity);
                new ContactJpaController(Util.emf).create(myContact);
            }

            if (!this.telefone.getText().isEmpty()) {
                Contact myContactNumber = new Contact();
                myContactNumber.setTypeContact(2);
                myContactNumber.setDescription(this.telefone.getText().trim());
                myContactNumber.setEntityId(FormeClientController.myentity);
                new ContactJpaController(Util.emf).create(myContactNumber);
            }

            if (!this.telefone2.getText().isEmpty()) {

                Contact myContactNumber2 = new Contact();
                myContactNumber2.setTypeContact(4);
                myContactNumber2.setDescription(this.telefone2.getText().trim());
                myContactNumber2.setEntityId(FormeClientController.myentity);
                new ContactJpaController(Util.emf).create(myContactNumber2);
            }

            if (!this.email.getText().isEmpty()) {

                Contact myContactNumber3 = new Contact();
                myContactNumber3.setTypeContact(3);
                myContactNumber3.setDescription(this.email.getText().trim());
                myContactNumber3.setEntityId(FormeClientController.myentity);
                new ContactJpaController(Util.emf).create(myContactNumber3);
            }

            if (selectedFile != null) {

                if (!FormeClientController.myentity.getPhotoList().isEmpty()) {

                    new File(FormeClientController.myentity.getPhotoList().get(0).toString()).delete();

                    new PhotoJpaController(Util.emf).destroy(FormeClientController.myentity.getPhotoList().get(0).getId());

                    Photo p = new Photo();
                    Util.newPathImagem(new File(EditarImagemViewController.newPath), new File(EditarImagemViewController.newPath).getName());
                    p.setPath(new File(EditarImagemViewController.newPath).getName());
                    p.setEntityId(FormeClientController.myentity);
                    p.setTypePhoty(3);
                    new PhotoJpaController(Util.emf).create(p);

                    new File(EditarImagemViewController.newPath).delete();

                    System.out.println("estive qui a verificar");
                } else {
                    Photo p = new Photo();
                    Util.newPathImagem(new File(EditarImagemViewController.newPath), new File(EditarImagemViewController.newPath).getName());
                    p.setPath(new File(EditarImagemViewController.newPath).getName());
                    p.setEntityId(FormeClientController.myentity);
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
            Util.enti.getTransaction().commit();
            Util.emf.close();

            TrayNotification tray = new TrayNotification("Cliente: " + obj.getName(),
                    "Acabou de alterar o cliente no sistema... ",
                    NotificationType.SUCCESS);
            //// com.developer.res.css.img
            tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();
            ClienteController.myIsntance.caregarLista();

            this.textInforme.getScene().getWindow().hide();

        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    public void upLoadSeletor() {
        this.tipoCliente.setItems(SelectorList.tipoCLiente());
        this.cidadeCliente.setItems(SelectorList.citySelector());
    }

    public static FormeClientController getInstance() {
        return instance;
    }

    public void setMyentity(Entity myentity) {
        FormeClientController.myentity = myentity;

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
        }
        

        querys.setParameter("entityId", myentity);

        this.myclient = (Client) querys.getSingleResult();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        this.nomecliente.setText(myentity.getName());
        if (myentity.getNIdetification() != null) {
            this.numeroIdentificacao.setText(myentity.getNIdetification());
        }
        this.tipoCliente.setValue(this.myclient.getTipoClienteid());

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

        idIdent.setVisible(false);
        this.idIdent.setText("(( " + myentity.getId() + " ))");

        if (!myentity.getPhotoList().isEmpty()) {

            //   System.out.println("i am year ");
            Image images = new Image("file:/" + myentity.getPhotoList().get(0).toString(), 180, 180, true, true);

            ImageView imageView = new ImageView(images);
            imageView.setFitWidth(180);
            imageView.setFitHeight(180);

            imageView.setPreserveRatio(true);
            this.vBoxImagemProduto.getChildren().clear();
            this.vBoxImagemProduto.getChildren().add(imageView);

        }

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
        if (this.tipoCliente.getSelectionModel().isEmpty()) {

            //flag = false;
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
        if (this.tipoCliente.getSelectionModel().isEmpty()) {

            //flag = false;
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

    @FXML
    private void clossingView(MouseEvent event) {
        this.endereco.getScene().getWindow().hide();
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

            Image image = new Image("file:/" + EditarImagemViewController.newPath, 180, 180, true, true);

            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(180);
            imageView.setFitHeight(180);

            imageView.setPreserveRatio(true);
            this.vBoxImagemProduto.getChildren().clear();
            this.vBoxImagemProduto.getChildren().add(imageView);

            System.out.println(selectedFile.toURI().toString());
        }

    }

    @FXML
    private void newTipoCliente(ActionEvent event) {
        if (popConfig.isShowing()) {
            popConfig.hide();

        } else {
            popLoad();
            TableInterativeController.myCity = null;
            TableInterativeController.myTipocliente = new Tipocliente();
            TableInterativeController.myComboBox = this.tipoCliente;
            popConfig.show(this.btn_tipoClient, 0);

            //popConfig.getRoot().setFocusTraversable(true);
        }
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
