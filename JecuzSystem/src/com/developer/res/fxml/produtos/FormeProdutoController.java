/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.produtos;

import com.developer.Main;
import com.developer.java.controller.PhotoJpaController;
import com.developer.java.controller.ProductJpaController;
import com.developer.java.controller.exceptions.NonexistentEntityException;
import com.developer.java.entity.Nivel1;
import com.developer.java.entity.Nivel2;
import com.developer.java.entity.Nivel3;
import com.developer.java.entity.Pais;
import com.developer.java.entity.Photo;
import com.developer.java.entity.Product;
import com.developer.java.entity.Produttype;
import com.developer.java.entity.Unidade;
import com.developer.res.fxml.editorImagem.EditarImagemViewController;
import com.developer.util.SelectorList;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
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
public class FormeProdutoController implements Initializable {

    @FXML
    private BorderPane formularioBorderPane;
    @FXML
    private Text textInforme;
    @FXML
    private TextField idIdent;
    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private JFXTextField codigoDeBarra;

    @FXML
    private JFXComboBox nivelDois;
    @FXML
    private JFXComboBox nivelTres;
    @FXML
    private JFXTextArea detalhes;

    public static FormeProdutoController instance;

    public static Product myProduct = null;
    @FXML
    private JFXComboBox nivelUm;
    @FXML
    private JFXComboBox cmb_marca;
    @FXML
    private JFXButton btn_tipo;

    public static final PopOver popConfig = new PopOver();
    private Parent popContent;
    @FXML
    private JFXButton btn_nivel1;
    @FXML
    private JFXTextField txt_valorUnidade;
    @FXML
    private JFXComboBox cb_unidades;
    @FXML
    private JFXButton btn_unidade;
    @FXML
    private JFXButton btn_nivel2;
    @FXML
    private JFXButton btn_nivel3;
    @FXML
    private JFXComboBox cob_origem;
    @FXML
    private JFXButton btn_origem;
    @FXML
    private VBox vBoxImagemProduto;

    private FileChooser fileChooser = new FileChooser();
    File selectedFile = null;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
        try {
            // popLoad();
            selectores();

            if (FormeProdutoController.myProduct != null) {
                this.setMyProduct(FormeProdutoController.myProduct);
            }
        } catch (Exception e) {
        }

        for (Node node : formularioBorderPane.getChildren()) {
            node.setOnMouseMoved((e) -> {

                if (popConfig.isShowing()) {
                    popConfig.hide();
                }
            });
        }
        FormeProdutoController.instance = this;
    }

    private void fecharFormulario(ActionEvent event) {
        BorderPane pane = ((BorderPane) formularioBorderPane.getParent());
        pane.getChildren().remove(2);
    }

    @FXML
    private void guardarCliente(ActionEvent event) throws IOException, NonexistentEntityException {

        if (FormeProdutoController.myProduct == null) {
            salvar();
        } else {
            alterar();
        }
    }

    public void salvar() throws IOException {

        Util.opeConnection();
        Util.enti.getTransaction().begin();

        Product entity = new Product();

        entity.setProdutTypeid((Produttype) this.getCmb_marca().getSelectionModel().getSelectedItem());

        if (!this.codigoDeBarra.getText().isEmpty()) {
            entity.setPartNamber(this.codigoDeBarra.getText().trim());
        }

        entity.setPaisId((Pais) this.cob_origem.getSelectionModel().getSelectedItem());

        entity.setInsertData(new Date());
        entity.setNivel3Id((Nivel3) nivelTres.getSelectionModel().getSelectedItem());

        entity.setUnidadeId((Unidade) this.cb_unidades.getSelectionModel().getSelectedItem());
        if (!this.txt_valorUnidade.getText().isEmpty()) {
            entity.setValorUnidade(BigDecimal.valueOf(Double.parseDouble(this.txt_valorUnidade.getText().trim())));
        }

        entity.setPrecounitario(BigDecimal.ZERO);
        entity.setEntradada(BigDecimal.ZERO);
        entity.setSaidas(BigDecimal.ZERO);
        entity.setPrecounitario(BigDecimal.ZERO);
        entity.setFaturacao(BigDecimal.ZERO);
        entity.setQuantidAtual(0);

        entity.setAutenticationId(Util.auts);

        if (!this.detalhes.getText().isEmpty()) {
            entity.setDescription(this.detalhes.getText().trim());
        }

        //entity.setDesiganation(designacao.getText().trim());
        entity.setState(0);

        new ProductJpaController(Util.emf).create(entity);

        if (selectedFile != null) {
            //EditarImagemViewController.newPath;

            Photo p = new Photo();
            Util.newPathImagem(new File(EditarImagemViewController.newPath), new File(EditarImagemViewController.newPath).getName());
            p.setPath(new File(EditarImagemViewController.newPath).getName());
            p.setProductId(entity);
            p.setTypePhoty(3);
            new PhotoJpaController(Util.emf).create(p);
            new File(EditarImagemViewController.newPath).delete();
        }

        Util.enti.getTransaction().commit();
        Util.emf.close();

        ProdutosController.instace.caregarLista();

        TrayNotification tray = new TrayNotification("Produto: ",
                "Acabou de alterar o produto no sistema... ",
                NotificationType.SUCCESS);
        //// com.developer.res.css.img
        tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
        tray.setRectangleFill(Paint.valueOf("Gray"));
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(Duration.seconds(3));

        this.codigoDeBarra.getScene().getWindow().hide();

        tray.showAndWait();

    }

    private void alterar() throws IOException, NonexistentEntityException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmando...");

        alert.setContentText(
                "Setifique de que Pretendes Alteras as Informações"
                + "\n - "
        );
        alert.setHeaderText(null);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            Util.opeConnection();
            Util.enti.getTransaction().begin();

            //FormeProdutoController.myProduct.setDesiganation(this.designacao.getText().trim());
            FormeProdutoController.myProduct.setProdutTypeid((Produttype) this.getCmb_marca().getSelectionModel().getSelectedItem());

            if (!this.codigoDeBarra.getText().isEmpty()) {
                FormeProdutoController.myProduct.setPartNamber(this.codigoDeBarra.getText().trim());
            }

            FormeProdutoController.myProduct.setPaisId((Pais) this.cob_origem.getSelectionModel().getSelectedItem());

            FormeProdutoController.myProduct.setInsertData(new Date());
            FormeProdutoController.myProduct.setNivel3Id((Nivel3) nivelTres.getSelectionModel().getSelectedItem());

            FormeProdutoController.myProduct.setUnidadeId((Unidade) this.cb_unidades.getSelectionModel().getSelectedItem());
            if (!this.txt_valorUnidade.getText().isEmpty()) {
                FormeProdutoController.myProduct.setValorUnidade(BigDecimal.valueOf(Double.parseDouble(this.txt_valorUnidade.getText().trim())));
            }

            // System.out.println(this.detalhes.getText());
            if (this.detalhes.getText() != null) {
                FormeProdutoController.myProduct.setDescription(this.detalhes.getText().trim());
            }

            Product obj = Util.enti.merge(FormeProdutoController.myProduct);

            if (selectedFile != null) {

                if (!myProduct.getPhotoList().isEmpty()) {
                    new File(myProduct.getPhotoList().get(0).toString()).delete();

                    new PhotoJpaController(Util.emf).destroy(myProduct.getPhotoList().get(0).getId());
                    Photo p = new Photo();
                    Util.newPathImagem(new File(EditarImagemViewController.newPath), new File(EditarImagemViewController.newPath).getName());
                    p.setPath(new File(EditarImagemViewController.newPath).getName());
                    p.setProductId(myProduct);
                    p.setTypePhoty(3);
                    new PhotoJpaController(Util.emf).create(p);

                    new File(EditarImagemViewController.newPath).delete();
                } else {
                    Photo p = new Photo();
                    Util.newPathImagem(new File(EditarImagemViewController.newPath), new File(EditarImagemViewController.newPath).getName());
                    p.setPath(new File(EditarImagemViewController.newPath).getName());
                    p.setProductId(myProduct);
                    p.setTypePhoty(3);
                    new PhotoJpaController(Util.emf).create(p);
                }

            } else {

            }

            Util.enti.getTransaction().commit();
            Util.emf.close();

            this.detalhes.getScene().getWindow().hide();

            TrayNotification tray = new TrayNotification("Produto: ",
                    "Foi atualizado no sistema... ",
                    NotificationType.SUCCESS);
            //// com.developer.res.css.img
            tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();
            ProdutosController.instace.caregarLista();
        } else {
            // ... user chose CANCEL or closed the dialog
        }

    }

    public void setMyProduct(Product myProduct) {

        this.textInforme.setText("Atualização");
        if (myProduct.getPartNamber() != null) {
            this.codigoDeBarra.setText(myProduct.getPartNamber());
        }

        this.cmb_marca.setValue(myProduct.getProdutTypeid());
        this.cob_origem.setValue(myProduct.getPaisId());

        if (myProduct.getValorUnidade() != null) {
            this.txt_valorUnidade.setText("" + myProduct.getValorUnidade());
        }
        this.cb_unidades.setValue(myProduct.getUnidadeId());

        this.detalhes.setText(myProduct.getDescription());
        if (myProduct.getNivel3Id() != null) {
            this.nivelUm.setValue(myProduct.getNivel3Id().getNivel2Id().getNivel1Id());
            this.nivelDois.setValue(myProduct.getNivel3Id().getNivel2Id());
            this.nivelTres.setValue(myProduct.getNivel3Id());
        }

        if (!myProduct.getPhotoList().isEmpty()) {

            //   System.out.println("i am year ");
            Image images = new Image("file:/" + myProduct.getPhotoList().get(0).toString(), 180, 180, true, true);

            ImageView imageView = new ImageView(images);
            imageView.setFitWidth(180);
            imageView.setFitHeight(180);

            imageView.setPreserveRatio(true);
            this.vBoxImagemProduto.getChildren().clear();
            this.vBoxImagemProduto.getChildren().add(imageView);

        }
    }

    public static FormeProdutoController getInstance() {
        return instance;
    }

    public void selectores() {
        cb_unidades.setItems(SelectorList.unidadeSelector());
        cob_origem.setItems(SelectorList.paisSelector());
        this.cmb_marca.setItems(SelectorList.produtTypeSelector());
        nivelUm.setItems(SelectorList.nivel1Selector());
        nivelDois.setItems(SelectorList.nivel2Selector());
        nivelTres.setItems(SelectorList.nivel3Selector());
        nivelUm.setOnAction((event) -> {
            nivelDois.setItems(SelectorList.nivel2Selector((Nivel1) nivelUm.getSelectionModel().getSelectedItem()));
        });

        nivelDois.setOnAction((Event event) -> {
            nivelTres.setItems(SelectorList.nivel3Selector((Nivel2) nivelDois.getSelectionModel().getSelectedItem()));
        });

    }

    boolean flag = false;

    @FXML
    private void clossingView(MouseEvent event) {

        try {
            btn_nivel1.getScene().getWindow().hide();
        } catch (Exception e) {
        }

    }

    @FXML
    private void callingFormTipeSub(ActionEvent event) {

        if (popConfig.isShowing()) {
            popConfig.hide();

        } else {
            popLoad();
            CategoriaViewController.nivel1 = null;
            CategoriaViewController.nivel2 = null;
            CategoriaViewController.nivel3 = null;
            CategoriaViewController.unidade = null;
            CategoriaViewController.produttype = new Produttype();
            CategoriaViewController.pais = null;
            CategoriaViewController.instance.getvBoxLayout();
            popConfig.show(btn_tipo, 0);

            //popConfig.getRoot().setFocusTraversable(true);
        }

    }

    public void selectProdutType() {
        this.cmb_marca.setItems(SelectorList.produtTypeSelector());
    }

    public void selectorNivel1() {
        nivelUm.setItems(SelectorList.nivel1Selector());
    }

    public void selectorNivel2() {
        nivelDois.setItems(SelectorList.nivel2Selector());
        if (nivelUm.getSelectionModel().getSelectedItem() != null) {
            nivelDois.setItems(SelectorList.nivel2Selector((Nivel1) nivelUm.getSelectionModel().getSelectedItem()));
        }
    }

    public void selectorPais() {
        cob_origem.setItems(SelectorList.paisSelector());
    }

    public void selectorNivel3() {
        nivelTres.setItems(SelectorList.nivel3Selector());
        if (nivelDois.getSelectionModel().getSelectedItem() != null) {
            nivelTres.setItems(SelectorList.nivel3Selector((Nivel2) nivelDois.getSelectionModel().getSelectedItem()));
        }
    }

    public void selectUnidade() {
        cb_unidades.setItems(SelectorList.unidadeSelector());
    }

    private void popLoad() {
        try {

            popContent = Util.getNode("res/fxml/produtos/categoriaView.fxml");
            popConfig.setContentNode(popContent);
            popConfig.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
            popConfig.setCornerRadius(5);

        } catch (IOException e) {
            //e.printStackTrace();
        }

    }

    @FXML
    private void addingNivel1(ActionEvent event) {

        if (popConfig.isShowing()) {
            popConfig.hide();

        } else {
            popLoad();
            CategoriaViewController.nivel1 = new Nivel1();
            CategoriaViewController.nivel2 = null;
            CategoriaViewController.nivel3 = null;
            CategoriaViewController.produttype = null;
            CategoriaViewController.unidade = null;
            CategoriaViewController.pais = null;
            CategoriaViewController.instance.getvBoxLayout();
            popConfig.show(btn_nivel1, 0);

            //popConfig.getRoot().setFocusTraversable(true);
        }

    }

    @FXML
    private void addingUnidade(ActionEvent event) {

        if (popConfig.isShowing()) {
            popConfig.hide();

        } else {
            popLoad();
            CategoriaViewController.nivel1 = null;
            CategoriaViewController.nivel2 = null;
            CategoriaViewController.nivel3 = null;
            CategoriaViewController.produttype = null;
            CategoriaViewController.unidade = new Unidade();
            CategoriaViewController.pais = null;

            CategoriaViewController.instance.getvBoxLayout();
            popConfig.show(btn_unidade, 0);

            //popConfig.getRoot().setFocusTraversable(true);
        }

    }

    @FXML
    private void addingNivel2(ActionEvent event) {
        if (popConfig.isShowing()) {
            popConfig.hide();

        } else {
            popLoad();
            CategoriaViewController.nivel1 = null;
            CategoriaViewController.nivel2 = new Nivel2();
            CategoriaViewController.nivel3 = null;
            CategoriaViewController.produttype = null;
            CategoriaViewController.unidade = null;
            CategoriaViewController.pais = null;
            CategoriaViewController.instance.getvBoxLayout();
            popConfig.show(btn_nivel2, 0);

            //popConfig.getRoot().setFocusTraversable(true);
        }
    }

    @FXML
    private void addingNivel3(ActionEvent event) {
        if (popConfig.isShowing()) {
            popConfig.hide();

        } else {
            popLoad();
            CategoriaViewController.nivel1 = null;
            CategoriaViewController.nivel2 = null;
            CategoriaViewController.nivel3 = new Nivel3();
            CategoriaViewController.produttype = null;
            CategoriaViewController.unidade = null;
            CategoriaViewController.pais = null;
            CategoriaViewController.instance.getvBoxLayout();
            popConfig.show(btn_nivel3, 0);

            //popConfig.getRoot().setFocusTraversable(true);
        }

    }

    public JFXComboBox getNivelDois() {
        return nivelDois;
    }

    public JFXComboBox getNivelTres() {
        return nivelTres;
    }

    public JFXComboBox getNivelUm() {
        return nivelUm;
    }

    public JFXComboBox getCmb_marca() {
        return cmb_marca;
    }

    public JFXComboBox getCb_unidades() {
        return cb_unidades;
    }

    public JFXComboBox getCob_origem() {
        return cob_origem;
    }

    @FXML
    private void addingOrigem(ActionEvent event) {
        if (popConfig.isShowing()) {
            popConfig.hide();

        } else {
            popLoad();
            CategoriaViewController.nivel1 = null;
            CategoriaViewController.nivel2 = null;
            CategoriaViewController.nivel3 = null;
            CategoriaViewController.produttype = null;
            CategoriaViewController.unidade = null;
            CategoriaViewController.pais = new Pais();
            popConfig.show(btn_origem, 0);

            //popConfig.getRoot().setFocusTraversable(true);
        }
    }

    @FXML
    private void setImagemEventMause(MouseEvent event) throws IOException, InterruptedException {

        if (cmb_marca.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        selectedFile = fileChooser.showOpenDialog(Main.instanceStage);
        if (selectedFile != null) {

            //com.developer.res.fxml.editorImagem
            EditarImagemViewController.selectedFile = selectedFile;
            EditarImagemViewController.nameFile = "Produto" + cmb_marca.getSelectionModel().getSelectedItem().toString();
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

}
