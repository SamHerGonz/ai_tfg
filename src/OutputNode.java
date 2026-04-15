import java.io.Serial;
import java.io.Serializable;

public class OutputNode extends Node implements Serializable {
	private double bias;
    @Serial
    private static final long serialVersionUID = -4041293869028036396L;

	public OutputNode(double max) {
        // Set the bias to a random value
		setBias(Math.random() * (max * 2) - max);
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}
}