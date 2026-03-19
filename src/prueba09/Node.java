package prueba09;

import java.io.Serializable;

public class Node implements Serializable{

	private static final long serialVersionUID = 1L;
	private double val;
	public Node() {
		
	}
	
	public double getVal() {
		return val;
	}
	
	public void setVal(double val) {
		this.val = val;
	}
}
