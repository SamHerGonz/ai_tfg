package ai.ai8;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class ConnectionNode extends Node implements Serializable {
	private ArrayList<Integer> idNodeFrontLayer;
	private ArrayList<Double> valFrontLayer;
	private double bias;
    @Serial
    private static final long serialVersionUID = 8381313154060074511L;
	
	public ConnectionNode(double max) {
		setIdNodeFrontLayer(new ArrayList<>());
		setValFrontLayer(new ArrayList<>());
        setBias(Math.random() * (max * 2) - max);
	}

	public ArrayList<Integer> getIdNodeFrontLayer() {
		return idNodeFrontLayer;
	}

	public void setIdNodeFrontLayer(ArrayList<Integer> idNodeFrontLayer) {
		this.idNodeFrontLayer = idNodeFrontLayer;
	}

	public ArrayList<Double> getValFrontLayer() {
		return valFrontLayer;
	}
	
	public void setValFrontLayer(ArrayList<Double> valFrontLayer) {
		this.valFrontLayer = valFrontLayer;
	}

	public double getBias() {
		return bias;
	}
	
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	
	public void addNodeFront(int idNode, double max) {
		getIdNodeFrontLayer().add(idNode);
		getValFrontLayer().add(Math.random() * (max * 2) - max);
	}

	// Value added to the next layer of the neural network from this neuron in the forward pass
	public double[] transferAllData(double value) {
		double[] ret_values = new double[valFrontLayer.size()];
		for (int i = 0; i < ret_values.length; i++) {
			int id = idNodeFrontLayer.get(i);
			ret_values[id] = value * valFrontLayer.get(i);
		}
		return ret_values;
	}

    // Aquí se edita el valor de los weights, biases y se calcula el peso del nodo siguiente
    public double[] changeWeights(double value, double[] values, double[] marginErrors, double learningRate) {
		double[] ret_values = new double[values.length];
        for (int i = 0; i < valFrontLayer.size(); i++) {
			int id = idNodeFrontLayer.get(i);
            double dif = marginErrors[id] - value;
            double derSigmoid = NeuralMath.setDerivativeSigmoid(values[id]);

            ret_values[i] = value * derSigmoid * dif * learningRate;
		}
		return ret_values;
    }
}