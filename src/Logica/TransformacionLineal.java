package Logica;

/**
 * Creado por David Valverde Garro - 2016034774
 * Fecha: 01-Jun-17 Tiempo: 11:28 PM
 */
public class TransformacionLineal {
    public static int[] hacerTransformacionLineal(int[][] matrizTransformacion, int[] vector){
        int[] resultado = {0,0};
        resultado[0] = (matrizTransformacion[0][0] * vector[0]) + (matrizTransformacion[0][1] * vector[1]);
        resultado[1] = (matrizTransformacion[1][0] * vector[0]) + (matrizTransformacion[1][1] * vector[1]);

        return resultado;
    }

    public static int[] multiplicarPorEscalar(int[] vector, int escalar){
        int[] resultado = {0,0};
        resultado[0] = escalar * vector[0];
        resultado[1] = escalar * vector[1];

        return resultado;
    }

    public static int[] sumarVectores(int[] vectorU, int[] vectorV){
        int[] resultado = {0,0};
        resultado[0] = vectorU[0] + vectorV[0];
        resultado[1] = vectorU[1] + vectorV[1];

        return resultado;
    }
}
