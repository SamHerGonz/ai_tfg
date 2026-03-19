package ai.ai3;

import java.io.Serializable;
import java.util.ArrayList;

public class InputNode extends Node implements Serializable{
	private ArrayList<Node> frontLayer;
	private ArrayList<Double> valFrontLayer;
    private static final long serialVersionUID = -7693503413333452026L;


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
	
	public void addNodeFront(Node node, int max) {
		getFrontLayer().add(node);
		getValFrontLayer().add(Math.random() * (max * 2) - max);
	}
	
	public void transferAllData(int minRange,int maxRange) {
        setValueSigmoid(minRange, maxRange);
        for (int i = 0; i < getFrontLayer().size(); i++) {
            getFrontLayer().get(i).addValue(NeuralMath.setSigmoid(getValue() * getValFrontLayer().get(i)));
		}
	}
}
