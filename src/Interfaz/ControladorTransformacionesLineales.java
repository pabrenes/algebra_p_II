package Interfaz;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    public Button botonCalcularGraficar;

    @FXML
    public TextField entradaAlfa;

    @FXML
    public TextField entradaBeta;

    @FXML
    public TextField entradaU1;

    @FXML
    public TextField entradaU2;

    @FXML
    public TextField entradaV1;

    @FXML
    public TextField entradaV2;

    @FXML
    public TextField entradaT11;

    @FXML
    public TextField entradaT12;

    @FXML
    public TextField entradaT21;

    @FXML
    public TextField entradaT22;

    private static final int TAMANO_MAXIMO_PLANO = 340; // Tamanno maximo en pixeles de los panes, se asume que los
                                                        // panes son cuadrados.

    private int[] vectorU = {0, 0};
    private int[] vectorV = {0, 0};

    private int alfa;
    private int beta;

    private int[][] matrizTransformacionLineal = {{1, 0},
                                                  {0, 1}};

    /**
     * Función que se autoejecuta en la creación de esta pantalla
     *
     * @param location  Localizaciión para obtener el path relativo a la raíz del objeto
     * @param resources Recursos necesarios para encontrar la raíz del objeto
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hacerEntradasNumericas();

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
    private void dibujarPlano(Pane grafica){
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

    private void obtenerEntradas(){
        alfa = Integer.parseInt(entradaAlfa.getText());
        beta = Integer.parseInt(entradaBeta.getText());

        vectorU[0] = Integer.parseInt(entradaU1.getText());
        vectorU[1] = Integer.parseInt(entradaU2.getText());

        vectorV[0] = Integer.parseInt(entradaU1.getText());
        vectorV[1] = Integer.parseInt(entradaV2.getText());

        matrizTransformacionLineal[0][0] = Integer.parseInt(entradaT11.getText());
        matrizTransformacionLineal[0][1] = Integer.parseInt(entradaT12.getText());
        matrizTransformacionLineal[1][0] = Integer.parseInt(entradaT21.getText());
        matrizTransformacionLineal[1][1] = Integer.parseInt(entradaT22.getText());
    }

    private void hacerEntradasNumericas(){
        limitarEntrada(entradaAlfa,2);
        limitarEntrada(entradaBeta,2);
        limitarEntrada(entradaU1,2);
        limitarEntrada(entradaU2, 2);
        limitarEntrada(entradaV1,2);
        limitarEntrada(entradaV2,2);
        limitarEntrada(entradaT11,2);
        limitarEntrada(entradaT12,2);
        limitarEntrada(entradaT21,2);
        limitarEntrada(entradaT22,2);
    }

    public static void limitarEntrada(final TextField textField, final int longitudMaxima) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                try {
                    if (textField.getText().length() > longitudMaxima) {
                        String s = textField.getText().substring(0, longitudMaxima);
                        textField.setText(s);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        });

        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (!newValue.matches("\\d*")) {
                        textField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        });
    }

}

