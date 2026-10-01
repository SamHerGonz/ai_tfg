package ai;

import ai.activationFunction.*;

import java.util.List;

public class SupervisedLearningNeuralNetwork extends NeuralNetwork {
    public SupervisedLearningNeuralNetwork(NeuralNetwork ai) {
        super(ai);
    }

    public SupervisedLearningNeuralNetwork(int[] shape, float max) throws Exception {
        super(shape, max);
    }

    public SupervisedLearningNeuralNetwork(int[] shape, float max, ActivationFunction activationFunction) throws Exception {
        super(shape, max, activationFunction);
    }

    public SupervisedLearningNeuralNetwork(int nInput, int nOutput, int minimization, float max) throws Exception {
        super(nInput, nOutput, minimization, max);
    }

    public SupervisedLearningNeuralNetwork(int nInput, int nOutput, int minimization, float max, ActivationFunction activationFunction) throws Exception {
        super(nInput, nOutput, minimization, max, activationFunction);
    }

    public void train(List<float[]> trainingDatas, List<float[]> expectedDatas, float learningRate) throws ExceptionInInitializerError {
        for (int j = 0; j < trainingDatas.size(); j++) {
            run(trainingDatas.get(j), expectedDatas.get(j), learningRate);
        }
    }

    public void train(List<float[]> trainingDatas, List<float[]> expectedDatas, int miniBatchSize, float learningRate) throws ExceptionInInitializerError {
        float[][] miniBatchData;
        float[][] miniBatchExpected;
        for (int j = 0; j < trainingDatas.size(); j += miniBatchSize) {
            if (j + miniBatchSize > trainingDatas.size()) {
                miniBatchData = new float[trainingDatas.size() - j][];
                miniBatchExpected = new float[trainingDatas.size() - j][];
            }
            else {
                miniBatchData = new float[miniBatchSize][];
                miniBatchExpected = new float[miniBatchSize][];
            }
            for (int k = 0; k < miniBatchData.length; k++) {
                miniBatchData[k] = trainingDatas.get(j + k);
                miniBatchExpected[k] = expectedDatas.get(j + k);
            }

            runMiniBatch(miniBatchData, miniBatchExpected, learningRate);
        }
    }

    public void run(float[] data, float[] expectedData, float learningRate) throws ExceptionInInitializerError {
        float[][]values = super.feedforward(data);
        changeWeightsAndBiases(backpropagation(values, expectedData), learningRate);
    }

    /**
     *
     * @param data Un array con los datos que se van a usar
     * @param expectedData Un array con el dato esperado de cada minibatch
     * @param learningRate Un multiplicador para ver cuánto aprende de esta iteración
     * @throws ExceptionInInitializerError Si los datos obtenidos se pueden usar, algunas de esas siendo verificaciones internas
     */
    public void runMiniBatch(float[][] data, float[][] expectedData, float learningRate) throws ExceptionInInitializerError {
        float[][][] changes = null;
        for (int i = 0; i < data.length; i++) {
            float[][] values = super.feedforward(data[i]);

            // Obtener los cambios de los pesos y biases
            if (changes != null) {
                changes = NeuralMath.addArrays(changes, backpropagation(values, expectedData[i]));
            }
            else {
                changes = backpropagation(values, expectedData[i]);
            }
        }
        // Hacer los cambios de los datos obtenidos
        changeWeightsAndBiases(changes, learningRate);
    }

    /**
     *
     * @param values todos los valores con la función sigmoide aplicada
     * @param expectedData Los outputs que realmente esperábamos
     * @return Todos los cambios que la red neuronal debería de hacer
     * @throws ExceptionInInitializerError Verificar si expectedData es usable
     */
    private float[][][] backpropagation(float[][] values, float[] expectedData) {
        // difNodeData es la diferencia que debería de tener el valor de ese nodo para tener un mejor resultado
        // El tamaño de difNodeData tiene el tamaño del array de los nodos excepto de la primera, la de los inputs, ya que no necesitamos computarla
        float[][] difNodeData = new float[getSizeTotal() - 1][];
        for (int i = 0; i < difNodeData.length; i++) {
            difNodeData[i] = new float[getNodes()[i + 1].length];
        }

        difNodeData[getSizeTotal() - 2] = NeuralMath.subtractArrays(expectedData, values[getSizeTotal() - 1]);

        // Calcular los datos esperados del resto de capas
        for (short i = (short) (getSizeTotal() - 2); i > 0; i--) {
            // Ya que la siguiente capa es siempre una instancia de ConnectionNode, no tenemos ningún motivo para verificarlo
            // En cada capa se obtienen el error de la capa anterior para ser usado en el siguiente paso de retropropagación
            for (int j = 0; j < getNodes()[i].length; j++) {
                difNodeData[i - 1][j] = ((ConnectionNode) getNodes()[i][j]).getExpectedDataNode(difNodeData[i]);
                difNodeData[i - 1][j] *= (float) getActivationFunction().derActivateFunction(values[i][j]);
            }
        }

        // El tamaño de los valores devueltos por esta función es el mismo que el número de capas en la red neuronal
        // El último no es usado para la última capa, porque no tiene conexiones. Se usa para los cambios de los biases
        float[][][] ret_values = new float[getSizeTotal()][][];

        // Usar los datos de los valores de los nodos en difNodeData de la red neuronal para obtener el cambio de cada peso en la primera capa
        ret_values[0] = new float[getNodes()[0].length][];
        for (int i = 0; i < getNodes()[0].length; i++) {
            ret_values[0][i] = ((InputNode) getNodes()[0][i]).getExpectedDataWeights(difNodeData[0], values[0][i], getNodes()[1].length);
        }

        // Usar los datos de los valores de los nodos en difNodeData de la red neuronal para obtener el cambio de cada peso en el resto de capas, menos la última (al descomentar el código de debajo, tienen que ser las 2 últimas)
        for (short i = 1; i < getSizeTotal() - 1; i++) {
            ret_values[i] = new float[getNodes()[i].length][];
            for (int j = 0; j < getNodes()[i].length; j++) {
                ret_values[i][j] = ((ConnectionNode) getNodes()[i][j]).getExpectedDataWeights(difNodeData[i], values[i][j], getNodes()[i + 1].length);
            }
        }

        // Usar los datos de los valores de los nodos en difNodeData de la red neuronal para obtener el cambio de cada bias de la red neuronal
        // Es guardado en la última posición del array devuelto
        ret_values[getSizeTotal() - 1] = difNodeData;
        return ret_values;
    }
}
