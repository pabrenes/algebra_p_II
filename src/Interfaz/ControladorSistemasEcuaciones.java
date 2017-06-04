package Interfaz;

import Logica.Fraccion;
import Logica.Matriz;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Pablo Brenes 2016250460
 * 27 may 2017.
 */
public class ControladorSistemasEcuaciones implements Initializable {

    private int filas;
    private int columnas;
    private Matriz sistema;
    private ArrayList<Group> parentesis;

    private final String[] VARIABLES = {"X", "Y", "Z", "W", "S"};
    private final int ERROR_DIV_ZERO = 1;
    private final int ERROR_NON_NUME = 2;
    private final int PARENTESIS_HGAP = 90;
    private final double X_PRIMER_PARENTESIS = 528d;
    private final double Y_PRIMER_PARENTESIS = 130d;
    private final int CASILLA_ALTO = 50;
    private final int CASILLA_ANCHO = 56;
    private double LAST_Y;

    /**
     * Función que se autoejecuta en la creación de esta pantalla
     *
     * @param location  Localizaciión para obtener el path relativo a la raíz del objeto
     * @param resources Recursos necesarios para encontrar la raíz del objeto
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filas = 2;
        columnas = 2;
        parentesis = new ArrayList<>();
        construirMatriz();
        configurarMenuFilas();
        configurarMenuColumnas();
        btnResolver.setOnAction(event -> resolver());
        btnLimpiar.setOnAction(event -> limpiar());
        btnVolver.setOnAction(event -> volver());
    }

    /**
     * Construye la matriz donde el usuario ingresa los datos
     */
    private void construirMatriz() {
        lblSolucionHeader.setText("");
        gridPaneSistema.getChildren().clear();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (j == columnas - 1) {
                    gridPaneSistema.add(construirEntradaMatriz(true, j), j, i);
                } else {
                    gridPaneSistema.add(construirEntradaMatriz(false, j), j, i);
                }
            }
        }
    }

    /**
     * Crea un HBox con dos texfield para la comunicación con el usuario y un label para presentación gráfica
     *
     * @param last   indica si es la última variable del sistema, para agregar la igualdad a cero
     * @param varPos posición de la variable actual
     * @return HBox, dentro de este un VBox con dos TextField y en medio un separador, además de un Label
     */
    private HBox construirEntradaMatriz(boolean last, int varPos) {
        HBox contenedorHorizontal = new HBox();
        VBox contenedorVertical = new VBox();
        contenedorVertical.setAlignment(Pos.CENTER);
        contenedorVertical.setMaxSize(45, 45);
        contenedorVertical.setMinSize(45, 45);
        TextField entradaNumerador = new TextField();
        entradaNumerador.setAlignment(Pos.CENTER);
        entradaNumerador.setText("0");
        Separator separador = new Separator(Orientation.HORIZONTAL);
        TextField entradaDenominador = new TextField();
        entradaDenominador.setAlignment(Pos.CENTER);
        entradaDenominador.setText("1");
        contenedorVertical.getChildren().addAll(entradaNumerador, separador, entradaDenominador);
        Label lblVariable = new Label();
        if (last) {
            lblVariable.setText(" " + VARIABLES[varPos] + "  =  0");
        } else {
            lblVariable.setText(" " + VARIABLES[varPos] + "  + ");
        }
        lblVariable.setFont(new Font("Times New Roman", 16));
        lblVariable.setMaxSize(55, 55);
        lblVariable.setMinSize(55, 55);
        contenedorHorizontal.setAlignment(Pos.CENTER);
        ControladorTransformacionesLineales.limitarEntrada(entradaNumerador, 5);
        ControladorTransformacionesLineales.limitarEntrada(entradaDenominador, 5);
        contenedorHorizontal.getChildren().addAll(contenedorVertical, lblVariable);
        return contenedorHorizontal;
    }

    /**
     * Asigna el trabajo a cada item del menú filas
     */
    private void configurarMenuFilas() {
        menuItemF2.setOnAction(event -> eventoMenuFila(2));
        menuItemF3.setOnAction(event -> eventoMenuFila(3));
        menuItemF4.setOnAction(event -> eventoMenuFila(4));
        menuItemF5.setOnAction(event -> eventoMenuFila(5));
    }

    /**
     * Asigna el trabajo a cada item del menú columnas
     */
    private void configurarMenuColumnas() {
        menuItemC2.setOnAction(event -> eventoMenuColumna(2));
        menuItemC3.setOnAction(event -> eventoMenuColumna(3));
        menuItemC4.setOnAction(event -> eventoMenuColumna(4));
        menuItemC5.setOnAction(event -> eventoMenuColumna(5));
    }

    /**
     * Trabajo que es asignado a cada itenMenu del menú Filas
     *
     * @param fila Fila a la cual pertenece el evento
     */
    private void eventoMenuFila(int fila) {
        filas = fila;
        limpiar();
    }

    /**
     * Trabajo que es asignado a cada itenMenu del menú Columnas
     *
     * @param columna Columna a la cual pertenece el evento
     */
    private void eventoMenuColumna(int columna) {
        columnas = columna;
        limpiar();
    }

    /**
     * Construye la matriz visual con las bases del sistema de solución para el usuario
     *
     * @param solucion vector de dos dimensiones con las bases de la solución del sistema
     */
    private void construirSolucion(Fraccion[][] solucion) {
        cleanParentesis();
        gridPaneSolucion.getChildren().clear();
        lblSolucionHeader.setFont(new Font("Times New Roman", 18));
        if (solucion.length == 0) {
            construirSolucionTrivial();
        } else {
            lblSolucionHeader.setText(
                    "El sistema posee soluciones infinitas. \n" +
                            "La dimensión del espacio de solución es " + solucion.length + ". \n" +
                            "Una base para el espacio del sistema de solución es:");
            for (int i = 0; i < solucion.length; i++) {
                Group tmpGroup = crearParentesisVector(
                        X_PRIMER_PARENTESIS + i * PARENTESIS_HGAP, Y_PRIMER_PARENTESIS);
                panelPrincipal.getChildren().add(tmpGroup);
                parentesis.add(tmpGroup);
                for (int j = 0; j < solucion[i].length; j++) {
                    gridPaneSolucion.add(construirEntradaSolucion(solucion, i, j), i, j);
                }
            }
            Group tmpGroup = crearComillas(solucion.length);
            panelPrincipal.getChildren().add(tmpGroup);
            parentesis.add(tmpGroup);
            tmpGroup = parentesisConjunto(solucion.length);
            panelPrincipal.getChildren().add(tmpGroup);
            parentesis.add(tmpGroup);
        }
    }

    /**
     * Solicita la construcción de la solución trivial para el sistema homogeneo
     */
    private void construirSolucionTrivial() {
        lblSolucionHeader.setText(
                "El sistema posee solución única. \n" +
                        "Su solución es:");
        for (int i = 0; i < columnas; i++) {
            gridPaneSolucion.add(construirEntradaTrivial(VARIABLES[i]), 0, i);
            gridPaneSolucion.add(construirEntradaTrivial("0"), 1, i);
        }
        Group parentesisVariables = crearParentesisVector(X_PRIMER_PARENTESIS, Y_PRIMER_PARENTESIS);
        Group parentesisTriviales = crearParentesisVector(X_PRIMER_PARENTESIS + PARENTESIS_HGAP, Y_PRIMER_PARENTESIS);
        Group igual = crearIgual();
        panelPrincipal.getChildren().addAll(parentesisVariables, parentesisTriviales, igual);
        parentesis.add(parentesisTriviales);
        parentesis.add(parentesisVariables);
        parentesis.add(igual);
    }

    /**
     * Construye una entrada para la solución trivial
     *
     * @param variable incognita a la que se iguala
     * @return VBox, con la entrada de la solución
     */
    private VBox construirEntradaTrivial(String variable) {
        VBox contenedorVertical = new VBox();
        contenedorVertical.setAlignment(Pos.CENTER);
        contenedorVertical.setMinSize(52, 52);
        contenedorVertical.setMaxSize(52, 52);
        Label incognita = new Label();
        incognita.setAlignment(Pos.CENTER);
        incognita.setText(variable);
        incognita.setFont(new Font("Times New Roman", 18));
        contenedorVertical.getChildren().add(incognita);
        return contenedorVertical;
    }

    /**
     * Construye cada entrada de la matriz donde se le presenta al usuario la solución
     *
     * @param solucion Arreglo de dos dimensiones donde se encuentra la solución
     * @param fila     Fila de la entrada que se trabaja
     * @param columna  Columna de la entrada que se trabaja
     * @return VBox, con dos TextField y un separator en el medio, para representar la fracción en la gráfica para el usuario
     */
    private VBox construirEntradaSolucion(Fraccion[][] solucion, int fila, int columna) {
        Fraccion fraccion = solucion[fila][columna];
        String numerador = String.valueOf(fraccion.getNumerador());
        String denominador = String.valueOf(fraccion.getDenominador());
        double sizeOfLine = 8.75 * Math.max(numerador.length(), denominador.length());
        VBox contenedorVertical = new VBox();
        contenedorVertical.setAlignment(Pos.CENTER);
        contenedorVertical.setMinSize(52, 52);
        contenedorVertical.setMaxSize(52, 52);
        Label entradaNumerador = new Label();
        entradaNumerador.setAlignment(Pos.CENTER);
        entradaNumerador.setText(numerador);
        entradaNumerador.setFont(new Font("Times New Roman", 16));
        if (fraccion.getDenominador() == 1) {
            contenedorVertical.getChildren().addAll(entradaNumerador);
            return contenedorVertical;
        }
        Label separador = new Label();
        separador.setMinSize(sizeOfLine, 1);
        separador.setMaxSize(sizeOfLine, 1);
        separador.setStyle("-fx-background-color: black");
        Label entradaDenominador = new Label();
        entradaDenominador.setAlignment(Pos.CENTER);
        entradaDenominador.setText(denominador);
        entradaDenominador.setFont(new Font("Times New Roman", 16));
        contenedorVertical.getChildren().addAll(entradaNumerador, separador, entradaDenominador);
        return contenedorVertical;
    }

    /**
     * Dada una entrada de la matriz gráfica del usuario, crea una Fracción a apartir de la información dada por la entrada
     *
     * @param vBox vBox donde se encuentra la información del usuario
     * @return Fraccion, con la información que proveo el usuario
     */
    private Fraccion capturarEntrada(VBox vBox) {
        TextField entradaNumerador = (TextField) vBox.getChildren().get(0);
        TextField entradaDenominador = (TextField) vBox.getChildren().get(2);
        int numerador;
        int denominador;
        try {
            numerador = Integer.parseInt(entradaNumerador.getText());
            denominador = Integer.parseInt(entradaDenominador.getText());
            if (denominador == 0) {
                generateError(ERROR_DIV_ZERO);
                return null;
            }
            return new Fraccion(numerador, denominador);
        } catch (NumberFormatException nfe) {
            generateError(ERROR_NON_NUME);
        }
        return null;
    }

    /**
     * Dado un error lanza un alert dialog indicando que hubo un error
     *
     * @param type Condición de error lanzada
     */
    private void generateError(int type) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Verifique la validez de las entradas:");
        if (type == ERROR_DIV_ZERO)
            alert.setContentText("Las fracciones no pueden presentar denominador cero.");
        if (type == ERROR_NON_NUME)
            alert.setContentText("Las entradas deben ser valores numéricos.");
        alert.show();
    }

    /**
     * Obtiene los datos ingresados por el usuario para construir la matriz del sistema
     */
    private boolean construirSistema() {
        Fraccion[][] matriz = new Fraccion[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int index = columnas * i + j;
                HBox hBox = (HBox) gridPaneSistema.getChildren().get(index);
                Fraccion fraccion = capturarEntrada((VBox) hBox.getChildren().get(0));
                if (fraccion == null)
                    return false;
                matriz[i][j] = fraccion;
            }
        }
        sistema = new Matriz(matriz);
        return true;
    }

    /**
     * Trabajo para el botón resolver, determina la solución y se la prensenta al usuario
     */
    private void resolver() {
        if (!construirSistema())
            return;
        Fraccion[][] solucion = sistema.espacioDeSolucion();
        construirSolucion(solucion);
    }

    /**
     * Limpia las entradas en caso de que el usuario lo requiera
     */
    private void limpiar() {
        gridPaneSistema.getChildren().clear();
        construirMatriz();
        cleanParentesis();
        gridPaneSolucion.getChildren().clear();
    }

    /**
     * Regresa al menú inicial
     */
    private void volver() {
        Stage escenario = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent raiz;                                                                                                    //Se crean estos tres objetos
        try {
            raiz = loader.load(getClass().getResource("Inicio.fxml").openStream());                                 //Con esto se indica el FXML de la nueva ventana
            escenario.setTitle("Bases y dimensión de un sistema homogéneo de ecuaciones lineales");
            escenario.setScene(new Scene(raiz));
            escenario.show();

            Stage temporal = (Stage) btnVolver.getScene().getWindow();                                                      //Se obtiene el stage al que pertenece el boton que abre la nueva ventana para poder cerrarla
            temporal.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea un paréntesis para almacenar el vector
     *
     * @param X Inicio del paréntesis en X coordenada del Panel
     * @param Y Inicio del paréntesis en y coordenada del Panel
     * @return Group, conjunto de Lines y CubicCurves que forman el parénteis
     */
    private Group crearParentesisVector(Double X, Double Y) {

        QuadCurve curvaSuperiorIzquierda = new QuadCurve(
                X, Y + 30,
                X, Y + 10,
                X + 10, Y);
        curvaSuperiorIzquierda.setStroke(Color.BLACK);
        curvaSuperiorIzquierda.setFill(null);
        curvaSuperiorIzquierda.setStrokeWidth(2);

        QuadCurve curvaSuperiorDerecha = new QuadCurve(
                X + CASILLA_ANCHO, Y + 30,
                X + CASILLA_ANCHO, Y + 10,
                X + CASILLA_ANCHO - 10, Y
        );
        curvaSuperiorDerecha.setStroke(Color.BLACK);
        curvaSuperiorDerecha.setFill(null);
        curvaSuperiorDerecha.setStrokeWidth(2);

        Line lineIzquierda = new Line();
        lineIzquierda.setStartX(X);
        lineIzquierda.setEndX(X);
        lineIzquierda.setStartY(Y + 32);

        Line lineDerecha = new Line();
        lineDerecha.setStartX(X + CASILLA_ANCHO);
        lineDerecha.setEndX(X + CASILLA_ANCHO);
        lineDerecha.setStartY(Y + 32);

        Y += (columnas * CASILLA_ALTO) + -5 * (columnas - 5);
        LAST_Y = Y;

        lineIzquierda.setEndY(Y - 32);
        lineIzquierda.setStroke(Color.BLACK);
        lineIzquierda.setStrokeWidth(2);

        lineDerecha.setEndY(Y - 32);
        lineIzquierda.setStroke(Color.BLACK);
        lineDerecha.setStrokeWidth(2);

        QuadCurve curvaInferiorIzquierda = new QuadCurve(
                X, Y - 30,
                X, Y - 10,
                X + 10, Y);
        curvaInferiorIzquierda.setStroke(Color.BLACK);
        curvaInferiorIzquierda.setFill(null);
        curvaInferiorIzquierda.setStrokeWidth(2);

        QuadCurve curvaInferiorDerecha = new QuadCurve(
                X + CASILLA_ANCHO, Y - 30,
                X + CASILLA_ANCHO, Y - 10,
                X + CASILLA_ANCHO - 10, Y
        );
        curvaInferiorDerecha.setStroke(Color.BLACK);
        curvaInferiorDerecha.setFill(null);
        curvaInferiorDerecha.setStrokeWidth(2);
        return new Group(
                curvaSuperiorIzquierda, lineIzquierda, curvaInferiorIzquierda,
                curvaSuperiorDerecha, lineDerecha, curvaInferiorDerecha);
    }

    /**
     * Crea los paréntesis que albergan al conjunto de solución
     *
     * @return Grupo, con los shapes necesarios para crear el conjunto de solución
     */
    private Group parentesisConjunto(int dimension) {
        Group group = new Group();
        double XDES = 12;
        double YDES = 10;
        double XEND = (PARENTESIS_HGAP * (dimension - 1)) + CASILLA_ANCHO;
        double X = X_PRIMER_PARENTESIS;
        double Y = Y_PRIMER_PARENTESIS;

        QuadCurve curvaSuperiorIzquierda = new QuadCurve(
                X - XDES, Y + 30 - YDES,
                X - XDES, Y - YDES,
                X - XDES + 10, Y - YDES);
        curvaSuperiorIzquierda.setStroke(Color.BLACK);
        curvaSuperiorIzquierda.setFill(null);
        curvaSuperiorIzquierda.setStrokeWidth(2);

        QuadCurve curvaSuperiorDerecha = new QuadCurve(
                X + XEND + XDES, Y + 30 - YDES,
                X + XEND + XDES, Y - YDES,
                X + XEND + XDES - 10, Y - YDES);
        curvaSuperiorDerecha.setStroke(Color.BLACK);
        curvaSuperiorDerecha.setFill(null);
        curvaSuperiorDerecha.setStrokeWidth(2);

        Line lineaSuperiorIzquierda = new Line();
        lineaSuperiorIzquierda.setStartX(X - XDES);
        lineaSuperiorIzquierda.setStartY(Y + 32 - YDES);
        lineaSuperiorIzquierda.setEndX(X - XDES);

        Line lineaSuperiorDerecha = new Line();
        lineaSuperiorDerecha.setStartX(X + XEND + XDES);
        lineaSuperiorDerecha.setStartY(Y + 32 - YDES);
        lineaSuperiorDerecha.setEndX(X + XEND + XDES);

        Y += ((columnas * CASILLA_ALTO) + -5 * (columnas - 5)) / 2 - YDES;

        lineaSuperiorIzquierda.setEndY(Y);
        lineaSuperiorIzquierda.setStroke(Color.BLACK);
        lineaSuperiorIzquierda.setStrokeWidth(2);

        lineaSuperiorDerecha.setEndY(Y);
        lineaSuperiorDerecha.setStroke(Color.BLACK);
        lineaSuperiorDerecha.setStrokeWidth(2);

        QuadCurve curvaMediaSuperiorIzquierda = new QuadCurve(
                X - XDES, Y,
                X - XDES, Y + YDES,
                X - XDES - XDES, Y + YDES);
        curvaMediaSuperiorIzquierda.setStroke(Color.BLACK);
        curvaMediaSuperiorIzquierda.setFill(null);
        curvaMediaSuperiorIzquierda.setStrokeWidth(2);

        QuadCurve curvaMediaSuperiorDerecha = new QuadCurve(
                X + XEND + XDES, Y,
                X + XEND + XDES, Y + YDES,
                X + XEND + XDES + XDES, Y + YDES);
        curvaMediaSuperiorDerecha.setStroke(Color.BLACK);
        curvaMediaSuperiorDerecha.setFill(null);
        curvaMediaSuperiorDerecha.setStrokeWidth(2);

        QuadCurve curvaMediaInferiorIzquierda = new QuadCurve(
                X - XDES - XDES, Y + YDES,
                X - XDES, Y + YDES,
                X - XDES, Y + YDES + YDES);
        curvaMediaInferiorIzquierda.setStroke(Color.BLACK);
        curvaMediaInferiorIzquierda.setFill(null);
        curvaMediaInferiorIzquierda.setStrokeWidth(2);

        QuadCurve curvaMediaInferiorDerecha = new QuadCurve(
                X + XEND + XDES + XDES, Y + YDES,
                X + XEND + XDES, Y + YDES,
                X + XEND + XDES, Y + YDES + YDES);
        curvaMediaInferiorDerecha.setStroke(Color.BLACK);
        curvaMediaInferiorDerecha.setFill(null);
        curvaMediaInferiorDerecha.setStrokeWidth(2);

        Line lineaInferiorIzquierda = new Line();
        lineaInferiorIzquierda.setStartX(X - XDES);
        lineaInferiorIzquierda.setStartY(Y + 32 - YDES);
        lineaInferiorIzquierda.setEndX(X - XDES);

        Line lineaInferiorDerecha = new Line();
        lineaInferiorDerecha.setStartX(X + XEND + XDES);
        lineaInferiorDerecha.setStartY(Y + 32 - YDES);
        lineaInferiorDerecha.setEndX(X + XEND + XDES);

        Y += ((columnas * CASILLA_ALTO) + -5 * (columnas - 5)) / 2 - YDES;

        lineaInferiorIzquierda.setEndY(Y);
        lineaInferiorIzquierda.setStroke(Color.BLACK);
        lineaInferiorIzquierda.setStrokeWidth(2);

        lineaInferiorDerecha.setEndY(Y);
        lineaInferiorDerecha.setStroke(Color.BLACK);
        lineaInferiorDerecha.setStrokeWidth(2);

        QuadCurve curvaInferiorIzquierda = new QuadCurve(
                X - XDES, Y,
                X - XDES, Y + 21 + YDES,
                X - XDES + 10, Y + 21 + YDES);
        curvaInferiorIzquierda.setStroke(Color.BLACK);
        curvaInferiorIzquierda.setFill(null);
        curvaInferiorIzquierda.setStrokeWidth(2);

        QuadCurve curvaInferiorDerecha = new QuadCurve(
                X + XEND + XDES, Y,
                X + XEND + XDES, Y + 21 + YDES,
                X + XEND + XDES - 10, Y + 21 + YDES);
        curvaInferiorDerecha.setStroke(Color.BLACK);
        curvaInferiorDerecha.setFill(null);
        curvaInferiorDerecha.setStrokeWidth(2);


        group.getChildren().addAll(
                curvaSuperiorIzquierda, lineaSuperiorIzquierda, curvaMediaSuperiorIzquierda, curvaMediaInferiorIzquierda, lineaInferiorIzquierda, curvaInferiorIzquierda,
                curvaSuperiorDerecha, lineaSuperiorDerecha, curvaMediaSuperiorDerecha, curvaMediaInferiorDerecha, lineaInferiorDerecha, curvaInferiorDerecha);
        return group;
    }

    /**
     * Crea las comillas que se paran los elementos del espacio de solución
     *
     * @return Retorna un grupo con las comillas asignadas
     */
    private Group crearComillas(int dimension) {
        Group grupo = new Group();
        double relativeY = (LAST_Y - Y_PRIMER_PARENTESIS) / 2 + Y_PRIMER_PARENTESIS;
        double tmp = PARENTESIS_HGAP / 2;
        double relativeX = tmp / 2 + tmp + X_PRIMER_PARENTESIS;
        for (int i = 0; i < dimension - 1; i++) {
            double X = relativeX + (PARENTESIS_HGAP * i) + 5;
            Circle punto = new Circle(X, relativeY, 2);
            punto.setStroke(Color.BLACK);
            QuadCurve cola = new QuadCurve(
                    X - 1.5, relativeY + 6,
                    X + 1.5, relativeY + 6,
                    X + 1.5, relativeY
            );
            cola.setStroke(Color.BLACK);
            cola.setFill(null);
            cola.setStrokeWidth(2);
            grupo.getChildren().addAll(punto, cola);
        }
        return grupo;
    }

    /**
     * Crea un signo igual para la solución trivial
     *
     * @return Grupo con dos iguales creados
     */
    private Group crearIgual() {
        Group group = new Group();
        double relativeY = (LAST_Y - Y_PRIMER_PARENTESIS) / 2 + Y_PRIMER_PARENTESIS;
        double tmp = PARENTESIS_HGAP / 2;
        double relativeX = tmp / 2 + tmp + X_PRIMER_PARENTESIS + 5;
        Line lineSuperior =
                new Line(relativeX - 5, relativeY - 4, relativeX + 6, relativeY - 4);
        lineSuperior.setStroke(Color.BLACK);
        lineSuperior.setStrokeWidth(3);
        Line lineInferior =
                new Line(relativeX - 5, relativeY + 4, relativeX + 6, relativeY + 4);
        lineInferior.setStroke(Color.BLACK);
        lineInferior.setStrokeWidth(3);
        group.getChildren().addAll(lineInferior, lineSuperior);
        return group;
    }

    /**
     * Eliminar los paréntesis actuales en la gráfica y limpia el ArrayList que los contiene
     */
    private void cleanParentesis() {
        panelPrincipal.getChildren().removeAll(parentesis);
        parentesis.clear();
    }

    //UI
    @FXML
    public MenuItem menuItemF2;
    @FXML
    public MenuItem menuItemF3;
    @FXML
    public MenuItem menuItemF4;
    @FXML
    public MenuItem menuItemF5;
    @FXML
    public MenuItem menuItemC2;
    @FXML
    public MenuItem menuItemC3;
    @FXML
    public MenuItem menuItemC4;
    @FXML
    public MenuItem menuItemC5;
    @FXML
    public Button btnResolver;
    @FXML
    public Button btnLimpiar;
    @FXML
    public Button btnVolver;
    @FXML
    public Pane panelPrincipal;
    @FXML
    public Label lblSolucionHeader;
    @FXML
    public GridPane gridPaneSistema;
    @FXML
    public GridPane gridPaneSolucion;
}
