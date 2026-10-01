package ai;

import java.io.Serial;
import java.io.Serializable;

public class OutputNode extends Node implements Serializable {
	private float bias;
    @Serial
    private static final long serialVersionUID = -4041293869028036396L;

	public OutputNode(float max) {
        // Inicializar el bias en un valor aleatorio del -max al max
		setBias((float) (Math.random() * (max * 2) - max));
	}

	public float getBias() {
		return bias;
	}

	public void setBias(float bias) {
		this.bias = bias;
	}

    public void addBias(float bias) {
        this.bias = getBias() + bias;
    }

    @Override
    public String toString() {
        return ("Output node:\n\t Bias: " + getBias());
    }

}