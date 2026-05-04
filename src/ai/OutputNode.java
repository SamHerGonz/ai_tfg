package ai;

import java.io.Serial;
import java.io.Serializable;

public class OutputNode extends Node implements Serializable {
	private float bias;
    @Serial
    private static final long serialVersionUID = -4041293869028036396L;

	public OutputNode(double max) {
        // Set the bias to a random value
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

}