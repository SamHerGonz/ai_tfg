package ai.ai1;

enum OutType {
	normal,
	yesNo,
	noNegative;
}

public class OutputNode extends Node {
	private static final long serialVersionUID = -1367214317918713913L;
	private OutType type;
	private double threshold;
	private double bias;

	public OutputNode() {
		setThreshold(0.5);
	}
	
	public OutType getType() {
		return type;
	}
	
	public void setType(OutType type) {
		this.type = type;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	
	public double getBias() {
		return bias;
	}
	
	public void setBias(double bias) {
		this.bias = bias;
	}
}
