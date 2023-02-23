/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splash;

import com.jfoenix.controls.JFXProgressBar;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class LoaderController implements Initializable {

    @FXML
    private JFXProgressBar myProgressBar;
    public static LoaderController myInstance;
    @FXML
    private BorderPane myBorder;


    /**
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        myInstance = this;
    }    
    
    
     private void showSplash(final Stage initStage, Task<?> task, InitCompletionHandler initCompletionHandler) {

        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {

                initStage.toFront();

                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });
    }

    public interface InitCompletionHandler {

        void complete();
    }

    public JFXProgressBar getMyProgressBar() {
        return myProgressBar;
    }
    
    
}
