package ai;

public class NeuralMath {

    /**
     * Activation function of the nodes
     * @param n value
     * @return The function activated
     */
    public static double setSigmoid(double n) {
        return (1 / (1 + Math.exp(- n)));
    }

    /**
     *  The derivative of the activation sigmoid function
     * @param sigmoid value
     * @return The derivative of the function activated
     */
    public static double setDerivativeSigmoid(double sigmoid) {
        return sigmoid * (1 - sigmoid);
    }

    public static double reverseSigmoid(double n) {
        if (n == 0) {
            return -999;
        }
        else if (n == 1){
            return NeuralMath.reverseSigmoid(0.9999999999999999);
        }
        return Math.log(n / (1 - n));
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

    public static double[][] multMatrix(double n, double[][] a1) {
        double[][] ret_array = new double[a1.length][a1[0].length];
        for (int i = 0; i < ret_array.length; i++) {
            for (int j = 0; j < ret_array[0].length; j++) {
                ret_array[i][j] += a1[i][j] * n;
            }
        }
        return ret_array;

    }

    public static double[][] multMatrix(double[][] a1, double[][] a2) {
        double[][] ret_array = new double[a1.length][a2[0].length];
        for (int i = 0; i < ret_array.length; i++) {
            for (int j = 0; j < ret_array[0].length; j++) {
                for (int k = 0; k < a1[0].length; k++) {
                    ret_array[i][j] += a1[i][k] * a2[k][j];
                }
            }
        }
        return ret_array;
    }

    public static double[][] transposeArray(double[][] array) {
        double[][] nArray = new double[array.length][];
        for (int i = 0; i < array.length; i++) {
            nArray[i] = new double[array[i].length];
            for (int j = 0; j < array[i].length; j++) {
                nArray[i][j] = array[j][i];
            }
        }
        return nArray;
    }
}