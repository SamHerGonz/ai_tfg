package ai;

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
        // Inicializar los arrayLists de las conexiones de la siguiente capa
        setIdNodeFrontLayer(new ArrayList<>());
        setWeightsFrontLayer(new ArrayList<>());
        // Inicializar el bias en un valor aleatorio del -max al max
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

    public void addWeightsFrontLayer(int index, double value) {
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

    public void addNodeFront(int idNode, double max) {
        // Inicializar el valor de una conexión, con un rango del -max al max
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

    /**
     *
     * @param nodeValue El valor que este nodo tenía en la alimentación hacia delante con la función sigmoide aplicada
     * @param errorsNextLayer El margen de error de la siguiente capa
     * @return El error del valor de este nodo
     */
    public double getExpectedDataNode(double nodeValue, double[] errorsNextLayer) {
        double ret_values = 0;
        for (int i = 0; i < weightsFrontLayer.size(); i++) {
            int id = idNodeFrontLayer.get(i);
            ret_values += weightsFrontLayer.get(i) * errorsNextLayer[id];
        }
        ret_values *= NeuralMath.setDerivativeSigmoid(nodeValue);
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
}