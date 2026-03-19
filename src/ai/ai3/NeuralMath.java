package ai.ai3;

public class NeuralMath {
	public static int sumArray(int[] array) {
		int n = 0;
		for (int i = 0; i < array.length; i++) {
			n += array[i];
		}
		return n;
	}

    public static double setSigmoid(double n) {
        return (1 / (1 + Math.exp(- n)));
    }
}
