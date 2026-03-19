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
	 * @param data the data recieved
	 * @param minRange the minimum number the data can have
	 * @param maxRange the maximum number the data can have
	 * @param expectedData the number expected to appear
	 * @param learningRate a multiplier to see how much it learns from this iteration. It has to be from 0 to 1
	 * @throws Exception
	 */
	public void run(double[] data, int minRange, int maxRange, int expectedData, double learningRate) throws Exception {
        // Creo un array para tener los valores de cada nodo
        double[][] values = new double[nodes.length][];
        for (int i = 0; i < values.length; i++) {
            values[i] = new double[nodes[i].length];
        }
		if (data.length != nodes[0].length) {
			throw new Exception("Error en los datos recibidos");
		}

		// Make a copy of the array data to values[0]. It has to be like that, because if not it makes reference to the same array ¿¿Why??
        System.arraycopy(data, 0, values[0], 0, values[0].length);

		for (int i = 0; i < nodes.length - 1; i++) {
			// Edit the values with the sigmoid function
			for (int j = 0; j < nodes[i].length; j++) {
				if (nodes[i][j] instanceof InputNode) {
					values[i][j] = ((InputNode)nodes[i][j]).setValueSigmoid(values[i][j], minRange, maxRange);
				}
				else if (nodes[i][j] instanceof ConnectionNode){
					values[i][j] = NeuralMath.setSigmoid(values[i][j] + ((ConnectionNode) nodes[i][j]).getBias());
				}
			}

			// Add the values of the next layer
			for (int j = 0; j < nodes[i].length; j++) {
				if (nodes[i][j] instanceof InputNode) {
					values[i + 1] = NeuralMath.addArrays(values[i + 1], ((InputNode)nodes[i][j]).transferAllData(values[i][j]));
				}
				else if (nodes[i][j] instanceof ConnectionNode){
					values[i + 1] = NeuralMath.addArrays(values[i + 1], ((ConnectionNode)nodes[i][j]).transferAllData(values[i][j]));
				}
			}
		}

		// Edit the values of the last layer with the sigmoid function
        for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
            values[values.length - 1][i] += ((OutputNode) nodes[nodes.length - 1][i]).getBias();
            values[values.length - 1][i] = NeuralMath.setSigmoid(values[values.length - 1][i]);
        }

		// Print all the output values
        for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
            System.out.println(values[values.length - 1][i]);
        }

        double[] a = {0,0,0,0,0,0,0,0,0,0};
        a[expectedData] = 1;

        if (learn) {
            learn(values, a, learningRate);
        }

	}
    // TODO: arreglar

	/**
	 *
	 * @param values all the values of the nodes with the sigmoid function applied
	 * @param expectedData the real array that we expècted
	 * @param learningRate a multiplier to see how much it learns from this iteration. It has to be from 0 to 1
	 * @throws Exception
	 */
	public void learn(double[][] values, double[] expectedData, double learningRate) throws Exception {
		// Exceptions. Verify if the data is usable
        double[][][] changedValues;
		if (learningRate < 0 || learningRate > 1) {
			throw new Exception("Error en la tasa de aprendizaje. Tiene que ser de un número del 0 al 1");
		}
		if (expectedData.length != nodes[nodes.length - 1].length) {
            throw new Exception("Error en los datos de aprendizaje recibidos");
        }
		if (values.length != nodes.length) {
			throw new Exception("Error en los valores recibidos, no hay el mismo número de capas en los valores y en los nodos de la red neuronal");
		}
		for (int i = 0; i < values.length; i++) {
			if (values[i].length != nodes[i].length) {
				throw new Exception("Error en los valores recibidos, la capa " + i + " no son del mismo tamaño");
			}
		}

		changedValues = new double[values.length - 1][][];

		for (int i = 0; i < changedValues.length; i++) {
			changedValues[i] = new double[values[i].length][];
		}
		for (int i = 0; i < changedValues.length; i++) {
			for (int j = 0; j < changedValues[i].length; j++) {
				if (nodes[i][j] instanceof InputNode) {
					changedValues[i][j] = new double[((InputNode)nodes[i][j]).getValFrontLayer().size()];
				}
				else if (nodes[i][j] instanceof ConnectionNode) {
					changedValues[i][j] = new double[((ConnectionNode)nodes[i][j]).getValFrontLayer().size()];
				}
			}
		}
        for (int i = nodes.length - 2; i >= 0; i-- ) {
            for (int j = 0; j < nodes[i].length; j++) {
                if (nodes[i][j] instanceof InputNode) {
                    double[] d = ((InputNode)nodes[i][j]).changeWeights(values[i][j], values[i + 1], expectedData, learningRate);
                    /*for (int k = 0; k < d.length; k++) {
						changedValues[i][j][k] = d[k];
					}*/
					System.arraycopy(d, 0, changedValues[i][j], 0, d.length);

                }
                else if (nodes[i][j] instanceof ConnectionNode){
					double[] d = ((ConnectionNode)nodes[i][j]).changeWeights(values[i][j], values[i + 1], expectedData, learningRate);
					/*for (int k = 0; k < d.length; k++) {
						changedValues[i][j][k] = d[k];
					}*/
					System.arraycopy(d, 0, changedValues[i][j], 0, d.length);

				}
            }

            expectedData = new double[nodes[i].length];

            for (int j = 0; j < nodes[i].length; j++) {
				for (int k = 0; k < changedValues[i][j].length; k++) {
					expectedData[j] += changedValues[i][j][k] / values[i][j];
				}
            }
        }
		for (int i = 0; i < changedValues.length; i++) {
			for (int j = 0; j < changedValues[i].length; j++) {
				for (int k = 0; k < changedValues[i][j].length; k++) {
					if (nodes[i][j] instanceof InputNode) {
						((InputNode) nodes[i][j]).getValFrontLayer().set(k, changedValues[i][j][k]);
					}
					else if (nodes[i][j] instanceof ConnectionNode) {
						((ConnectionNode)nodes[i][j]).getValFrontLayer().set(k,changedValues[i][j][k]);
					}

				}
			}
		}
    }
}
