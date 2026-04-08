package ai.ai8;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class InputNode extends Node implements Serializable{
	private ArrayList<Integer> idNodeFrontLayer;
	private ArrayList<Double> valFrontLayer;
    @Serial
    private static final long serialVersionUID = -7693503413333452026L;


    public InputNode() {
		setIdNodeFrontLayer(new ArrayList<>());
		setValFrontLayer(new ArrayList<>());
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
	
	// Convierte un número en un rango entre el minRange al MaxRange en un número del 0 al 1
	public double setSigmoid(double value, int minRange, int maxRange) {
        return value / (maxRange - minRange) - minRange;
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

    // Aquí se calcula el valor del dato esperado de esta capa (expectedData), o por lo menos la parte que indica esta capa
    public double[] calculateExpectedDataFromOutputNode(double value, double[] outputs) {
        double[] ret_values = new double[outputs.length];
        double derSigmoid = NeuralMath.setDerivativeSigmoid(value);
        for (int i = 0; i < valFrontLayer.size(); i++) {
            int id = idNodeFrontLayer.get(i);
            ret_values[i] = derSigmoid * outputs[id];
        }
        return ret_values;
    }

    public double[] calculateExpectedDataFromConnectionNode(double value, ArrayList<Integer> idWeights, ArrayList<Double> weights, double[] outputs, int sizeLayer) {
        double[] ret_values = new double[sizeLayer];
        double derSigmoid = NeuralMath.setDerivativeSigmoid(value);
        for (int i = 0; i < idWeights.size(); i++) {
            if (idNodeFrontLayer.contains(i)) {
                int id = idWeights.get(i);
                ret_values[i] = derSigmoid;
                for (double output : outputs) {
                    ret_values[i] += weights.get(id) * output;
                }
            }
        }
        return ret_values;
    }

}