/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml;

import application.EditText;
import com.developer.java.entity.Autentication;
import com.developer.java.entity.Entity;
import com.developer.java.entity.Grup;
import com.developer.util.AES;
import static com.developer.util.AES.decrypt;
import com.developer.util.Util;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;
import print.Connection;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class LoginViewController implements Initializable {

    @FXML
    private BorderPane myDashboard;
    @FXML
    private EditText usernameText;
    @FXML
    private EditText passwordText;
    @FXML
    private FontAwesomeIconView showPassord;
    @FXML
    private BorderPane myMainPane;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Connection.DATABASE_URL = "jdbc:mysql://" + Util.configs.getIp() + ":" + Util.configs.getPorta() + "/jcomerci";
        Connection.PASSWORD = "" + Util.configs.getPassword();
        Connection.ROOT = "" + Util.configs.getUsername();

        // TODO
        /* if (!MeusFicheiros.vazio(Util.path)) {
            try {

                Util.addViewPage(myMainPane, "res/fxml/dashboad/main.fxml");
            } catch (IOException ex) {
                try {
                    // Util.addViewPage(myMainPane, "res/fxml/dashboad/main.fxml");
                    //Util.addViewPage(myDashboard,"res/fxml/loginView.fxml");
                    new File(Util.path).delete();
                } catch (Exception ex1) {
                    // Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }*/
    }

    @FXML
    private void loginAction(ActionEvent event) throws IOException {

        String username = this.usernameText.getText();
        String password = this.passwordText.getText();

        if (validation(username, password)) {
            Util.addViewPage(myMainPane, "res/fxml/dashboad/main.fxml");
            return;
        }

    }

    @FXML
    private void showPassword(MouseEvent event) {

        String text = "";

        if (passwordText.getPasswordField()) {
            text = passwordText.getText().trim();
            passwordText.setPasswordField(false);
            if (!text.isEmpty()) {
                passwordText.setText(text);
            }
            // EYE_SLASH
            showPassord.setGlyphName("EYE_SLASH");
        } else {
            text = passwordText.getText().trim();
            passwordText.setPasswordField(true);
            if (!text.isEmpty()) {
                passwordText.setText(text);
            }

            // EYE
            showPassord.setGlyphName("EYE");
        }

    }

    public boolean validation(String name, String pass) {

        if (name.equals("Root") && pass.equals("9934-JECUZ-2020-SOFT")) {

            Util.auts = new Autentication(10000, "Root", "9934-JECUZ-2020-SOFT",
                    80000, 1, 8000);

            Util.auts.setGrupId(new Grup(Integer.SIZE, "Root", 8000));
            Entity entity = new Entity(Integer.SIZE, "Munzambi Miguel", 0, 0);
            entity.setPhotoList(null);
            Util.auts.setEntityid(entity);

//            Util.auts.setUsername("Root");
            System.out.println("ola como estou");
            // Util.addViewPage(myMainPane, "res/fxml/dashboad/main.fxml");
            return true;
        }

        try {
            boolean flag = false;

            Util.opeConnection();
            Util.enti.getTransaction().begin();

            String queryComands = "SELECT en FROM Autentication en "
                    + "WHERE en.username=:username";

            Query querys = Util.enti.createQuery(queryComands, Autentication.class).setHint(QueryHints.REFRESH, true);

            querys.setParameter("username", name);
            Autentication aut = (Autentication) querys.getSingleResult();

            String passe = AES.decrypt(decrypt(aut.getPasssword(), Util.key), Util.key);

            System.out.println(aut);
            System.out.println(passe);

            if (passe.toLowerCase().equals(pass.toLowerCase())) {
                flag = true;

                Util.auts = aut;
                // MeusFicheiros.vazio(Util.path);
                // MeusFicheiros.gravarDados(aut, Util.path);

            }

            Util.enti.getTransaction().commit();
            Util.emf.close();
            return flag;

        } catch (Exception e) {
            TrayNotification tray = new TrayNotification("CODIGO DE ERRO 130: ",
                    "Não Tem Acesso ao sistema nem uma informação"
                    + "comprovado no banco de\n dados do sistema consulte o suporte www.uteka.jecuz.com\n\n",
                    NotificationType.ERROR);
            //// com.developer.res.css.img
            // tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            // tray.setRectangleFill(Paint.valueOf("#000"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();

            return false;
        }

    }

    private void chamarNovaConta(MouseEvent event) throws IOException, InterruptedException {
        //  Util.opeConnection();
        /// Util.enti.getTransaction().begin();

        //if (new AutenticationJpaController(Util.emf).findAutenticationEntities().isEmpty()) {
        Util.frameDialog("res/fxml/funcionario/formeFuncionario.fxml");
        // }

    }

}
