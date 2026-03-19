package ai.ai4;


import java.util.ArrayList;

public class InputNode extends Node {
	private ArrayList<Node> frontLayer;
	private ArrayList<Double> valFrontLayer;
	
	
	public InputNode() {
		setFrontLayer(new ArrayList<Node>());
		setValFrontLayer(new ArrayList<Double>());
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
	
	// Convierte un número en un rango entre el minRange al MaxRange en un número del 0 al 1
	public void setValueSigmoid(int minRange, int maxRange) {
		setValue(getValue() / (maxRange - minRange) - minRange);
	}
	
	public void addNodeFront(Node node) {
		getFrontLayer().add(node);
		getValFrontLayer().add(Math.random() * 6 - 3);
	}
	
	public void transferAllData() {
		for (int i = 0; i < getFrontLayer().size(); i++) {
			getFrontLayer().get(i).addValue(getValue() * getValFrontLayer().get(i));
		}
	}
}
