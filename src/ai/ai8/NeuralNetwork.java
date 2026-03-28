package ai.ai8;

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

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node[shape[i]];
        }

        for (int i = 0; i < shape[0]; i++) {
			nodes[0][i] = new InputNode();
		}
		
		for (int i = 1; i < shape.length - 1; i++) {
			for (int j = 0; j < shape[i]; j++) {
				nodes[i][j] = new ConnectionNode(max);
			}
		}
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

    // Funciona

	/**
	 *
	 * @param data the data received
	 * @param minRange the minimum number the data can have
	 * @param maxRange the maximum number the data can have
	 * @param expectedData the array of data expected to appear
	 * @param learningRate a multiplier to see how much it learns from this iteration. It has to be from 0 to 1
	 * @throws Exception
	 */
	public void run(double[] data, int minRange, int maxRange, double[] expectedData, double learningRate, boolean showMarginError) throws Exception {
        // Creo un array para tener los valores de cada nodo
        double[][] values = new double[nodes.length][];
        for (int i = 0; i < values.length; i++) {
            values[i] = new double[nodes[i].length];
        }
		if (data.length != nodes[0].length) {
			throw new Exception("Error en los datos recibidos. No son del mismo tamaño");
		}

		// Make a copy of the array data to values[0]. It has to be like that, because if not it makes reference to the same array ¿¿Why??
        System.arraycopy(data, 0, values[0], 0, values[0].length);

		for (int i = 0; i < nodes.length - 1; i++) {

			// Add the values of the next layer, without the sigmoid function
			for (int j = 0; j < nodes[i].length; j++) {
				if (nodes[i][j] instanceof InputNode) {
					values[i + 1] = NeuralMath.addArrays(
                            values[i + 1],
                            ((InputNode)nodes[i][j]).transferAllData(
                                    ((InputNode)nodes[i][j]).setSigmoid(values[i][j], minRange, maxRange)));
				}
				else if (nodes[i][j] instanceof ConnectionNode){
					values[i + 1] = NeuralMath.addArrays(
                            values[i + 1], ((ConnectionNode)nodes[i][j]).transferAllData(
                                    NeuralMath.setSigmoid(values[i][j] + ((ConnectionNode) nodes[i][j]).getBias())));
				}
			}
		}

		// Edit the values of the last layer with the sigmoid function
        for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
            values[values.length - 1][i] += ((OutputNode) nodes[nodes.length - 1][i]).getBias();
            values[values.length - 1][i] = NeuralMath.setSigmoid(values[values.length - 1][i]);
        }



        if (showMarginError) {
            // Print all the output values
            for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
                System.out.println(values[values.length - 1][i]);
            }

            double marginError = 0;
            for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
                marginError += Math.pow(expectedData[i] - values[values.length - 1][i], 2) / 2;
            }
            System.out.println("Error de margen: " + marginError);
        }
        if (learn) {
            learn(values, expectedData, learningRate);
        }

	}
    // TODO: arreglar

	/**
	 *
	 * @param values all the values of the nodes without the sigmoid function applied
	 * @param expectedData the real array that we expècted
	 * @param learningRate a multiplier to see how much it learns from this iteration. It has to be from 0 to 1
	 * @throws Exception
	 */
	public void learn(double[][] values, double[] expectedData, double learningRate) throws Exception {
		// Exceptions. Verify if the data is usable
        if (learningRate < 0 || learningRate > 1) {
            throw new Exception("Error en la tasa de aprendizaje. Tiene que ser de un número del 0 al 1");
        }
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

        double[][][] newWeightValues = getShapeWeightDoubles();

        for (int i = nodes.length - 2; i >= 0; i-- ) {
            for (int j = 0; j < nodes[i].length; j++) {
                if (nodes[i][j] instanceof InputNode) {
                    newWeightValues[i][j] = ((InputNode)nodes[i][j]).changeWeights(values[i][j], values[i + 1], expectedData);
                }
                else if (nodes[i][j] instanceof ConnectionNode){
                    newWeightValues[i][j] = ((ConnectionNode)nodes[i][j]).changeWeights(values[i][j], values[i + 1], expectedData);
				}
            }

            expectedData = new double[nodes[i].length];

            for (int j = 0; j < nodes[i].length; j++) {
				for (int k = 0; k < newWeightValues[i][j].length; k++) {
					expectedData[j] += newWeightValues[i][j][k] / values[i][j];
				}
            }
        }

		for (int i = 0; i < newWeightValues.length; i++) {
			for (int j = 0; j < newWeightValues[i].length; j++) {
				for (int k = 0; k < newWeightValues[i][j].length; k++) {
					if (nodes[i][j] instanceof InputNode) {
						((InputNode) nodes[i][j]).getValFrontLayer().set(k,
                                ((InputNode)nodes[i][j]).getValFrontLayer().get(k) - newWeightValues[i][j][k] * learningRate);
					}
					else if (nodes[i][j] instanceof ConnectionNode) {
						((ConnectionNode)nodes[i][j]).getValFrontLayer().set(k,
                                ((ConnectionNode)nodes[i][j]).getValFrontLayer().get(k) - newWeightValues[i][j][k] * learningRate);
					}

				}
			}
		}
    }

	private double[][][] getShapeWeightDoubles() {
		double[][][] weightValues = new double[nodes.length - 1][][];

        for (int i = 0; i < weightValues.length; i++) {
            weightValues[i] = new double[nodes[i].length][];
        }
		return weightValues;
	}
}
