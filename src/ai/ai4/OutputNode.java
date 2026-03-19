package ai.ai4;


import java.util.ArrayList;

public class OutputNode extends Node {
	private ArrayList<Node> backLayer;
	private double bias;
	
	
	public OutputNode() {
		setBackLayer(new ArrayList<Node>());
		setBias(Math.random() * 4 - 2);
	}
	
	
	public ArrayList<Node> getBackLayer() {
		return backLayer;
	}
	
	public void setBackLayer(ArrayList<Node> backLayer) {
		this.backLayer = backLayer;
	}
	
	public double getBias() {
		return bias;
	}
	
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	
	public void addNodeBack(Node node) {
		getBackLayer().add(node);
	}
	
	public void setValueSigmoid(int nNodesLayer) {
		addValue(bias);
		setValue(1 / (1 + Math.exp(- getValue())));
	}
}
