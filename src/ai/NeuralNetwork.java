package ai;

import java.io.Serial;
import java.io.Serializable;

public class NeuralNetwork implements Serializable {
    private Node[][] nodes;

    // Array que contiene el tamaño total del array nodos. Existe para optimización de código, para no leer la longitud cada vez (relativamente lento)
    private int totalShape;
    // Array que contiene los tamaños de cada capa del array nodos. Existe para optimización de código, para no leer la longitud cada vez (relativamente lento)
    private int[] shapeLayers;

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

        this.shapeLayers = new int[nodes.length];
        this.totalShape = nodes.length;
        for (int i = 0; i < totalShape; i++) {
            this.shapeLayers[i] = nodes[i].length;
        }
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
            for (int k = 0; k < shapeLayers[shapeLayers.length - 1]; k++) {
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
                for (int j = 0; j < shapeLayers[shapeLayers.length - 1]; j++) {
                    marginError += Math.pow(expectedData[i][j] - values[values.length - 1][j], 2) / 2;
                }

                // Imprimir los outputs
                for (int j = 0; j < shapeLayers[shapeLayers.length - 1]; j++) {
                    System.out.println(values[values.length - 1][j]);
                }

                System.out.println("Error de margen: " + marginError);
            }

            // Obtener los cambios de los pesos y biases
            // TODO: Aquí se pasa mucho tiempo haciendo cálculos, pero no mucho, optimizar si es posible
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
        double[] values = run(data, minRange, maxRange)[totalShape - 1];
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
            for (int i = 0; i < shapeLayers[shapeLayers.length - 1]; i++) {
                marginError += Math.pow(expectedData[i] - NeuralMath.setSigmoid(values[values.length - 1][i]), 2) / 2;
            }

            // Imprimir los outputs
            for (int i = 0; i < shapeLayers[shapeLayers.length - 1]; i++) {
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
        double[][] values = new double[totalShape][];
        for (int i = 0; i < totalShape; i++) {
            values[i] = new double[shapeLayers[i]];
        }
        if (data.length != shapeLayers[0]) {
            throw new ExceptionInInitializerError("Error en los datos recibidos. No son del mismo tamaño");
        }

        // Cambiar los valores de todos los InputNodes desde el rango de min a max al rango de 0 a 1
        for (int i = 0; i < shapeLayers[0]; i++) {
            values[0][i] = InputNode.setSigmoid(data[i], minRange, maxRange);
        }

        // TODO: Aquí se pasa mucho tiempo haciendo cálculos, optimizar
        // Añadir los valores de la siguiente capa, con la función sigmoide en la primera capa
        for (int j = 0; j < shapeLayers[0]; j++) {
            values[1] = NeuralMath.addArrays(
                    values[1], ((InputNode)nodes[0][j]).transferAllData(
                            values[0][j], shapeLayers[1]));
        }

        // Añadir el bias de cada nodo en la segunda capa, y editar el valor con la función sigmoide
        // El valor de la variable capa es 1 porque estamos editando la segunda capa ahora, al ya haber pasado todos los datos
        if (nodes[1][0]instanceof ConnectionNode) {
            setUpConnections(values[1], 1);
        }
        else {
            setUpOutputs(values[1], 1);
        }

        // TODO: Aquí se pasa mucho tiempo haciendo cálculos, optimizar
        for (int i = 1; i < totalShape - 1; i++) {
            // Añadir los valores de la siguiente capa
            for (int j = 0; j < shapeLayers[i]; j++) {
                values[i + 1] = NeuralMath.addArrays(
                        values[i + 1], ((ConnectionNode)nodes[i][j]).transferAllData(
                                values[i][j], shapeLayers[i + 1]));
            }

            // Añadir el bias de cada nodo, y editar el valor con la función sigmoide
            if (nodes[i + 1][0]instanceof ConnectionNode) {
                setUpConnections(values[i + 1], i + 1);
            }
            // Añadir el bias de cada nodo en la última capa, y editar el valor con la función sigmoide
            else {
                setUpOutputs(values[i + 1], i + 1);
            }
        }

        return values;
    }

    private void setUpConnections(double[] values, int capa) {
        for (int j = 0; j < shapeLayers[capa]; j++) {
            values[j] += ((ConnectionNode) nodes[capa][j]).getBias();
            values[j] = NeuralMath.setSigmoid(values[j]);
        }
    }

    private void setUpOutputs(double[] values, int capa) {
        for (int j = 0; j < shapeLayers[capa]; j++) {
            values[j] += ((OutputNode) nodes[capa][j]).getBias();
            values[j] = NeuralMath.setSigmoid(values[j]);
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
        if (expectedData.length != shapeLayers[shapeLayers.length - 1]) {
            throw new ExceptionInInitializerError("Error en los datos de aprendizaje recibidos. No es del tamaño correcto");
        }
        if (values.length != totalShape) {
            throw new ExceptionInInitializerError("Error en los valores recibidos, no hay el mismo número de capas en los valores y en los nodos de la red neuronal");
        }
        for (int i = 0; i < totalShape; i++) {
            if (values[i].length != shapeLayers[i]) {
                throw new ExceptionInInitializerError("Error en los valores recibidos, la capa " + i + " no son del mismo tamaño que el de esa capa de nodos");
            }
        }
        // difNodeData es la diferencia que debería de tener el valor de ese nodo para tener un mejor resultado
        // El tamaño de difNodeData tiene el tamaño del array de los nodos excepto de la primera, la de los inputs, ya que no necesitamos computarla
        double[][] difNodeData = new double[totalShape - 1][];
        for (int i = 0; i < totalShape - 1; i++) {
            difNodeData[i] = new double[shapeLayers[i + 1]];
        }

        difNodeData[totalShape - 2] = NeuralMath.subtractArrays(expectedData, values[totalShape - 1]);

        // Calcular los datos esperados de la última capa en expectedDatas
        // Ya hemos calculado el margen de error de este nodo en el targetOutputs
        for (int j = 0; j < shapeLayers[shapeLayers.length - 1]; j++) {
            difNodeData[totalShape - 2][j] *= NeuralMath.setDerivativeSigmoid(values[totalShape - 1][j]);
        }

        // Calcular los datos esperados del resto de capas
        for (int i = totalShape - 2; i > 0; i--) {
            // Ya que la siguiente capa es siempre una instancia de ConnectionNode, no tenemos ningún motivo para verificarlo
            // En cada capa se obtienen el margen de error de la capa anterior para ser usado en el siguiente paso de retropropagación
            for (int j = 0; j < shapeLayers[i]; j++) {
                difNodeData[i - 1][j] = ((ConnectionNode) nodes[i][j]).calculateExpectedDataNode(values[i][j], difNodeData[i]);
            }
        }

        // El tamaño de los valores devueltos por esta función es el mismo que el número de capas en la red neuronal
        // El último no es usado para la última capa, porque no tiene conexiones. Se usa para los cambios de los biases

        double[][][] ret_values = new double[totalShape][][];

        // Usar los datos de los valores de los nodos en difNodeData de la red neuronal para obtener el cambio de cada peso en la primera capa
        ret_values[0] = new double[shapeLayers[0]][];
        for (int i = 0; i < shapeLayers[0]; i++) {
            ret_values[0][i] = ((InputNode) nodes[0][i]).getExpectedDataWeights(difNodeData[0], values[0][i], shapeLayers[1]);
        }

        // Usar los datos de los valores de los nodos en difNodeData de la red neuronal para obtener el cambio de cada peso en el resto de capas, menos la última
        for (int i = 1; i < totalShape - 1; i++) {
            ret_values[i] = new double[shapeLayers[i]][];
            for (int j = 0; j < shapeLayers[i]; j++) {
                ret_values[i][j] = ((ConnectionNode) nodes[i][j]).getExpectedDataWeights(difNodeData[i], values[i][j], shapeLayers[i + 1]);
            }
        }

        // Usar los datos de los valores de los nodos en difNodeData de la red neuronal para obtener el cambio de cada bias de la red neuronal
        // Es guardado en la última posición del array devuelto
        ret_values[totalShape - 1] = difNodeData;
        return ret_values;
    }

    /**
     *
     * @param changes Los cambios aplicados a la red neuronal
     * @param learningRate Un multiplicador para ver cuánto aprende de esta iteración
     */
    public void changeWeightsAndBiases(double[][][] changes, double learningRate) {
        // Usar los datos esperados de changes para cambiar los pesos de la primera capa de la red neuronal
        for (int j = 0; j < shapeLayers[0]; j++) {
            // Obtener el index del peso. Después, actualiza ese peso
            for (int k = 0; k < ((InputNode) nodes[0][j]).getWeightsFrontLayer().size(); k++) {
                int index = ((InputNode) nodes[0][j]).getIdNodeFrontLayer().get(k);
                ((InputNode) nodes[0][j]).addWeightsFrontLayer(k, changes[0][j][index] * learningRate);
            }
        }

        // Usar los datos esperados de changes para cambiar los pesos del resto de capas de la red neuronal
        for (int i = 1; i < totalShape - 1; i++) {
            for (int j = 0; j < shapeLayers[i]; j++) {
                // Obtener el index del peso. Después, actualiza ese peso
                for (int k = 0; k < ((ConnectionNode) nodes[i][j]).getWeightsFrontLayer().size(); k++) {
                    int index = ((ConnectionNode) nodes[i][j]).getIdNodeFrontLayer().get(k);
                    ((ConnectionNode) nodes[i][j]).addWeightsFrontLayer(k, changes[i][j][index] * learningRate);
                }
            }
        }

        // Usar los datos esperados de changes para cambiar todos los biases de la red neuronal
        for (int i = 1; i < totalShape - 1; i++) {
            for (int j = 0; j < shapeLayers[i]; j++) {
                ((ConnectionNode) nodes[i][j]).addBias(changes[changes.length - 1][i - 1][j] * learningRate);
            }
        }

        // Usar los datos esperados de changes para cambiar los biases de la última capa de la red neuronal
        for (int j = 0; j < shapeLayers[shapeLayers.length - 1]; j++) {
            ((OutputNode) nodes[totalShape - 1][j]).addBias(changes[changes.length - 1][totalShape - 2][j] * learningRate);
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

        s.append("\nNúmero de capas: ").append(totalShape);

        for (int i = 0; i < totalShape - 1; i++) {
            s.append("\n\tCapa ").append(i).append(": ").append(nodes[i].length).append(" nodos, con ").append(getWeightsCount(i)).append(" pesos");
        }

        s.append("\n\tCapa ").append(totalShape - 1).append(": ").append(shapeLayers[shapeLayers.length - 1]).append(" nodos");

        s.append("\nNúmero total pesos (w): ");
        int n = getWeightsCount();
        s.append(n).append("\nNúmero total de parámetros (w y biases): ");

        for (int i = 1; i < totalShape; i++) {
            n += nodes[i].length;
        }
        s.append(n);
        return s.toString();
    }
}
