/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.produtos;

import com.developer.java.controller.ProduttypeJpaController;
import com.developer.java.controller.Nivel1JpaController;
import com.developer.java.controller.Nivel2JpaController;
import com.developer.java.controller.Nivel3JpaController;
import com.developer.java.controller.PaisJpaController;
import com.developer.java.controller.UnidadeJpaController;
import com.developer.java.entity.Nivel1;
import com.developer.java.entity.Nivel2;
import com.developer.java.entity.Nivel3;
import com.developer.java.entity.Pais;
import com.developer.java.entity.Produttype;
import com.developer.java.entity.Unidade;
import com.developer.util.Util;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
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
public class CategoriaViewController implements Initializable {

    @FXML
    private JFXTextField txt_designacao;

    public static Nivel1 nivel1;
    public static Nivel2 nivel2;
    public static Nivel3 nivel3;
    public static Produttype produttype;
    public static Unidade unidade;
    public static Pais pais;
    @FXML
    private VBox vbInforme;
    @FXML
    private Label information;
    @FXML
    private BorderPane borderMain;
    @FXML
    private VBox vBoxLayout;

    JFXTextField txt_sigla = new JFXTextField();

    public static CategoriaViewController instance;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        this.txt_designacao.setFocusTraversable(true);

        this.txt_sigla.setOnKeyPressed((j) -> {
            if (j.getCode() == KeyCode.ENTER) {
                guardar();
            }

            if (j.getCode() == KeyCode.ESCAPE) {
                this.txt_designacao.getScene().getWindow().hide();
            }
        });

        this.txt_designacao.setOnKeyPressed((j) -> {
            if (j.getCode() == KeyCode.ENTER) {
                guardar();
            }

            if (j.getCode() == KeyCode.ESCAPE) {
                this.txt_designacao.getScene().getWindow().hide();
            }
        });

        instance = this;

    }

    @FXML
    private void guardarInformacao(ActionEvent event) {

        guardar();
    }

    public void guardar() {
        if (validation()) {

            // informatiom = this.txt_designacao.getText().trim();
            if (produttype != null) {
                insertDesignatio();
            }

            if (nivel1 != null) {
                insertNivel1();
            }

            if (nivel2 != null) {
                insertNivel2();
            }

            if (nivel3 != null) {
                insertNivel3();
            }

            if (pais != null) {
                insertPais();
            }

            if (unidade != null) {
                insertUnidade();
            }

        }
    }

    boolean validation() {

        if (this.txt_designacao.getText().trim().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite a Designação!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_designacao.getValidators().add(validator);

            this.txt_designacao.focusedProperty().addListener((o, oldVal, newVal) -> {
                if (!newVal) {
                    this.txt_designacao.validate();
                }

                this.vbInforme.setVisible(false);
            });
            this.txt_designacao.validate();
            if (unidade == null) {
                return false;
            }
        }

        if (unidade != null && this.txt_sigla.getText().isEmpty()) {
            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite a Sigla!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_sigla.getValidators().add(validator);

            this.txt_sigla.focusedProperty().addListener((o, oldVal, newVal) -> {
                if (!newVal) {
                    this.txt_sigla.validate();
                }

                this.vbInforme.setVisible(false);
            });
            this.txt_sigla.validate();
            return false;
        }

        return true;
    }

    public void insertDesignatio() {

        if (validation("Produttype", "designation")) {
            Util.opeConnection();

            produttype.setDesignation(this.txt_designacao.getText());
            produttype.setDateInsert(new Date());
            produttype.setFuncionario(Util.auts);
            new ProduttypeJpaController(Util.emf).create(produttype);

            FormeProdutoController.instance.selectProdutType();

            this.txt_designacao.getScene().getWindow().hide();
            FormeProdutoController.instance.getCmb_marca().setValue(produttype);

        } else {
            this.vbInforme.setVisible(true);
            this.information.setText("'" + this.txt_designacao.getText() + "' não é valido ja existe na lista!...");
        }

    }

    public void getvBoxLayout() {

        if (unidade != null) {

            txt_sigla.setPromptText("Digite a sigla * .");
            txt_sigla.setLabelFloat(true);

            vBoxLayout.getChildren().add(1, txt_sigla);

        } else {
            vBoxLayout.getChildren().remove(1);
        }

    }

    public void insertNivel1() {

        // Nivel1
        if (validation("Nivel1", "designation")) {
            Util.opeConnection();
            Util.enti.getTransaction().begin();

            nivel1.setDesignation(this.txt_designacao.getText().trim());
            new Nivel1JpaController(Util.emf).create(nivel1);
            Util.enti.getTransaction().commit();
            Util.emf.close();

            // 
            FormeProdutoController.instance.selectorNivel1();
            FormeProdutoController.instance.getNivelUm().setValue(nivel1);
            this.txt_designacao.getScene().getWindow().hide();

        } else {
            this.vbInforme.setVisible(true);
            this.information.setText("'" + this.txt_designacao.getText() + "' não é valido ja existe na lista!...");
        }
    }

    public void insertNivel2() {

        if (FormeProdutoController.instance.getNivelUm().getSelectionModel().getSelectedItem() != null) {

            if (validationNiveBfor("Nivel2", (Nivel1) FormeProdutoController.instance.getNivelUm().getSelectionModel().getSelectedItem(),
                    "designation"
            )) {

                Util.opeConnection();
                Util.enti.getTransaction().begin();
                nivel2.setNivel1Id((Nivel1) FormeProdutoController.instance.getNivelUm().getSelectionModel().getSelectedItem());
                nivel2.setDesignation(this.txt_designacao.getText().trim());
                new Nivel2JpaController(Util.emf).create(nivel2);
                Util.enti.getTransaction().commit();
                Util.emf.close();

                this.txt_designacao.getScene().getWindow().hide();
            } else {
                this.vbInforme.setVisible(true);
                this.information.setText("'" + this.txt_designacao.getText() + "' não é valido ja existe na lista!...");
            }

            // 
            FormeProdutoController.instance.selectorNivel2();
            FormeProdutoController.instance.getNivelDois().setValue(nivel2);

        } else {
            TrayNotification tray = new TrayNotification("Information: ",
                    "Não selecion nem uma dependencia não foi possivel guardar ",
                    NotificationType.WARNING);
            //// com.developer.res.css.img
            // tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            this.txt_designacao.getScene().getWindow().hide();
        }

    }

    public void insertNivel3() {

        if (FormeProdutoController.instance.getNivelDois().getSelectionModel().getSelectedItem() != null) {

            if (validationNiveBfor("Nivel3", (Nivel2) FormeProdutoController.instance.getNivelDois().getSelectionModel().getSelectedItem(),
                    "designation"
            )) {

                Util.opeConnection();
                Util.enti.getTransaction().begin();

                nivel3.setNivel2Id((Nivel2) FormeProdutoController.instance.getNivelDois().getSelectionModel().getSelectedItem());
                nivel3.setDesignation(this.txt_designacao.getText().trim());
                new Nivel3JpaController(Util.emf).create(nivel3);

                Util.enti.getTransaction().commit();
                Util.emf.close();
                this.txt_designacao.getScene().getWindow().hide();
            } else {
                this.vbInforme.setVisible(true);
                this.information.setText("'" + this.txt_designacao.getText() + "' não é valido ja existe na lista!...");
            }

            FormeProdutoController.instance.selectorNivel3();
            FormeProdutoController.instance.getNivelTres().setValue(nivel3);

        } else {
            TrayNotification tray = new TrayNotification("Information: ",
                    "Não selecion nem uma dependencia não foi possivel guardar ",
                    NotificationType.WARNING);
            //// com.developer.res.css.img
            // tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            this.txt_designacao.getScene().getWindow().hide();
        }

    }

    /**
     *
     * @param Table
     * @param colunm
     * @return
     */
    private boolean validation(String Table, String colunm) {
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        String queryComands = "SELECT p FROM " + Table + " p WHERE"
                + " p." + colunm + "=:" + colunm + "";
        Query querys = Util.enti.createQuery(queryComands);
        querys.setParameter("" + colunm, this.txt_designacao.getText());
        Util.enti.getTransaction().commit();
        List lis = querys.getResultList();
        Util.emf.close();

        //  Valor falso ou verdadeiro
        return lis.isEmpty();

    }

    /**
     *
     * @param table
     * @param table2
     * @param colunm
     * @return
     *
     * Este metodo tem a finalidade de validar se o elemento ja exite na sub
     * categoria
     */
    public boolean validationNiveBfor(String table, Nivel1 table2, String colunm) {

        Util.opeConnection();
        Util.enti.getTransaction().begin();

        String queryComands = "SELECT p FROM " + table + " p "
                + " WHERE "
                + " p." + colunm + "=:" + colunm + " AND p.nivel1Id=:nivel1Id";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        querys.setParameter("" + colunm, this.txt_designacao.getText());
        querys.setParameter("nivel1Id", table2);

        Util.enti.getTransaction().commit();

        List lis = querys.getResultList();
        Util.emf.close();

        return lis.isEmpty();
    }

    public boolean validationNiveBfor(String table, Nivel2 table2, String colunm) {

        Util.opeConnection();
        Util.enti.getTransaction().begin();

        String queryComands = "SELECT p FROM " + table + " p "
                + " WHERE "
                + " p." + colunm + "=:" + colunm + " AND p.nivel2Id=:nivel2Id";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        querys.setParameter("" + colunm, this.txt_designacao.getText());
        querys.setParameter("nivel2Id", table2);

        Util.enti.getTransaction().commit();

        List lis = querys.getResultList();
        Util.emf.close();

        return lis.isEmpty();
    }

    private void insertPais() {

        if (validation("Pais", "designation")) {
            Util.opeConnection();
            Util.enti.getTransaction().begin();

            pais.setDesignation(this.txt_designacao.getText().trim());
            new PaisJpaController(Util.emf).create(pais);
            Util.enti.getTransaction().commit();
            Util.emf.close();

            // 
            FormeProdutoController.instance.selectorPais();
            this.txt_designacao.getScene().getWindow().hide();
            FormeProdutoController.instance.getCob_origem().setValue(pais);

        } else {
            this.vbInforme.setVisible(true);
            this.information.setText("'" + this.txt_designacao.getText() + "' não é valido ja existe na lista!...");
        }

    }

    public void insertUnidade() {

        if (validation("Unidade", "designacao")) {
            Util.opeConnection();
            Util.enti.getTransaction().begin();

            unidade.setDesignacao(this.txt_designacao.getText().trim());
            unidade.setSigla(this.txt_sigla.getText().trim());
            new UnidadeJpaController(Util.emf).create(unidade);
            Util.enti.getTransaction().commit();
            Util.emf.close();

            // 
            FormeProdutoController.instance.selectUnidade();
            this.txt_designacao.getScene().getWindow().hide();
            FormeProdutoController.instance.getCb_unidades().setValue(unidade);

        } else {
            this.vbInforme.setVisible(true);
            this.information.setText("'" + this.txt_designacao.getText() + "' não é valido ja existe na lista!...");
        }
    }
}
