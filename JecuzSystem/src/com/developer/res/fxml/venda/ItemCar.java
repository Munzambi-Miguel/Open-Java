package com.developer.res.fxml.venda;


import com.developer.Main;
import com.developer.java.entity.Product;
import com.developer.res.fxml.clientes.ClienteController;
import com.developer.res.fxml.produtos.ProdutosController;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class ItemCar {

    private final String itemName;
    private final String preco;
    private final int quantidade;
    private final String total;
    private final HBox myBox;
    private final JFXButton btnEliminar;
    private final JFXButton btnEditar;
    private Product product;

    public ItemCar(String itemName, String preco, int quantidade, String total, HBox myBox,
            JFXButton btnEliminar, JFXButton btnEditar, Product product) {
        this.itemName = itemName;
        this.preco = preco;
        this.quantidade = quantidade;
        this.total = total;
        this.myBox = myBox;
        this.btnEliminar = btnEliminar;
        this.btnEditar = btnEditar;
        this.product = product;

        this.btnEditar.setStyle(" -fx-background-color:   #24B6F7 ! important;");
        this.btnEliminar.setStyle(" -fx-background-color:   #d9534f ! important;");

        this.myBox.getChildren().addAll(btnEditar, btnEliminar);
        this.myBox.setAlignment(Pos.CENTER);
        this.myBox.setStyle(" -fx-cursor: hand;");
    }

    public String getItemName() {
        return itemName;
    }

    public String getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public HBox getMyBox() {
        return myBox;
    }

    public JFXButton getBtnEliminar() {
        return btnEliminar;
    }

    public JFXButton getBtnEditar() {
        return btnEditar;
    }

    private void evento() {

        btnEditar.setOnAction((ActionEvent event) -> {
            try {
                // FormeProdutoController.myProduct = ProdutosController.ItemLista.this.entity;
                Util.frameDialog("res/fxml/venda/ProductView.fxml");

                //
            } catch (IOException ex) {
                Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnEliminar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Util.opeConnection();
                Util.enti.getTransaction().begin();
                //  Product myEntity = new ProductJpaController(Util.emf).findProduct(this.id);
                //  myEntity.setState(-1);
                // Product remEntity = Util.enti.merge(myEntity);
                
                Util.enti.getTransaction().commit();
                // myClient.caregarLista();
                
                TrayNotification tray = new TrayNotification("Produto: ",
                        "Foi Removido no sistema... ",
                        NotificationType.SUCCESS);
                //// com.developer.res.css.img
                tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                tray.setRectangleFill(Paint.valueOf("Gray"));
                // tray.setAnimationType(tray.animations.AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
                tray.showAndWait();
            }
        });

    }

    public String getTotal() {
        return total;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return itemName;
    }
    
    

}
