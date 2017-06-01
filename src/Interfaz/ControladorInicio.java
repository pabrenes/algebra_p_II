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
    public Button btn1;
    @FXML
    public Button btn2;
    @FXML
    public Button btn3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn1.setOnAction(event -> {
            abrirVentanaBases();
        });
    }

    public void abrirVentanaBases(){
        Stage escenario = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Parent raiz = null;                                                                                              //Se crean estos tres objetos
        try {
            raiz = loader.load(getClass().getResource("Interfaz.fxml").openStream());                           //Con esto se indica el FXML de la nueva ventana
        } catch (IOException e) {
            e.printStackTrace();
        }
        ControladorInterfaz controlador = (ControladorInterfaz) loader.getController();                            //Se instancia el controlador respectivo, se debe hacer un casting para que funcione.
        escenario.setTitle("Bases y dimensión de un sistema homogéneo de ecuaciones lineales");
        escenario.setScene(new Scene(raiz));
        escenario.show();

        Stage temporal = (Stage) btn1.getScene().getWindow();                                             //Se obtiene el stage al que pertenece el boton que abre la nueva ventana para poder cerrarla
        temporal.close();
    }
}
