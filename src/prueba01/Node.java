package prueba01;

public class Node {
	private double val;
	private double[] values;
	private Node[] conections;
	
	
	public Node() {
		setValues(new double[0]);
		setConections(new Node[0]);
	}
	
	public Node(int n) throws Exception {
		setValues(new double[n]);
		setConections(new Node[n]);
	}
	
	public Node(double[] values, Node[] conections) throws Exception {
		if (values.length != conections.length) {
			throw new Exception("Error");
		}
		setValues(values);
		setConections(conections);
	}
	
	
	public double getVal() {
		return val;
	}

	public void setVal(double val) {
		this.val = val;
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
	
	public void changeValues(int reward) {
		
	}
	
}
