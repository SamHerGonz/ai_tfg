package prueba10;

import java.io.Serializable;

public class Node implements Serializable{
	
	private static final long serialVersionUID = -8207463873783980213L;
	private double val;
	
	
	public Node() {
		
	}
	
	
	public double getVal() {
		return val;
	}
	
	public void setVal(double val) {
		this.val = val;
	}
	
	public void addVal(double val) {
		this.val += val;
	}
}
