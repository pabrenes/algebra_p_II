package Logica;


import java.util.ArrayList;

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
    private static final Fraccion FRACCION_UNITARIA = new Fraccion(1, 1);

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
     * Dada la matriz actual, genera los vectores que formarán la base para las soluciones del sistema
     *
     * @return Fraccion[][], Vector de dos dimensiones con las bases para formar una solución al sistema
     */
    public Fraccion[][] espacioDeSolucion() {
        Fraccion[][] matrizEscalonada = matrizEscalonada();                                                             //Se obtiene la versión escalonada de esta matriz.
        Fraccion[][] matrizNormalizada = new Fraccion[columnas][columnas];                                              //Se genera espacio para una nueva matriz cuadrada dada por la cantidad de variables
        Fraccion[][] espacioDeSolucion;                                                                                 //Los vectores que formarán parte de la base
        ArrayList<Integer> independientes = new ArrayList<>();                                                          //Se almacenan las variables independientes
        int diagonalActual = 0;                                                                                         //Auxiliar de desplazamiento
        int filaReal = 0;                                                                                               //Auxiliar de desplazamiento
        for (int i = 0; i < columnas; i++) {
            if (filaReal < filas && matrizEscalonada[filaReal][diagonalActual].equals(FRACCION_UNITARIA)) {             //Si la matriz actual posee un uno en la posición de la diagonal si se diera el caso de que fuese cuadrada
                matrizNormalizada[i] = matrizEscalonada[filaReal];                                                      //Mantengo esa misma columna para la matriz nueva cuadrada que se forma
                filaReal++;                                                                                             //Si usé la matriz actual, debo pasar a la siguiente de esta
                diagonalActual++;                                                                                       //avanzo a la siguiente diagonal
            } else {                                                                                                    //Si en la matriz actual hace falta un valor diagonal
                matrizNormalizada[i] = ghostRow(diagonalActual);                                                        //Se crea la nueva fila con el valor diagonal independiente
                diagonalActual++;                                                                                       //No me moví en la real, cree una nueva columna, pero sí en la nueva cuadrada
            }
        }
        //A este punto ya existe una matriz cuadrada con las variables de solución vamos a procesar las independientes
        for (int i = 0; i < columnas; i++) {                                                                            //Para cada nueva fila de la matriz normalizada
            if (columnaIndependiente(matrizNormalizada[i], i)) {                                                        //Si una variable es independiente
                independientes.add(i);                                                                                  //guarde el índice de columna de este
            }
        }
        espacioDeSolucion = new Fraccion[independientes.size()][columnas];                                              //Espacio en memoria para las soluciones
        diagonalActual = 0;                                                                                             //Re uso esta variable como index manual, ya que uso un for each en vez de un for con índice disponible
        for (int columnaIndependiente : independientes) {                                                               //Para cada linea de columna donde se determino que la variable era independiente
            for (int i = 0; i < columnas; i++) {                                                                        //Vamos a extraer el vector formado por esa columna
                if (columnaIndependiente != i) {                                                                        //Si la entrada que veo no es la diagonal (variable independiente) la mantengo, en caso contrario la hago negativa
                    espacioDeSolucion[diagonalActual][i] = Fraccion.multiplicar(                                        //Se hace negativa ya que es el efecto de "despejar", estoy dando este valor como el necesario para formar otro
                            matrizNormalizada[i][columnaIndependiente], FRACCION_NEGATIVA);
                } else {
                    espacioDeSolucion[diagonalActual][i] = matrizNormalizada[i][columnaIndependiente];                  //Si es la diagonal, copiela sin invertir el signo, esta siempre es la FRACCION_UNITARIA
                }
            }
            diagonalActual++;                                                                                           //Debo avanzar a la siguiente columna (indice del for each)
        }
        return espacioDeSolucion;                                                                                       //Retorne el espacio de solución encontrado
    }

    /**
     * Determina si una columna (variable) de una matriz de soluciones es independiente, una variable es independiente, si en toda
     * la fila donde se encuentra es la única con un valor
     *
     * @param fila     Vector unidimensional donde se validará la independencia de la columna
     * @param diagonal posición donde sólo debería existir el valor
     * @return Boleean, es independiente la columna ? true | false
     */
    private boolean columnaIndependiente(Fraccion[] fila, int diagonal) {
        for (int i = diagonal + 1; i < columnas; i++) {
            if (!fila[i].equals(FRACCION_CERO)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Crea una fila de fracciones todas en cero, excepto la indicada por el parámetro, la cual sera la unitaria
     *
     * @param diagPos Posición en la que se desea la fracción sea la unitaria
     * @return Fracción[], vector unidimensional con todas sus entradas en cero, excepto la indicada
     */
    private Fraccion[] ghostRow(int diagPos) {
        Fraccion[] ghostRow = new Fraccion[columnas];
        for (int i = 0; i < columnas; i++) {
            if (i == diagPos) {
                ghostRow[i] = FRACCION_UNITARIA;
            } else {
                ghostRow[i] = FRACCION_CERO;
            }
        }
        return ghostRow;
    }

    /**
     * Toma la matriz presente en esta instancia y la lleva a su forma escalonada reducida, note que retorna un nuevo arreglo
     * de dos dimensiones, no modifica la matriz real de esta instancia
     *
     * @return Fracion[][] La matriz actual en forma escalonada reducida
     */
    private Fraccion[][] matrizEscalonada() {
        Fraccion[][] escalonada = copiarMatriz();                                                                       //Se trabaja sobre una copia
        int columnaActual = 0;
        boolean flag;

        for (int i = 0; i < filas; i++) {                                                                               //Debo avanzar fila a fila, aplicando la reducción a una misma columna en todas las filas
            if (columnaActual >= columnas)
                break;                                                                       //Puede que haya menos variables que ecuaciones
            while (columnaActual <= columnas - 1 && escalonada[i][columnaActual].equals(FRACCION_CERO)) {               //Mientras no sobrepase el límite de columnas y mi entrada actual no sea cero puedo reducir las dmeás columnas
                flag = isDeadColumn(escalonada, i, columnaActual);                                                       //Si la columna quedó en ceros al menos desde la fila actual hacia abajo, o está reducida, o la variable no posee más información
                if (flag)
                    columnaActual++;                                                                              //Si la variable no me aporta más información, debo reducir en la siguiente y olvidarme de la actual
            }
            if (columnaActual >= columnas)
                break;                                                                       //Se valida lo mismo dos veces, pero el bloque de código anterior es posible que altere la columna actual
            multiplicarFila(escalonada, Fraccion.invertir(escalonada[i][columnaActual]), i);                            //Si encontré una entrada diferente a cero, debo convertirla en un uno, por eso se multiplica por su inversa
            for (int j = 0; j < filas; j++) {                                                                           //Una vez tengo un uno en la entrada, debo formar ceros hacia abajo y arriba de la columna en que me encuentro
                if (j != i && !escalonada[j][columnaActual].equals(FRACCION_CERO)) {                                    //Es importante no aplicar el proceso en la misma fila actual y se evita aplicar el proceso si ya era cero la entrada
                    sumarFilas(escalonada, Fraccion.multiplicar(
                            escalonada[j][columnaActual], FRACCION_NEGATIVA), i, j);                                    //Se aplica la resta para formar un cero
                }
            }
            columnaActual++;                                                                                            //Naturalmente al procesar por completo una columna en una fila, avanzo a la siguiente, por eso la validación al inicio del for
        }
        return escalonada;                                                                                              //Se regresa la matriz de la forma más reducida posible
    }

    /**
     * En el proceso de reducción si me topo con una columna para una fila que es cero, puede que en la misma columna, pero
     * en una fila más abajo que esta, se encuentre un valor diferente de cero, en cuyo caso vale la pena intercambiar dihcas
     * filas, para obtener la matriz escalonada reducida
     *
     * @param matriz  Matriz sobre la que se trabajará
     * @param fila    fila en la que se encuentra un valor de cero en el proceso de reducción
     * @param columna columna sobre la que se está trabjando en el proceso de reducción
     * @return Boleean, en caso de que la columna de esa matriz pueda ser intercambiada con otra ? false | true
     */
    private boolean isDeadColumn(Fraccion[][] matriz, int fila, int columna) {
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
                copia[i][j] = getEntry(i, j).copiarFraccionConArreglo();
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

    /**
     * Envía la matriz en formato de String a la consola
     */
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
