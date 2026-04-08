package ai.ai8;

import java.io.Serial;
import java.io.Serializable;

public class OutputNode extends Node implements Serializable {
	private double bias;
    @Serial
    private static final long serialVersionUID = -4041293869028036396L;

	public OutputNode(double max) {
		setBias(Math.random() * (max * 2) - max);
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

    // Aquí se calcula el valor del dato esperado de esta capa (expectedData), o por lo menos la parte que indica esta capa
    public double[] calculateExpectedDataFromOutputNode(double value, double[] outputs, int sizeLayer) {
        double[] ret_values = new double[sizeLayer];
        double derSigmoid = NeuralMath.setDerivativeSigmoid(value);
        for (int i = 0; i < sizeLayer; i++) {
            ret_values[i] = derSigmoid * outputs[i];
        }
        return ret_values;
    }

}