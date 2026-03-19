package ai.ai4;

public abstract class Node {
	private double value;
	
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public void addValue(double value) {
		setValue(getValue() + value);
	}
}
