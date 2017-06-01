package Interfaz;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Creado por David Valverde Garro - 2016034774
 * Fecha: 31-May-17 Tiempo: 9:54 PM
 */


public class ControladorTransformacionesLineales implements Initializable{
    @FXML
    public Pane paneSuperiorIzquierdo;

    @FXML
    public Pane paneSuperiorDerecho;

    @FXML
    public Pane paneInferiorIzquierdo;

    @FXML
    public Pane paneInferiorDerecho;

    @FXML
    public GridPane cuadrillaPlanos;

    private int tamannoMaximoPlano = 340;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dibujarPlano(paneSuperiorIzquierdo);
        dibujarPlano(paneSuperiorDerecho);
        dibujarPlano(paneInferiorIzquierdo);
        dibujarPlano(paneInferiorDerecho);
    }

    public void dibujarPlano(Pane grafica){
        for(int i = 0; i <= tamannoMaximoPlano; i += tamannoMaximoPlano / 10){
            Line lineaVertical = new Line();
            grafica.getChildren().addAll(lineaVertical);
            lineaVertical.setStartX(i);
            lineaVertical.setStartY(0.0f);
            lineaVertical.setEndX(i);
            lineaVertical.setEndY(tamannoMaximoPlano);
            lineaVertical.setStroke(Color.GREY);

            Line lineaHorizontal = new Line();
            grafica.getChildren().addAll(lineaHorizontal);
            lineaHorizontal.setStartX(0.0f);
            lineaHorizontal.setStartY(i);
            lineaHorizontal.setEndX(tamannoMaximoPlano);
            lineaHorizontal.setEndY(i);
            lineaHorizontal.setStroke(Color.GREY);

            if(i == tamannoMaximoPlano / 2){
                lineaVertical.setStrokeWidth(3.0);
                lineaHorizontal.setStrokeWidth(3.0);
                lineaVertical.setStroke(Color.BLACK);
                lineaHorizontal.setStroke(Color.BLACK);
            }
        }
    }
}

