package ai.ai2;

public class Connection {
	private Node node1;
	private Node node2;
	private double connectionValue;
	
	public Connection(Node node1, Node node2) {
		setNode1(node1);
		setNode2(node2);
		setConnectionValue(Math.random() * 20 - 10);
	}
	
	
	public Node getNode1() {
		return node1;
	}
	
	private void setNode1(Node node1) {
		this.node1 = node1;
	}
	
	public Node getNode2() {
		return node2;
	}
	
	private void setNode2(Node node2) {
		this.node2 = node2;
	}
	
	public double getConnectionValue() {
		return connectionValue;
	}
	
	public void setConnectionValue(double connectionValue) {
		this.connectionValue = connectionValue;
	}
	
	
	public void transferData(int direction) {
		if (direction == 0) {
			node2.addValue(node1.getValue() * connectionValue);
		}
		else {
			
		}
	}
}
