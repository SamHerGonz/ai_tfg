package ai.ai7;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class ConnectionNode extends Node implements Serializable {
	private ArrayList<Node> frontLayer;
	private ArrayList<Double> valFrontLayer;
	private ArrayList<Node> backLayer;
	private double bias;
    @Serial
    private static final long serialVersionUID = 8381313154060074511L;
	
	public ConnectionNode(double max) {
		setFrontLayer(new ArrayList<>());
		setValFrontLayer(new ArrayList<>());
		setBackLayer(new ArrayList<>());
        setBias(Math.random() * (max * 2) - max);
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
	
	
	public void addNodeFront(Node node, double max) {
		getFrontLayer().add(node);
		getValFrontLayer().add(Math.random() * (max * 2) - max);
	}
	
	public void addNodeBack(Node node) {
		getBackLayer().add(node);
	}

	public void transferAllData() {
        double sigmoidVal = NeuralMath.setSigmoid(getValue() + getBias());
        for (int i = 0; i < getFrontLayer().size(); i++) {
			getFrontLayer().get(i).addValue(sigmoidVal * getValFrontLayer().get(i));
		}
	}

    // Aquí se edita el valor de los weights, biases y se calcula el peso del nodo siguiente
    public void getLearningData(double[] marginErrors , double learningRate) {
        double sigmoidVal = NeuralMath.setSigmoid(getValue());
        for (int i = 0; i < valFrontLayer.size(); i++) {
            double n = valFrontLayer.get(i);
            double dif = marginErrors[i] - NeuralMath.setSigmoid(frontLayer.get(i).getValue());
            double derSigmoid = NeuralMath.setDerivativeSigmoid(NeuralMath.setSigmoid(frontLayer.get(i).getValue()));

            valFrontLayer.set(i,
                    n +
                    sigmoidVal * derSigmoid * dif * learningRate);

            addExpected(frontLayer.get(i).getExpected() * sigmoidVal);

            if (frontLayer.get(i) instanceof ConnectionNode)
                ((ConnectionNode)frontLayer.get(i)).setBias(
                        ((ConnectionNode)frontLayer.get(i)).getBias() +
                                derSigmoid * dif * learningRate);
            else if (frontLayer.get(i) instanceof OutputNode) {
                ((OutputNode)frontLayer.get(i)).setBias(
                        ((OutputNode)frontLayer.get(i)).getBias() +
                                derSigmoid * dif * learningRate);
            }
        }
    }
}