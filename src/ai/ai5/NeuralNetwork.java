package ai.ai5;

import java.io.Serializable;

public class NeuralNetwork implements Serializable {
	private int[] shape;
	private Node[] nodes;
    private boolean learn;
    private static final long serialVersionUID = 3877515453607213504L;

    public NeuralNetwork(int[] shape, double max) {
		setShape(shape);
		setNodes(createNodes(max));
		setLearn(true);
        connectNodes(max);
	}
	
	
	public Node[] getNodes() {
		return nodes;
	}
	
	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}
	
	public int[] getShape() {
		return shape;
	}
	
	public void setShape(int[] shape) {
		this.shape = shape;
	}

    public boolean isLearn() {
        return learn;
    }

    public void setLearn(boolean learn) {
        this.learn = learn;
    }

    private Node[] createNodes(double max) {
		Node[] nodes = new Node[NeuralMath.sumArray(getShape())];
		int count = 0;
		for (int i = 0; i < getShape()[0]; i++) {
			nodes[count] = new InputNode();
			count++;
		}
		
		for (int i = 1; i < getShape().length - 1; i++) {
			for (int j = 0; j < getShape()[i]; j++) {
				nodes[count] = new ConnectionNode(max);
				count++;
			}
		}
        for (int i = 0; i < getShape()[shape.length - 1]; i++) {
            nodes[count] = new OutputNode(max);
            count++;
        }
		return nodes;
	}
	
	private void connectNodes(double max) {
		int count = 0;
		for (int i = 0; i < shape.length; i++) {
			for (int j = 0; j < shape[i]; j++) {
				if (nodes[count + j] instanceof InputNode) {
					for (int k = 0; k < shape[i + 1]; k++) {
						((InputNode)nodes[count + j]).addNodeFront(nodes[count + k + shape[i]], max);
					}
				}
				else if (nodes[count + j] instanceof ConnectionNode) {
					for (int k = 0; k < shape[i + 1]; k++) {
						((ConnectionNode)nodes[count + j]).addNodeFront(nodes[count + k + shape[i]], max);
					}
					
					for (int k = 0; k < shape[i - 1]; k++) {
						((ConnectionNode)nodes[count + j]).addNodeBack(nodes[count - shape[i - 1] + k]);
					}
				}
				else {
                    for (int k = 0; k < shape[i - 1]; k++) {
                        ((OutputNode)nodes[count + j]).addNodeBack(nodes[count - shape[i - 1] + k]);
                    }
                }
			}
			count += shape[i];
		}
	}

	public void run(double[] data, int minRange, int maxRange, int expectedData, double lerningRate) throws Exception {
		int count = 0;
		
		if (data.length != shape[0]) {
			throw new Exception("Error en los datos recibidos");
		}

        for (int i = 0; i < nodes.length; i++) {
            nodes[i].setValue(0);
        }
		for (int i = 0; i < data.length; i++) {
			nodes[i].setValue(data[i]);
		}

		for (int i = 0; i < shape.length - 1; i++) {
			for (int j = 0; j < shape[i]; j++) {
				if (nodes[count + j] instanceof InputNode) {
					((InputNode)nodes[count + j]).transferAllData(minRange, maxRange);
				}
				else if (nodes[count + j] instanceof ConnectionNode){
					((ConnectionNode)nodes[count + j]).transferAllData();
				}
			}
			count += shape[i];
		}

        for (int i = count; i < count + shape[shape.length - 1]; i++) {
            ((OutputNode)nodes[i]).setValueSigmoid();
        }

        for (int i = count; i < count + shape[shape.length - 1]; i++) {
			System.out.println(nodes[i].getValue());
		}
        double[] a = {0,0,0,0,0,0,0,0,0,0};
        if (expectedData != 0) {
            a[expectedData - 1] = 1;
        }
        else {
            a[a.length - 1] = 1;
        }
        if (learn) {
            learn(a, lerningRate);
        }

	}

	public void learn(double[] expectedData, double learningRate) throws Exception {
        int count = nodes.length - 1;
        if (expectedData.length != shape[shape.length - 1]) {
            throw new Exception("Error en los datos recibidos");
        }

        for (int i = 0; i < nodes.length; i++) {
            nodes[i].setExpected(0);
        }

        for (int i = 0; i < expectedData.length; i++) {
            nodes[count - (expectedData.length - 1) + i].setExpected(expectedData[i]);
        }

        for (int i = shape.length - 1; i > 0; i-- ) {

            for (int j = 0; j < shape[i]; j++) {
                if (nodes[count - j] instanceof ConnectionNode) {
                    ((ConnectionNode)nodes[count - j]).transferLearningData(learningRate);
                }
                else if (nodes[count - j] instanceof OutputNode) {
                    ((OutputNode) nodes[count - j]).transferLearningData(learningRate);
                }
            }
            count -= shape[i];
            if (nodes[count] instanceof ConnectionNode) {
                for (int j = 0; j < shape[i - 1]; j++) {
                    ((ConnectionNode)nodes[count - j]).setValueSigmoid();
                    nodes[count - j].setExpected(NeuralMath.setSigmoid(nodes[count - j].getExpected()));
                }
            }
        }
    }
}
