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

    public int getWeightsCount(int layer) {
        int n = 0;
            for (Node value : getNodes()[layer]) {
                if (value instanceof InputNode) {
                    n += ((InputNode) value).getIdNodeFrontLayer().size();
                } else if (value instanceof ConnectionNode) {
                    n += ((ConnectionNode) value).getIdNodeFrontLayer().size();
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

        // Crear la forma de la red neuronal
        nodes[0] = new InputNode[shape[0]];
        for (int i = 1; i < nodes.length - 1; i++) {
            nodes[i] = new ConnectionNode[shape[i]];
        }
        nodes[shape.length - 1] = new OutputNode[shape[shape.length - 1]];

        // Rellenar la primera capa del array de nodos con InputNodes
        for (int i = 0; i < shape[0]; i++) {
            nodes[0][i] = new InputNode();
        }

        // Rellenar el resto de capas excepto la última del array de nodos con ConnectionNodes
        for (int i = 1; i < shape.length - 1; i++) {
            for (int j = 0; j < shape[i]; j++) {
                nodes[i][j] = new ConnectionNode(max);
            }
        }

        // Rellenar la última capa del array de nodos con OutputNodes
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
        // Iterar sobre todas las capas excepto la última
        for (int i = 0; i < nodes.length - 2; i++) {
            // Iterar sobre cada nodo de cada capa
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

        // Conectar la penúltima capa con todas sus conexiones
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
     * @param layer La capa en la que el nodo está
     * @param firstLayerIndex El nodo de la capa
     * @param lastLayerIndex El otro nodo de la capa
     * @throws Exception Si el peso no existe, no está conectado a nada más o el nodo al que está conectado no tiene ninguna conexión más
     */
    public void removeWeight(int layer, int firstLayerIndex, int lastLayerIndex) throws Exception {
        boolean b = false;
        if (nodes[layer][firstLayerIndex] instanceof InputNode) {
            // Solo si el nodo tiene más de 2 conexiones por delante
            if (((InputNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().size() >= 2) {
                // Si la conexión existe
                if (((InputNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().contains(lastLayerIndex)) {
                    // b es si el nodo del que estamos desconectando tiene 2 o más conexiones
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
                        // Eliminar la conexión
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
            // Solo si el nodo tiene más de 2 conexiones por delante
            if (((ConnectionNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().size() >= 2) {
                // Si la conexión existe
                if (((ConnectionNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().contains(lastLayerIndex)) {
                    // b es si el nodo del que estamos desconectando tiene 2 o más conexiones
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
                        // Eliminar la conexión
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
     * @param data Un array con los datos que se van a usar
     * @param minRange El número mínimo que el input puede tener
     * @param maxRange El número máximo que el input puede tener
     * @param expectedData Un array con el dato esperado de cada minibatch
     * @param learningRate Un multiplicador para ver cuánto aprende de esta iteración
     * @param showMarginError Si quieres que se vea el resultado obtenido
     * @throws ExceptionInInitializerError Si los datos obtenidos se pueden usar, algunas de esas siendo verificaciones internas
     */
    public void runMiniBatch(double[][] data, int minRange, int maxRange, double[][] expectedData, double learningRate, boolean showMarginError) throws ExceptionInInitializerError {
        double[][][] changes = null;
        for (int i = 0; i < data.length; i++) {
            double[][] values = run(data[i], minRange, maxRange);

            if (showMarginError) {
                // Calcular el margen de error de la alimentación para delante respecto al dato esperado
                double marginError = 0;
                for (int j = 0; j < nodes[nodes.length - 1].length; j++) {
                    marginError += Math.pow(expectedData[i][j] - values[values.length - 1][j], 2) / 2;
                }

                // Imprimir los outputs
                for (int j = 0; j < nodes[nodes.length - 1].length; j++) {
                    System.out.println(values[values.length - 1][j]);
                }

                System.out.println("Error de margen: " + marginError);
            }

            // Obtener los cambios de los pesos y biases
            if (learn) {
                if (changes != null) {
                    changes = NeuralMath.addArrays(changes, learn(values, expectedData[i]));
                }
                else {
                    changes = learn(values, expectedData[i]);
                }
            }
        }
        // Hacer los cambios de los datos obtenidos
        changeWeightsAndBiases(changes, learningRate);
    }

    public int getAnswer(double[] data, int minRange, int maxRange, boolean show) throws ExceptionInInitializerError {
        double[] values = run(data, minRange, maxRange)[nodes.length - 1];
        int max = 0;
        for (int i = 1; i < values.length; i++) {
            if (values[i] > values[max]) {
                max = i;
            }
        }
        if (show) {
            System.out.println("\nReal value: " + max);
            for (double value : values) {
                System.out.println(value);
            }
        }
        return max;
    }

    /**
     *
     * @param data Un array con los datos que se van a usar
     * @param minRange El número mínimo que el input puede tener
     * @param maxRange El número máximo que el input puede tener
     * @param expectedData Un array con el dato esperado de cada minibatch
     * @param learningRate Un multiplicador para ver cuánto aprende de esta iteración
     * @param showMarginError Si quieres que se vea el resultado obtenido
     * @throws ExceptionInInitializerError Si los datos obtenidos se pueden usar, algunas de esas siendo verificaciones internas
     */
    public void run(double[] data, int minRange, int maxRange, double[] expectedData, double learningRate, boolean showMarginError) throws ExceptionInInitializerError {
        //
        // Ejecutar la red neuronal, y obtener los valores de todos los nodos durante la alimentación hacia delante
        double[][] values = run(data, minRange, maxRange);

        if (showMarginError) {
            // Calcular el margen de error de la alimentación para delante respecto al dato esperado
            double marginError = 0;
            for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
                marginError += Math.pow(expectedData[i] - NeuralMath.setSigmoid(values[values.length - 1][i]), 2) / 2;
            }

            // Imprimir los outputs
            for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
                System.out.println((values[values.length - 1][i]));
            }

            System.out.println("Error de margen: " + marginError);
        }

        if (learn) {
            // Obtener los cambios de los pesos y biases y hacer los cambios de los datos obtenidos
            changeWeightsAndBiases(learn(values, expectedData), learningRate);
        }
    }

    /**
     *
     * @param data Un array con los datos que se van a usar
     * @param minRange El número mínimo que el input puede tener
     * @param maxRange El número máximo que el input puede tener
     * @return Los valores de cada nodo con la función sigmoide aplicada. El tamaño es el mismo que el de los nodos
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
            setUpConnections(values, 0);
        }
        else {
            setUpOutputs(values);
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
                setUpConnections(values, i);
            }
            // Add the biases of each node in the last layer and edit the value with the sigmoid function
            else {
                setUpOutputs(values);
            }
        }

        return values;
    }

    private void setUpConnections(double[][] values, int i) {
        for (int j = 0; j < nodes[i + 1].length; j++) {
            values[i + 1][j] += ((ConnectionNode) nodes[i + 1][j]).getBias();
            values[i + 1][j] = NeuralMath.setSigmoid(values[i + 1][j]);
        }
    }

    private void setUpOutputs(double[][] values) {
        for (int j = 0; j < nodes[nodes.length - 1].length; j++) {
            values[nodes.length - 1][j] += ((OutputNode) nodes[nodes.length - 1][j]).getBias();
            values[nodes.length - 1][j] = NeuralMath.setSigmoid(values[nodes.length - 1][j]);
        }
    }

    /**
     *
     * @param values todos los valores con la función sigmoide aplicada
     * @param expectedData Los outputs que realmente esperábamos
     * @return Todos los cambios que la red neuronal debería de hacer
     * @throws ExceptionInInitializerError Verificar si expectedData es usable
     */
    private double[][][] learn(double[][] values, double[] expectedData) throws ExceptionInInitializerError {
        // Excepciones. Verificar si los datos son usables
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

        // El tamaño de difDatas es el tamaño del array de los nodos excepto de la primera, la de los inputs, ya que no necesitamos computarla
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
                difDatas[i - 1][j] = ((ConnectionNode) nodes[i][j]).calculateExpectedDataNode(values[i][j], difDatas[i]);
            }
        }

        // The size of the returned values is the same as the number of layers in the neural network.
        // The last one is not used for the last layer, it's used for the biases
        double[][][] ret_values = new double[nodes.length][][];

        // Use the expectedDatas to get the changes of the weights of the ai.NeuralNetwork in the first layer
        ret_values[0] = new double[nodes[0].length][];
        for (int i = 0; i < nodes[0].length; i++) {
            ret_values[0][i] = ((InputNode) nodes[0][i]).getExpectedDataWeights(difDatas[0], values[0][i], nodes[1].length);
        }

        // Use the expectedDatas to get the changes of the weights of the ai.NeuralNetwork in the remaining layers, except in the last one
        for (int i = 1; i < nodes.length - 1; i++) {
            ret_values[i] = new double[nodes[i].length][];
            for (int j = 0; j < nodes[i].length; j++) {
                ret_values[i][j] = ((ConnectionNode) nodes[i][j]).getExpectedDataWeights(difDatas[i], values[i][j], nodes[i + 1].length);
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

        for (int i = 0; i < nodes.length - 1; i++) {
            s.append("\n\tCapa ").append(i).append(": ").append(nodes[i].length).append(" nodos, con ").append(getWeightsCount(i)).append(" pesos");
        }

        s.append("\n\tCapa ").append(nodes.length - 1).append(": ").append(nodes[nodes.length - 1].length).append(" nodos");

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
