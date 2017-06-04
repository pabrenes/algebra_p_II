package Interfaz;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Pablo Brenes 2016250460
 * 4 jun 2017.
 */
class CambiarVentana {

    static final String FXML_MENU_PRINCIPAL = "Inicio.fxml";
    static final String TITLE_MENU_PRINCIPAL = "Tarea Programada 2 - Álgebra Lineal para Computación.";
    static final String FXML_SISTEMAS_ECUACIONES = "SistemasEcuaciones.fxml";
    static final String TITLE_SISTEMAS_ECUACIONES = "Bases y dimensión del espacio de solución de un sistema homogéneo de ecuaciones lineales.";
    static final String FXML_TRANSFORMACIONES_LINEALES = "TransformacionesLineales.fxml";
    static final String TITLE_TRANSFORMACIONES_LINEALES = "Visualización de vectores y transformaciones lineales.";

    /**
     * Dado el nombre de un archivo fxml, instancia la ventana de este con un título dado, además de recibir un botón
     * para poder cerrar la ventana anterior que llame a este método
     * @param ventana nombre del file para instanciar gráfica
     * @param title título que poseerá la ventanda
     * @param toClose botón el cual llama esta ventana para poder cerrarla
     */
    void abrirVentana(String ventana, String title, Button toClose) {
        Stage escenario = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent raiz;                                                                                                    //Se crean estos tres objetos
        try {
            raiz = loader.load(getClass().getResource(ventana).openStream());                                           //Con esto se indica el FXML de la nueva ventana
            escenario.setTitle(title);
            escenario.setScene(new Scene(raiz));
            escenario.show();

            Stage temporal = (Stage) toClose.getScene().getWindow();                                              //Se obtiene el stage al que pertenece el boton que abre la nueva ventana para poder cerrarla
            temporal.close();
        } catch (IOException ignored) {
        }
    }

}
