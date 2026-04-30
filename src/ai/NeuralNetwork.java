package ai;

import java.io.Serial;
import java.io.Serializable;

public class NeuralNetwork implements Serializable {
    private Node[][] nodes;
    private boolean learn;
    @Serial
    private static final long serialVersionUID = 3877515453607213504L;

    public NeuralNetwork(int[] shape, double max) throws Exception {
        setNodes(createNodes(shape, max));
        setLearn(true);
        connectNodes(max);
    }

    public NeuralNetwork(int nInput, int nOutput, double max) throws Exception {
        int[] shape = new int[((int)(Math.sqrt(nInput)) - nOutput) / 2 + 1];
        for (int i = 0; i < shape.length; i++) {
            shape[i] = (int) Math.pow((int)(Math.sqrt(nInput)) - i * 2, 2);
        }
        shape[shape.length - 1] = nOutput;
        setNodes(createNodes(shape, max));
        setLearn(true);
        connectNodesDeepMode(max);
    }


    public Node[][] getNodes() {
        return nodes;
    }

    public int getWeightsCount() {
        int n = 0;
        for (Node[] node : nodes) {
            for (Node value : node) {
                if (value instanceof InputNode) {
                    n += ((InputNode) value).getIdNodeFrontLayer().size();
                } else if (value instanceof ConnectionNode) {
                    n += ((ConnectionNode) value).getIdNodeFrontLayer().size();
                }
            }
        }
        return n;
    }
    public void setNodes(Node[][] nodes) {
        this.nodes = nodes;
    }

    public boolean isLearn() {
        return learn;
    }

    public void setLearn(boolean learn) {
        this.learn = learn;
    }

    private Node[][] createNodes(int[] shape, double max) throws Exception {
        if (shape.length <= 1) {
            throw new Exception("Tiene que tener más de una capa");
        }

        Node[][] nodes = new Node[shape.length][];

        // Create the shape of the Neural Network
        nodes[0] = new InputNode[shape[0]];
        for (int i = 1; i < nodes.length - 1; i++) {
            nodes[i] = new ConnectionNode[shape[i]];
        }
        nodes[shape.length - 1] = new OutputNode[shape[shape.length - 1]];

        // Fill the first layer of the nodes array with InputNodes
        for (int i = 0; i < shape[0]; i++) {
            nodes[0][i] = new InputNode();
        }

        // Fill all the layers except the first and last layers with ConnectionNodes
        for (int i = 1; i < shape.length - 1; i++) {
            for (int j = 0; j < shape[i]; j++) {
                nodes[i][j] = new ConnectionNode(max);
            }
        }

        // Fill the last layer of the nodes array with OutputNodes
        for (int i = 0; i < shape[shape.length - 1]; i++) {
            nodes[nodes.length - 1][i] = new OutputNode(max);
        }
        return nodes;
    }

    private void connectNodes(double max) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i][0] instanceof InputNode) {
                for (int j = 0; j < nodes[i].length; j++) {
                    for (int k = 0; k < nodes[i + 1].length; k++) {
                        ((InputNode) nodes[i][j]).addNodeFront(k, max);
                    }
                }
            } else if (nodes[i][0] instanceof ConnectionNode) {
                for (int j = 0; j < nodes[i].length; j++) {
                    for (int k = 0; k < nodes[i + 1].length; k++) {
                        ((ConnectionNode) nodes[i][j]).addNodeFront(k, max);
                    }
                }
            }
        }
    }

    private void connectNodesDeepMode(double max) {
        // Iterate through all layers except the last one
        for (int i = 0; i < nodes.length - 2; i++) {
            // Iterate through each node of each layer
            for (int j = 0; j < nodes[i].length; j++) {
                for (int k = -1; k <= 1; k++) {
                    for (int l = -1; l <= 1; l++) {
                        if (j + k >= 0 && j + l >= 0 && j + k + (l * (int)Math.sqrt(nodes[i + 1].length)) >= 0 && j + k + (l * (int)Math.sqrt(nodes[i + 1].length)) < nodes[i + 1].length) {
                            if (nodes[i][j] instanceof InputNode) {
                                ((InputNode)nodes[i][j]).addNodeFront(j + k + (l * 28), max);
                            }
                            else if (nodes[i][j] instanceof ConnectionNode) {
                                ((ConnectionNode)nodes[i][j]).addNodeFront(j + k + (l * 28), max);
                            }
                        }
                    }
                }
            }
        }
        // Connect the last layer with all the connections
        for (int j = 0; j < nodes[nodes.length - 2].length; j++) {
            for (int k = 0; k < nodes[nodes.length - 1].length; k++) {
                ((ConnectionNode)nodes[nodes.length - 2][j]).addNodeFront(k, max);
            }
        }
    }

    public void addWeight(int layer, int firstLayerIndex, int lastLayerIndex, double max) throws Exception {
        /* if (lastLayerIndex <= nodes[layer + 1].length) {
            throw new Exception("No existe el nodo número " + lastLayerIndex + " de la capa " + (layer + 1));
        }*/
        if (nodes[layer][firstLayerIndex] instanceof InputNode) {
            if (((InputNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().contains(lastLayerIndex)) {
                throw new Exception("Esta conexión ya existe");
            }
            ((InputNode) nodes[layer][firstLayerIndex]).addNodeFront(lastLayerIndex, max);
        }
        else if (nodes[layer][firstLayerIndex] instanceof ConnectionNode) {
            if (((ConnectionNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().contains(lastLayerIndex)) {
                throw new Exception("Esta conexión ya existe");
            }
            ((ConnectionNode) nodes[layer][firstLayerIndex]).addNodeFront(lastLayerIndex, max);
        }
    }

    /**
     *
     * @param layer The layer in which the node is
     * @param firstLayerIndex The node of the layer
     * @param lastLayerIndex The other node of the layer
     * @throws Exception If the weight is already eliminated, or it causes problems, like it isn't connected anywhere in the Network in a direction
     */
    public void removeWeight(int layer, int firstLayerIndex, int lastLayerIndex) throws Exception {
        boolean b = false;
        if (nodes[layer][firstLayerIndex] instanceof InputNode) {
            // Only if the node has more than 2 connections in the front
            if (((InputNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().size() >= 2) {
                // If the connection exists
                if (((InputNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().contains(lastLayerIndex)) {
                    // b is if the node we are disconnecting to has 2 or more connections
                    for (int i = 0; i < nodes[layer].length; i++) {
                        if (i == lastLayerIndex) {
                            continue;
                        }
                        if (((InputNode) nodes[layer][i]).getIdNodeFrontLayer().contains(lastLayerIndex)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        // Remove the connection
                        int i = ((InputNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().indexOf(lastLayerIndex);
                        ((InputNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().remove(i);
                        ((InputNode) nodes[layer][firstLayerIndex]).getWeightsFrontLayer().remove(i);
                    } else {
                        throw new Exception("La neurona de lastLayerIndex que estás eliminando no está conectado por detrás a ninguna neurona");
                    }
                }
                else {
                    throw new Exception("Esta conexión no existe");
                }
            }
            else {
                throw new Exception("Esta neurona no está conectado por delante a ninguna neurona");
            }
        }
        else if (nodes[layer][firstLayerIndex] instanceof ConnectionNode) {
            // Only if the node has more than 2 connections in the front
            if (((ConnectionNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().size() >= 2) {
                // If the connection exists
                if (((ConnectionNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().contains(lastLayerIndex)) {
                    // b is if the node we are disconnecting to has 2 or more connections
                    for (int i = 0; i < nodes[layer].length; i++) {
                        if (i == firstLayerIndex) {
                            continue;
                        }
                        if (((ConnectionNode) nodes[layer][i]).getIdNodeFrontLayer().contains(lastLayerIndex)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        // Remove the connection
                        int i = ((ConnectionNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().indexOf(lastLayerIndex);
                        ((ConnectionNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().remove(i);
                        ((ConnectionNode) nodes[layer][firstLayerIndex]).getWeightsFrontLayer().remove(i);
                    } else {
                        throw new Exception("La neurona de lastLayerIndex que estás eliminando no está conectado por detrás a ninguna neurona");
                    }
                }
                else {
                    throw new Exception("Esta conexión no existe");
                }
            }
            else {
                throw new Exception("Esta neurona solo está conectado por delante a una neurona");
            }
        }
    }

    /**
     *
     * @param data An array with the datas which are going to be used
     * @param minRange the minimum number the data can have
     * @param maxRange the maximum number the data can have
     * @param expectedData the array of data expected to appear
     * @param learningRate a multiplier to see how much it learns from this iteration. It has to be from 0 to 1
     * @param showMarginError if you want the result and the margin error to be shown
     * @throws ExceptionInInitializerError if the data received is not valid. There are other internal verifications, but you shouldn't worry about them here
     */
    public void runMiniBatch(double[][] data, int minRange, int maxRange, double[][] expectedData, double learningRate, boolean showMarginError) throws ExceptionInInitializerError {
        double[][][] changes = null;
        for (int i = 0; i < data.length; i++) {
            double[][] values = run(data[i], minRange, maxRange);
            double[] ret_values = values[values.length - 1].clone();

            for (int j = 0; j < ret_values.length; j++) {
                ret_values[j] = NeuralMath.setSigmoid(ret_values[j]);
            }

            if (showMarginError) {
                // Calculate the margin error of the feedforward (this function) respect to the expected value
                double marginError = 0;
                for (int j = 0; j < nodes[nodes.length - 1].length; j++) {
                    marginError += Math.pow(expectedData[i][j] - NeuralMath.setSigmoid(ret_values[j]), 2) / 2;
                }

                // Print all the output values
                for (int j = 0; j < nodes[nodes.length - 1].length; j++) {
                    System.out.println(ret_values[j]);
                }

                System.out.println("Error de margen: " + marginError);
            }

            // Add the changes for the weights and biases
            if (learn) {
                if (changes != null) {
                    changes = NeuralMath.addArrays(changes, learn(values, expectedData[i]));
                }
                else {
                    changes = learn(values, expectedData[i]);
                }
            }
        }
        // Make all the changes got from all the data collected. Since it sums all the data, the learning rate is divided by the number of data received
        changeWeightsAndBiases(changes, learningRate);
    }

    public int getAnswer(double[] data, int minRange, int maxRange, boolean show) throws ExceptionInInitializerError {
        double[][] values = run(data, minRange, maxRange);
        int max = 0;
        for (int i = 1; i < values[values.length - 1].length; i++) {
            if (values[values.length - 1][i] > values[values.length - 1][max]) {
                max = i;
            }
        }
        if (show) {
            System.out.println("\nReal value: " + max);
            for (int i = 0; i < values[values.length - 1].length; i++) {
                System.out.println(values[values.length - 1][i]);
            }
        }
        return max;
    }

    /**
     *
     * @param data the data received
     * @param minRange the minimum number the data can have
     * @param maxRange the maximum number the data can have
     * @param expectedData the array of data expected to appear
     * @param learningRate a multiplier to see how much it learns from this iteration. It has to be from 0 to 1
     * @param showMarginError if you want the result and the margin error to be shown
     * @throws ExceptionInInitializerError if the data received is not valid. There are other internal verifications, but you shouldn't worry about them here
     */
    public void run(double[] data, int minRange, int maxRange, double[] expectedData, double learningRate, boolean showMarginError) throws ExceptionInInitializerError {
        // Run the Neural network and get the values of all the nodes during the feedforward
        double[][] values = run(data, minRange, maxRange);

        if (showMarginError) {
            // Calculate the margin error of the feedforward (this function) respect to the expected value
            double marginError = 0;
            for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
                marginError += Math.pow(expectedData[i] - NeuralMath.setSigmoid(values[values.length - 1][i]), 2) / 2;
            }

            // Print all the output values
            for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
                System.out.println((values[values.length - 1][i]));
            }

            System.out.println("Error de margen: " + marginError);
        }

        if (learn) {
            double[][][] changes = learn(values, expectedData);
            changeWeightsAndBiases(changes, learningRate);
        }
    }

    /**
     *
     * @param data the data received
     * @param minRange the minimum number the data can have
     * @param maxRange the maximum number the data can have
     * @return The values of each node with the sigmoid function applied
     * @throws ExceptionInInitializerError if the data received is not valid
     */
    private double[][] run(double[] data, int minRange, int maxRange) throws ExceptionInInitializerError {
        // Creo un array para tener los valores de cada nodo
        double[][] values = new double[nodes.length][];
        for (int i = 0; i < values.length; i++) {
            values[i] = new double[nodes[i].length];
        }
        if (data.length != nodes[0].length) {
            throw new ExceptionInInitializerError("Error en los datos recibidos. No son del mismo tamaño");
        }

        // Change the values of all the InputNodes from a range of minRange to maxRange to a range of 0 to 1
        for (int i = 0; i < values[0].length; i++) {
            values[0][i] = InputNode.setSigmoid(data[i], minRange, maxRange);
        }


        // Add the values of the next layer, without the sigmoid function in the first layer
        for (int j = 0; j < nodes[0].length; j++) {
            values[1] = NeuralMath.addArrays(
                    values[1], ((InputNode)nodes[0][j]).transferAllData(
                            values[0][j], nodes[1].length));
        }

        // Add the bias of each node in the second layer, and edit the value with the sigmoid function
        if (nodes[1][0]instanceof ConnectionNode) {
            setUpConnection(values, 0);
        }
        else {
            setUpOutput(values);
        }


        for (int i = 1; i < nodes.length - 1; i++) {
            // Add the values of the next layer
            for (int j = 0; j < nodes[i].length; j++) {
                values[i + 1] = NeuralMath.addArrays(
                        values[i + 1], ((ConnectionNode)nodes[i][j]).transferAllData(
                                values[i][j], nodes[i + 1].length));
            }

            // Add the biases of each node and edit the value with the sigmoid function
            if (nodes[i + 1][0]instanceof ConnectionNode) {
                setUpConnection(values, i);
            }
            // Add the biases of each node in the last layer and edit the value with the sigmoid function
            else {
                setUpOutput(values);
            }
        }

        return values;
    }

    private void setUpConnection(double[][] values, int i) {
        for (int j = 0; j < nodes[i + 1].length; j++) {
            values[i + 1][j] += ((ConnectionNode) nodes[i + 1][j]).getBias();
            values[i + 1][j] = NeuralMath.setSigmoid(values[i + 1][j]);
        }
    }

    private void setUpOutput(double[][] values) {
        for (int j = 0; j < nodes[nodes.length - 1].length; j++) {
            values[nodes.length - 1][j] += ((OutputNode) nodes[nodes.length - 1][j]).getBias();
            values[nodes.length - 1][j] = NeuralMath.setSigmoid(values[nodes.length - 1][j]);
        }
    }

    /**
     *
     * @param values all the values of the nodes with the sigmoid function applied
     * @param expectedData the real array that we expècted
     * @return All the changes the Neural network has to do
     * @throws ExceptionInInitializerError verify if all the values and expectedData are usable
     */
    private double[][][] learn(double[][] values, double[] expectedData) throws ExceptionInInitializerError {
        // Exceptions. Verify if the data is usable
        if (expectedData.length != nodes[nodes.length - 1].length) {
            throw new ExceptionInInitializerError("Error en los datos de aprendizaje recibidos. No es del tamaño correcto");
        }
        if (values.length != nodes.length) {
            throw new ExceptionInInitializerError("Error en los valores recibidos, no hay el mismo número de capas en los valores y en los nodos de la red neuronal");
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i].length != nodes[i].length) {
                throw new ExceptionInInitializerError("Error en los valores recibidos, la capa " + i + " no son del mismo tamaño que el de esa capa de nodos");
            }
        }

        // The size of difDatas is the size of the array of nodes except the input node layer, since we don't need to compute it
        double[][] difDatas = new double[nodes.length - 1][];
        for (int i = 0; i < difDatas.length; i++) {
            difDatas[i] = new double[nodes[i + 1].length];
        }

        double[] lastLayer = new double[values[values.length - 1].length];
        // Copy the values of the last layer of values into lastLayer
        System.arraycopy(values[values.length - 1], 0, lastLayer, 0, lastLayer.length);

        double[] difOutputs = NeuralMath.subtractArrays(expectedData, lastLayer);

        // Calculate the expectedDatas for the last layer
        // We already have the expected data of the marginError in the targetOutputs
        for (int j = 0; j < nodes[nodes.length - 1].length; j++) {
            difDatas[nodes.length - 2][j] = (difOutputs[j] * NeuralMath.setDerivativeSigmoid(values[values.length - 1][j]));
        }

        // Calculate the expectedDatas of all the remaining layers
        for (int i = nodes.length - 2; i > 0; i--) {
            // Since the next layer is always an instance of ai.ConnectionNode, we don't have any reason to verify it.
            // We need to calculate the margin error
            // Each layer I get the values of the previous layer (the layer in which we really are) to be used in the next step of backpropagation, node by node, not all the layer
            for (int j = 0; j < nodes[i].length; j++) {
                difDatas[i - 1][j] = ((ConnectionNode) nodes[i][j]).calculateExpectedData(values[i][j], difDatas[i]);
            }
        }

        // The size of the returned values is the same as the number of layers in the neural network.
        // The last one is not used for the last layer, it's used for the biases
        double[][][] ret_values = new double[nodes.length][][];

        // Use the expectedDatas to get the changes of the weights of the ai.NeuralNetwork in the first layer
        ret_values[0] = new double[nodes[0].length][];
        for (int j = 0; j < nodes[0].length; j++) {
            ret_values[0][j] = new double[nodes[1].length];
            for (int k = 0; k < ((InputNode) nodes[0][j]).getWeightsFrontLayer().size(); k++) {
                int index = ((InputNode) nodes[0][j]).getIdNodeFrontLayer().get(k);
                ret_values[0][j][index] = difDatas[0][index] * values[0][j];
            }
        }

        // Use the expectedDatas to get the changes of the weights of the ai.NeuralNetwork in the remaining layers, except in the last one
        for (int i = 1; i < nodes.length - 1; i++) {
            ret_values[i] = new double[nodes[i].length][];
            for (int j = 0; j < nodes[i].length; j++) {
                ret_values[i][j] = new double[nodes[i + 1].length];
                for (int k = 0; k < ((ConnectionNode) nodes[i][j]).getWeightsFrontLayer().size(); k++) {
                    int index = ((ConnectionNode) nodes[i][j]).getIdNodeFrontLayer().get(k);
                    ret_values[i][j][index] = difDatas[i][index] * values[i][j];
                }
            }
        }

        // Use the expectedDatas to get the changes of the biases of the Neural Network.
        // it's saved in the last position of the array returned
        ret_values[ret_values.length - 1] = new double[nodes.length - 1][];
        for (int i = 1; i < nodes.length; i++) {
            ret_values[ret_values.length - 1][i - 1] = new double[nodes[i].length];
            System.arraycopy(difDatas[i - 1], 0, ret_values[ret_values.length - 1][i - 1], 0, nodes[i].length);
        }
        return ret_values;
    }

    /**
     *
     * @param changes The changes applied to the neural Network
     * @param learningRate a multiplier to see how much it learns from this iteration. It has to be from 0 to 1
     */
    public void changeWeightsAndBiases(double[][][] changes, double learningRate) {

        // Use the expectedDatas to change the weights of the ai.NeuralNetwork
        for (int i = 0; i < nodes.length - 1; i++) {
            if (nodes[i][0] instanceof InputNode) {
                for (int j = 0; j < nodes[i].length; j++) {
                    // Get the index of the weight. Then, it updates that weight
                    for (int k = 0; k < ((InputNode) nodes[i][j]).getWeightsFrontLayer().size(); k++) {
                        int index = ((InputNode) nodes[i][j]).getIdNodeFrontLayer().get(k);
                        ((InputNode) nodes[i][j]).addWeightsFrontLayer(k, changes[i][j][index] * learningRate);
                    }
                }
            }
            else if (nodes[i][0] instanceof ConnectionNode) {
                for (int j = 0; j < nodes[i].length; j++) {
                    // Get the index of the weight. Then, it updates that weight
                    for (int k = 0; k < ((ConnectionNode) nodes[i][j]).getWeightsFrontLayer().size(); k++) {
                        int index = ((ConnectionNode) nodes[i][j]).getIdNodeFrontLayer().get(k);
                        ((ConnectionNode) nodes[i][j]).addWeightsFrontLayer(k, changes[i][j][index] * learningRate);
                    }
                }
            }
        }

        // Use the expectedDatas to change the biases of the Neural Network
        for (int i = 1; i < nodes.length; i++) {
            if (nodes[i][0] instanceof ConnectionNode) {
                for (int j = 0; j < nodes[i].length; j++) {
                    ((ConnectionNode) nodes[i][j]).addBias(changes[changes.length - 1][i - 1][j] * learningRate);
                }
            }
            else if (nodes[i][0] instanceof OutputNode) {
                for (int j = 0; j < nodes[i].length; j++) {
                    ((OutputNode) nodes[i][j]).addBias(changes[changes.length - 1][i - 1][j] * learningRate);
                }
            }
        }
    }

    public void mutate() {
        mutate((double) 1 / getWeightsCount(), 0.1);
    }

    public void mutate(double mutationChance, double mutationChange) {
        for (Node[] layer : nodes) {
            for (Node node : layer) {
                if (node instanceof InputNode) {
                    for (int i = 0; i < ((InputNode) node).getWeightsFrontLayer().size(); i++) {
                        if (Math.random() <= mutationChance) {
                            ((InputNode) node).addWeightsFrontLayer(i, (Math.random() * mutationChange * 2 - mutationChange));
                        }
                    }
                } else if (node instanceof ConnectionNode) {
                    for (int i = 0; i < ((ConnectionNode) node).getWeightsFrontLayer().size(); i++) {
                        if (Math.random() <= mutationChance) {
                            ((ConnectionNode) node).addWeightsFrontLayer(i, (Math.random() * mutationChange * 2 - mutationChange));
                        }
                    }
                    if (Math.random() <= mutationChance) {
                        ((ConnectionNode) node).addBias((Math.random() * mutationChange * 2 - mutationChange));
                    }
                } else if (node instanceof OutputNode) {
                    if (Math.random() <= mutationChance) {
                        ((OutputNode) node).addBias((Math.random() * mutationChange * 2 - mutationChange));
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Neural Network: ");

        s.append("\nNúmero de capas: ").append(nodes.length);

        for (int i = 0; i < nodes.length; i++) {
            s.append("\n\tCapa ").append(i).append(": ").append(nodes[i].length).append(" ");
        }

        s.append("\nNúmero total pesos (w): ");
        int n = getWeightsCount();
        s.append(n).append("\nNúmero total de parámetros (w y biases): ");

        for (int i = 1; i < nodes.length; i++) {
            n += nodes[i].length;
        }
        s.append(n);
        return s.toString();
    }
}
