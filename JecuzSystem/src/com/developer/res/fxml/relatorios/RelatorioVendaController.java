/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.relatorios;

import com.developer.Main;
import com.developer.java.entity.Fatura;
import com.developer.java.entity.Movement;
import com.developer.res.fxml.ajuste.ProdutosMontraController;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class RelatorioVendaController implements Initializable {

    @FXML
    private BorderPane mainBordeClientes;
    @FXML
    private BorderPane myBorderPaneInternar;
    @FXML
    private TableView tableView;
    @FXML
    private Pagination paginacao;
    @FXML
    private TableColumn tc_operador;
    @FXML
    private TableColumn tc_fatura;
    @FXML
    private TableColumn tc_total;
    @FXML
    private TableColumn tc_cliente;
    @FXML
    private TableColumn tc_datahora;
    @FXML
    private TableColumn tc_acao;

    private final int QUANTIDADE_PAGINA = 13;
    @FXML
    private TableColumn tc_ordem;
    @FXML
    private TableColumn  tc_entrege;
    @FXML
    private TableColumn  tc_troco;
    @FXML
    private TableColumn  tc_tipo;

  /**
   * 
   * @param url
   * @param rb 
   */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        caregarLista();
    }

    @FXML
    private void callingHome(ActionEvent event) {
    }

    @FXML
    private void openModal(ActionEvent event) {
    }

    public void caregarLista() {

        tc_ordem.setCellValueFactory(
                new PropertyValueFactory("imageView")
        );
        tc_operador.setCellValueFactory(
                new PropertyValueFactory("operador")
        );

        tc_fatura.setCellValueFactory(
                new PropertyValueFactory("fatura")
        );
        tc_total.setCellValueFactory(
                new PropertyValueFactory("total")
        );
        tc_cliente.setCellValueFactory(
                new PropertyValueFactory("clientes")
        );
        tc_datahora.setCellValueFactory(
                new PropertyValueFactory("datahora")
        );
        tc_entrege.setCellValueFactory(
                new PropertyValueFactory("entrege")
        );
        tc_troco.setCellValueFactory(
                new PropertyValueFactory("troco")
        );

        tc_acao.setCellValueFactory(
                new PropertyValueFactory("myBox")
        );
        tc_tipo.setCellValueFactory(
                new PropertyValueFactory("tipoVenda")
        );

        paginacao.setPageFactory((Integer pagina) -> {
            atualizarGrade(pagina);
            return tableView;
        });

    }

    
    
    private void atualizarGrade(Integer pagina) {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        String queryComands = "SELECT m FROM Fatura m WHERE"
                + " m.state=0 ";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        List<Movement> lista = querys.getResultList();

        if (lista.isEmpty()) {
            paginacao.setPageCount(1);
        }
        Util.enti.getTransaction().commit();
        Util.emf.close();

        try {
            paginacao.setPageCount((int) Math.ceil(((double) lista.size()) / QUANTIDADE_PAGINA));
            tableView.setItems(listar(QUANTIDADE_PAGINA, pagina));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ObservableList<ProdutosMontraController.ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComands = "SELECT m FROM Fatura m WHERE"
                + " m.state=0 ";

        Query querys = Util.enti.createQuery(queryComands, Fatura.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        List<Fatura> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();

        System.out.println(olis);
        for (Fatura obj : lista) {

            i++;
            FontAwesomeIconView iconPrints = new FontAwesomeIconView();
            iconPrints.setGlyphName("EYE");

            iconPrints.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

            olis.add(new ItemLista(
                    obj.getId(),
                    i,
                    ""+(obj.getFuncionarioId()== null? "Funcionário não identificado !...":obj.getFuncionarioId().getEntityid()),
                    "" + obj.getFatura(),
                    "" + Util.muedaLocalT("" + obj.getTotal()),
                    "" + Util.muedaLocalT("" + obj.getValorPago()),
                    "" + Util.muedaLocalT("" + obj.getTroco()),
                    "" + Util.dateHoraFormat(obj.getDataInsert()),
                    "" + (!obj.getTipoPagamento() ? "Normal" : "Cartão"),
                    "" + obj.getCliente(),
                    new HBox(5),
                    new JFXButton("", iconPrints),
                    this,
                    obj,
                    obj.getFuncionarioId()== null? new ImageView():(obj.getFuncionarioId().getEntityid().getPhotoList().isEmpty()
                    ? new ImageView()
                    : new ImageView(new Image("file:/"+ Util.caminhoImagem + obj.getFuncionarioId().getEntityid().getPhotoList()
                            .get(0).toString(), 30, 30, false, false)                    
                    ))
            ));
        }

        return olis;
    }

    public final class ItemLista {

        private final int id;
        private final int index;
        private final String operador;
        private final String fatura;
        private final String total;
        private final String entrege;
        private final String datahora;
        private final String troco;
        private final String tipoVenda;
        private final String clientes;
        private final HBox myBox;
        private final JFXButton viewButton;
        private RelatorioVendaController myInstance;
        private Fatura entity;
        private final ImageView imageView;

        public ItemLista(
                int id,
                int index,
                String operador,
                String fatura,
                String total,
                String entrege,
                String troco,
                String datahora,
                String tipoVenda,
                String clientes,
                HBox myBox, JFXButton viewButton,
                RelatorioVendaController myInstance,
                Fatura entity,
                ImageView imageView) {
            this.id = id;
            this.index = index;
            this.operador = operador;
            this.fatura = fatura;
            this.total = total;
            this.entrege = entrege;
            this.troco = troco;
            this.datahora = datahora;
            this.tipoVenda = tipoVenda;
            this.clientes = clientes;
            this.myBox = myBox;
            this.viewButton = viewButton;
            this.entity = entity;
            this.imageView = imageView;

            this.imageView.setClip(null);
            this.imageView.setCursor(Cursor.HAND);

            // -fx-border-radius: 10 10 10 10;
            //-fx-background-radius: 10 10 10 10;
            if (entity.getFuncionarioId() == null || entity.getFuncionarioId().getEntityid().getPhotoList().isEmpty()) {
                this.imageView.setImage(new Image(Main.class.getResourceAsStream("res/img/information.png"), 30, 30, true, true));
            } else {
   //this.imageView.setImage(new Image(Main.class.getResourceAsStream("res/img/information.png"), 30, 30, true, true));
                File file = new File("Imagens/").getAbsoluteFile();
                this.imageView.setImage(new Image("file:" + Util.configs.getDirector()+"\\" + entity.getFuncionarioId().getEntityid().getPhotoList().get(0).toString(), 30, 30, true, true));
                // this.imageView.setImage(new Image("file:/C:/JBGest/application/JecuzSystem/Imagens/"+entity.getPhotoList().get(0).toString(), 30, 30, true, true));
                //System.out.println("file:" + Util.configs.getDirector() + entity.getPhotoList().get(0).toString());
            }

            // System.out.println(entity.getPath().charAt(entity.getPath().length() - 4));
            this.imageView.setPreserveRatio(false);

            this.imageView.setStyle(" -fx-border-radius: 10 10 10 10");
            this.imageView.setStyle(" -fx-background-radius: 10 10 10 10");

            this.viewButton.setStyle(" -fx-background-color:  #24B6F7  ! important;");

            this.myBox.getChildren().addAll(viewButton);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle(" -fx-cursor: hand;");

            evento();
        }

        private void evento() {

            viewButton.setOnAction((ActionEvent event) -> {

                TrayNotification tray = new TrayNotification("Produto: ",
                        "Foi Removido no sistema... ",
                        NotificationType.SUCCESS);
                //// com.developer.res.css.img
                tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                tray.setRectangleFill(Paint.valueOf("Gray"));
                tray.setAnimationType(AnimationType.POPUP);
                tray.showAndDismiss(Duration.seconds(3));
                tray.showAndWait();

            });

        }

        public int getId() {
            return id;
        }

        public int getIndex() {
            return index;
        }

        public String getOperador() {
            return operador;
        }
        
        

        public String getFatura() {
            return fatura;
        }

        public String getTotal() {
            return total;
        }

        public String getEntrege() {
            return entrege;
        }

        public String getTroco() {
            return troco;
        }

        public String getTipoVenda() {
            return tipoVenda;
        }

        public String getCliente() {
            return clientes;
        }

        public HBox getMyBox() {
            return myBox;
        }

        public JFXButton getViewButton() {
            return viewButton;
        }

        public RelatorioVendaController getMyInstance() {
            return myInstance;
        }

        public Fatura getEntity() {
            return entity;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public String getDatahora() {
            return datahora;
        }

        public String getClientes() {
            return clientes;
        }

    }

}
