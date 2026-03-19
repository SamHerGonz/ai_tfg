package prueba10_2;

public class ConnectionNode extends Node {

	private static final long serialVersionUID = 1951659229203210059L;
	private double[] values;
	private Node[] conectionsFoward;
	
	
	public ConnectionNode() {
		setValues(new double[0]);
		setconectionsFoward(new Node[0]);
	}
	
	public ConnectionNode(int n) throws Exception {
		setValues(new double[n]);
		setconectionsFoward(new Node[n]);
	}
	
	public ConnectionNode(double[] values, Node[] conectionsFoward) throws Exception {
		if (values.length != conectionsFoward.length) {
			throw new Exception("Error");
		}
		setValues(values);
		setconectionsFoward(conectionsFoward);
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	public Node[] getconectionsFoward() {
		return conectionsFoward;
	}

	public void setconectionsFoward(Node[] conectionsFoward) {
		this.conectionsFoward = conectionsFoward;
	}
	
	public void sendValues() {
		for (int i = 0; i < conectionsFoward.length; i++) {
			conectionsFoward[i].setVal(conectionsFoward[i].getVal() + getVal() * getValues()[i]);
		}
	}
}
