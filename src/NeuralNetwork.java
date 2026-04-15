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

    // It theoretically works
    public void removeWeight(int layer, int firstLayerIndex, int lastLayerIndex) {
        if (nodes[layer][firstLayerIndex] instanceof InputNode) {
            if (((InputNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().contains(lastLayerIndex)) {
                int i = ((InputNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().indexOf(lastLayerIndex);
                ((InputNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().remove(i);
                ((InputNode)nodes[layer][firstLayerIndex]).getWeightsFrontLayer().remove(i);
            }
        }
        else if (nodes[layer][firstLayerIndex] instanceof ConnectionNode) {
            if (((ConnectionNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().contains(lastLayerIndex)) {
                int i = ((ConnectionNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().indexOf(lastLayerIndex);
                ((ConnectionNode)nodes[layer][firstLayerIndex]).getIdNodeFrontLayer().remove(i);
                ((ConnectionNode)nodes[layer][firstLayerIndex]).getWeightsFrontLayer().remove(i);
            }
        }
    }

	/**
	 *
	 * @param data the data received
	 * @param minRange the minimum number the data can have
	 * @param maxRange the maximum number the data can have
	 * @param expectedData the array of data expected to appear
	 * @param learningRate a multiplier to see how much it learns from this iteration. It has to be from 0 to 1
	 * @throws Exception if the data received is not valid. There are other internal verifications, but you shouldn't worry about them here
	 */
	public double[] run(double[] data, int minRange, int maxRange, double[] expectedData, double learningRate, boolean showMarginError) throws Exception {
        // Creo un array para tener los valores de cada nodo
        double[][] values = new double[nodes.length][];
        double[] ret_values;
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

        ret_values = values[values.length - 1].clone();
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
            learn(values, expectedData, learningRate);
        }
        return ret_values;
	}

	/**
	 *
	 * @param values all the values of the nodes without the sigmoid function applied
	 * @param expectedData the real array that we expècted
	 * @param learningRate a multiplier to see how much it learns from this iteration. It has to be from 0 to 1
	 * @throws Exception verify if all the values and expectedData are usable
	 */
	public void learn(double[][] values, double[] expectedData, double learningRate) throws Exception {
		// Exceptions. Verify if the data is usable
        /*if (learningRate < 0 || learningRate > 1) {
            throw new Exception("Error en la tasa de aprendizaje. Tiene que ser de un número del 0 al 1");
        }*/
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

        // Use the expectedDatas to change the weights of the NeuralNetwork
        for (int i = 0; i < nodes.length - 1; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                if (nodes[i][j] instanceof InputNode) {
                    for (int k = 0; k < ((InputNode) nodes[i][j]).getWeightsFrontLayer().size(); k++) {
                        int index = ((InputNode) nodes[i][j]).getIdNodeFrontLayer().get(k);
                            ((InputNode) nodes[i][j]).getWeightsFrontLayer().set(k,
                                    ((InputNode)nodes[i][j]).getWeightsFrontLayer().get(k) +
                                            difDatas[i][index] *
                                                    NeuralMath.setSigmoid(values[i][j]) * learningRate);
                    }

                }
                else if (nodes[i][j] instanceof ConnectionNode) {
                    for (int k = 0; k < ((ConnectionNode) nodes[i][j]).getWeightsFrontLayer().size(); k++) {
                        int index = ((ConnectionNode) nodes[i][j]).getIdNodeFrontLayer().get(k);
                        ((ConnectionNode) nodes[i][j]).getWeightsFrontLayer().set(k,
                                ((ConnectionNode)nodes[i][j]).getWeightsFrontLayer().get(k) +
                                        difDatas[i][index] *
                                                NeuralMath.setSigmoid(values[i][j]) * learningRate);
                    }
                }
            }
        }

        // Use the expectedDatas to change the biases of the Neural Network
        for (int i = 1; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                if (nodes[i][j] instanceof ConnectionNode) {
                    ((ConnectionNode)nodes[i][j]).setBias(
                            ((ConnectionNode)nodes[i][j]).getBias() + difDatas[i - 1][j] * learningRate);
                }
                else if (nodes[i][j] instanceof OutputNode) {
                    ((OutputNode)nodes[i][j]).setBias(
                            ((OutputNode)nodes[i][j]).getBias() + difDatas[i - 1][j] * learningRate);

                }
            }
        }
    }
}
