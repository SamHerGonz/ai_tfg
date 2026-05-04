package ai;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class ConnectionNode extends Node implements Serializable {
    private ArrayList<Integer> idNodeFrontLayer;
    private ArrayList<Float> weightsFrontLayer;
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

    public ArrayList<Float> getWeightsFrontLayer() {
        return weightsFrontLayer;
    }

    public void setWeightsFrontLayer(ArrayList<Float> weightsFrontLayer) {
        this.weightsFrontLayer = weightsFrontLayer;
    }

    public void addWeightsFrontLayer(int index, float value) {
        this.getWeightsFrontLayer().set(index, this.getWeightsFrontLayer().get(index) + value);
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public void addBias(double bias) {
        setBias(getBias() + bias);
    }

    public void addNodeFront(int idNode, float max) {
        // Initialize the value of a connection
        getIdNodeFrontLayer().add(idNode);
        getWeightsFrontLayer().add((float) (Math.random() * (max * 2) - max));
    }

    // Value added to the next layer of the neural network from this neuron in the forward pass
    public float[] transferAllData(float value, int sizeNextLayer) {
        float[] ret_values = new float[sizeNextLayer];
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
    public float calculateExpectedDataNode(float nodeValue, float[] errorsNextLayer) {
        float ret_values = 0;
        for (int i = 0; i < weightsFrontLayer.size(); i++) {
            int id = idNodeFrontLayer.get(i);
            ret_values += weightsFrontLayer.get(i) * errorsNextLayer[id];
        }
        ret_values *= NeuralMath.setDerivativeSigmoid(nodeValue);
        return ret_values;
    }

    protected float[] getExpectedDataWeights(float[] difDatas, float value, int sizeNextLayer) {
        float[] ret_values = new float[sizeNextLayer];
        for (int i = 0; i < getWeightsFrontLayer().size(); i++) {
            int index = getIdNodeFrontLayer().get(i);
            ret_values[index] = difDatas[index] * value;
        }
        return ret_values;
    }
}