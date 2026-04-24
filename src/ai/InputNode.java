package ai;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class InputNode extends Node implements Serializable{
    private ArrayList<Integer> idNodeFrontLayer;
    private ArrayList<Double> weightsFrontLayer;
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

    public ArrayList<Double> getWeightsFrontLayer() {
        return weightsFrontLayer;
    }

    public void setWeightsFrontLayer(ArrayList<Double> weightsFrontLayer) {
        this.weightsFrontLayer = weightsFrontLayer;
    }

    public void addWeightsFrontLayer(int index, double value) {
        this.getWeightsFrontLayer().set(index, this.getWeightsFrontLayer().get(index) + value);
    }

    // Convierte un número en un rango entre el minRange al MaxRange en un número del 0 al 1
    public static double setSigmoid(double value, int minRange, int maxRange) {
        return value / (maxRange - minRange);
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
}