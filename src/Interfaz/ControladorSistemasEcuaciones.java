package Interfaz;

import Logica.Fraccion;
import Logica.Matriz;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Pablo Brenes (El gay del grupo) 2016250460
 * 27 may 2017.
 */
public class ControladorSistemasEcuaciones implements Initializable {

    private int filas;
    private int columnas;
    private Matriz sistema;

    private final String[] VARIABLES = {" X", " Y", " Z", "W", " S"};
    private final int ERROR_DIV_ZERO = 1;
    private final int ERROR_NON_NUME = 2;

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
        construirMatriz();
    }

    /**
     * Trabajo que es asignado a cada itenMenu del menú Columnas
     *
     * @param columna Columna a la cual pertenece el evento
     */
    private void eventoMenuColumna(int columna) {
        columnas = columna;
        construirMatriz();
    }

    /**
     * Construye la matriz visual con las bases del sistema de solución para el usuario
     *
     * @param solucion vector de dos dimensiones con las bases de la solución del sistema
     */
    private void construirSolucion(Fraccion[][] solucion) {
        if (solucion.length == 0){
            System.out.println("TODO CERO PAPU");
        }
        gridPaneSolucion.getChildren().clear();
        for (int i = 0; i < solucion.length; i++) {
            for (int j = 0; j < solucion[i].length; j++) {
                gridPaneSolucion.add(construirEntradaSolucion(solucion, i, j), i, j);
            }
        }
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
        try{
            numerador = Integer.parseInt(entradaNumerador.getText());
            denominador = Integer.parseInt(entradaDenominador.getText());
            if (denominador == 0){
                generateError(ERROR_DIV_ZERO);
                return null;
            }
            return new Fraccion(numerador, denominador);
        }catch(NumberFormatException nfe){
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
        gridPaneSolucion.getChildren().clear();
    }

    /**
     * Regresa al menú inicial
     */
    private void volver() {
        Stage escenario = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent raiz;                                                                                             //Se crean estos tres objetos
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
    public GridPane gridPaneSistema;
    @FXML
    public GridPane gridPaneSolucion;
}
