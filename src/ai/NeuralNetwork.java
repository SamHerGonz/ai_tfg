package ai;

import ai.activationFunction.ActivationFunction;
import ai.activationFunction.SigmoidFunction;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public abstract class NeuralNetwork implements Serializable {
    private Node[][] nodes;
    private short sizeTotal;
    private ActivationFunction activationFunction;

    @Serial
    private static final long serialVersionUID = 3877515453607213504L;

    public NeuralNetwork(NeuralNetwork ai) {
        setNodes(Arrays.copyOf(ai.getNodes(), ai.sizeTotal));
        setActivationFunction(ai.activationFunction);
    }

    public NeuralNetwork(int[] shape, float max) {
        this(shape,max, new SigmoidFunction());
    }

    public NeuralNetwork(int[] shape, float max, ActivationFunction activationFunction) {
        setNodes(createNodes(shape, max));
        setActivationFunction(activationFunction);
    }

    public NeuralNetwork(int nInput, int nOutput, int minimization, float max) {
        this(nInput, nOutput, minimization, max, new SigmoidFunction());
    }

    public NeuralNetwork(int nInput, int nOutput, int minimization, float max, ActivationFunction activationFunction) {
        int[] shape = new int[((int)(Math.sqrt(nInput)) - nOutput) / (minimization * 2) + 1];
        for (int i = 0; i < shape.length; i++) {
            shape[i] = (int) Math.pow((int)(Math.sqrt(nInput)) - i * minimization * 2, 2);
        }
        shape[shape.length - 1] = nOutput;
        setNodes(createNodes(shape, max));
        connectNodesDeepMode(minimization * 2, max);
        setActivationFunction(activationFunction);
    }


    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    public Node[][] getNodes() {
        return nodes;
    }

    public short getSizeTotal() {
        return sizeTotal;
    }

    public int getWeightsCount() {
        int n = 0;
        for (int i = 0; i < sizeTotal; i++) {
            n += getWeightsCount(i);
        }
        return n;
    }

    public int getWeightsCount(int layer) {
        int n = 0;
            for (Node value : getNodes()[layer]) {
                if (value instanceof InputNode) {
                    n += ((InputNode) value).getIdNodeFrontLayer().length;
                } else if (value instanceof ConnectionNode) {
                    n += ((ConnectionNode) value).getIdNodeFrontLayer().length;
                }
            }
        return n;
    }

    private void setNodes(Node[][] nodes) {
        this.nodes = nodes;
        sizeTotal = (short) nodes.length;
    }

    private Node[][] createNodes(int[] shape, float max) {
        if (shape.length <= 1) {
            throw new ExceptionInInitializerError("Tiene que tener más de una capa");
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
            nodes[0][i] = new InputNode(max, nodes[1].length);
        }

        // Rellenar el resto de capas excepto la última del array de nodos con ConnectionNodes
        for (int i = 1; i < shape.length - 1; i++) {
            for (int j = 0; j < shape[i]; j++) {
                nodes[i][j] = new ConnectionNode(max, nodes[i + 1].length);
            }
        }

        // Rellenar la última capa del array de nodos con OutputNodes
        for (int i = 0; i < shape[shape.length - 1]; i++) {
            nodes[nodes.length - 1][i] = new OutputNode(max);
        }
        return nodes;
    }

    public void addNode(int layerNumber, float max, boolean connectAll) {
        if (layerNumber < 0 || layerNumber >= sizeTotal) {
            throw new ExceptionInInitializerError("La capa insertada no está en el rango");
        }
        //Crear el nuevo nodo
        Node node;
        if (layerNumber == 0) {
            node = new InputNode(max, nodes[1].length);
        }
        else if (layerNumber != sizeTotal - 1) {
            node = new ConnectionNode(max, nodes[layerNumber + 1].length);
        }
        else {
            node = new OutputNode(max);
        }

        Node[] layer = new Node[nodes[layerNumber].length + 1];
        System.arraycopy(nodes[layerNumber], 0, layer, 0, nodes[layerNumber].length);
        layer[layer.length - 1] = node;
        nodes[layerNumber] = layer;

        if (connectAll) {
            // Conectar los nodos de detrás con el nuevo nodo
            if (layerNumber != 0) {
                for (short i = 0; i < nodes[layerNumber - 1].length; i++) {
                    try {
                        addWeight(layerNumber - 1, i, (short) (nodes[layerNumber].length - 1), max);
                    } catch (Exception _) {}
                }
            }

            // Conectar el nuevo nodo con todos los nodos de delante
            if (layerNumber != sizeTotal - 1) {
                for (short i = 0; i < nodes[layerNumber + 1].length; i++) {
                    try {
                        addWeight(layerNumber, (short) (nodes[layerNumber].length - 1), i, max);
                    } catch (Exception _) {}
                }
            }
        }
        else {
            // Conectar un nodo aleatorio de detrás con el nuevo nodo
            if (layerNumber != 0) {
                try {
                    addWeight(layerNumber - 1, (short)(Math.random() * nodes[layerNumber - 1].length),
                            (short) (nodes[layerNumber].length - 1), max);
                } catch (Exception _) {}
            }

            // Conectar el nuevo nodo con un nodo aleatorio de delante
            if (layerNumber != sizeTotal - 1) {
                try {
                    addWeight(layerNumber, (short) (nodes[layerNumber].length - 1),
                            (short)(Math.random() * nodes[layerNumber + 1].length), max);
                } catch (Exception _) {}
            }
        }
    }

    private void connectNodesDeepMode(int minimization, float max) {
        // Iterar sobre todas las capas excepto la última
        for (int i = 1; i < nodes.length - 1; i++) {
            int sqrtLength = (int) Math.sqrt(nodes[i].length);
            // Iterar sobre cada nodo de cada capa
            if (nodes[i - 1][0] instanceof InputNode) {
                for (short j = 0; j < nodes[i].length; j++) {
                    int posX = (j % sqrtLength) + minimization;
                    int posY = (j / sqrtLength) + minimization;
                    for (int k = -minimization; k <= minimization; k++) {
                        for (int l = -minimization; l <= minimization; l++) {
                            ((InputNode)nodes[i - 1][((posY + k) * sqrtLength) + (posX + l)]).addWeightFront(j,max);
                        }
                    }
                }
            }
            else {
                for (short j = 0; j < nodes[i].length; j++) {
                    int posX = (j % sqrtLength) + minimization;
                    int posY = (j / sqrtLength) + minimization;
                    for (int k = -minimization; k <= minimization; k++) {
                        for (int l = -minimization; l <= minimization; l++) {
                            ((ConnectionNode)nodes[i - 1][((posY + k) * sqrtLength) + (posX + l)]).addWeightFront(j,max);
                        }
                    }
                }
            }
        }

        // Conectar la penúltima capa con todas sus conexiones
        for (int j = 0; j < nodes[nodes.length - 2].length; j++) {
            for (short k = 0; k < nodes[nodes.length - 1].length; k++) {
                ((ConnectionNode)nodes[nodes.length - 2][j]).addWeightFront(k, max);
            }
        }
    }

    public void addWeight(int layer, short firstLayerIndex, short lastLayerIndex, float max) throws Exception {
        weightErrorVerification(layer, firstLayerIndex, lastLayerIndex);
        if (nodes[layer][firstLayerIndex] instanceof InputNode) {
            if (((InputNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayerContains(lastLayerIndex)) {
                throw new Exception("Esta conexión ya existe");
            }
            ((InputNode) nodes[layer][firstLayerIndex]).addWeightFront(lastLayerIndex, max);
        }
        else if (nodes[layer][firstLayerIndex] instanceof ConnectionNode) {
            if (((ConnectionNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayerContains(lastLayerIndex)) {
                throw new Exception("Esta conexión ya existe");
            }
            ((ConnectionNode) nodes[layer][firstLayerIndex]).addWeightFront(lastLayerIndex, max);
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
        weightErrorVerification(layer, firstLayerIndex, lastLayerIndex);
        boolean b = false;
        if (nodes[layer][firstLayerIndex] instanceof InputNode) {
            // Solo si el nodo tiene más de 2 conexiones por delante
            if (((InputNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().length >= 2) {
                // Si la conexión existe
                if (((InputNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayerContains(lastLayerIndex)) {
                    // b es si el nodo del que estamos desconectando tiene 2 o más conexiones
                    for (int i = 0; i < nodes[layer].length; i++) {
                        if (i == lastLayerIndex) {
                            continue;
                        }
                        if (((InputNode) nodes[layer][i]).getIdNodeFrontLayerContains(lastLayerIndex)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        // Eliminar la conexión
                        ((InputNode) nodes[layer][firstLayerIndex]).removeConnection(lastLayerIndex);
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
            if (((ConnectionNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().length >= 2) {
                // Si la conexión existe
                if (((ConnectionNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayerContains(lastLayerIndex)) {
                    // b es si el nodo del que estamos desconectando tiene 2 o más conexiones
                    for (int i = 0; i < nodes[layer].length; i++) {
                        if (i == firstLayerIndex) {
                            continue;
                        }
                        if (((ConnectionNode) nodes[layer][i]).getIdNodeFrontLayerContains(lastLayerIndex)) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        // Eliminar la conexión
                        ((ConnectionNode) nodes[layer][firstLayerIndex]).removeConnection(lastLayerIndex);
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

    private void weightErrorVerification(int layer, int firstLayerIndex, int lastLayerIndex) throws Exception {
        if (layer >= sizeTotal) {
            throw new Exception("No existe la capa número " + layer);
        }
        if (firstLayerIndex > nodes[layer].length) {
            throw new Exception("No existe el nodo número " + firstLayerIndex + " de la capa " + layer);
        }
        if (lastLayerIndex > nodes[layer + 1].length) {
            throw new Exception("No existe el nodo número " + lastLayerIndex + " de la capa " + (layer + 1));
        }
    }

    public float[] getAnswer(float[] data) throws ExceptionInInitializerError {
        return feedforward(data)[getSizeTotal() - 1];
    }


    /**
     *
     * @param data Un array con los datos que se van a usar
     * @return Los valores de cada nodo con la función sigmoide aplicada. El tamaño es el mismo que el de los nodos
     * @throws ExceptionInInitializerError if the data received is not valid
     */
    protected float[][] feedforward(float[] data) throws ExceptionInInitializerError {
        // Restart the values of the nodes
        float[][] values = new float[getSizeTotal()][];
        for (int i = 1; i < values.length; i++) {
            values[i] = new float[getNodes()[i].length];
        }

        if (data.length != getNodes()[0].length) {
            throw new ExceptionInInitializerError("Error en los datos recibidos. No son del mismo tamaño");
        }

        // Pasar valores de todos los datos al primer array de los valores (InputNode)
        values[0] = data;

        // Añadir los valores de la siguiente capa, con la función sigmoide en la primera capa
        for (int j = 0; j < getNodes()[0].length; j++) {
            ((InputNode)getNodes()[0][j]).transferAllData(values[1], values[0][j]);
        }

        // Añadir el bias de cada nodo en la segunda capa, y editar el valor con la función sigmoide
        // El valor de la variable capa es 1 porque estamos editando la segunda capa ahora, al ya haber pasado todos los datos
        if (getNodes()[1][0]instanceof ConnectionNode) {
            setUpConnections(values[1], 1);
        }
        else {
            setUpOutputs(values[1], 1);
        }

        for (short i = 1; i < getSizeTotal() - 1; i++) {
            // Añadir los valores de la siguiente capa
            for (int j = 0; j < getNodes()[i].length; j++) {
                ((ConnectionNode)getNodes()[i][j]).transferAllData(values[i + 1], values[i][j]);
            }

            // Añadir el bias de cada nodo, y editar el valor con la función sigmoide
            if (getNodes()[i + 1][0]instanceof ConnectionNode) {
                setUpConnections(values[i + 1], i + 1);
            }
            // Añadir el bias de cada nodo en la última capa, y editar el valor con la función sigmoide
            else {
                setUpOutputs(values[i + 1], i + 1);
            }
        }

        return values;
    }

    private void setUpConnections(float[] values, int capa) {
        for (int j = 0; j < getNodes()[capa].length; j++) {
            values[j] += ((ConnectionNode) getNodes()[capa][j]).getBias();
            values[j] = (float) getActivationFunction().activateFunction(values[j]);
        }
    }

    private void setUpOutputs(float[] values, int capa) {
        for (int j = 0; j < getNodes()[capa].length; j++) {
            values[j] += ((OutputNode) getNodes()[capa][j]).getBias();
            values[j] = (float) getActivationFunction().activateFunction(values[j]);
        }
    }

    /**
     *
     * @param changes Los cambios aplicados a la red neuronal
     * @param learningRate Un multiplicador para ver cuánto aprende de esta iteración
     */
    public void changeWeightsAndBiases(float[][][] changes, float learningRate) {

        // Usar los datos esperados de changes para cambiar los pesos de la primera capa de la red neuronal
        for (int j = 0; j < nodes[0].length; j++) {
            // Obtener el index del peso. Después, actualiza ese peso
            for (int k = 0; k < ((InputNode) nodes[0][j]).getWeightsFrontLayer().length; k++) {
                int index = ((InputNode) nodes[0][j]).getIdNodeFrontLayer()[k];
                ((InputNode) nodes[0][j]).addToWeightsFrontLayer(k, changes[0][j][index] * learningRate);
            }
        }

        // Usar los datos esperados de changes para cambiar los pesos del resto de capas de la red neuronal
        for (short i = 1; i < sizeTotal - 1; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                // Obtener el index del peso. Después, actualiza ese peso
                for (int k = 0; k < ((ConnectionNode) nodes[i][j]).getWeightsFrontLayer().length; k++) {
                    int index = ((ConnectionNode) nodes[i][j]).getIdNodeFrontLayer()[k];
                    ((ConnectionNode) nodes[i][j]).addToWeightsFrontLayer(k, changes[i][j][index] * learningRate);
                }
            }
        }

        // Usar los datos esperados de changes para cambiar todos los biases de la red neuronal
        for (short i = 1; i < sizeTotal - 1; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                ((ConnectionNode) nodes[i][j]).addBias(changes[changes.length - 1][i - 1][j] * learningRate);
            }
        }

        // Usar los datos esperados de changes para cambiar los biases de la última capa de la red neuronal
        for (int j = 0; j < nodes[sizeTotal - 1].length; j++) {
            ((OutputNode) nodes[sizeTotal - 1][j]).addBias(changes[changes.length - 1][sizeTotal - 2][j] * learningRate);
        }
    }

    public void mutate() {
        mutate((float) 1 / getWeightsCount(), 0.1F);
    }

    public void mutate(float mutationChance, float mutationChange) {
        for (Node[] layer : nodes) {
            for (Node node : layer) {
                if (node instanceof InputNode) {
                    for (int i = 0; i < ((InputNode) node).getWeightsFrontLayer().length; i++) {
                        if (Math.random() <= mutationChance) {
                            ((InputNode) node).addToWeightsFrontLayer(i, (float) (Math.random() * mutationChange * 2 - mutationChange));
                        }
                    }
                } else if (node instanceof ConnectionNode) {
                    for (int i = 0; i < ((ConnectionNode) node).getWeightsFrontLayer().length; i++) {
                        if (Math.random() <= mutationChance) {
                            ((ConnectionNode) node).addToWeightsFrontLayer(i, (float) (Math.random() * mutationChange * 2 - mutationChange));
                        }
                    }
                    if (Math.random() <= mutationChance) {
                        ((ConnectionNode) node).addBias((float) (Math.random() * mutationChange * 2 - mutationChange));
                    }
                } else if (node instanceof OutputNode) {
                    if (Math.random() <= mutationChance) {
                        ((OutputNode) node).addBias((float) (Math.random() * mutationChange * 2 - mutationChange));
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Neural Network: ");

        s.append("\nNúmero de capas: ").append(sizeTotal);

        for (short i = 0; i < sizeTotal - 1; i++) {
            s.append("\n\tCapa ").append(i).append(": ").append(nodes[i].length).append(" nodos, con ").append(getWeightsCount(i)).append(" pesos");
        }

        s.append("\n\tCapa ").append(sizeTotal - 1).append(": ").append(nodes[sizeTotal - 1].length).append(" nodos");

        return getTotalData(s);
    }

    public String printDetailed() {
        StringBuilder s = new StringBuilder("Neural Network: ");

        s.append("\nNúmero de capas: ").append(sizeTotal);

        for (short i = 0; i < sizeTotal; i++) {
            s.append(printDetailed(i));
        }

        return getTotalData(s);
    }

    public String printDetailed(int layer) {
        StringBuilder s = new StringBuilder();
        if (layer != sizeTotal - 1) {
            s.append("\n\tCapa ").append(layer).append(": ").append(nodes[layer].length).append(" nodos, con ").append(getWeightsCount(layer)).append(" pesos");
            for (int j = 0; j < nodes[layer].length; j++) {
                s.append(printDetailed(layer, j));
            }
        }
        else {
            s.append("\n\tCapa ").append(sizeTotal - 1).append(": ").append(nodes[sizeTotal - 1].length).append(" nodos");
            for (int j = 0; j < nodes[sizeTotal - 1].length; j++) {
                s.append(printDetailed(layer, j));
            }
        }
        return s.toString();
    }

    public String printDetailed(int layer, int nodeIndex) {
        return "\n\t\t" + nodes[layer][nodeIndex].toString().replace("\n", "\n\t\t").replace("node:", "node number " + nodeIndex + ":");
    }


    private String getTotalData(StringBuilder s) {
        s.append("\nNúmero total pesos (w): ");
        int n = getWeightsCount();
        s.append(n).append("\nNúmero total de parámetros (w y biases): ");

        for (int i = 1; i < sizeTotal; i++) {
            n += nodes[i].length;
        }
        s.append(n);
        return s.toString();
    }
}
