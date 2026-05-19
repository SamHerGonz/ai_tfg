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

    public static double[] moveMatrix(double[] record, int width, int height, int x, int y) {
        double[] ret_value = new double[record.length];
        System.arraycopy(record, 0, ret_value, 0, ret_value.length);
        for (int i = 0; i < x; i++) {
            if (canMoveMatrix(record,4, height)) {
                if (ret_value.length - 1 >= 0) System.arraycopy(ret_value, 0, ret_value, 1, ret_value.length - 1);
            }
        }
        for (int i = 0; i > x; i--) {
            if (canMoveMatrix(record,2, height)) {
                if (ret_value.length - 1 >= 0) System.arraycopy(ret_value, 1, ret_value, 0, ret_value.length - 1);
            }
        }
        for (int i = 0; i < y; i++) {
            if (canMoveMatrix(record,3, width)) {
                if (ret_value.length - 1 - width >= 0) System.arraycopy(ret_value, 1, ret_value, width + 1, ret_value.length - 1 - width);
            }
        }
        for (int i = 0; i > y; i--) {
            if (canMoveMatrix(record,1, width)) {
                if (ret_value.length - width >= 0) System.arraycopy(ret_value, width, ret_value, 0, ret_value.length - width);
            }
        }
        return ret_value;
    }

    private static boolean canMoveMatrix(double[] record, int direction, int size) {
        boolean b = true;
        switch (direction) {
            case 1:
                for (int i = 0; i < size; i++) {
                    if (record[i] != 0) {
                        b = false;
                        break;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < size; i++) {
                    if (record[i * size] != 0) {
                        b = false;
                        break;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < size; i++) {
                    if (record[(size - 1) * size + i] != 0) {
                        b = false;
                        break;
                    }
                }
                break;
            case 4:
                for (int i = 0; i < size; i++) {
                    if (record[i * size + (size - 1)] != 0) {
                        b = false;
                        break;
                    }
                }
                break;
        }
        return b;
    }

}