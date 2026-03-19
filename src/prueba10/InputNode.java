package prueba10;

public class InputNode extends Node {
	
	private static final long serialVersionUID = -7693503413333452026L;
	private double[] values;
	
	
	public InputNode() {
		setValues(new double[0]);
	}
	
	public InputNode(int n) throws Exception {
		setValues(new double[n]);
	}
	
	public InputNode(double[] values) throws Exception {
		setValues(values);
	}
	
	
	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}
	
	
	public void sendValues() {
		for (int i = 0; i < values.length; i++) {
			SysAI.nodes[1][i].addVal(getVal() * values[i]);
		}
	}
}
