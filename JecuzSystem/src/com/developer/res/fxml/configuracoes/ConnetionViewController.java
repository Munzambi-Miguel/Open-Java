/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.configuracoes;

import com.developer.Main;
import static com.developer.Main.roots;
import com.developer.java.controller.GrupJpaController;
import com.developer.java.controller.PaisJpaController;
import com.developer.java.entity.Grup;
import com.developer.java.entity.Pais;
import static com.developer.res.fxml.configuracoes.Connection.connection;
import com.developer.util.Configuracoes;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import static javafx.fxml.FXMLLoader.load;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import print.Connection;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class ConnetionViewController implements Initializable {

    @FXML
    private Text textInforme;

    @FXML
    private JFXTextField ipNretWork;
    @FXML
    private JFXTextField porta;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField password;
    @FXML
    private JFXButton guardar;
    Configuracoes config = new Configuracoes();
    @FXML
    private Label lb_filePath;
    public String directory;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        lerMeuArquivos(Util.configPath);

        this.ipNretWork.setText(config.getIp());
        this.porta.setText(config.getPorta());
        this.username.setText(config.getUsername());
        this.password.setText(config.getPassword());
        this.lb_filePath.setText(config.getDirector());

    }

    @FXML
    private void clossingView(MouseEvent event) {
        this.ipNretWork.getScene().getWindow().hide();
    }

    public void escrevarArquivo(String Path, String objs) {

        try {
            FileOutputStream arquivo = new FileOutputStream(Path, true);
            PrintWriter pr = new PrintWriter(arquivo);
            pr.println(objs);
            pr.close();
            arquivo.close();

        } catch (Exception e) {

        }

    }

    @FXML
    private void novaConfiguracao(ActionEvent event) throws IOException, InterruptedException {

        eliminarDadosDoFicheiro(Util.configPath);

        escrevarArquivo(Util.configPath, "ip:" + this.ipNretWork.getText());
        escrevarArquivo(Util.configPath, "port:" + this.porta.getText());
        escrevarArquivo(Util.configPath, "user:" + this.username.getText());
        escrevarArquivo(Util.configPath, "pass:" + this.password.getText());
        escrevarArquivo(Util.configPath, "file:" + this.directory);

        try {
            Main.instanceStage.close();

            Util.opeConnection();
            Util.enti.getTransaction().begin();

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
            Stage stage = new Stage();
            stage.setScene(scene);

            ////    stage.centerOnScreen();
            ////    stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
            ////    stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
            ////    toggleMaximized(stage);
            // stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
            stage.setMaximized(true);

            Main.instanceStage = stage;

          

        } catch (Exception e) {

            try {
                this.ipNretWork.getScene().getWindow().hide();

                Util.opeConnection();

                Util.enti.getTransaction().begin();

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
                Stage stage = new Stage();
                stage.setScene(scene);

                ////    stage.centerOnScreen();
                ////    stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
                ////    stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
                ////    toggleMaximized(stage);
                // stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
                stage.setMaximized(true);

                Main.instanceStage = stage;
            } catch (Exception as) {

                Util.frameDialogO("res/fxml/configuracoes/connetionView.fxml");
            }

        }

        // Main.instanceStage.show();
    }

    public void lerMeuArquivos(String Path) {
        try {

            FileInputStream arquivo = new FileInputStream(Path);
            InputStreamReader input = new InputStreamReader(arquivo);
            BufferedReader br = new BufferedReader(input);
            String linha;
            int i = 0;
            do {
                linha = br.readLine();
                if (linha != null) {

                    if (i == 0) {
                        config.setIp(linha.replace("ip:", ""));
                    }

                    if (i == 1) {
                        config.setPorta(linha.replace("port:", ""));
                    }
                    if (i == 2) {
                        config.setUsername(linha.replace("user:", ""));
                    }
                    if (i == 3) {
                        config.setPassword(linha.replace("pass:", "").replace("null", ""));
                    }
                    if (i == 4) {
                        config.setDirector(linha.replace("file:", "").replace("null", ""));
                    }

                    i++;
                }

            } while (linha != null);

        } catch (Exception e) {

        }

    }

    public void eliminarDadosDoFicheiro(String Path) {
        File file = new File(Path);
        try {

            FileWriter fw2 = new FileWriter(file, false);
            fw2.close();

        } catch (IOException e) {

        }

    }

    @FXML
    private void collingPath(MouseEvent event) {

        try {

            DirectoryChooser chooser = new DirectoryChooser();
            // .showOpenDialog(Main.instanceStage);
            File selectedDirectory = chooser.showDialog(Main.instanceStage);

            if (!selectedDirectory.getAbsolutePath().contains("\\uteka\\imagens")) {

                File fls = new File(selectedDirectory + "\\uteka\\imagens\\temp");
                File flss = new File(selectedDirectory + "\\uteka\\imagens");

                if (!fls.exists()) {
                    flss.setWritable(true);
                    fls.setWritable(true);

                    fls.mkdirs();

                }

                selectedDirectory = flss;
            }

            lb_filePath.setText(selectedDirectory.getAbsolutePath());
            directory = selectedDirectory.getAbsolutePath();
            Util.caminhoImagem = selectedDirectory.getAbsolutePath();

        } catch (Exception e) {

            TrayNotification tray = new TrayNotification("CODIGO DE ERRO 279: ",
                    "Não foi selecionada a pasta para inserir os ficheiros do sistema"
                    + " \n Para mais informação consulte o suporte www.uteka.jecuz.com\n\n",
                    NotificationType.ERROR);
            //// com.developer.res.css.img
            // tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            //tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();
        }
    }

   

}
