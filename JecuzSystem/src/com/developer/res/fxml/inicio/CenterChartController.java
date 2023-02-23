/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.developer.res.fxml.inicio;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 *
 * @author hdglo
 */
public class CenterChartController implements Initializable {

    @FXML
    private AreaChart chartLateral;
    @FXML
    private NumberAxis yChartL;
    @FXML
    private CategoryAxis xChartL;
    //private BarChart  myChartCentral;
    
    @FXML
    private BarChart  myChartCentral;
    @FXML
    private NumberAxis yChary;
    @FXML
    private CategoryAxis xChart;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        chartLateral();
        chartCentral();
    }

    private void chartLateral() {
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("2003");
        series3.getData().add(new XYChart.Data("Munzambi", 25601.34));
        series3.getData().add(new XYChart.Data("Miguel", 20148.82));
        series3.getData().add(new XYChart.Data("Anna", 10000));
        series3.getData().add(new XYChart.Data("Ntemo", 35407.15));
        series3.getData().add(new XYChart.Data("Elsa", 12000));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("2004");
        series2.getData().add(new XYChart.Data("Armando", 57401.85));
        series2.getData().add(new XYChart.Data("Bernardo", 41941.19));
        series2.getData().add(new XYChart.Data("Paulo", 45263.37));
        series2.getData().add(new XYChart.Data("Sebastião", 117320.16));
        series2.getData().add(new XYChart.Data("Heugenio", 14845.27));

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("2005");
        series1.getData().add(new XYChart.Data("Stefany", 45000.65));
        series1.getData().add(new XYChart.Data("Costa", 44835.76));
        series1.getData().add(new XYChart.Data("Silvana", 18722.18));
        series1.getData().add(new XYChart.Data("Pedro", 17557.31));
        series1.getData().add(new XYChart.Data("Soza", 92633.68));

        chartLateral.getData().addAll(series2, series1, series3);
    }

    private void chartCentral() {
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("2019");
        series3.getData().add(new XYChart.Data("Janeiro", 25601.34));
        series3.getData().add(new XYChart.Data("Fevereiro", 20148.82));
        series3.getData().add(new XYChart.Data("Março", 10000));
        series3.getData().add(new XYChart.Data("Abril", 35407.15));
        series3.getData().add(new XYChart.Data("Maio", 12000));
        series3.getData().add(new XYChart.Data("Junho", 25601.34));
        series3.getData().add(new XYChart.Data("Julho", 20148.82));
        series3.getData().add(new XYChart.Data("Agosto", 10000));
        series3.getData().add(new XYChart.Data("Setembro", 35407.15));
        series3.getData().add(new XYChart.Data("Outubro", 12000));

        series3.getData().add(new XYChart.Data("Novembro ", 57401.85));
        series3.getData().add(new XYChart.Data("Desembro ", 41941.19));
        
         XYChart.Series series1 = new XYChart.Series();
        series1.setName("2020");
        
     /*   series3.getData().add(new XYChart.Data("Dia 1", 45263.37));
        series3.getData().add(new XYChart.Data("Dia 2", 11320.16));
        series3.getData().add(new XYChart.Data("Dia 3", 1845.27));

       
        series1.getData().add(new XYChart.Data("Dia 4", 45000.65));
        series1.getData().add(new XYChart.Data("Dia 5", 44835.76));
        series1.getData().add(new XYChart.Data("Dia 6", 18722.18));
        series1.getData().add(new XYChart.Data("Dia 7", 17557.31));
        series1.getData().add(new XYChart.Data("Dia 8", 92633.68));*/
        series1.getData().add(new XYChart.Data("Dia 9", 45000.65));
        series1.getData().add(new XYChart.Data("Dia 10", 44835.76));
        
        series1.getData().add(new XYChart.Data("Dia 11", 18722.18));
        series1.getData().add(new XYChart.Data("Dia 12", 17557.31));
        series1.getData().add(new XYChart.Data("Dia 13", 2633.68));
        ////   myChartCentral.getData().addAll(series2, series1, series3);
        myChartCentral.getData().addAll(series3, series1);
    }
}
