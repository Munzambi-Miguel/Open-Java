/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.venda;

import com.developer.java.entity.Product;
import static com.developer.res.fxml.venda.VendaViewController.myList;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class ProductViewController implements Initializable {

    @FXML
    private BorderPane myDashboard;

    public static Product obj;
    @FXML
    private VBox imageVBespace;
    @FXML
    private Text textInforme;
    @FXML
    private Text designacao_text;
    @FXML
    private Text cateroria_text1;
    @FXML
    private Text categoria_text2;
    @FXML
    private Text categoria_text3;
    @FXML
    private JFXButton addCar_btn;
    @FXML
    private JFXTextField quantidade_text;
    @FXML
    private Text text_preco;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        listElement();
        this.quantidade_text.setFocusTraversable(true);

        this.quantidade_text.setText("1");
        this.quantidade_text.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.addCar();
            }
        });
    }

    @FXML
    private void clossingView(MouseEvent event) {
        this.myDashboard.getScene().getWindow().hide();
    }

    public void listElement() {
        if (obj != null) {
            designacao_text.setText(obj.getProdutTypeid().toString());
            if (obj.getNivel3Id() != null) {
                cateroria_text1.setText(obj.getNivel3Id().getNivel2Id().getNivel1Id().toString());
                categoria_text2.setText(obj.getNivel3Id().getNivel2Id().toString());
                categoria_text3.setText(obj.getNivel3Id().toString());
            }

            text_preco.setText(Util.muedaLocalT("" + obj.getPrecounitario()));

            if (!obj.getPhotoList().isEmpty()) {

                //   System.out.println("i am year ");
                Image images = new Image("file:/" + obj.getPhotoList().get(0).toString(), 300, 300, true, true);

                ImageView imageView = new ImageView(images);
                imageView.setFitWidth(300);
                imageView.setFitHeight(300);

                imageView.setPreserveRatio(true);
                this.imageVBespace.getChildren().clear();
                this.imageVBespace.getChildren().add(imageView);

            }
        }
    }

    @FXML
    private void addingCarriMethod(TouchEvent event) {
        addCar();
    }

    @FXML
    private void addCarriMethod(ActionEvent event) {
        addCar();
    }

    public void addCar() {

        try {
            FontAwesomeIconView iconPrints = new FontAwesomeIconView();
            FontAwesomeIconView iconPrints2 = new FontAwesomeIconView();

            iconPrints.setGlyphName("PENCIL_SQUARE_ALT");
            iconPrints2.setGlyphName("TRASH_ALT");

            iconPrints.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));
            iconPrints2.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

            if (!myList.isEmpty()) {

                for (ItemCar itemCar : myList) {

                    if (itemCar.toString().equals(obj.toString()) && (itemCar.getQuantidade() + Integer.parseInt(this.quantidade_text.getText().trim()) <= obj.getQuantidAtual())) {

                        myList.set(myList.indexOf(itemCar), new ItemCar(
                                obj.toString(),
                                Util.muedaLocalT("" + obj.getPrecounitario()),
                                itemCar.getQuantidade() + Integer.parseInt(this.quantidade_text.getText().trim()),
                                Util.muedaLocalT("" + obj.getPrecounitario().multiply(BigDecimal.valueOf(itemCar.getQuantidade() + Integer.parseInt(this.quantidade_text.getText().trim())))),
                                new HBox(5),
                                new JFXButton("", iconPrints2),
                                new JFXButton("", iconPrints),
                                obj
                        ));

                        VendaViewController.sumTotal();
                        this.myDashboard.getScene().getWindow().hide();
                        return;
                    } else if (!(itemCar.getQuantidade() + Integer.parseInt(this.quantidade_text.getText().trim()) <= obj.getQuantidAtual())) {

                        final Runnable runnable
                                = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
                        if (runnable != null) {
                            runnable.run();
                        }
                        return;
                    }

                }

            }

            if ((Integer.parseInt(this.quantidade_text.getText().trim()) <= obj.getQuantidAtual())) {
                myList.add(new ItemCar(
                        obj.toString(),
                        Util.muedaLocalT("" + obj.getPrecounitario()),
                        Integer.parseInt(this.quantidade_text.getText().trim()),
                        Util.muedaLocalT("" + obj.getPrecounitario().multiply(BigDecimal.valueOf(Integer.parseInt(this.quantidade_text.getText().trim())))),
                        new HBox(5),
                        new JFXButton("", iconPrints2),
                        new JFXButton("", iconPrints),
                        obj
                ));
            } else {
                final Runnable runnable
                        = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
                if (runnable != null) {
                    runnable.run();
                }
                return;
            }

            VendaViewController.sumTotal();
            this.myDashboard.getScene().getWindow().hide();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        //VendaViewController.myTableView.setFocusTraversable(true);
    }

}
