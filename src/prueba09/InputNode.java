package prueba09;

public class InputNode extends Node {
	private double[] values;
	private Node[] conections;
	
	
	public InputNode() {
		setValues(new double[0]);
		setConections(new Node[0]);
	}
	
	public InputNode(int n) throws Exception {
		setValues(new double[n]);
		setConections(new Node[n]);
	}
	
	public InputNode(double[] values, Node[] conections) throws Exception {
		if (values.length != conections.length) {
			throw new Exception("Error");
		}
		setValues(values);
		setConections(conections);
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	public Node[] getConections() {
		return conections;
	}

	public void setConections(Node[] conections) {
		this.conections = conections;
	}
	
	public void sendValues() {
		for (int i = 0; i < conections.length; i++) {
			conections[i].setVal(conections[i].getVal() + getVal() * getValues()[i]);
		}
	}
}
