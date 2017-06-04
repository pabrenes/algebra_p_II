package Interfaz;

import Logica.TransformacionLineal;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Creado por David Valverde Garro - 2016034774
 * Fecha: 31-May-17 Tiempo: 9:54 PM
 */


public class ControladorTransformacionesLineales implements Initializable {
    @FXML
    public Pane paneSuperiorIzquierdo;

    @FXML
    public Pane paneSuperiorDerecho;

    @FXML
    public Pane paneInferiorIzquierdo;

    @FXML
    public Pane paneInferiorDerecho;

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

    @FXML
    public Label labelUnidad;

    @FXML
    public Label labelXActual;

    @FXML
    public Label labelYActual;

    @FXML
    public Button botonVolver;

    @FXML
    public Button botonCalcularGraficar;


    private static final double TAMANO_MAXIMO_PLANO = 380; // Tamanno maximo en pixeles de los panes, se asume que los
                                                           // panes son cuadrados.
    private static final double EQUIVALENTE_CERO = TAMANO_MAXIMO_PLANO / 2;

    private static double escala = 1;
    private static double segmentacion = 1;

    private int[] vectorU = {0, 0};
    private int[] vectorV = {0, 0};

    private int[] transU = {0, 0};
    private int[] transV = {0, 0};

    private int[] alfaU = {0, 0};
    private int[] betaV = {0, 0};
    private int[] alfaU_BetaV = {0, 0};

    private int[] transAlfaU = {0, 0};
    private int[] transBetaV = {0, 0};
    private int[] transAlfaU_BetaV = {0, 0};

    private int alfa;
    private int beta;

    private int[][] matrizTransformacionLineal = {{1, 0},
            {0, 1}};

    /**
     * Función que se autoejecuta en la creación de esta pantalla
     *
     * @param location  Localización para obtener el path relativo a la raíz del objeto
     * @param resources Recursos necesarios para encontrar la raíz del objeto
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hacerEntradasNumericas();

        dibujarPlano(paneSuperiorIzquierdo);
        dibujarPlano(paneSuperiorDerecho);
        dibujarPlano(paneInferiorIzquierdo);
        dibujarPlano(paneInferiorDerecho);

        botonCalcularGraficar.setOnAction(event -> configBotonCalcular());
        botonVolver.setOnAction(event -> volver());
    }

    /**
     * Funcion que obtiene las entradas, calcula las transformaciones, determina la escala y la segmentacion para los
     * vectores calculados y grafica todos los vectores con sus animaciones.
     */
    private void configBotonCalcular() {
        obtenerEntradas();
        calcularTransformaciones();
        escala = determinarEscala();
        segmentacion = determinarSegmentacion();

        labelUnidad.setText("Escala: " + escala + "     Segmentos: " + segmentacion);

        limpiarPlanos();
        dibujarPlano(paneSuperiorIzquierdo);
        dibujarPlano(paneSuperiorDerecho);
        dibujarPlano(paneInferiorIzquierdo);
        dibujarPlano(paneInferiorDerecho);

        dibujarVector(vectorU[0], vectorU[1], paneSuperiorIzquierdo, Color.RED);
        dibujarVector(vectorV[0], vectorV[1], paneSuperiorIzquierdo, Color.GREEN);

        dibujarVector(transU[0], transU[1], paneSuperiorDerecho, Color.RED);
        dibujarVector(transV[0], transV[1], paneSuperiorDerecho, Color.GREEN);

        dibujarVector(alfaU[0], alfaU[1], paneInferiorIzquierdo, Color.RED);
        dibujarVector(betaV[0], betaV[1], paneInferiorIzquierdo, Color.GREEN);
        dibujarVector(alfaU_BetaV[0], alfaU_BetaV[1], paneInferiorIzquierdo, Color.BLUE);

        dibujarVector(transAlfaU[0], transAlfaU[1], paneInferiorDerecho, Color.RED);
        dibujarVector(transBetaV[0], transBetaV[1], paneInferiorDerecho, Color.GREEN);
        dibujarVector(transAlfaU_BetaV[0], transAlfaU_BetaV[1], paneInferiorDerecho, Color.BLUE);

        iniciarAnimacion(alfaU, alfaU_BetaV, betaV, paneInferiorIzquierdo);
        iniciarAnimacion(transAlfaU, transAlfaU_BetaV, transBetaV, paneInferiorDerecho);
    }


    /**
     * Dibuja un plano cartesiano completo sobre la grafica dada.
     *
     * @param grafica
     */
    private void dibujarPlano(Pane grafica) {
        dibujarEjesSecundarios(grafica);
        dibujarEjesXY(grafica);
    }

    /**
     * Dibuja los ejes principales X y Y sobre el centro del pane ingresado.
     *
     * @param grafica Pane sobre el cual se dibujaran los ejes
     */
    private void dibujarEjesXY(Pane grafica) {
        Line ejeX = new Line();
        grafica.getChildren().addAll(ejeX);
        ejeX.setStartX(0.0f);
        ejeX.setStartY(EQUIVALENTE_CERO);
        ejeX.setEndX(TAMANO_MAXIMO_PLANO);
        ejeX.setEndY(EQUIVALENTE_CERO);
        ejeX.setStroke(Color.BLACK);
        ejeX.setStrokeWidth(3.0);

        Line ejeY = new Line();
        grafica.getChildren().addAll(ejeY);
        ejeY.setStartX(EQUIVALENTE_CERO);
        ejeY.setStartY(0.0f);
        ejeY.setEndX(EQUIVALENTE_CERO);
        ejeY.setEndY(TAMANO_MAXIMO_PLANO);
        ejeY.setStroke(Color.BLACK);
        ejeY.setStrokeWidth(3.0);
    }

    /**
     * Dibuja los ejes secundarios de un plano cartesiano sobre el pane ingresado.
     *
     * @param grafica Pane sobre el cual se dibujaran los ejes
     */
    private void dibujarEjesSecundarios(Pane grafica) {
        for (double i = 0; i <= TAMANO_MAXIMO_PLANO; i += (TAMANO_MAXIMO_PLANO / segmentacion)) {
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

            lineaHorizontal.setOnMouseEntered(event -> {
                labelXActual.setText("X:");
                labelYActual.setText("Y: " + (-1 * determinarCoordenadas(lineaHorizontal.getEndY())));
            });

            lineaVertical.setOnMouseEntered(event -> {
                labelXActual.setText("X: " + determinarCoordenadas(lineaVertical.getEndX()));
                labelYActual.setText("Y:");
            });
        }
    }

    /**
     * Dibuja un vector del color ingresado desde (0,0) hasta (x,y) sobre la grafica dada. Se emplea un calculo para
     * deterinar pasar de las coordenadas x,y dadas a las de JavaFX basado en el equivalente a cero en las coordenadas
     * del Pane, la segmentacion para los vectores dados y la escala que se esta empleando.
     *
     * @param x Coordenada X donde apunta el vector.
     * @param y Coordenada Y donde apunta el vector.
     * @param plano Grafica (JavaFX Pane) donde se dibuja el vector.
     * @param color Color que tendra el vector.
     * @return Referencia al objeto Line que dibuja el vector.
     */
    private static Line dibujarVector(double x, double y, Pane plano, Color color) {
        double coordenadaX = EQUIVALENTE_CERO + (EQUIVALENTE_CERO / (segmentacion / 2)) * (x / escala);
        double coordenadaY = EQUIVALENTE_CERO - (EQUIVALENTE_CERO / (segmentacion / 2)) * (y / escala);
        Line lineaVector = new Line();
        plano.getChildren().addAll(lineaVector);
        lineaVector.setStartX(EQUIVALENTE_CERO);
        lineaVector.setStartY(EQUIVALENTE_CERO);
        lineaVector.setEndX(coordenadaX);
        lineaVector.setEndY(coordenadaY);
        lineaVector.setStrokeWidth(3.0f);
        lineaVector.setStroke(color);
        Tooltip tooltip = new Tooltip("(" + (int) x + ", " + (int) y + ")");
        Tooltip.install(lineaVector, tooltip);
        if (!color.equals(Color.MAGENTA)) {
            Polygon trianguloVector = new Polygon();
            trianguloVector.getPoints().addAll(
                    coordenadaX - 6, coordenadaY - 10,
                    coordenadaX - 6, coordenadaY + 10,
                    coordenadaX + 4, coordenadaY
            );
            trianguloVector.setStroke(color);
            trianguloVector.setFill(color);
            trianguloVector.setRotate(getRotate(x, y));
            Tooltip.install(trianguloVector, tooltip);
            plano.getChildren().addAll(trianguloVector);
        }
        return lineaVector;
    }

    /**
     * Funcion que calcula la rotacion que deben tener los triangulos de cabeza de los vectores.
     *
     * @param x Coordenada X de uno de los vertices del triangulo
     * @param y Coordenada Y de uno de los vertices del triangulo
     * @return Rotacion en grados de las coordenadas
     */
    private static double getRotate(double x, double y) {
        double temp = Math.atan2(y, x) * 180 / Math.PI;
        if (temp < 0)
            return Math.abs(temp);
        else
            return 360 - temp;
    }

    /**
     * Convierte el numero ingresado del sistema de coordenadas de JavaFX (Y) al sistema de coordenadas con planos
     * cartesianos (x).
     *
     * @param y Imagen de X, donde X es una coordenada cartesiana y Y una coordenada en JavaFX
     * @return Preimagen de Y
     */
    private static double determinarCoordenadas(double y) {
        return Math.round(((y - EQUIVALENTE_CERO) * ((segmentacion / 2) * escala)) / EQUIVALENTE_CERO);
    }

    /**
     * Funcion que crea un vector fantasma animado con el siguiente desplazamiento:
     *
     * Su punto de inicio desde el punto de inicio del vector U
     * hasta el punto de final del mismo vector
     *
     * Su punto de final desde el punto de inicio del
     * vector V hasta el punto de final de la suma de u + v
     *
     * @param vectorU Vector u
     * @param vectorU_V Suma de u u v
     * @param vectorV Vector v
     * @param grafico Pane sobre el cual se hara la animacion.
     */
    private void iniciarAnimacion(int[] vectorU, int[] vectorU_V, int[] vectorV, Pane grafico) {
        Line lineaU = dibujarVector(vectorU[0], vectorU[1], grafico, Color.RED);
        Line lineaU_V = dibujarVector(vectorU_V[0], vectorU_V[1], grafico, Color.BLUE);
        Line lineaFantasma = dibujarVector(vectorV[0], vectorV[1], grafico, Color.MAGENTA);

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);

        final KeyValue keyValueStartX = new KeyValue(lineaFantasma.startXProperty(), lineaU.getEndX());
        final KeyValue keyValueStartY = new KeyValue(lineaFantasma.startYProperty(), lineaU.getEndY());
        final KeyValue keyValueEndX = new KeyValue(lineaFantasma.endXProperty(), lineaU_V.getEndX());
        final KeyValue keyValueEndY = new KeyValue(lineaFantasma.endYProperty(), lineaU_V.getEndY());

        final KeyFrame keyFrameStartX = new KeyFrame(Duration.seconds(4), keyValueStartX);
        final KeyFrame keyFrameStartY = new KeyFrame(Duration.seconds(4), keyValueStartY);
        final KeyFrame keyFrameEndX = new KeyFrame(Duration.seconds(4), keyValueEndX);
        final KeyFrame keyFrameEndY = new KeyFrame(Duration.seconds(4), keyValueEndY);

        timeline.getKeyFrames().add(keyFrameStartX);
        timeline.getKeyFrames().add(keyFrameStartY);
        timeline.getKeyFrames().add(keyFrameEndX);
        timeline.getKeyFrames().add(keyFrameEndY);

        timeline.play();
    }

    private void limpiarPlanos() {
        limpiarPlano(paneSuperiorIzquierdo);
        limpiarPlano(paneSuperiorDerecho);
        limpiarPlano(paneInferiorIzquierdo);
        limpiarPlano(paneInferiorDerecho);
    }

    private void limpiarPlano(Pane cuadro) {
        cuadro.getChildren().remove(1, cuadro.getChildren().size());
    }

    private void obtenerEntradas() {
        alfa = Integer.parseInt(entradaAlfa.getText());
        beta = Integer.parseInt(entradaBeta.getText());

        vectorU[0] = Integer.parseInt(entradaU1.getText());
        vectorU[1] = Integer.parseInt(entradaU2.getText());

        vectorV[0] = Integer.parseInt(entradaV1.getText());
        vectorV[1] = Integer.parseInt(entradaV2.getText());

        matrizTransformacionLineal[0][0] = Integer.parseInt(entradaT11.getText());
        matrizTransformacionLineal[0][1] = Integer.parseInt(entradaT12.getText());
        matrizTransformacionLineal[1][0] = Integer.parseInt(entradaT21.getText());
        matrizTransformacionLineal[1][1] = Integer.parseInt(entradaT22.getText());
    }

    private void calcularTransformaciones() {
        transU = TransformacionLineal.hacerTransformacionLineal(matrizTransformacionLineal, vectorU);
        transV = TransformacionLineal.hacerTransformacionLineal(matrizTransformacionLineal, vectorV);

        alfaU = TransformacionLineal.multiplicarPorEscalar(vectorU, alfa);
        betaV = TransformacionLineal.multiplicarPorEscalar(vectorV, beta);
        alfaU_BetaV = TransformacionLineal.sumarVectores(alfaU, betaV);

        transAlfaU = TransformacionLineal.hacerTransformacionLineal(matrizTransformacionLineal, alfaU);
        transBetaV = TransformacionLineal.hacerTransformacionLineal(matrizTransformacionLineal, betaV);
        transAlfaU_BetaV = TransformacionLineal.hacerTransformacionLineal(matrizTransformacionLineal, alfaU_BetaV);
    }

    private double determinarEscala() {
        /*int mayor = determinarMayor();
        int escalaActual = 10;
        while (true){
            if(mayor<escalaActual){
                break;
            }
            else{
                escalaActual += 10;
            }
        }
        return escalaActual / 10;*/
        return Math.pow(10, (int) Math.log10(determinarMayor()));
    }

    private int determinarSegmentacion() {
        int mayor = determinarMayor();
        return 2 * ((int) (mayor / escala) + 1);
    }

    private int determinarMayor() {
        ArrayList<int[]> matrizVectores = hacerMatrizVectores();

        int mayor = 0;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.println(matrizVectores.get(i)[j]);
                if (Math.abs(matrizVectores.get(i)[j]) > mayor) {
                    mayor = Math.abs(matrizVectores.get(i)[j]);
                }
            }
        }
        return mayor;
    }

    private ArrayList<int[]> hacerMatrizVectores() {
        ArrayList<int[]> matrizVectores = new ArrayList<>();
        matrizVectores.add(vectorU);
        matrizVectores.add(vectorV);

        matrizVectores.add(transU);
        matrizVectores.add(transV);

        matrizVectores.add(alfaU);
        matrizVectores.add(betaV);
        matrizVectores.add(alfaU_BetaV);

        matrizVectores.add(transAlfaU);
        matrizVectores.add(transBetaV);
        matrizVectores.add(transAlfaU_BetaV);

        return matrizVectores;
    }

    private void hacerEntradasNumericas() {
        int longitudMaxima = 3;

        limitarEntrada(entradaAlfa, longitudMaxima);
        limitarEntrada(entradaBeta, longitudMaxima);
        limitarEntrada(entradaU1, longitudMaxima);
        limitarEntrada(entradaU2, longitudMaxima);
        limitarEntrada(entradaV1, longitudMaxima);
        limitarEntrada(entradaV2, longitudMaxima);
        limitarEntrada(entradaT11, longitudMaxima);
        limitarEntrada(entradaT12, longitudMaxima);
        limitarEntrada(entradaT21, longitudMaxima);
        limitarEntrada(entradaT22, longitudMaxima);
    }

    static void limitarEntrada(final TextField textField, final int longitudMaxima) {
        //Tamanno
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

        //Numericos
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (!newValue.equals("") & !newValue.equals("-")) {
                        int numero = Math.abs(Integer.parseInt(newValue));

                        if ((Math.log10(numero) + 1) >= longitudMaxima) {
                            textField.setText(oldValue);
                        }
                    }
                } catch (Exception e) {
                    textField.setText(oldValue);
                }
            }
        });
    }

    private void volver() {
        Stage escenario = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent raiz;                                                                                                    //Se crean estos tres objetos
        try {
            raiz = loader.load(getClass().getResource("Inicio.fxml").openStream());                                 //Con esto se indica el FXML de la nueva ventana
            escenario.setTitle("Bases y dimensión de un sistema homogéneo de ecuaciones lineales");
            escenario.setScene(new Scene(raiz));
            escenario.show();

            Stage temporal = (Stage) botonVolver.getScene().getWindow();                                                      //Se obtiene el stage al que pertenece el boton que abre la nueva ventana para poder cerrarla
            temporal.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

