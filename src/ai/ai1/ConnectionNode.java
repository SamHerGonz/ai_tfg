package ai.ai1;

public class ConnectionNode extends Node {
	private static final long serialVersionUID = 1951659229203210059L;
	private double bias;
	
	public double getBias() {
		return bias;
	}
	
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	public ConnectionNode() {
		setBias(Math.random());
	}
}
