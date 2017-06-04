package Interfaz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ControladorInicio implements Initializable {
    @FXML
    public Button btnSoluciones;
    @FXML
    public Button btnTransformaciones;
    @FXML
    public Button btnImagenes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSoluciones.setOnAction(event -> abrirVentana("SistemasEcuaciones.fxml",
                "Bases y dimensión del espacio de solución de un sistema homogéneo de ecuaciones lineales."));
        btnTransformaciones.setOnAction(event -> abrirVentana("TransformacionesLineales.fxml",
                "Visualización de vectores y transformaciones lineales"));
    }

    private void abrirVentana(String ventana, String title) {
        Stage escenario = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent raiz;                                                                                                    //Se crean estos tres objetos
        try {
            raiz = loader.load(getClass().getResource(ventana).openStream());                                           //Con esto se indica el FXML de la nueva ventana
            escenario.setTitle(title);
            escenario.setScene(new Scene(raiz));
            escenario.show();

            Stage temporal = (Stage) btnSoluciones.getScene().getWindow();                                              //Se obtiene el stage al que pertenece el boton que abre la nueva ventana para poder cerrarla
            temporal.close();
        } catch (IOException ignored) {
        }
    }
}
