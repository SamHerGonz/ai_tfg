package ai.ai1;

import java.io.Serializable;

public class Node implements Serializable {
	private static final long serialVersionUID = 1;
	private double value;
	
	
	public Node() {
		
	}
	
	
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
