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
    public double[] transferAllData(double value, int sizeNextLayer) {
        double[] ret_values = new double[sizeNextLayer];
        for (int i = 0; i < valFrontLayer.size(); i++) {
			int id = idNodeFrontLayer.get(i);
            ret_values[id] = value * valFrontLayer.get(id);
        }

        return ret_values;
    }
}