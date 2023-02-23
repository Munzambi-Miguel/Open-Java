/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splash;

import com.developer.Main;
import com.developer.util.Util;
import com.jfoenix.controls.JFXProgressBar;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import splash.LoaderController.InitCompletionHandler;

/**
 *
 * @author Munzambi Miguel
 */
public class SplashScrean extends Application {

    private Parent root;
    public static String[] argAs;

    @Override
    public void start(Stage stage) throws Exception {

        root = FXMLLoader.load(getClass().getResource("loader.fxml"));

        try {

            final Task<ObservableList<String>> friendTask = new Task<ObservableList<String>>() {
                @SuppressWarnings("override")
                protected ObservableList<String> call() throws InterruptedException {

                    //Util.opeConnection();
                    double j = 0;
                    for (int i = 10; i < 300; i++) {

                        //  Util.opeConnection();
                        Thread.sleep(5);
                        if (i > 1) {

                            lerMeuArquivos("ConfigData.dll");
                            double percent = i / 300f;

                            JFXProgressBar pss = LoaderController.myInstance.getMyProgressBar();
                            pss.progressProperty().set(percent > 1 ? 1 : percent);
                        }

                    }

                    return null;
                }
            };

            showSplash(stage, friendTask, () -> {
                try {

                    showMainStage(friendTask.valueProperty());
                } catch (Exception ex) {

                }
            });
            // th.start();
            new Thread(friendTask).start();
        } catch (Exception e) {
        }

        Scene scene = new Scene(root);
        scene.setFill(null);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("uteka.png")));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        SplashScrean.stage = stage;
        stage.show();

    }

    public static Stage stage;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SplashScrean.argAs = args;
        launch(args);
    }

    private void showSplash(final Stage initStage, Task<?> task, InitCompletionHandler initCompletionHandler) {
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {

                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), root);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });
    }

    private void showMainStage(ReadOnlyObjectProperty<ObservableList<String>> friends) throws Exception {

        try {

            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("uteka.png")));
            Main.getView(stage);
            Util.caminhoImagem = Util.configs.getDirector();
            //stage.show();
        } catch (Exception e) {

            //    Util.showExceptionDialog(e);
        }

        //LigarServidor.stopApach();
        // LigarServidor.stopMysql();
    }

    public boolean lerMeuArquivos(String Path) {
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
                        Util.configs.setIp(linha.replace("ip:", ""));
                    }

                    if (i == 1) {
                        Util.configs.setPorta(linha.replace("port:", ""));
                    }
                    if (i == 2) {
                        Util.configs.setUsername(linha.replace("user:", ""));
                    }
                    if (i == 3) {
                        Util.configs.setPassword(linha.replace("pass:", "").replace("null", ""));
                    }
                    if (i == 4) {
                        Util.configs.setDirector(linha.replace("file:", "").replace("null", ""));
                    }

                    i++;
                }

            } while (linha != null);

            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
