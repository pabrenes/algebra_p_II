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
    private static final Fraccion FRACCION_NEGATIVA = new Fraccion(-1, 1);

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
     * Toma la matriz presente en esta instancia y la lleva a su forma escalonada reducida, note que retorna un nuevo arreglo
     * de dos dimensiones, no modifica la matriz real de esta instancia
     *
     * @return Fracion[][] La matriz actual en forma escalonada reducida
     */
    public Fraccion[][] matrizEscalonada() {
        Fraccion[][] escalonada = copiarMatriz();
        int columnaActual = 0;
        boolean flag;

        for (int i = 0; i < filas; i++) {
            if (columnaActual >= columnas) break;
            while (columnaActual <= columnas - 1 && escalonada[i][columnaActual].equals(FRACCION_CERO)) {
                flag = isDeadColum(escalonada, i, columnaActual);
                if (flag) columnaActual++;
            }
            if (columnaActual >= columnas) break;
            multiplicarFila(escalonada, Fraccion.invertir(escalonada[i][columnaActual]), i);
            for (int j = 0; j < filas; j++) {
                if (j != i && !escalonada[j][columnaActual].equals(FRACCION_CERO)) {
                    sumarFilas(escalonada, Fraccion.multiplicar(
                            escalonada[j][columnaActual], FRACCION_NEGATIVA), i, j);
                }
            }
            columnaActual++;
        }
        return escalonada;
    }

    /**
     * En el proceso de reducción si me topo con una columna para una fila que es cero, puede que en la misma columna, pero
     * en una fila más abajo que esta, se encuentre un valor diferente de cero, en cuyo caso vale la pena intercambiar dihcas
     * filas, para obtener la matriz escalonada reducida
     *
     * @param matriz Matriz sobre la que se trabajará
     * @param fila fila en la que se encuentra un valor de cero en el proceso de reducción
     * @param columna columna sobre la que se está trabjando en el proceso de reducción
     * @return Boleean, en caso de que la columna de esa matriz pueda ser intercambiada con otra ? false | true
     */
    private boolean isDeadColum(Fraccion[][] matriz, int fila, int columna) {
        if (fila != filas - 1) {
            for (int j = fila + 1; j < filas; j++) {
                if (!matriz[j][columna].equals(FRACCION_CERO)) {
                    intercambiarFilas(matriz, fila, j);
                    return false;
                }
            }
        }
        return true;
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
                copia[i][j] = getEntry(i, j).copiarFraccion();
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
     *
     * @param matriz Matriz en la cual será realizada la operación
     * @param fila1  Fila número uno a intercambiar
     * @param fila2  Fila número dos a intercambiar
     */
    private void intercambiarFilas(Fraccion[][] matriz, int fila1, int fila2) {
        Fraccion[] buffer = matriz[fila1];
        matriz[fila1] = matriz[fila2];
        matriz[fila2] = buffer;
    }

    /**
     * Función que multiplica todas las entradas de una fila por un escalar
     *
     * @param matriz  Matriz en la cual será realizada la operación
     * @param escalar Escalara por el cual será multiplicada la fila
     * @param fila    Fila en la que se aplicará la multiplicación por escalar
     */
    private void multiplicarFila(Fraccion[][] matriz, Fraccion escalar, int fila) {
        for (int i = 0; i < matriz[fila].length; i++) {
            matriz[fila][i] = Fraccion.multiplicar(escalar, matriz[fila][i]);
        }
    }

    /**
     * Función que suma a una fila (fila2) otra fila multiplicada por un escalar (fila1 * escalar)
     *
     * @param matriz  matriz en la cual será realizada la operación
     * @param escalar escalar por la que se multiplicará la fila1
     * @param fila1   fila cual será multiplicada por un escalar para luego sumarla a otra
     * @param fila2   fila a la cual se le sumará la fila1 multiplicada por un escalar
     */
    private void sumarFilas(Fraccion[][] matriz, Fraccion escalar, int fila1, int fila2) {
        int largoFila = matriz[fila1].length;
        for (int i = 0; i < largoFila; i++) {
            matriz[fila2][i] = Fraccion.sumar(matriz[fila2][i],
                    Fraccion.multiplicar(matriz[fila1][i], escalar));
        }
    }

    public void print() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(matriz[i][j].toString());
                System.out.print('\t');
            }
            System.out.println();
        }
    }

}
