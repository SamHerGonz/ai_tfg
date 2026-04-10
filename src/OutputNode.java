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

    /**
     *
     * @param value The value of the
     * @param outputNeuralNetwork
     * @return
     */
    // Aquí se calcula el valor del dato esperado de esta capa (expectedData), o por lo menos la parte que indica esta capa
    public double calculateExpectedData(double value, double outputNeuralNetwork) {
        double ret_values = 1;
        double derSigmoid = NeuralMath.setDerivativeSigmoid(value);
        ret_values *= derSigmoid * outputNeuralNetwork;
        return ret_values;
    }

}