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

    /**
     *
     * @param value The value that the node had during the foward pass
     * @param outputs The values that the next layer of nodes had
     * @return
     */
    // Aquí se calcula el valor del dato esperado de esta capa (expectedData), o por lo menos la parte que indica esta capa
    public double[] calculateExpectedDataFromOutputNode(double value, double[] outputs) {
		double[] ret_values = new double[outputs.length];
        double derSigmoid = NeuralMath.setDerivativeSigmoid(value);
        for (int i = 0; i < ret_values.length; i++) {
			int id = idNodeFrontLayer.get(i);
            ret_values[i] = derSigmoid * outputs[id];
		}
		return ret_values;
    }

    public double[] calculateExpectedDataFromConnectionNode(double value, int[] idWeights, double[] weights, double[] outputs) {
        double[] ret_values = new double[outputs.length];
        double derSigmoid = NeuralMath.setDerivativeSigmoid(value);
        for (int i = 0; i < ret_values.length; i++) {
            int id = idWeights[i];
            ret_values[i] = weights[id] * outputs[id] * derSigmoid;
        }
        return ret_values;
    }
}