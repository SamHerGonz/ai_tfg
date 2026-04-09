package ai.ai8;

import java.util.ArrayList;

public class NeuralMath {
    public static double setSigmoid(double n) {
        return (1 / (1 + Math.exp(- n)));
    }
    public static double setDerivativeSigmoid(double n) {
        return setSigmoid(n) * (1 - setSigmoid(n));
    }
    public static double[] addArrays(double[] a1, double[] a2, ArrayList<Integer> indexesA2) {
        if (a1 == null) {
            a1 = new double[a2.length];
        }
        double[] ret_array = a1.clone();
        for (int i = 0; i < indexesA2.size(); i++) {
            ret_array[i] = a1[i] + a2[i];
        }
        return ret_array;
    }

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