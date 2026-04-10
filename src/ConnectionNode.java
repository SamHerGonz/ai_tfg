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
	public double[] transferAllData(double value, int sizeNextLayer) {
		double[] ret_values = new double[sizeNextLayer];
		for (int i = 0; i < valFrontLayer.size(); i++) {
			int id = idNodeFrontLayer.get(i);
			ret_values[id] = value * valFrontLayer.get(id);
		}
		return ret_values;
	}

    /**
     *
     * @param value
     * @param outputsNextLayer The outputs of the next layer
     * @return
     */
    public double calculateExpectedData(double value, double[] outputsNextLayer) {
        double ret_values = NeuralMath.setDerivativeSigmoid(value);
        for (int i = 0; i < outputsNextLayer.length; i++) {
            if (idNodeFrontLayer.contains(i)) {
                int id = idNodeFrontLayer.get(i);
                for (double output : outputsNextLayer) {
                    ret_values += valFrontLayer.get(id) * output;
                }
            }
        }
        return ret_values;
    }
}