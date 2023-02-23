/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.util;

import com.developer.Main;
import static com.developer.Main.instanceStage;
import com.developer.java.entity.Autentication;
import com.developer.java.entity.Movement;
import com.jfoenix.controls.JFXDatePicker;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import static javafx.stage.Modality.WINDOW_MODAL;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Munzambi Miguel
 */
public abstract class Util {

//    public static void mudarPersistencia() {
//
//        String userName = "root";
//        String ip = "localhost";
//        String porta = "3306";
//        String passwod = "";
//
//        // Map<String, String> configOverride = new HashMap<String, String>();
//        java.util.Map configOverride = new HashMap();
//        configOverride.put("javax.persistence.jdbc.url", "jdbc:mysql://" + ip + ":" + porta + "/db_restaurante?zeroDateTimeBehavior=convertToNull");
//        configOverride.put("javax.persistence.jdbc.user", userName);
//        configOverride.put("javax.persistence.jdbc.password", passwod);
//
//        entity = Persistence.createEntityManagerFactory("AppRestauranteV1PU", configOverride);
//        entity = Persistence.createEntityManagerFactory("AppRestauranteV1PU", configOverride);
//
//        entityManager = entity.createEntityManager();
//    }
    public static EntityManagerFactory emf = null;
    @SuppressWarnings("StaticNonFinalUsedInInitialization")
    public static EntityManager enti = null;
    public static String key = "ANLUAN-MIGUEL-ZONEM-MUNZAMBI-ABXZO!!##-BETA";
    public static final String path = "Aut.dll";
    public static final String configPath = "ConfigData.dll";

    public static Autentication auts;
    public static Configuracoes configs;
    public static String openCaixa = "OPENCAIXA.dll";

    public static void opeConnection() {

        try {
            //emf.createEntityManager().flush();
            //EntityManagerUtil.getEM();
            emf = EntityManagerUtil.getEntityManager();
            enti = EntityManagerUtil.getEM();

            //enti.getTransaction().begin();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("LocalVariableHidesMemberVariable")
    public static int redifinerAIndexTabela() {

        Dimension Tela = Toolkit.getDefaultToolkit().getScreenSize();
        @SuppressWarnings("UnusedAssignment")
        int QUANTIDADE_PAGINA = 0;

        if (Tela.height <= 600) {
            QUANTIDADE_PAGINA = 5;
        } else if (Tela.height <= 720) {
            QUANTIDADE_PAGINA = 7;
        } else if (Tela.height <= 768) {
            QUANTIDADE_PAGINA = 8;
        } else if (Tela.height <= 800) {
            QUANTIDADE_PAGINA = 10;
        } else {
            QUANTIDADE_PAGINA = 16;
        }
        return QUANTIDADE_PAGINA;
    }

    public static Dimension Tela = Toolkit.getDefaultToolkit().getScreenSize();

    public static BorderPane myItemLayouts;

    public static void addViewPage(BorderPane myLayout, final String path) throws IOException {

        /**
         * Munzambi Ntemo Miguel Estudante da universidade catolica
         *
         * Metodo de classe tem a finalidade de icluir um componente fxml dentro
         * de outro
         */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(path));
        myItemLayouts = loader.load();
        FadeTransition fIn = new FadeTransition(Duration.millis(1000), myItemLayouts);
        fIn.setFromValue(0.0);
        fIn.setToValue(1.0);
        fIn.play();
        myLayout.setCenter(myItemLayouts);
    }

    public static void addViewPageLeft(BorderPane myLayout, final String path) throws IOException {

        /**
         * Munzambi Ntemo Miguel Estudante da universidade catolica
         *
         * Metodo de classe tem a finalidade de icluir um componente fxml dentro
         * de outro
         */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(path));
        myItemLayouts = loader.load();
        FadeTransition fIn = new FadeTransition(Duration.millis(1000), myItemLayouts);
        fIn.setFromValue(0.0);
        fIn.setToValue(1.0);
        fIn.play();
        myLayout.setLeft(myItemLayouts);
    }

    public static BorderPane getNode(String url) throws IOException {

        /**
         * Este metodo tem a finalidade de retornar o node para outras entidades
         * que o chamarem
         */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(url));
        BorderPane node = loader.load();
        return node;
    }

    public static Stage dialogInstance = null;
    public static Stage dialogInstance2 = null;

    public static void frameDialog(String path) throws IOException, InterruptedException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(path));
        BorderPane myItemLayouts = loader.load();

        Stage dialogLayout = new Stage();
        dialogLayout.getIcons().add(new Image(Main.class.getResourceAsStream("uteka.png")));
        //dialogLayout.getIcons().add(new Image(getClass().getResourceAsStream("uteka.png")));
        // dialogLayout.getIcons().add(new Image(getClass().getResourceAsStream("uteka.png")));
        dialogLayout.initStyle(StageStyle.UNDECORATED);

        dialogLayout.setTitle("Adicionando");
        dialogLayout.initModality(Modality.WINDOW_MODAL);
        dialogLayout.setResizable(false);
        dialogLayout.initOwner(instanceStage);

        dialogInstance = dialogLayout;
        dialogInstance2 = dialogLayout;
        FadeTransition fIn = new FadeTransition(Duration.millis(1000), myItemLayouts);
        fIn.setFromValue(0.0);
        fIn.setToValue(1.0);
        fIn.play();

        Scene scene = new Scene(myItemLayouts);
        dialogLayout.setScene(scene);
        dialogLayout.getIcons().add(new Image(Main.class.getResourceAsStream("uteka.png")));
        dialogLayout.showAndWait();

    }

    public static void frameDialogO(String path) throws IOException, InterruptedException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(path));
        BorderPane myItemLayouts = loader.load();

        Stage dialogLayout = new Stage();
        dialogLayout.initStyle(StageStyle.UNDECORATED);

        dialogLayout.setTitle("Adicionando");
        dialogLayout.initModality(Modality.WINDOW_MODAL);
        dialogLayout.setResizable(false);

        FadeTransition fIn = new FadeTransition(Duration.millis(1000), myItemLayouts);
        fIn.setFromValue(0.0);
        fIn.setToValue(1.0);
        fIn.play();

        Scene scene = new Scene(myItemLayouts);
        dialogLayout.setScene(scene);

        dialogLayout.show();
        Main.instanceStage = dialogLayout;

    }

    public static void frameDialog2(String path) throws IOException, InterruptedException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(path));
        BorderPane myItemLayouts = loader.load();

        Stage dialogLayout = new Stage();
        dialogLayout.initStyle(StageStyle.UNDECORATED);

        dialogLayout.setTitle("Adicionando Novo");
        dialogLayout.initModality(Modality.WINDOW_MODAL);
        dialogLayout.setResizable(false);
        dialogLayout.initOwner(dialogInstance2);
        //dialogInstance2 = dialogLayout;

        FadeTransition fIn = new FadeTransition(Duration.millis(1000), myItemLayouts);
        fIn.setFromValue(0.0);
        fIn.setToValue(1.0);
        fIn.play();

        Scene scene = new Scene(myItemLayouts);
        dialogLayout.setScene(scene);

        dialogLayout.showAndWait();

    }

    /**
     *
     * @param mueda
     * @return
     *
     * Retorna um valor de mueda deste formato 0.000,00
     */
    public static String muedaLocal(String mueda) {
        BigDecimal valor = new BigDecimal(mueda);
        NumberFormat nf = NumberFormat.getInstance();

        String formatado = nf.format(valor);

        String text = formatado.replace("AOA", "");
        return text;
    }

    public static String muedaLocalT(String mueda) {
        BigDecimal valor = new BigDecimal(mueda);
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));

        String formatado = nf.format(valor);

        String text = formatado.replace("AOA", "");

        if (text.contains(".")) {
            return text.replace(",", " ");
        } else {
            return text.replace(",", " ") + ".00";
        }
    }

    public static BigDecimal muedaLocalTRecerc(String mueda) {
        return new BigDecimal(mueda.replace(" ", ""));
    }

    public static void frameDialog(String path, String nome, boolean flag, boolean flags) throws IOException, InterruptedException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(path));
        BorderPane myItemLayouts = loader.load();

        Stage dialogLayout = new Stage();
        dialogLayout.initStyle(StageStyle.UNDECORATED);
        dialogLayout.setTitle(nome);
        if (flag) {
            dialogLayout.initModality(WINDOW_MODAL);
        }
        dialogLayout.setResizable(flags);
        dialogLayout.initOwner(instanceStage);
        dialogLayout.toFront();
        dialogInstance = dialogLayout;
        dialogInstance.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(myItemLayouts);
        dialogLayout.setScene(scene);

        dialogLayout.showAndWait();

    }

    public static void frameDialogs(String path, String nome, boolean flag, boolean flags) throws IOException, InterruptedException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(path));
        BorderPane myItemLayouts = loader.load();
        Stage dialogLayout = new Stage();
        dialogLayout.initStyle(StageStyle.UNDECORATED);
        dialogLayout.setTitle(nome);
        if (flag) {
            dialogLayout.initModality(WINDOW_MODAL);
        }
        dialogLayout.setResizable(flags);
        dialogLayout.initOwner(instanceStage);
        dialogLayout.toFront();
        dialogInstance = dialogLayout;

        int y = Tela.height - 100;
        int x = (Tela.width / 2) + 200;

        Scene scene = new Scene(myItemLayouts, x, (y - 40));
        dialogLayout.setScene(scene);
        dialogLayout.setScene(scene);
        dialogLayout.setX((x - 420));
        dialogLayout.setY(40);
        dialogLayout.showAndWait();

    }

    public static void data(JFXDatePicker datePik, Date dataString) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        String data[] = sdf.format(dataString).split("-");

        int dia = Integer.parseInt(data[0]);
        int mes = Integer.parseInt(data[1]);
        int ano = Integer.parseInt(data[2]);

        int[] datas = new int[3];

        datePik.setValue(LocalDate.of(ano, mes, dia));
    }

    public static String dataFormat(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }

    public static Date dataFormat(String date) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String dateHoraFormat(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(date).replaceAll(" ", " / ");
    }

    /**
     * Buscando as informações do objecto que precizamos setar na tabela
     * <p>
     * Vamos cosiderar a variavel Object name
     * <p>
     */
    public static Stage dialogInstanceDu = null;
    public static Label label;
    public static String objectName;

    public static Object verListObj(String path, String nome, boolean flag, boolean flags, String nomeTab) throws IOException {

        label = new Label(nomeTab);
        label.setFont(new Font("Segoe UI", 16));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(path));
        BorderPane myItemLayouts = loader.load();

        Stage dialogLayout = new Stage();
        dialogLayout.setTitle(nome);
        if (flag) {
            dialogLayout.initModality(WINDOW_MODAL);
        }
        dialogLayout.setResizable(flags);
        dialogLayout.initOwner(dialogInstance);
        dialogLayout.toFront();
        dialogInstanceDu = dialogLayout;
        Scene scene = new Scene(myItemLayouts);
        dialogLayout.setScene(scene);

        dialogLayout.showAndWait();
        return objectName;
    }

    public static void setContent(Tab content, String url) throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(url));

            BorderPane element = loader.load();
            content.setContent(element);
        } catch (Exception e) {
        }
    }

    public static Date redizirUmMes(Date date) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            String datas[] = sdf.format(date).split("-");

            int dia = Integer.parseInt(datas[0]);
            int mes = Integer.parseInt(datas[1]);
            int ano = Integer.parseInt(datas[2]);

            int reduzirMes = mes - 1;

            Date use = sdf.parse("" + ano + "-" + reduzirMes + "-" + dia);
            return use;
        } catch (ParseException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Date somarDia(Date date) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            String datas[] = sdf.format(date).split("-");

            int dia = Integer.parseInt(datas[0]);
            int mes = Integer.parseInt(datas[1]);
            int ano = Integer.parseInt(datas[2]);

            int somardia = dia + 1;

            Date use = sdf.parse(somardia + "-" + mes + "-" + ano);

            return use;
        } catch (Exception ex) {

        }
        return null;
    }

    public static String dateCode(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dates = sdf.format(date).replaceAll(" ", "");
        dates = dates.replaceAll("-", "");
        dates = dates.replaceAll(":", "");

        new java.util.Date();
        return dates;

    }

    public static BorderPane getNoder(final String pathName) throws IOException {

        /**
         * Munzambi Ntemo Miguel Estudante da universidade catolica
         *
         * Metodo de classe tem a finalidade de icluir um componente fxml dentro
         * de outro
         */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource(pathName));
        BorderPane myItemLayouts = loader.load();
        return myItemLayouts;
    }

    public static void showExceptionDialog(Exception e) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("An error occurred:");

        String content = "Error: ";
        if (null != e) {
            content += e.toString() + "\n\n";
        }

        alert.setContentText(content);

        Exception ex = new Exception(e);

        //Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);

        String exceptionText = sw.toString();

        //Set up TextArea
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setPrefHeight(600);
        textArea.setPrefWidth(800);

        //Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(textArea);

        alert.showAndWait();
    }

    public static String executed(String name) {
        String str = "";

        try {

            Process p = Runtime.getRuntime().exec("" + name); // Run
            final InputStream in = p.getInputStream();
            final InputStreamReader src = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(src);
            String line;
            while ((line = br.readLine()) != null) {
                str += line.replace('/', ' ') + "\n";
            }

        } catch (IOException e) {
        }
        return str;

    }

    public static EntityManagerFactory openConnetion() {

        EntityManagerFactory entity = Persistence.createEntityManagerFactory("SystemPU");
        EntityManager entityManager = entity.createEntityManager();
        return entity;
    }

    public static void closeConnection() {

    }

    public static String muedaSendText(String mueda) {

        mueda = mueda.replace(".", "");
        mueda = mueda.replace(",", ".");

        return mueda;
    }

    public static BigDecimal mueda(String mueda) {

        mueda = mueda.replace(".", "");
        mueda = mueda.replace(",", ".");
        return BigDecimal.valueOf(Double.parseDouble(mueda));
    }

    public static BigDecimal muedaTs(String mueda) {

        mueda = mueda.replace(" ", "");
        mueda = mueda.replace(",", ".");
        return BigDecimal.valueOf(Double.parseDouble(mueda));
    }

    public static String caminho = "Imagens\\";

    public static String caminhoImagem = "";
    // public static String caminho = "\\\\192.168.56.1\\imagens\\";

    public static String newPathImagem(File path, String name) throws IOException {

        File fls = new File(path.getAbsolutePath());
        //System.out.println("SOut direct: " + fls);
        BufferedImage imageB = ImageIO.read(fls);
        //  String datas = ("" + dateCode(new Date())).replace("-", "").replace(":", "");
        /// System.out.println(datas);
        // System.out.println("ORK: " + configs.getDirector());

        File fl = new File((configs.getDirector() + "\\" + name));
        System.out.println("QUem : " + fl);
        if (ImageIO.write(imageB, "png", fl)) {
            return fl.toURI().toString();

        } else {
            return null;
        }

    }

    public static String absolutePath() {

        File file = new File(caminhoImagem).getAbsoluteFile();
        System.out.println(file.toURI().toString());
        return file.toURI().toString();

    }

    public static String enclypt(String text) {
        String str = null;

        return str;
    }

    public static String calcularImposto(Movement obj) {

        if ((obj.getPercentagemImposto()) != null) {

            return Util.muedaLocalT(""
                    + new BigDecimal(
                            "" + obj.getProductId().getPrecounitario()).add(
                            new BigDecimal("" + obj.getProductId().getPrecounitario()).multiply(
                                    new BigDecimal("" + obj.getPercentagemImposto()).divide(
                                            BigDecimal.valueOf(100)
                                    )
                            )
                    ).multiply(
                            new BigDecimal("" + obj.getQuantAtual())
                    )
            );
        } else {

            return Util.muedaLocalT("" + new BigDecimal("" + obj.getProductId().getPrecounitario())
                    .multiply(new BigDecimal("" + obj.getProductId().getQuantidAtual())));
        }

    }

}

/**
 * FXMLLoader loader = new FXMLLoader();
 * loader.setLocation(MenuPrincipalController.class.getResource(pathName));
 * BorderPane myItemLayouts = loader.load();
 * myMenuLayout.setCenter(myItemLayouts); MenuPrincipalController.myInstancOf =
 * myItemLayouts;
 */
