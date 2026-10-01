package ai;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class InputNode extends Node implements Serializable {
    private int[] idNodeFrontLayer;
    private float[] weightsFrontLayer;
    @Serial
    private static final long serialVersionUID = -7693503413333452026L;


    public InputNode(float max, int nConnections) {
        // Inicializar los arrayLists de las conexiones de la siguiente capa
        setIdNodeFrontLayer(new int[nConnections]);
        setWeightsFrontLayer(new float[nConnections]);

        for (int i = 0; i < idNodeFrontLayer.length; i++) {
            idNodeFrontLayer[i] = i;
            weightsFrontLayer[i] = (float) (Math.random() * (max * 2) - max);
        }
    }

    public int[] getIdNodeFrontLayer() {
        return idNodeFrontLayer;
    }

    public void setIdNodeFrontLayer(int[] idNodeFrontLayer) {
        this.idNodeFrontLayer = idNodeFrontLayer;
    }

    public boolean getIdNodeFrontLayerContains(int lastLayerIndex) {
        boolean contains = false;
        for (int i : idNodeFrontLayer) {
            if (i == lastLayerIndex) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    public float[] getWeightsFrontLayer() {
        return weightsFrontLayer;
    }

    public float findWeightsFrontLayer(int index) throws Exception {
        float idNodeIndex = -1;
        for (int i = 0; i < idNodeFrontLayer.length; i++) {
            int j = idNodeFrontLayer[i];
            if (j == index) {
                idNodeIndex = weightsFrontLayer[i];
                break;
            }
        }
        if (idNodeIndex == -1) {
            throw new Exception();
        }
        return idNodeIndex;
    }

    public void setWeightsFrontLayer(float[] weightsFrontLayer) {
        this.weightsFrontLayer = weightsFrontLayer;
    }

    public void addToWeightsFrontLayer(int index, float value) {
        weightsFrontLayer[index] = this.weightsFrontLayer[index] + value;
    }

    public void removeConnection(int lastLayerIndex) {
        int[] newIdNodeFrontLayer = new int[idNodeFrontLayer.length - 1];
        int idNodeIndex = -1;
        for (int i = 0; i < idNodeFrontLayer.length; i++) {
            int j = idNodeFrontLayer[i];
            if (j == lastLayerIndex) {
                idNodeIndex = i;
                break;
            }
        }
        if (idNodeIndex >= 0) {
            System.arraycopy(idNodeFrontLayer, 0, newIdNodeFrontLayer, 0, idNodeIndex);
            if (newIdNodeFrontLayer.length - idNodeIndex >= 0)
                System.arraycopy(idNodeFrontLayer, idNodeIndex + 1, newIdNodeFrontLayer, idNodeIndex, newIdNodeFrontLayer.length - idNodeIndex);
        }
        setIdNodeFrontLayer(newIdNodeFrontLayer);

        float[] newWeightsFrontLayer = new float[weightsFrontLayer.length - 1];
        if (idNodeIndex >= 0) {
            System.arraycopy(weightsFrontLayer, 0, newWeightsFrontLayer, 0, idNodeIndex);
            if (newWeightsFrontLayer.length - idNodeIndex >= 0)
                System.arraycopy(weightsFrontLayer, idNodeIndex + 1, newWeightsFrontLayer, idNodeIndex, newWeightsFrontLayer.length - idNodeIndex);
        }
        setWeightsFrontLayer(newWeightsFrontLayer);
    }

    public void addWeightFront(int idNode, float max) {
        // Inicializar el valor de una conexión, con un rango del -max al max
        int[] newIdNodeFrontLayer = new int[idNodeFrontLayer.length + 1];
        System.arraycopy(idNodeFrontLayer, 0, newIdNodeFrontLayer, 0, idNodeFrontLayer.length);
        newIdNodeFrontLayer[idNodeFrontLayer.length] = idNode;
        setIdNodeFrontLayer(newIdNodeFrontLayer);

        float[] newWeightsFrontLayer = new float[weightsFrontLayer.length + 1];
        System.arraycopy(weightsFrontLayer, 0, newWeightsFrontLayer, 0, weightsFrontLayer.length);
        newWeightsFrontLayer[weightsFrontLayer.length] = (float) (Math.random() * (max * 2) - max);
        setWeightsFrontLayer(newWeightsFrontLayer);
    }

    // Valor añadido a la siguiente capa de la red neuronal desde esta neurona en la alimentación hacia delante
    public void transferAllData(float[] values, float value) {
        for (int i = 0; i < weightsFrontLayer.length; i++) {
            int id = idNodeFrontLayer[i];
            values[id] += value * weightsFrontLayer[i];
        }
    }

    protected float[] getExpectedDataWeights(float[] difDatas, float value, int sizeNextLayer) {
        float[] ret_values = new float[sizeNextLayer];
        for (int i = 0; i < getWeightsFrontLayer().length; i++) {
            int index = getIdNodeFrontLayer()[i];
            ret_values[index] = difDatas[index] * value;
        }
        return ret_values;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Input node:\n\t");
        s.append("Pesos:");
        for (int i = 0; i < getIdNodeFrontLayer().length; i++) {
            s.append("\n\tPeso con dirección al nodo ").append(getIdNodeFrontLayer()[i]).append(": ").append(getWeightsFrontLayer()[i]);
        }
        return s.toString();
    }
}