package ai.ai8;

public class NeuralMath {
    public static double setSigmoid(double n) {
        return (1 / (1 + Math.exp(- n)));
    }
    public static double setDerivativeSigmoid(double n) {
        return setSigmoid(n) * (1 - setSigmoid(n));
    }
    public static double[] addArrays(double[] a1, double[] a2) {
        double[] ret_array = new double[a1.length];
        for (int i = 0; i < ret_array.length; i++) {
            ret_array[i] = a1[i] + a2[i];
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
}