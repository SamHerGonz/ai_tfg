package ai.ai2;

public class NeuralNetwork {
	private Node[] nodes;
	private Connection[][] connections; 
	private int[] shape;
	
	public NeuralNetwork(int[] shape) {
		setShape(shape);
		setNodes(createNodes(shape));
		setConnections(addConnections());
	}
	
	
	public Node[] getNodes() {
		return nodes;
	}
	
	private void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}	
	
	public Connection[][] getConnections() {
		return connections;
	}
	
	private void setConnections(Connection[][] connections) {
		this.connections = connections;
	}
	
	public int[] getShape() {
		return shape;
	}
	
	public void setShape(int[] shape) {
		this.shape = shape;
	}
	
	
	private Node[] createNodes(int[] shape) {
		Node[] nodes;
		int length = 0;
		
		for (int i = 0; i < shape.length; i++) {
			length += shape[i];
		}
		nodes = new Node[length];
		
		for (int i = 0; i < shape[0]; i++) {
			nodes[i] = new InputNode();
		}
		
		for (int i = shape[0]; i < nodes.length - shape[shape.length - 1]; i++) {
			nodes[i] = new ConnectionNode();
		}
		
		for (int i = nodes.length - shape[shape.length - 1]; i < nodes.length; i++) {
			nodes[i] = new OutputNode(i);
		}
		return nodes;
	}
	
	
	public Connection[][] addConnections() {
		Connection[][] values = new Connection[shape.length - 1][];
		int counter = 0;
		
		for (int i = 0; i < values.length; i++) {
			
			values[i] = new Connection[shape[i] * shape[i + 1]];
			for (int j = 0; j < shape[i]; j++) {
				
				for (int k = 0; k < shape[i + 1]; k++) {
					values[i][j * shape[i + 1] + k] = new Connection(nodes[counter + j], nodes[counter + shape[i] + k]);
				}
			}
			counter += shape[i];
		}
		
		return values;
	}
	
	public boolean run(double[] inputs, int real) {
		int layer = 0;
		for (int i = 0; i < inputs.length; i++) {
			nodes[i].setValue(inputs[i]);
		}
		
		for (int i = inputs.length; i < nodes.length; i++) {
			nodes[i].setValue(0);
		}
		
		for (int i = 0; i < connections.length; i++) {
			for (int j = 0; j < connections[i].length; j++) {
				connections[i][j].transferData(0);
			}
			if (connections[i][0].getNode2() instanceof ConnectionNode) {
				for (int j = 0; j < getShape()[i + 1]; j++) {
					((ConnectionNode)(connections[i][j].getNode2())).setValueSigmoid(shape[layer]);
				}
				layer++;
			}
			else if (connections[i][0].getNode2() instanceof OutputNode) {
				for (int j = 0; j < getShape()[i + 1]; j++) {
					((OutputNode)(connections[i][j].getNode2())).setValueSigmoid(shape[layer]);
				}
				layer++;
			}
		}
		
		//learn(real);
		
		for (int i = 0; i < getShape()[shape.length - 1]; i++) {
			System.out.println(connections[connections.length - 1][i].getNode2().getValue());
		}
		System.out.println();
		return true;
	}
	
	private void learn (int real) {
		for (int i = connections.length - 1; i >= 0; i--) {
			for (int j = 0; j < connections[i].length; j++) {
				connections[i][j].transferData(1);
			}
			if (connections[i][0].getNode1() instanceof ConnectionNode) {
				for (int j = 0; j < getShape()[i + 1]; j++) {
					((ConnectionNode)(connections[i][j].getNode1())).learn();
				}
			}
			else if (connections[i][0].getNode1() instanceof InputNode) {
				for (int j = 0; j < getShape()[i + 1]; j++) {
					((InputNode)(connections[i][j].getNode1())).learn();
				}
			}
		}
	}
}
