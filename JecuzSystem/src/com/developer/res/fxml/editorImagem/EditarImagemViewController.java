/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.editorImagem;

import com.developer.Main;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class EditarImagemViewController implements Initializable {

    @FXML
    private VBox imagemViBox;

    public static File selectedFile;

    public static String nameFile = "";

    RubberBandSelection rubberBandSelection;

    public static String newPath;

    ImageView imageView;
    @FXML
    private JFXButton finalizar;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString(), 460, 460, true, true);
            // Image image = new Image(selectedFile.toURI().toString());
            imageView = new ImageView(image);
            imageView.setFitWidth(460);
            imageView.setFitHeight(460);

            Group imageLayer = new Group();

            imageLayer.getChildren().add(imageView);

            imageView.setPreserveRatio(true);
            this.imagemViBox.getChildren().clear();
            this.imagemViBox.getChildren().add(imageLayer);

            rubberBandSelection = new RubberBandSelection(imageLayer);

            ContextMenu contextMenu = new ContextMenu();

            // MenuItem cropMenuItem = new MenuItem("Crop");
            finalizar.setOnAction((ActionEvent e) -> {
                // get bounds for image crop
                Bounds selectionBounds = rubberBandSelection.getBounds();
                crop(selectionBounds);
                finalizar.getScene().getWindow().hide();

            });
            //contextMenu.getItems().add(cropMenuItem);

            // set context menu on image layer
            imageLayer.setOnMousePressed((MouseEvent event) -> {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(imageLayer, event.getScreenX(), event.getScreenY());
                }
            });

        }
    }

    private void crop(Bounds bounds) {

        File file = new File(Util.configs.getDirector() + "\\temp\\" + selectedFile.getName().replace(".", "-") + ".dll");

        file.setWritable(true);
        
        System.out.println("ERROR 65: " + Util.configs.getDirector());

        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage(width, height);
        imageView.snapshot(parameters, wi);

        // save image (without alpha)
        // --------------------------------
        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);

        try {
            /*
            if (!file.exists()) {
                TrayNotification tray = new TrayNotification("CODIGO DE ERRO 139: ",
                        "Não é Possivel carregar a imagem, Verifique se o director \nde imagem esta configurado "
                        + "O em caso que seja maquina cliente veja se \ntem permições para inserir a imagem...\n\n"
                                + file,
                        NotificationType.ERROR);
                //// com.developer.res.css.img
               // tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                tray.setRectangleFill(Paint.valueOf("Gray"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
                tray.showAndWait();

                return;
            }*/

            ImageIO.write(bufImageRGB, "jpg", file);
            newPath = file.getAbsolutePath();
            System.out.println("Image saved to " + file.getAbsolutePath());

        } catch (IOException e) {

        }

        graphics.dispose();

    }

    @FXML
    private void clossingView(MouseEvent event) {
        imagemViBox.getScene().getWindow().hide();
    }

    @FXML
    private void mudarImagem(ActionEvent event) {
    }

    @FXML
    private void finalzandoOperação(ActionEvent event) {
    }

    public static class RubberBandSelection {

        final DragContext dragContext = new DragContext();
        Rectangle rect = new Rectangle();

        Group group;

        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }

        public RubberBandSelection(Group group) {

            this.group = group;

            rect = new Rectangle(0, 0, 0, 0);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (event.isSecondaryButtonDown()) {
                    return;
                }

                // remove old rect
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove(rect);

                // prepare new drag operation
                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();

                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().add(rect);

            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (event.isSecondaryButtonDown()) {
                    return;
                }

                double offsetX = event.getX() - dragContext.mouseAnchorX;
                double offsetY = event.getY() - dragContext.mouseAnchorY;

                if (offsetX > 0) {
                    rect.setWidth(offsetX);
                } else {
                    rect.setX(event.getX());
                    rect.setWidth(dragContext.mouseAnchorX - rect.getX());
                }

                if (offsetY > 0) {
                    rect.setHeight(offsetY);
                } else {
                    rect.setY(event.getY());
                    rect.setHeight(dragContext.mouseAnchorY - rect.getY());
                }
            }
        };

        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if (event.isSecondaryButtonDown()) {
                    return;
                }

                // remove rectangle
                // note: we want to keep the ruuberband selection for the cropping => code is just commented out
                /*
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove( rect);
                 */
            }
        };

        private static final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;

        }
    }

}
