/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer;

import com.developer.java.controller.AutenticationJpaController;
import com.developer.java.controller.GrupJpaController;
import com.developer.java.controller.PaisJpaController;
import com.developer.java.entity.Grup;
import com.developer.java.entity.Pais;
import com.developer.util.Util;
import java.io.IOException;
import javafx.application.Application;
import static javafx.fxml.FXMLLoader.load;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author hdglo
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        stage.getIcons().add(new Image(getClass().getResourceAsStream("uteka.png")));
        try {

            Util.opeConnection();
            Util.enti.getTransaction().begin();

            if (new GrupJpaController(Util.emf).findGrupEntities().isEmpty()) {
                new GrupJpaController(Util.emf).create(new Grup(Integer.SIZE, "Admin", 1));
            }

            if (new AutenticationJpaController(Util.emf).findAutenticationEntities().isEmpty()) {

                if (new PaisJpaController(Util.emf).findPaisEntities().isEmpty()) {
                    Pais p = new Pais();
                    p.setDesignation("Angola");
                    p.setNomeMueda("Kwanza");
                    p.setSiglaMueda("Akz");

                    new GrupJpaController(Util.emf).create(new Grup(Integer.SIZE, "Admin", 1));
                    new GrupJpaController(Util.emf).create(new Grup(Integer.SIZE, "Caixa", 0));

                    new PaisJpaController(Util.emf).create(p);
                }

            }

            Util.enti.getTransaction().commit();
            Util.emf.close();

            getView(stage);

            Util.caminhoImagem = Util.configs.getDirector();

        } catch (Exception e) {
            // e.printStackTrace();
            Util.frameDialog("res/fxml/configuracoes/connetionView.fxml");

        }

//        Util.enti.getTransaction().commit();
//        Util.emf.close(); 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(Main.class, args);
    }

    public static void getView(Stage stage) throws IOException, InterruptedException {

        try {

            Util.opeConnection();
            Util.enti.getTransaction().begin();
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("uteka.png")));
            if (new PaisJpaController(Util.emf).findPaisEntities().isEmpty()) {
                Pais p = new Pais();
                p.setDesignation("Angola");
                p.setNomeMueda("Kwanza");
                p.setSiglaMueda("Akz");

                new GrupJpaController(Util.emf).create(new Grup(Integer.SIZE, "Admin", 1));
                new GrupJpaController(Util.emf).create(new Grup(Integer.SIZE, "Caixa", 0));

                new PaisJpaController(Util.emf).create(p);
            }
            Util.enti.getTransaction().commit();

            Util.emf.close();

            /// com.developer.res.fxml
            Parent root = load(Main.class.getResource("res/fxml/loginView.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(
                    Main.class.getResource("res/css/style.css").toExternalForm(),
                    Main.class.getResource("res/css/controls-spc.css").toExternalForm(),
                    Main.class.getResource("res/css/jfx-tab-pane.css").toExternalForm(),
                    Main.class.getResource("res/css/tableView.css").toExternalForm(),
                    Main.class.getResource("res/css/pagination.css").toExternalForm(),
                    Main.class.getResource("res/css/title-pane.css").toExternalForm()
            );
            roots = (BorderPane) root;

            stage.setScene(scene);

            ////    stage.centerOnScreen();
            ////    stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
            ////    stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
            ////    toggleMaximized(stage);
            stage.initStyle(StageStyle.TRANSPARENT);

            stage.show();
            stage.setMaximized(true);
            instanceStage = stage;

        } catch (Exception ex) {
            // ex.printStackTrace();
            Util.frameDialog("res/fxml/configuracoes/connetionView.fxml");

        }

    }

    public static Stage instanceStage;
    public static BorderPane roots;
    public static boolean maxTrue = false;
}
