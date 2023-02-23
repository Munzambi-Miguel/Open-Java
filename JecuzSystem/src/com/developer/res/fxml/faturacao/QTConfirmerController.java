/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.faturacao;

import com.developer.Main;
import com.developer.java.controller.ItemJpaController;
import com.developer.java.controller.MovementJpaController;
import com.developer.java.entity.Fatura;
import com.developer.java.entity.Item;
import com.developer.java.entity.Movement;
import com.developer.util.Util;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class QTConfirmerController implements Initializable {

    @FXML
    private VBox vBoxLayout;
    @FXML
    private JFXTextField txt_designacao;

    public static Fatura myFa;
    public static Item myIt;
    public static String tipoDevolucao;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.txt_designacao.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {

                // Chamando esse metos
                guardarInformacaoItem();

            }

        });
    }

    @FXML
    private void cancelarDevolucao(ActionEvent event) {
        this.txt_designacao.getScene().getWindow().hide();
    }

    @FXML
    private void guardarInformacao(ActionEvent event) {
        guardarInformacaoItem();
    }

    private void guardarInformacaoItem() {

        // Validando se a caixa de texto está preenchido....
        if (validation()) {

            /**
             * Vamos estabelecer as politicas de devolução de itens
             *
             * No entanto vamos mostrar como vai funcionar a logica para a
             * devolução de item Abaixo temos os passos descritos de forma
             * detalhado.
             *
             * 1) Alteramos os item da fatura para o valor a retirar Para tal
             * vamos verificar se o valor a retirar é superior a quaitidade de
             * item que está previamente apresentado.
             *
             * 2) Vamos criar uma nova requisição de devolução Bom no entanto
             * será na mesmma o item criado, mais com a finalidade guardar a
             * quantidade retirado no sistema, com estado de menos -10 para
             * identificar que é item devolvido vamos também apresentar a sua
             * quantidade em negativo, com a descrição do tipo de devolução
             *
             * 3) As devoluções terão dois nivel de inserção a) inserção com a
             * possibilidade de fazer a transferencia nas entradas b) Sem
             * possibilidade de efectual transferencias nas entradas no sistema.
             *
             * -- Bom vomos ver o algoritmos como está estruturado
             *
             * Obs. Para as validações vamos mostrar os niveis que possivelmente
             * teremos no nosso sistema. a) validação para rever se o campo de
             * texto está preenchido ou não b) validação para rever se o numero
             * que está preenchido é superior ao nivel de devolução
             *
             * com estás condições compridas assim será possivel efectual a
             * logica do negocio
             */
            // Verificando se a quamtidade é admicivel ao nivel que pretendemos devolver
            try {
                if (Integer.parseInt(this.txt_designacao.getText().trim()) <= myIt.getQuantity()) {

                    /**
                     * Bom agora que estámos aqui vamos então criar as
                     * instancias que possibilita executar a logica que está
                     * escrito a cima.:
                     */
                    // preparando as instancias de conexão 
                    Util.opeConnection();
                    Util.enti.getTransaction().begin();

                    int quantidadeAtual = myIt.getQuantity() - Integer.parseInt(this.txt_designacao.getText().trim());

                    myIt.setQuantity(quantidadeAtual);
                    if (quantidadeAtual == 0) {
                        myIt.setState(-1);
                    }

                    myIt.setDataUpdate(new Date());
                    myFa.setCliente(DevolucaoItensController.myInstance.getTxt_clienteNome().getText()+ " / " +DevolucaoItensController.myInstance.getTxt_contactoTelefone().getText());
                    Util.enti.merge(myIt);
                    Util.enti.merge(myFa);

                    /**
                     * Ja que alteramos a quantidade do item agora vamos criar
                     * uma nova instencia do item para mantermos registrado o
                     * item no sistema. que devolvemos.
                     */
                    if ("Danificado".equals(tipoDevolucao)) {
                        Item itemDoCarry = new Item();

                        itemDoCarry.setFaturaId(myFa);
                        itemDoCarry.setProductId(myIt.getProductId());

                        itemDoCarry.setQuantity(-Integer.parseInt(this.txt_designacao.getText().trim()));
                        itemDoCarry.setDataUpdate(new Date());
                        
                        itemDoCarry.setProduto(myIt.getProduto());
                        itemDoCarry.setTotal("-" + myIt.getProductId().getPrecounitario().multiply(BigDecimal.valueOf(Double.valueOf(this.txt_designacao.getText().trim()))));

                        itemDoCarry.setState(-10);
                        itemDoCarry.setInformação("" + tipoDevolucao);
                        itemDoCarry.setOperador(Util.auts);

                        new ItemJpaController(Util.emf).create(itemDoCarry);
                    } else {
                        /**
                         * Vamos efectuar um movimento de transferencia no
                         * sistema do Stock para que o utilizador possa vender
                         * ou dar entrada no sistema
                         *
                         *
                         */

                        Movement m = new Movement();

                        m.setQuantAtual(Integer.parseInt(this.txt_designacao.getText().trim()));
                        m.setQuantidade(Integer.parseInt(this.txt_designacao.getText().trim()));

                        m.setProductId(myIt.getProductId());

                        m.setDataInsert(new Date());
                        m.setInformacao("Movimento de Transferencia");

                        m.setSerialNamber(myIt.getProductId().getPartNamber());

                        m.setMovimentType(15);

                        m.setAutenticationId(Util.auts);

                        new MovementJpaController(Util.emf).create(m);
                    }

                    // Fechando as instancias de conexão no sistema
                    Util.enti.getTransaction().commit();
                    Util.emf.close();

                    TrayNotification tray = new TrayNotification("Item: ",
                            "Operação de devolução efetuado com sucesso... ",
                            NotificationType.INFORMATION);
                    //// com.developer.res.css.img
                    tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                    tray.setRectangleFill(Paint.valueOf("Gray"));
                    tray.setAnimationType(AnimationType.POPUP);
                    tray.showAndDismiss(Duration.seconds(3));

                    myFa = null;
                    myIt = null;

                    DevolucaoItensController.myInstance.caregarItem();
                    DevolucaoController.myInstace.caregarLista();

                    this.txt_designacao.getScene().getWindow().hide();
                } else {
                    TrayNotification tray = new TrayNotification("Item: ",
                            "Excedeu o Limite, a quantidade inserido é invalido... ",
                            NotificationType.ERROR);
                    //// com.developer.res.css.img
                    //tray.setImage(new Image(Main.class.getResourceAsStream("res/img/infor.png")));
                    tray.setRectangleFill(Paint.valueOf("Gray"));
                    tray.setAnimationType(AnimationType.POPUP);
                    tray.showAndDismiss(Duration.seconds(3));
                }
            } catch (NumberFormatException e) {

            }

        }

    }

    boolean validation() {

        if (this.txt_designacao.getText().trim().isEmpty()) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Porfavor digite a Designação!");
            validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
                    .glyph(FontAwesomeIcon.WARNING)
                    .size("1em")
                    .styleClass("error")
                    .build());
            this.txt_designacao.getValidators().add(validator);

            this.txt_designacao.focusedProperty().addListener((o, oldVal, newVal) -> {
                if (!newVal) {
                    this.txt_designacao.validate();
                }

            });
            this.txt_designacao.validate();

            return false;

        }

        return true;
    }

}
