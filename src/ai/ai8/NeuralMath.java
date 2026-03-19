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
}