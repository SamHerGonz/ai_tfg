import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class ConnectionNode extends Node implements Serializable {
	private ArrayList<Integer> idNodeFrontLayer;
	private ArrayList<Double> weightsFrontLayer;
	private double bias;
    @Serial
    private static final long serialVersionUID = 8381313154060074511L;
	
	public ConnectionNode(double max) {
        // Initialize the arrayLists of the connections of the next layer
		setIdNodeFrontLayer(new ArrayList<>());
		setWeightsFrontLayer(new ArrayList<>());
        // Set the bias to a random value
        setBias(Math.random() * (max * 2) - max);
	}

	public ArrayList<Integer> getIdNodeFrontLayer() {
		return idNodeFrontLayer;
	}

	public void setIdNodeFrontLayer(ArrayList<Integer> idNodeFrontLayer) {
		this.idNodeFrontLayer = idNodeFrontLayer;
	}

	public ArrayList<Double> getWeightsFrontLayer() {
		return weightsFrontLayer;
	}
	
	public void setWeightsFrontLayer(ArrayList<Double> weightsFrontLayer) {
		this.weightsFrontLayer = weightsFrontLayer;
	}

	public double getBias() {
		return bias;
	}
	
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	
	public void addNodeFront(int idNode, double max) {
        // Initialize the value of a connection
		getIdNodeFrontLayer().add(idNode);
		getWeightsFrontLayer().add(Math.random() * (max * 2) - max);
	}

	// Value added to the next layer of the neural network from this neuron in the forward pass
	public double[] transferAllData(double value, int sizeNextLayer) {
		double[] ret_values = new double[sizeNextLayer];
		for (int i = 0; i < weightsFrontLayer.size(); i++) {
			int id = idNodeFrontLayer.get(i);
			ret_values[id] = value * weightsFrontLayer.get(i);
		}
		return ret_values;
	}

    /**
     *
     * @param nodeValue The value that this node had in the feedforward without the sigmoid function applied to it
     * @param errorsNextLayer The errors of the next layer
     * @return The error of the value of this node
     */
    public double calculateExpectedData(double nodeValue, double[] errorsNextLayer) {
        double ret_values = 0;
        for (int i = 0; i < weightsFrontLayer.size(); i++) {
            int id = idNodeFrontLayer.get(i);
            ret_values += weightsFrontLayer.get(i) * errorsNextLayer[id];
        }
        ret_values *= NeuralMath.setDerivativeSigmoid(nodeValue);
        return ret_values;
    }
}