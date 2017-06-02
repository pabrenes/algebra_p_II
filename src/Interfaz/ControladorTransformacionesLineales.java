package Interfaz;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    private static final int TAMANO_MAXIMO_PLANO = 340; // Tamanno maximo en pixeles de los panes, se asume que los
                                                        // panes son cuadrados.

    /**
     * Función que se autoejecuta en la creación de esta pantalla
     *
     * @param location  Localizaciión para obtener el path relativo a la raíz del objeto
     * @param resources Recursos necesarios para encontrar la raíz del objeto
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dibujarPlano(paneSuperiorIzquierdo);
        dibujarPlano(paneSuperiorDerecho);
        dibujarPlano(paneInferiorIzquierdo);
        dibujarPlano(paneInferiorDerecho);

        /*Line lineaPrueba = new Line();
        paneSuperiorIzquierdo.getChildren().addAll(lineaPrueba);
        lineaPrueba.setStartX(170.0f);
        lineaPrueba.setStartY(170.0f);
        lineaPrueba.setEndX(280.0f);
        lineaPrueba.setEndY(340.0f);
        lineaPrueba.setStrokeWidth(3.0f);
        lineaPrueba.setStroke(Color.GREEN);

        //paneSuperiorIzquierdo.getChildren().addAll(lineaPrueba);*/
    }

    /**
     * Dibuja los ejes principales y secundarios de un plano cartesiano sobre el pane ingresado
     *
     * @param grafica Pane sobre el cual se dibujaran los ejes
     */
    public void dibujarPlano(Pane grafica){
        for(int i = 0; i <= TAMANO_MAXIMO_PLANO; i += (TAMANO_MAXIMO_PLANO / 20)){
            Line lineaVertical = new Line();
            grafica.getChildren().addAll(lineaVertical);
            lineaVertical.setStartX(i);
            lineaVertical.setStartY(0.0f);
            lineaVertical.setEndX(i);
            lineaVertical.setEndY(TAMANO_MAXIMO_PLANO);
            lineaVertical.setStroke(Color.GREY);

            Line lineaHorizontal = new Line();
            grafica.getChildren().addAll(lineaHorizontal);
            lineaHorizontal.setStartX(0.0f);
            lineaHorizontal.setStartY(i);
            lineaHorizontal.setEndX(TAMANO_MAXIMO_PLANO);
            lineaHorizontal.setEndY(i);
            lineaHorizontal.setStroke(Color.GREY);

            if(i == TAMANO_MAXIMO_PLANO / 2){
                lineaVertical.setStrokeWidth(3.0);
                lineaHorizontal.setStrokeWidth(3.0);
                lineaVertical.setStroke(Color.BLACK);
                lineaHorizontal.setStroke(Color.BLACK);
            }
        }
    }
}

