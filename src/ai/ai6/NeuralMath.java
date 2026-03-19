package ai.ai6;

public class NeuralMath {
    public static double setSigmoid(double n) {
        return (1 / (1 + Math.exp(- n)));
    }
    public static double setDerivativeSigmoid(double n) {
        return setSigmoid(n) * (1 - setSigmoid(n));
    }
}