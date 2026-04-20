import java.io.Serial;
import java.io.Serializable;

public class NeuralNetwork implements Serializable {
	private Node[][] nodes;
    private boolean learn;
    @Serial
    private static final long serialVersionUID = 3877515453607213504L;

    public NeuralNetwork(int[] shape, double max) {
		setNodes(createNodes(shape, max));
		setLearn(true);
        connectNodes(max);
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

    private Node[][] createNodes(int[] shape, double max) {
		Node[][] nodes = new Node[shape.length][];

        // Create the shape of the Neural Network
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node[shape[i]];
        }

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
			for (int j = 0; j < nodes[i].length; j++) {
				if (nodes[i][j] instanceof InputNode) {
					for (int k = 0; k < nodes[i + 1].length; k++) {
						((InputNode)nodes[i][j]).addNodeFront(k, max);
					}
				}
				else if (nodes[i][j] instanceof ConnectionNode) {
					for (int k = 0; k < nodes[i + 1].length; k++) {
                        ((ConnectionNode)nodes[i][j]).addNodeFront(k, max);
					}
				}
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
            if (((InputNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().size() >= 2) {
                if (((InputNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().contains(lastLayerIndex)) {
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
            if (((ConnectionNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().size() >= 2) {
                if (((ConnectionNode) nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().contains(lastLayerIndex)) {
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
     * @throws Exception if the data received is not valid. There are other internal verifications, but you shouldn't worry about them here
     */
    public void runMiniBatch(double[][] data, int minRange, int maxRange, double[][] expectedData, double learningRate, boolean showMarginError) throws Exception {
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

    public int getAnswer(double[] data, int minRange, int maxRange) throws Exception {
        double[][] values = run(data, minRange, maxRange);
        int max = 0;
        for (int j = 1; j < values[values.length - 1].length; j++) {
            if (values[values.length - 1][j] > values[values.length - 1][max]) {
                max = j;
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
     * @throws Exception if the data received is not valid. There are other internal verifications, but you shouldn't worry about them here
     */
    public double[] run(double[] data, int minRange, int maxRange, double[] expectedData, double learningRate, boolean showMarginError) throws Exception {
        // Run the Neural network and get the values of all the nodes during the feedforward
        double[][] values = run(data, minRange, maxRange);
        double[] ret_values = values[values.length - 1].clone();

        for (int i = 0; i < ret_values.length; i++) {
            ret_values[i] = NeuralMath.setSigmoid(ret_values[i]);
        }

        if (showMarginError) {
            // Calculate the margin error of the feedforward (this function) respect to the expected value
            double marginError = 0;
            for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
                marginError += Math.pow(expectedData[i] - NeuralMath.setSigmoid(ret_values[i]), 2) / 2;
            }

            // Print all the output values
            for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
                System.out.println(ret_values[i]);
            }

            System.out.println("Error de margen: " + marginError);
        }

        if (learn) {
            double[][][] changes = learn(values, expectedData);
            changeWeightsAndBiases(changes, learningRate);
        }

        return ret_values;
    }

	/**
	 *
	 * @param data the data received
	 * @param minRange the minimum number the data can have
	 * @param maxRange the maximum number the data can have
     * @return The values of each node without the sigmoid function applied
	 * @throws Exception if the data received is not valid. There are other internal verifications, but you shouldn't worry about them here
	 */
	private double[][] run(double[] data, int minRange, int maxRange) throws Exception {
        // Creo un array para tener los valores de cada nodo
        double[][] values = new double[nodes.length][];
        for (int i = 0; i < values.length; i++) {
            values[i] = new double[nodes[i].length];
        }
		if (data.length != nodes[0].length) {
			throw new Exception("Error en los datos recibidos. No son del mismo tamaño");
		}

		// Make a copy of the array data to values[0]. It has to be like that, because if not it makes reference to the same array ¿¿Why?? I thought it never did that in java
        System.arraycopy(data, 0, values[0], 0, values[0].length);

        // Change the values of all the InputNodes from a range of minRange to maxRange to a range of 0 to 1
        for (int i = 0; i < values[0].length; i++) {
            values[0][i] = InputNode.setSigmoid(data[i], minRange, maxRange);
        }


		for (int i = 0; i < nodes.length - 1; i++) {

			// Add the values of the next layer, without the sigmoid function
			for (int j = 0; j < nodes[i].length; j++) {
				if (nodes[i][j] instanceof InputNode) {
					values[i + 1] = NeuralMath.addArrays(
                            values[i + 1], ((InputNode)nodes[i][j]).transferAllData(
                                    values[i][j], nodes[i + 1].length));
				}
				else if (nodes[i][j] instanceof ConnectionNode){
					values[i + 1] = NeuralMath.addArrays(
                            values[i + 1], ((ConnectionNode)nodes[i][j]).transferAllData(
                                    NeuralMath.setSigmoid(values[i][j]), nodes[i + 1].length));
				}
			}
            // Add the biases of each node
            for (int j = 0; j < nodes[i + 1].length; j++) {
                if (nodes[i + 1][j] instanceof ConnectionNode) {
                    values[i + 1][j] += ((ConnectionNode) nodes[i + 1][j]).getBias();
                }
                else if (nodes[i + 1][j] instanceof OutputNode) {
                    values[i + 1][j] += ((OutputNode) nodes[i + 1][j]).getBias();
                }
            }
		}

        return values;
	}

	/**
     *
     * @param values all the values of the nodes without the sigmoid function applied
     * @param expectedData the real array that we expècted
     * @return All the changes the Neural network has to do
     * @throws Exception verify if all the values and expectedData are usable
     */
	private double[][][] learn(double[][] values, double[] expectedData) throws Exception {
		// Exceptions. Verify if the data is usable
        if (expectedData.length != nodes[nodes.length - 1].length) {
            throw new Exception("Error en los datos de aprendizaje recibidos. No es del tamaño correcto");
        }
        if (values.length != nodes.length) {
            throw new Exception("Error en los valores recibidos, no hay el mismo número de capas en los valores y en los nodos de la red neuronal");
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i].length != nodes[i].length) {
                throw new Exception("Error en los valores recibidos, la capa " + i + " no son del mismo tamaño que el de esa capa de nodos");
            }
        }

        double[][] difDatas = new double[nodes.length - 1][];
        for (int i = 0; i < difDatas.length; i++) {
            difDatas[i] = new double[nodes[i + 1].length];
        }

        // Foreach en el values[values.length - 1]
        double[] lastLayer = new double[values[values.length - 1].length];
        for (int i = 0; i < lastLayer.length; i++) {
            lastLayer[i] = NeuralMath.setSigmoid(values[values.length - 1][i]);
        }

        double[] difOutputs = NeuralMath.subtractArrays(expectedData, lastLayer);

        // Calculate the expectedDatas for the last layer
        // We already have the expected data of the marginError in the targetOutputs
        for (int j = 0; j < nodes[nodes.length - 1].length; j++) {
            difDatas[nodes.length - 2][j] = (difOutputs[j] * NeuralMath.setDerivativeSigmoid(values[values.length - 1][j]));
        }

        // Calculate the expectedDatas of all the remaining layers
        for (int i = nodes.length - 2; i > 0; i--) {
            // Since the next layer is always an instance of ConnectionNode, we don't have any reason to verify it.
            // We need to calculate the margin error
            // Each layer I get the values of the previous layer (the layer in which we really are) to be used in the next step of backpropagation, node by node, not all the layer
            for (int j = 0; j < nodes[i].length; j++) {
                difDatas[i - 1][j] = ((ConnectionNode) nodes[i][j]).calculateExpectedData(values[i][j], difDatas[i]);
            }
        }

        double[][][] ret_values = new double[nodes.length][][];
        // Use the expectedDatas to get the changes of the weights of the NeuralNetwork
        for (int i = 0; i < nodes.length - 1; i++) {
            ret_values[i] = new double[nodes[i].length][];
            for (int j = 0; j < nodes[i].length; j++) {
                ret_values[i][j] = new double[nodes[i + 1].length];
                if (nodes[i][j] instanceof InputNode) {
                    for (int k = 0; k < ((InputNode) nodes[i][j]).getWeightsFrontLayer().size(); k++) {
                        int index = ((InputNode) nodes[i][j]).getIdNodeFrontLayer().get(k);
                        ret_values[i][j][index] = difDatas[i][index] * NeuralMath.setSigmoid(values[i][j]);
                    }

                }
                else if (nodes[i][j] instanceof ConnectionNode) {
                    for (int k = 0; k < ((ConnectionNode) nodes[i][j]).getWeightsFrontLayer().size(); k++) {
                        int index = ((ConnectionNode) nodes[i][j]).getIdNodeFrontLayer().get(k);
                        ret_values[i][j][index] = difDatas[i][index] * NeuralMath.setSigmoid(values[i][j]);
                    }
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

        // Use the expectedDatas to change the weights of the NeuralNetwork
        for (int i = 0; i < nodes.length - 1; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                // Get the index of the weight. Then, it updates that weight
                if (nodes[i][j] instanceof InputNode) {
                    for (int k = 0; k < ((InputNode) nodes[i][j]).getWeightsFrontLayer().size(); k++) {
                        int index = ((InputNode) nodes[i][j]).getIdNodeFrontLayer().get(k);
                        ((InputNode) nodes[i][j]).getWeightsFrontLayer().set(k,
                                ((InputNode)nodes[i][j]).getWeightsFrontLayer().get(k) +
                                        changes[i][j][index] * learningRate);
                    }

                }
                else if (nodes[i][j] instanceof ConnectionNode) {
                    for (int k = 0; k < ((ConnectionNode) nodes[i][j]).getWeightsFrontLayer().size(); k++) {
                        int index = ((ConnectionNode) nodes[i][j]).getIdNodeFrontLayer().get(k);
                        ((ConnectionNode) nodes[i][j]).getWeightsFrontLayer().set(k,
                                ((ConnectionNode)nodes[i][j]).getWeightsFrontLayer().get(k) +
                                        changes[i][j][index] * learningRate);
                    }
                }
            }
        }

        // Use the expectedDatas to change the biases of the Neural Network
        for (int i = 1; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                if (nodes[i][j] instanceof ConnectionNode) {
                    ((ConnectionNode)nodes[i][j]).setBias(
                            ((ConnectionNode)nodes[i][j]).getBias() + changes[changes.length - 1][i - 1][j] * learningRate);
                }
                else if (nodes[i][j] instanceof OutputNode) {
                    ((OutputNode)nodes[i][j]).setBias(
                            ((OutputNode)nodes[i][j]).getBias() + changes[changes.length - 1][i - 1][j] * learningRate);

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
                            ((InputNode) node).getWeightsFrontLayer().set(i, ((InputNode) node).getWeightsFrontLayer().get(i) + (Math.random() * mutationChange * 2 - mutationChange));
                        }
                    }
                } else if (node instanceof ConnectionNode) {
                    for (int i = 0; i < ((ConnectionNode) node).getWeightsFrontLayer().size(); i++) {
                        if (Math.random() <= mutationChance) {
                            ((ConnectionNode) node).getWeightsFrontLayer().set(i, ((ConnectionNode) node).getWeightsFrontLayer().get(i) + (Math.random() * mutationChange * 2 - mutationChange));
                        }
                    }
                    if (Math.random() <= mutationChance) {
                        ((ConnectionNode) node).setBias(((ConnectionNode) node).getBias() + (Math.random() * mutationChange * 2 - mutationChange));
                    }
                } else if (node instanceof OutputNode) {
                    if (Math.random() <= mutationChance) {
                        ((OutputNode) node).setBias(((OutputNode) node).getBias() + (Math.random() * mutationChange * 2 - mutationChange));
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
        s.append(n).append("\n Número total de parámetros (w y biases): ");

        for (int i = 1; i < nodes.length; i++) {
            n += nodes[i].length;
        }
        s.append(n);
        return s.toString();
    }
}
