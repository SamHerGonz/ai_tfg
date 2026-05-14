package ai;

import java.io.Serial;
import java.io.Serializable;

public class OutputNode extends Node implements Serializable {
	private double bias;
    @Serial
    private static final long serialVersionUID = -4041293869028036396L;

	public OutputNode(double max) {
        // Inicializar el bias en un valor aleatorio del -max al max
		setBias(Math.random() * (max * 2) - max);
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

    public void addBias(double bias) {
        this.bias = getBias() + bias;
    }

    @Override
    public String toString() {
        return ("Output node:\n\t Bias: " + getBias());
    }

}