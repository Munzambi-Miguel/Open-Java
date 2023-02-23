/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.venda;

import com.developer.java.entity.Product;
import com.developer.res.fxml.fornecimento.FornecedorController;
import static com.developer.res.fxml.venda.VendaViewController.myList;
import com.developer.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javax.persistence.Query;
import org.eclipse.persistence.config.QueryHints;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class ProdutAddCarController implements Initializable {

    @FXML
    private Pagination paginacao;
    @FXML
    private TableView tableView;
    @FXML
    private BorderPane myBorders;
    @FXML
    private Text textInforme;
    @FXML
    private TableColumn tc_code;
    @FXML
    private TableColumn tc_name;
    @FXML
    private TableColumn tc_valor;
    @FXML
    private TableColumn tc_acao;
    @FXML
    private JFXTextField tx_busca;

    private final int QUANTIDADE_PAGINA = 13;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        caregarLista();
        tx_busca.setOnKeyReleased((event) -> {
             caregarLista();
        });
    }

    @FXML
    private void clossingView(MouseEvent event) {
        this.myBorders.getScene().getWindow().hide();
    }

    public void caregarLista() {

        tc_code.setCellValueFactory(
                new PropertyValueFactory("index")
        );

        tc_name.setCellValueFactory(
                new PropertyValueFactory("desigancao")
        );

        tc_valor.setCellValueFactory(
                new PropertyValueFactory("valor")
        );

        tc_acao.setCellValueFactory(
                new PropertyValueFactory("myBox")
        );

        paginacao.setPageFactory((Integer pagina) -> {
            atualizarGrade(pagina);
            return tableView;
        });

    }

    private void atualizarGrade(Integer pagina) {
        Util.opeConnection();
        Util.enti.getTransaction().begin();

        String queryComands = "SELECT p FROM Product p WHERE"
                + " p.state=0 and p.quantidAtual>0"
                + "  order by p.id desc";

        Query querys = Util.enti.createQuery(queryComands).setHint(QueryHints.REFRESH, true);
        List<Product> lista = querys.getResultList();

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

    public final class ItemLista {

        private final int id;
        private final int index;
        private final String desigancao;
        private final String valor;
        private final HBox myBox;
        private final JFXButton btnEditar;
        private ProdutAddCarController instance;
        private Product entity;

        public ItemLista(int id, int index,
                String desigancao,
                String valor,
                HBox myBox,
                JFXButton btnEditar,
                ProdutAddCarController instance,
                Product entity
        ) {
            this.id = id;
            this.index = index;
            this.desigancao = desigancao;
            this.valor = valor;
            this.myBox = myBox;
            this.btnEditar = btnEditar;
            this.instance = instance;
            this.entity = entity;

            this.btnEditar.setStyle(" -fx-background-color:   #24B6F7 ! important;");
            this.myBox.getChildren().addAll(btnEditar);
            this.myBox.setAlignment(Pos.CENTER);
            this.myBox.setStyle("-fx-cursor: hand;");

            evento();

        }

        private void evento() {

            btnEditar.setOnAction((ActionEvent event) -> {

                this.addCar();
            });

        }

        public int getId() {
            return id;
        }

        public int getIndex() {
            return index;
        }

        public String getDesigancao() {
            return desigancao;
        }

        public String getValor() {
            return valor;
        }

        public HBox getMyBox() {
            return myBox;
        }

        public JFXButton getBtnEditar() {
            return btnEditar;
        }

        public ProdutAddCarController getInstance() {
            return instance;
        }

        public Product getEntity() {
            return entity;
        }

        public void addCar() {

            try {
                FontAwesomeIconView iconPrints = new FontAwesomeIconView();
                FontAwesomeIconView iconPrints2 = new FontAwesomeIconView();

                iconPrints.setGlyphName("PENCIL_SQUARE_ALT");
                iconPrints2.setGlyphName("TRASH_ALT");

                iconPrints.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));
                iconPrints2.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

                if (!myList.isEmpty()) {

                    for (ItemCar itemCar : myList) {

                        if (itemCar.toString().equals(entity.toString()) && itemCar.getQuantidade() < entity.getQuantidAtual()) {

                            myList.set(myList.indexOf(itemCar), new ItemCar(
                                    entity.toString(),
                                    Util.muedaLocalT("" + entity.getPrecounitario()),
                                    itemCar.getQuantidade() + 1,
                                    Util.muedaLocalT("" + entity.getPrecounitario().multiply(BigDecimal.valueOf(itemCar.getQuantidade() + 1))),
                                    new HBox(5),
                                    new JFXButton("", iconPrints2),
                                    new JFXButton("", iconPrints),
                                    entity
                            ));

                            VendaViewController.sumTotal();

                            return;
                        } else if (!(itemCar.getQuantidade() < entity.getQuantidAtual())) {

                            final Runnable runnable
                                    = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
                            if (runnable != null) {
                                runnable.run();
                            }
                            return;
                        }

                    }

                }

                myList.add(new ItemCar(
                        entity.toString(),
                        Util.muedaLocalT("" + entity.getPrecounitario()),
                        1,
                        Util.muedaLocalT("" + entity.getPrecounitario().multiply(BigDecimal.valueOf(1))),
                        new HBox(5),
                        new JFXButton("", iconPrints2),
                        new JFXButton("", iconPrints),
                        entity
                ));

                VendaViewController.sumTotal();

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            //VendaViewController.myTableView.setFocusTraversable(true);
        }

    }

    private ObservableList<FornecedorController.ItemLista> listar(int quantidade, int pagina) {
        //observableList.clear();
        Util.opeConnection();
        Util.enti.getTransaction().begin();
        // List<Myentity> lista = new MyentityJpaController(Util.emf).findMyentityEntities(quantidade, (pagina * quantidade));

        String queryComandss = "SELECT p FROM Product p WHERE"
                + " p.state=:state and p.quantidAtual>0 "
                + " order by p.id desc";

        Query querys = Util.enti.createQuery(queryComandss, Product.class).setHint(QueryHints.REFRESH, true)
                .setMaxResults(quantidade).setFirstResult((pagina * quantidade));

        querys.setParameter("state", 0);

        List<Product> lista = querys.getResultList();

        Util.enti.getTransaction().commit();
        Util.emf.close();

        int i = 0;
        ObservableList olis = FXCollections.observableArrayList();
        for (Product obj : lista) {

            if (obj.toString().toLowerCase().contains(tx_busca.getText().toLowerCase())) {
                i++;
                FontAwesomeIconView iconPrints2 = new FontAwesomeIconView();
                iconPrints2.setGlyphName("PENCIL_SQUARE_ALT");
                iconPrints2.setFill(javafx.scene.paint.Paint.valueOf("WHITE"));

                olis.add(new ItemLista(
                        obj.getId(), i,
                        obj.toString(),
                        "" + Util.muedaLocalT("" + obj.getPrecounitario()),
                        new HBox(5),
                        new JFXButton("", iconPrints2),
                        this, obj)
                );
            }
        }

        return olis;
    }

}
