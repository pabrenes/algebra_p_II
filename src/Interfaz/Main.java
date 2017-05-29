package Interfaz;

import Logica.Fraccion;
import Logica.Matriz;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Pablo Brenes 2016250460
 * 27 may 2017.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Interfaz.fxml"));
        primaryStage.setTitle("Tarea Programada 2 - Álgebra Lineal para Computación");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        //launch(args);

        Fraccion[][] matriz = {
                {new Fraccion(1, 1), new Fraccion(2, 1), new Fraccion(3, 1), new Fraccion(4, 1)},
                {new Fraccion(4, 1), new Fraccion(3, 1), new Fraccion(2, 1), new Fraccion(1, 1)}
        };

        Matriz matrix = new Matriz(matriz);
        matrix.print();
        System.out.println("---------------------------");
        Matriz nueva = new Matriz (matrix.espacioDeSolucion());
        nueva.print();



    }

}
