package ai;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class InputNode extends Node implements Serializable {
    private ArrayList<Integer> idNodeFrontLayer;
    private ArrayList<Double> weightsFrontLayer;
    @Serial
    private static final long serialVersionUID = -7693503413333452026L;


    public InputNode() {
        // Inicializar los arrayLists de las conexiones de la siguiente capa
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

    public void addNodeFront(int idNode, double max) {
        // Inicializar el valor de un peso
        getIdNodeFrontLayer().add(idNode);
        getWeightsFrontLayer().add(Math.random() * (max * 2) - max);
    }

    // Valor añadido a la siguiente capa de la red neuronal desde esta neurona en la alimentación hacia delante
    public double[] transferAllData(double value, int sizeNextLayer) {
        double[] ret_values = new double[sizeNextLayer];
        for (int i = 0; i < weightsFrontLayer.size(); i++) {
            int id = idNodeFrontLayer.get(i);
            ret_values[id] = value * weightsFrontLayer.get(i);
        }

        return ret_values;
    }

    protected double[] getExpectedDataWeights(double[] difDatas, double value, int sizeNextLayer) {
        double[] ret_values = new double[sizeNextLayer];
        for (int i = 0; i < getWeightsFrontLayer().size(); i++) {
            int index = getIdNodeFrontLayer().get(i);
            ret_values[index] = difDatas[index] * value;
        }
        return ret_values;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Input node:\n\t");
        s.append("Pesos:");
        for (int i = 0; i < getIdNodeFrontLayer().size(); i++) {
            s.append("\n\tPeso con dirección al nodo ").append(getIdNodeFrontLayer().get(i)).append(": ").append(getWeightsFrontLayer().get(i));
        }
        return s.toString();
    }
}