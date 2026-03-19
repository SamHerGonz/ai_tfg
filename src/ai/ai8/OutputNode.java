package ai.ai8;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class OutputNode extends Node implements Serializable {
	private ArrayList<Node> backLayer;
	private double bias;
    @Serial
    private static final long serialVersionUID = -4041293869028036396L;

	public OutputNode(double max) {
		setBackLayer(new ArrayList<>());
		setBias(Math.random() * (max * 2) - max);
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
}