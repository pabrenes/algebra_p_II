package Interfaz;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;


public class ControladorInicio extends CambiarVentana implements Initializable {
    @FXML
    public Button btnSoluciones;
    @FXML
    public Button btnTransformaciones;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSoluciones.setOnAction(event -> abrirVentana(
                CambiarVentana.FXML_SISTEMAS_ECUACIONES,
                CambiarVentana.TITLE_SISTEMAS_ECUACIONES,
                btnSoluciones
        ));
        btnTransformaciones.setOnAction(event -> abrirVentana(
                CambiarVentana.FXML_TRANSFORMACIONES_LINEALES,
                CambiarVentana.TITLE_TRANSFORMACIONES_LINEALES,
                btnTransformaciones
        ));
    }
}
