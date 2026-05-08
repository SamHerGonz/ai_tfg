package ai;

public class NeuralMath {

    /**
     * Función de activación de los nodos
     * @param n Valor
     * @return La funcion activada
     */
    public static double setSigmoid(double n) {
        return (1 / (1 + Math.exp(- n)));
    }

    /**
     *  La derivada de la función de activación de los nodos
     * @param sigmoid valor
     * @return La derivada de la funcion activada
     */
    public static double setDerivativeSigmoid(double sigmoid) {
        return sigmoid * (1 - sigmoid);
    }

    /**
     * Sum the values of the 2 arrays. They both need to have the same size
     * @param a1 the first array. It can be null
     * @param a2 the second array
     * @return An array with the sum of both arrays
     */
    public static double[] addArrays(double[] a1, double[] a2) {
        if (a1 == null) {
            a1 = new double[a2.length];
        }
        double[] ret_array = a1.clone();
        for (int i = 0; i < a1.length; i++) {
            ret_array[i] = a1[i] + a2[i];
        }
        return ret_array;
    }

    /**
     * Sum the values of the 2 arrays. They both need to have the same size, in all of its dimensions
     * @param a1 the first array. It can be null
     * @param a2 the second array
     * @return An array with the sum of both arrays
     */
    public static double[][][] addArrays(double[][][] a1, double[][][] a2) {
        double[][][] ret_array = a1.clone();
        for (int i = 0; i < a2.length; i++) {
            for (int j = 0; j < a2[i].length; j++) {
                for (int k = 0; k < a2[i][j].length; k++) {
                    ret_array[i][j][k] = a1[i][j][k] + a2[i][j][k];
                }
            }
        }
        return ret_array;
    }

    /**
     * Subtract the values of a1 to a2. Both have to be the same size
     * @param a1 Array number 1
     * @param a2 Array number 2
     * @return a1 - a2 in an array
     */
    public static double[] subtractArrays(double[] a1, double[] a2) {
        double[] ret_array = new double[a1.length];
        for (int i = 0; i < ret_array.length; i++) {
            ret_array[i] = a1[i] - a2[i];
        }
        return ret_array;
    }

    public static int getMaxPosition(double[] array) {
        int maxAnswer = 0;
        for (int j = 1; j < array.length; j++) {
            if (array[j] > array[maxAnswer]) {
                maxAnswer = j;
            }
        }
        return maxAnswer;
    }
}