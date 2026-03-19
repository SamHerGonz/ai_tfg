package ai.ai2;

enum OutType {
	normal,
	yesNo,
	noNegative;
}

public class OutputNode extends Node {
	private static final long serialVersionUID = -1367214317918713913L;
	private OutType type;
	private double bias;
	private int anwser;

	public OutputNode(int value) {
		setAnwser(value);
	}
	
	
	public OutType getType() {
		return type;
	}
	
	public void setType(OutType type) {
		this.type = type;
	}
	
	public double getBias() {
		return bias;
	}
	
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	public int getAnwser() {
		return anwser;
	}
	
	private void setAnwser(int anwser) {
		this.anwser = anwser;
	}
	
	
	public void setValueSigmoid(int capaAnterior) {
		addValue(bias);
		setValue(1 / (1 + Math.exp(- getValue() / capaAnterior)));
	}
}
