package Logica;

/**
 * Created by Pablo Brenes 2016250460
 * 27 may 2017.
 */
public class Fraccion {

    private long numerador;
    private long denominador;

    /**
     * Builder de la clase, crea un nuevo objeto Fración con los valores dados por parámetro
     *
     * @param _numerador   Valor para el numerador
     * @param _denominador Valor para el denominador
     */
    public Fraccion(long _numerador, long _denominador) {
        numerador = _numerador;
        denominador = _denominador;
    }

    /**
     * Suma dos fracciones dadas
     *
     * @param operando1 Fracción a sumar
     * @param operando2 Fracción a sumar
     * @return Fraccion, valores de las fracciones sumadas y simplificadas
     */
    static Fraccion sumar(Fraccion operando1, Fraccion operando2) {
        long numeradorResultado;
        long denominadorResultado;

        if (operando1.denominador == operando2.denominador) {
            numeradorResultado = operando1.numerador + operando2.numerador;
            denominadorResultado = operando1.denominador;
        } else {
            numeradorResultado = (operando1.numerador * operando2.denominador) +
                    (operando2.numerador * operando1.denominador);
            denominadorResultado = operando1.denominador * operando2.denominador;
        }

        return simplificar(new Fraccion(numeradorResultado, denominadorResultado));
    }

    /**
     * Multiplica dos fracciones dadas
     *
     * @param operando1 Fraccion a multiplicar
     * @param operando2 Fraccion a multiplicar
     * @return Fraccion, valores de las fracciones multiplicados y simplicados
     */
    static Fraccion multiplicar(Fraccion operando1, Fraccion operando2) {
        long numeradorResultado = operando1.numerador * operando2.numerador;
        long denominadorResultado = operando1.denominador * operando2.denominador;

        return simplificar(new Fraccion(numeradorResultado, denominadorResultado));
    }

    /**
     * Calcula la inversa de una fracción
     *
     * @param fraccion Fraccion a cacular inversa
     * @return Fraccion, inversa de la fracción simplificada
     */
    static Fraccion invertir(Fraccion fraccion) {
        return simplificar(new Fraccion(fraccion.denominador, fraccion.numerador));
    }

    /**
     * Función encargada de simplificar y estandarizar los valores de una fracción
     * Si la fracción posee denominador y numerador negativos pasan a ser positivos
     * Si la fracción posee denominador negativo, envía este al numerador
     *
     * @param fraccion fracción que debe ser simplificada
     * @return Fracción, con valores simplificados y signos estandarizados
     */
    private static Fraccion simplificar(Fraccion fraccion) {
        if (fraccion.numerador == 0) {
            return new Fraccion(0, 1);
        } else {
            long divisorComun = encontrarDivisorComun(fraccion.numerador, fraccion.denominador);
            long numeradorResultado = fraccion.numerador / divisorComun;
            long denominadorResultado = fraccion.denominador / divisorComun;
            if (numeradorResultado < 0 && denominadorResultado < 0 ||
                    numeradorResultado > 0 && denominadorResultado < 0) {
                numeradorResultado *= -1;
                denominadorResultado *= -1;
            }
            return new Fraccion(numeradorResultado, denominadorResultado);
        }
    }

    /**
     * Función encargada de enviar las entradas en valor absoluto a otra función recursiva encargada del procedimiento real
     *
     * @param numero1 Long con el primer número a encontra su divisor común con el segundo.
     * @param numero2 Long con el segundo número a encontra su divisor común con el primero.
     * @return Long con el divisor común encontrado
     */
    private static long encontrarDivisorComun(long numero1, long numero2) {
        return encontrarDivisorComunAux(Math.abs(numero1), Math.abs(numero2));
    }

    /**
     * Función que encuentra el máximo divisor común entre dos números.
     *
     * @param numero1 Long con el primer número a encontra su divisor común con el segundo.
     * @param numero2 Long con el segundo número a encontra su divisor común con el primero.
     * @return Long con el divisor común encontrado
     */
    private static long encontrarDivisorComunAux(long numero1, long numero2) {
        long divisor = 2;
        long minimo = numero1;
        if (numero1 > numero2)
            minimo = numero2;
        while (!(divisor > minimo)) {
            if (numero1 % divisor == 0 && numero2 % divisor == 0)
                return divisor * encontrarDivisorComunAux(numero1 / divisor, numero2 / divisor);
            divisor++;
        }
        return 1;
    }

    /**
     * Getter del numerador
     *
     * @return Long, numerador de la fracción
     */
    private long getNumerador() {
        return numerador;
    }

    /**
     * Getter del denominador
     *
     * @return Long, denominador de la Fracción
     */
    private long getDenominador() {
        return denominador;
    }

    /**
     * Se encarga de retornar una nueva Fraccion en base en la actual
     *
     * @return Fraccion, copia de la fraccion actual
     */
    Fraccion copiarFraccionConArreglo() {
        return simplificar(this);
    }

    /**
     * Función que determina si dos fracciones son iguales
     * @param fraccion Fracción a determinar su igualdad con la actual
     * @return Boleean, iguales ? true : false
     */
    boolean equals(Fraccion fraccion) {
        return (fraccion.getNumerador() == numerador && fraccion.getDenominador() == denominador);
    }

    /**
     * Metodo que convierte la información de la fracción a un String
     * @return String, fracción en string
     */
    @Override
    public String toString() {
        return numerador + "/" + denominador;
    }
}
