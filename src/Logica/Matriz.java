package Logica;


/**
 * Created by Pablo Brenes 2016250460
 * 27 may 2017.
 */
public class Matriz {

    private Fraccion[][] matriz;
    private int filas;
    private int columnas;

    private static final Fraccion FRACCION_CERO = new Fraccion(0, 1);

    /**
     * Builder de la clase, crea un vector estático de dos dimensiones según los tamaños dados por parámetro
     *
     * @param _filas    Tamaño en filas de la matriz
     * @param _columnas Tamaño en columnas de la matriz
     */
    public Matriz(int _filas, int _columnas) {
        filas = _filas;
        columnas = _columnas;
        matriz = new Fraccion[filas][columnas];
    }

    /**
     * Builder de la calse, crea una nueva matriz a base de un vectos estático de dos dimensiones dado como parámetro
     *
     * @param _matriz Vector estático de dos dimensiones con las fracciones de la matriz.
     */
    public Matriz(Fraccion[][] _matriz) {
        filas = _matriz.length;
        columnas = _matriz[0].length;
        matriz = _matriz;
    }





    /**
     * Retorna una copia del vector actual de dos dimensiones con la información de las Fracciones
     *
     * @return Fraccion[][], copia del vector de dos dimensiones actual que posee esta matriz
     */
    private Fraccion[][] copiarMatriz() {
        Fraccion[][] copia = new Fraccion[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                copia[i][j] = getEntry(i, j).clone();
            }
        }
        return copia;
    }

    /**
     * Retorna la entrada de la matriz dada la fila y cclumna de la matriz
     *
     * @param fila    Fila donde se encuentra la entrada
     * @param columna Columna donde se encuentra la entrada
     * @return Fraccion, la fracción de la matriz dada la fila y columna
     */
    private Fraccion getEntry(int fila, int columna) {
        return matriz[fila][columna];
    }

    /**
     * Función que dadas dos filas de una matriz intercambia la posición de estas
     * @param matriz Matriz en la cual será realizada la operación
     * @param fila1 Fila número uno a intercambiar
     * @param fila2 Fila número dos a intercambiar
     */
    private void intercambiarFilas(Fraccion[][] matriz, int fila1, int fila2) {
        Fraccion[] buffer = matriz[fila1];
        matriz[fila1] = matriz[fila2];
        matriz[fila2] = buffer;
    }

    /**
     * Función que multiplica todas las entradas de una fila por un escalar
     * @param matriz Matriz en la cual será realizada la operación
     * @param escalar Escalara por el cual será multiplicada la fila
     * @param fila Fila en la que se aplicará la multiplicación por escalar
     */
    public void multiplicarFila(Fraccion[][] matriz, Fraccion escalar, int fila) {
        for (int i = 0; i < matriz[fila].length; i++) {
            matriz[fila][i] = Fraccion.multiplicar(escalar, matriz[fila][i]);
        }
    }

    /**
     * Función que suma a una fila (fila2) otra fila multiplicada por un escalar (fila1 * escalar)
     *
     * @param matriz matriz en la cual será realizada la operación
     * @param escalar escalar por la que se multiplicará la fila1
     * @param fila1 fila cual será multiplicada por un escalar para luego sumarla a otra
     * @param fila2 fila a la cual se le sumará la fila1 multiplicada por un escalar
     */
    public void sumarFilas(Fraccion[][] matriz, Fraccion escalar, int fila1, int fila2) {
        int largoFila = matriz[fila1].length;
        for (int i = 0; i < largoFila; i++) {
            matriz[fila2][i] = Fraccion.sumar(matriz[fila2][i],
                    Fraccion.multiplicar(matriz[fila1][i], escalar));
        }
    }

}
