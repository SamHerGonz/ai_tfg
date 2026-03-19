package ai.ai7;

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
						((InputNode)nodes[i][j]).addNodeFront(nodes[i + 1][k], max);
					}
				}
				else if (nodes[i][j] instanceof ConnectionNode) {
					for (int k = 0; k < nodes[i + 1].length; k++) {
                        ((ConnectionNode)nodes[i][j]).addNodeFront(nodes[i + 1][k], max);
					}
					
					for (int k = 0; k < nodes[i - 1].length; k++) {
                        ((ConnectionNode)nodes[i][j]).addNodeBack(nodes[i - 1][k]);
                    }
				}
				else {
                    for (int k = 0; k < nodes[i - 1].length; k++) {
                        ((OutputNode)nodes[i][j]).addNodeBack(nodes[i - 1][k]);
                    }
                }
			}
		}
	}

    // Funciona
	public void run(double[] data, int minRange, int maxRange, int expectedData, double learningRate) throws Exception {
		if (data.length != nodes[0].length) {
			throw new Exception("Error en los datos recibidos");
		}

        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                nodes[i][j].setValue(0);
            }
        }
		for (int i = 0; i < data.length; i++) {
			nodes[0][i].setValue(data[i]);
		}

		for (int i = 0; i < nodes.length - 1; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				if (nodes[i][j] instanceof InputNode) {
					((InputNode)nodes[i][j]).transferAllData(minRange, maxRange);
				}
				else if (nodes[i][j] instanceof ConnectionNode){
					((ConnectionNode)nodes[i][j]).transferAllData();
				}
			}
		}

        for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
            nodes[nodes.length - 1][i].addValue(((OutputNode) nodes[nodes.length - 1][i]).getBias());
        }

        for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
            System.out.println(NeuralMath.setSigmoid(nodes[nodes.length - 1][i].getValue()));
        }

        double[] a = {0,0,0,0,0,0,0,0,0,0};
        a[expectedData] = 1;

        if (learn) {
            learn(a, learningRate);
        }

	}
    // TODO: arreglar
	public void learn(double[] expectedData, double learningRate) throws Exception {
        if (expectedData.length != nodes[nodes.length - 1].length) {
            throw new Exception("Error en los datos recibidos");
        }

        for (int i = nodes.length - 2; i >= 0; i-- ) {
            double[] layerMarginErrors = new double[nodes[i + 1].length];

            for (int j = 0; j < nodes[i + 1].length; j++) {
                layerMarginErrors[j] = expectedData[j] - NeuralMath.setSigmoid(nodes[i + 1][j].getValue());
            }


            for (int j = 0; j < nodes[i].length; j++) {
                if (nodes[i][j] instanceof InputNode) {
                    ((InputNode)nodes[i][j]).getLearningData(layerMarginErrors, learningRate);
                }
                else if (nodes[i][j] instanceof ConnectionNode){
                    ((ConnectionNode)nodes[i][j]).getLearningData(layerMarginErrors, learningRate);
                }
            }

            expectedData = new double[nodes[i].length];

            for (int j = 0; j < nodes[i].length; j++) {
                if (nodes[i][j] instanceof InputNode) {
                    expectedData[j] = NeuralMath.setDerivativeSigmoid(NeuralMath.setSigmoid(((InputNode)nodes[i][j]).getFrontLayer().get(i).getValue()));
                }
                else if (nodes[i][j] instanceof ConnectionNode){
                    expectedData[j] = NeuralMath.setDerivativeSigmoid(NeuralMath.setSigmoid(((ConnectionNode)nodes[i][j]).getFrontLayer().get(i).getValue()));
                }
            }
        }
    }
}
