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
                {new Fraccion(7, 3), new Fraccion(0, 1), new Fraccion(1, 1)},
                {new Fraccion(1, 1), new Fraccion(0, 1), new Fraccion(0, 1)},
                {new Fraccion(7, 1), new Fraccion(0, 3), new Fraccion(46, 7)}
        };

        Matriz matrix = new Matriz(matriz);
        matrix.print();
        Fraccion[][] escalonada = matrix.matrizEscalonada();
        Matriz matrixEscalonada = new Matriz(escalonada);
        System.out.println();
        matrixEscalonada.print();



    }

}
