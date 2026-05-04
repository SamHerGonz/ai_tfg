package ai;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class InputNode extends Node implements Serializable{
    private ArrayList<Integer> idNodeFrontLayer;
    private ArrayList<Float> weightsFrontLayer;
    @Serial
    private static final long serialVersionUID = -7693503413333452026L;


    public InputNode() {
        // Initialize the arrayLists of the connections of the next layer
        setIdNodeFrontLayer(new ArrayList<>());
        setWeightsFrontLayer(new ArrayList<>());
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

    // Convierte un número en un rango entre el minRange al MaxRange en un número del 0 al 1
    public static float setSigmoid(float value, int minRange, int maxRange) {
        return value / (maxRange - minRange);
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

    protected float[] getExpectedDataWeights(float[] difDatas, float value, int sizeNextLayer) {
        float[] ret_values = new float[sizeNextLayer];
        for (int i = 0; i < getWeightsFrontLayer().size(); i++) {
            int index = getIdNodeFrontLayer().get(i);
            ret_values[index] = difDatas[index] * value;
        }
        return ret_values;
    }
}