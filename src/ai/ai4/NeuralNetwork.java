package ai.ai4;

public class NeuralNetwork {
	private int[] shape;
	private Node[] nodes;
	// No sé por qué he hecho esto, supongo que pensé que podría ser más útil para dividir los outputs y el resto
	//En ese caso, debería hacer lo mismo con los InputNodes
	private OutputNode[] outputNodes;
	
	public NeuralNetwork(int[] shape) {
		setShape(shape);
		setNodes(createNodes());
		setOutputNodes(createOutputNodes(shape[shape.length - 1]));
		connectNodes();
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
	
	public OutputNode[] getOutputNodes() {
		return outputNodes;
	}
	
	public void setOutputNodes(OutputNode[] outputNodes) {
		this.outputNodes = outputNodes;
	}
	
	private Node[] createNodes() {
		Node[] nodes = new Node[NeuralMath.sumArray(getShape())];
		int count = 0;
		for (int i = 0; i < getShape()[0]; i++) {
			nodes[count] = new InputNode();
			count++;
		}
		
		for (int i = 1; i < getShape().length - 1; i++) {
			for (int j = 0; j < getShape()[i]; j++) {
				nodes[count] = new ConnectionNode();
				count++;
			}
		}
        for (int i = 0; i < getShape()[shape.length - 1]; i++) {
            nodes[count] = new OutputNode();
            count++;
        }
		return nodes;
	}
	
	private OutputNode[] createOutputNodes(int n) {
		OutputNode[] nodes = new OutputNode[n];
		int count = 0;

		return nodes;
	}
	
	private void connectNodes() {
		int count = 0;
		for (int i = 0; i < shape.length - 2; i++) {
			for (int j = 0; j < shape[i]; j++) {
				if (nodes[count + j] instanceof InputNode) {
					for (int k = 0; k < shape[i + 1]; k++) {
						((InputNode)nodes[count + j]).addNodeFront(nodes[count + k + shape[i]]);
					}
				}
				else if (nodes[count + j] instanceof ConnectionNode) {
					for (int k = 0; k < shape[i + 1]; k++) {
						((ConnectionNode)nodes[count + j]).addNodeFront(nodes[count + k + shape[i]]);
					}
					
					for (int k = 0; k < shape[i]; k++) {
						((ConnectionNode)nodes[count + j]).addNodeBack(nodes[count - shape[i - 1] + k]);
					}
				}
				else {

				}
			}
			count += shape[i];
		}
		for (int i = 0; i < shape[shape.length - 2]; i++) {
			for (int j = 0; j < shape[shape.length - 1]; j++) {
				((ConnectionNode)nodes[count + i]).addNodeFront(outputNodes[j]);
			}
			
			for (int j = 0; j < shape[shape.length - 2]; j++) {
				((ConnectionNode)nodes[count + i]).addNodeBack(nodes[count - shape[shape.length - 2] + j]);
			}
		}
		count += shape[shape.length - 2];
		for (int i = 0; i < shape[shape.length - 1]; i++) {
			for (int j = 0; j < shape[shape.length - 2]; j++) {
				(outputNodes[i]).addNodeBack(nodes[count - shape[shape.length - 2] + j]);
			}
		}
	}
	public void run(double[] data, int minRange, int maxRange, boolean learn) throws Exception {
		int count = 0;
		
		if (data.length != shape[0]) {
			throw new Exception("Error en los datos recibidos");
		}
		for (int i = 0; i < data.length; i++) {
			nodes[i].setValue(data[i]);
		}
		
		for (int i = 0; i < data.length; i++) {
			((InputNode)nodes[i]).setValueSigmoid(minRange, maxRange);
		}
		
		for (int i = 0; i < shape.length - 1; i++) {
			if (nodes[count] instanceof ConnectionNode) {
				for (int j = 0; j < shape[i]; j++) {
					((ConnectionNode)nodes[count + j]).setValueSigmoid(shape[i]);
				}
			}

			for (int j = 0; j < shape[i]; j++) {
				if (nodes[count + j] instanceof InputNode) {
					((InputNode)nodes[count + j]).transferAllData();
				}
				else if (nodes[count + j] instanceof ConnectionNode){
					((ConnectionNode)nodes[count + j]).transferAllData();
				}
			}
			count += shape[i];
		}
		
		for (int j = 0; j < shape[shape.length - 1]; j++) {
			((OutputNode)outputNodes[j]).setValueSigmoid(shape[shape.length - 1]);
		}
		
		for (int i = 0; i < shape[shape.length - 1]; i++) {
			System.out.println(outputNodes[i].getValue());
		}
	}

	public void learn(int expectedData) throws Exception {
        int count = 0;
    }
}
