package ai.ai4;


import java.util.ArrayList;

public class ConnectionNode extends Node {
	private ArrayList<Node> frontLayer;
	private ArrayList<Double> valFrontLayer;
	private ArrayList<Node> backLayer;
	private double bias;

	
	public ConnectionNode() {
		setFrontLayer(new ArrayList<Node>());
		setValFrontLayer(new ArrayList<Double>());
		setBackLayer(new ArrayList<Node>());
		setBias(Math.random() * 4 - 2);
	}
	
	
	public ArrayList<Node> getFrontLayer() {
		return frontLayer;
	}
	
	public void setFrontLayer(ArrayList<Node> frontLayer) {
		this.frontLayer = frontLayer;
	}
	
	public ArrayList<Double> getValFrontLayer() {
		return valFrontLayer;
	}
	
	public void setValFrontLayer(ArrayList<Double> valFrontLayer) {
		this.valFrontLayer = valFrontLayer;
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
	
	
	public void addNodeFront(Node node) {
		getFrontLayer().add(node);
		getValFrontLayer().add(Math.random() * 4 - 2);
	}
	
	public void addNodeBack(Node node) {
		getBackLayer().add(node);
	}
	
	public void transferAllData() {
		for (int i = 0; i < getFrontLayer().size(); i++) {
			getFrontLayer().get(i).addValue(getValue() * getValFrontLayer().get(i));
		}
	}
	
	public void setValueSigmoid(int nNodesLayer) {
		addValue(bias);
		setValue(1 / (1 + Math.exp(- getValue())));
	}
}
