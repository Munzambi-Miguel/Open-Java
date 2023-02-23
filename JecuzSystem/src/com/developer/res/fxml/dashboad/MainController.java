/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.dashboad;

import com.developer.Main;
import com.developer.java.controller.AberturaDiaJpaController;
import com.developer.java.controller.MaquinaJpaController;
import com.developer.java.entity.Autentication;
import com.developer.java.entity.Maquina;
import com.developer.util.GetMacAdress;
import com.developer.util.NumCaixa;
import com.developer.util.Util;
import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class MainController implements Initializable {

    @FXML
    private JFXButton btn_inicio;
    @FXML
    private JFXButton btn_cliente;
    @FXML
    private JFXButton btn_fornecimento;
    @FXML
    private JFXButton btn_produto;
    @FXML
    private JFXButton btn_funcionario;
    @FXML
    private JFXButton btn_entrada;
    @FXML
    private JFXButton btn_ajuste;
    @FXML
    private JFXButton btn_venda;
    @FXML
    private BorderPane myBorderPane;
    @FXML
    private Label title;
    @FXML
    private JFXBadge notifications;
    @FXML
    private JFXBadge messages;
    @FXML
    private JFXButton config;
    @FXML
    private HBox configure;

    public static final PopOver popConfig = new PopOver();
    private Parent popContent;
    @FXML
    private TitledPane cadastros_title;
    @FXML
    private TitledPane stock_title;
    @FXML
    private JFXBadge notifications1;
    @FXML
    private BorderPane menuViewBord;
    @FXML
    private BorderPane myMainPaneLS;
    @FXML
    private JFXButton btn_saida;
    @FXML
    private VBox myVB;
    @FXML
    private Label labeSetConfig;

    boolean flag = true;
    @FXML
    private TitledPane relatorio_title;
    @FXML
    private TitledPane relatorio_title1;
    @FXML
    private JFXButton btn_rvenda;
    @FXML
    private JFXButton btn_rmovimento;
    @FXML
    private JFXButton btn_rcaixa;
    @FXML
    private JFXButton btn_rinventario;
    @FXML
    private Text username;
    @FXML
    private Text usernameseton;
    @FXML
    private Circle myCircle;
    @FXML
    private Circle myPerfil2;
    @FXML
    private JFXButton btn_dataconfig;
    @FXML
    private TitledPane relatorio_title2;
    @FXML
    private JFXButton btn_venda1;
    @FXML
    private JFXButton btn_perfilempresa;

    public static BorderPane myStaticBorder;
    @FXML
    private TitledPane relatorio_title3;
    @FXML
    private JFXButton btn_caixaInformation;
    @FXML
    private JFXButton btn_devolucao;
    @FXML
    private JFXButton btn_anulamento;
    @FXML
    private JFXButton btn_tabelas;
    @FXML
    private AnchorPane tabela_view;
    @FXML
    private AnchorPane venda_btn;
    @FXML
    private VBox vb_menu;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        myStaticBorder = this.myBorderPane;
        popLoad();
        spands();

        Util.opeConnection();
        Util.enti.getTransaction().begin();

        /*
            Autentication aut = new AutenticationJpaController(Util.emf)
                    .findAutentication(((Autentication) MeusFicheiros.carregarDados(Util.path)).getId());
         */
        Autentication aut = Util.auts;
        Util.auts = aut;

        username.setText(aut.getUsername());
        usernameseton.setText(aut.getUsername());
        Image im = new Image(Main.class.getResourceAsStream("res/img/information.png"), 400, 400, true, true);

        if (aut.getGrupId().getGrupType() == 0) {

            // cadastros_title.setVisible(false);
            //stock_title.setVisible(false);
            // tabela_view.setVisible(false);
            // relatorio_title.setVisible(false);
            // relatorio_title3.setVisible(false);
            // relatorio_title1.setVisible(false);
            // relatorio_title2.setVisible(false);         
            ((VBox) cadastros_title.getParent()).getChildren().remove(cadastros_title);
            ((VBox) stock_title.getParent()).getChildren().remove(stock_title);
            ((VBox) tabela_view.getParent()).getChildren().remove(tabela_view);
            ((VBox) relatorio_title.getParent()).getChildren().remove(relatorio_title);
            ((VBox) relatorio_title3.getParent()).getChildren().remove(relatorio_title3);
            ((VBox) relatorio_title1.getParent()).getChildren().remove(relatorio_title1);
            ((VBox) relatorio_title2.getParent()).getChildren().remove(relatorio_title2);
            //      ((VBox)cadastros_title.getParent()).getChildren().remove(relatorio_title1);

        } else if ((aut.getGrupId().getGrupType() == 1)) {
            // O Administrador não vê as configurações no sistema
            ((VBox) relatorio_title1.getParent()).getChildren().remove(relatorio_title1);
        } else if (aut.getGrupId().getGrupType() == 8000) {
            myVB.setStyle("-fx-background-color: #f80202 !important;");
            configure.setStyle("-fx-background-color: #f80202 !important;");
            ((VBox) venda_btn.getParent()).getChildren().remove(venda_btn);
            im = new Image(Main.class.getResourceAsStream("res/img/tecnico.png"));
        }

        if (!Util.caminhoImagem.isEmpty() && (aut.getEntityid().getPhotoList().size() > 0)) {
            //File files = new File(Util.absolutePath() + aut.getEntityid().getPhotoList().get(0).toString());

            System.out.println("Valor: " + aut.getEntityid().getPhotoList().size());

            if (aut.getEntityid().getPhotoList().size() > 0) {
                im = new Image(Util.absolutePath() + aut.getEntityid().getPhotoList().get(0).toString(), 400, 400, true, true);

            }
        }

        this.myCircle.setFill(new ImagePattern(im));
        this.myPerfil2.setFill(new ImagePattern(im));

        Util.enti.getTransaction().commit();
        Util.emf.close();
        // TODO

        try {

            Util.addViewPage(myBorderPane, "res/fxml/inicio/dashboard.fxml");
        } catch (Exception ex) {
            System.out.println("Ola como estas -");
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);

        }

        for (Node node : this.myMainPaneLS.getChildren()) {
            node.setFocusTraversable(false);

        }

        controleConfig();
        crateMaquina();
    }

    @FXML
    private void callingHome(ActionEvent event) throws IOException {
        /// com.developer.res.fxml.clientes
        Util.addViewPage(myBorderPane, "res/fxml/inicio/dashboard.fxml");
    }

    @FXML
    private void callingCliente(ActionEvent event) throws IOException {
        /// com.developer.res.fxml.inicio
        Util.addViewPage(myBorderPane, "res/fxml/clientes/Cliente.fxml");
    }

    @FXML
    private void callingFornecimento(ActionEvent event) throws IOException {
        // com.developer.res.fxml.fornecimento
        Util.addViewPage(myBorderPane, "res/fxml/fornecimento/fornecedor.fxml");
    }

    @FXML
    private void callingProduto(ActionEvent event) throws IOException {

        // com.developer.res.fxml.produtos
        Util.addViewPage(myBorderPane, "res/fxml/produtos/produtos.fxml");
    }

    @FXML
    private void callingFuncionario(ActionEvent event) throws IOException {
        // com.developer.res.fxml.funcionario
        Util.addViewPage(myBorderPane, "res/fxml/funcionario/funcionario.fxml");
    }

    @FXML
    private void callingEntradas(ActionEvent event) throws IOException {
        // com.developer.res.fxml.entrada
        Util.addViewPage(myBorderPane, "res/fxml/entrada/entradaStock.fxml");
    }

    @FXML
    private void callingAjusteStock(ActionEvent event) throws IOException {

        //com.developer.res.fxml.ajuste
        Util.addViewPage(myBorderPane, "res/fxml/ajuste/ajusteStock.fxml");
    }

    @FXML
    private void chamadoMenuVenda(ActionEvent event) throws IOException {

        // com.developer.res.fxml.venda
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        if (new AberturaDiaJpaController(Util.emf).findAberturaDia(InetAddress.getLocalHost().getHostAddress()) != null) {

            TrayNotification tray = new TrayNotification("Venda: ",
                    "Bem vindo ao menu de Venda aguarde enquanto \n organizamos seu ambiente!... ",
                    NotificationType.SUCCESS);
            tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();

            Util.addViewPage(myBorderPane, "res/fxml/venda/vendaView.fxml");
        } else {
            TrayNotification tray = new TrayNotification("Facturação: ",
                    "Não foi possivel, caixa fechado!... ",
                    NotificationType.ERROR);
            //tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
            tray.setRectangleFill(Paint.valueOf("Gray"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.showAndDismiss(Duration.seconds(3));
            tray.showAndWait();
        }

        Util.enti.getTransaction().commit();
        Util.emf.close();
    }

    @FXML
    private void openNotification(MouseEvent event) {
    }

    @FXML
    private void openMessages(MouseEvent event) {
    }

    @FXML
    private void openConfig(MouseEvent event) {

        if (popConfig.isShowing()) {
            popConfig.hide();
        } else {
            popConfig.show(labeSetConfig, 0);

        }

    }

    private void popLoad() {
        try {
            popContent = Util.getNode("res/fxml/dashboad/configuracaoView.fxml");
            popConfig.setContentNode(popContent);
            popConfig.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void maxTesla(ActionEvent event) {

        if (Main.instanceStage.isMaximized()) {
            Main.instanceStage.setFullScreen(false);
        } else {
            Main.instanceStage.setFullScreen(true);
        }

    }

    public void spands() {

        this.cadastros_title.setOnMouseClicked((event) -> {
            stock_title.setExpanded(false);
        });

        stock_title.setOnMouseClicked((MouseEvent event) -> {
            cadastros_title.setExpanded(false);
        });
    }

    int i = 0;

    @FXML
    private void hamburgerMenu(ActionEvent event) {

        //if(this.myBorderPane.getChildren())
        if (i == 0) {
            this.myMainPaneLS.getChildren().remove(this.menuViewBord);
            i = 1;
        } else {
            this.myMainPaneLS.setLeft(this.menuViewBord);
            i = 0;
        }
    }

    @FXML
    private void callingSaida(ActionEvent event) throws IOException {

        Util.addViewPage(myBorderPane, "res/fxml/saida/saidaStock.fxml");

    }

    private void controleConfig() {
        configure.setOnMouseMoved((m) -> {
            flag = false;
        });

        configure.setOnMouseExited((m) -> {
            flag = true;
        });

        for (Node node : myMainPaneLS.getChildren()) {
            node.setOnMouseMoved((e) -> {

                if (flag) {
                    if (popConfig.isShowing()) {
                        popConfig.hide();
                    }
                }

            });
        }
    }

    @FXML
    private void callingRVenda(ActionEvent event) throws IOException {

        //com.developer.res.fxml.relatorios
        Util.addViewPage(myBorderPane, "res/fxml/relatorios/relatorioVenda.fxml");

    }

    @FXML
    private void callingRMovimento(ActionEvent event) throws IOException {
        //com.developer.res.fxml.relatorios
        Util.addViewPage(myBorderPane, "res/fxml/relatorios/relatorioMovimento.fxml");
    }

    @FXML
    private void callingRCaixa(ActionEvent event) throws IOException {
        //com.developer.res.fxml.relatorios
        Util.addViewPage(myBorderPane, "res/fxml/relatorios/relatorioCaixa.fxml");
    }

    @FXML
    private void callingRInventario(ActionEvent event) throws IOException {
        //com.developer.res.fxml.relatorios
        Util.addViewPage(myBorderPane, "res/fxml/relatorios/inventario.fxml");
    }

    @FXML
    private void callingConfigData(ActionEvent event) throws IOException, InterruptedException {
        //com.developer.res.fxml.configuracoes
        Util.frameDialog("res/fxml/configuracoes/connetionView.fxml");
    }

    private void callingBCaps(ActionEvent event) throws IOException {
        Util.addViewPage(myBorderPane, "res/fxml/configuracoes/bcaps.fxml");
    }

    @FXML
    private void callingEPrerfil(ActionEvent event) throws IOException {
        //com.developer.res.fxml.empresa
        Util.addViewPage(myBorderPane, "res/fxml/empresa/perfilEmpresa.fxml");
    }

    @FXML
    private void callingFcaixa(ActionEvent event) throws IOException {
        //com.developer.res.fxml.faturacao
        Util.addViewPage(myBorderPane, "res/fxml/faturacao/caixa.fxml");
    }

    @FXML
    private void callingRDevolucao(ActionEvent event) throws IOException {
        //
        Util.addViewPage(myBorderPane, "res/fxml/faturacao/devolucao.fxml");
    }

    @FXML
    private void callingRAnulamento(ActionEvent event) throws IOException {
        Util.addViewPage(myBorderPane, "res/fxml/faturacao/anularFatura.fxml");
    }

    private void callingRPromocoes(ActionEvent event) throws IOException {
        Util.addViewPage(myBorderPane, "res/fxml/faturacao/promocoes.fxml");
    }

    private void callingRVendaParcela(ActionEvent event) throws IOException {
        Util.addViewPage(myBorderPane, "res/fxml/faturacao/vendaParcelar.fxml");
    }

    private void crateMaquina() {
        //To change body of generated methods, choose Tools | Templates.
        try {
            Util.opeConnection();
            Util.enti.getTransaction().begin();

            Maquina ma = new Maquina();
            ma.setDataInsert(new Date());
            ma.setIp(InetAddress.getLocalHost().getHostAddress());
            ma.setLogState(0);
            ma.setState(1);
            ma.setMaquiAddress(GetMacAdress.getMack());
            ma.setNumero(NumCaixa.gatNunCaixa());

            new MaquinaJpaController(Util.emf).create(ma);

            Util.enti.getTransaction().commit();
            Util.emf.close();

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @FXML
    private void callingTabelas(ActionEvent event) throws IOException {
        //com.developer.res.fxml.tabelas
        Util.addViewPage(myBorderPane, "res/fxml/tabelas/tabelas.fxml");
    }

}
