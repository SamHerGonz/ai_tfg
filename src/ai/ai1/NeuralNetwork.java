package ai.ai1;

public class NeuralNetwork {
	private Node[][] nodes;
	private double[][][] valuesConnections;
	
	public NeuralNetwork(int[] shape) {
		setNodes(createNode(shape));
		setValuesConnections(addValues(shape));
	}
	
	
	public Node[][] getNodes() {
		return nodes;
	}
	
	private void setNodes(Node[][] nodes) {
		this.nodes = nodes;
	}
	
	public double[][][] getValuesConnections() {
		return valuesConnections;
	}
	
	private void setValuesConnections(double[][][] valuesConnections) {
		this.valuesConnections = valuesConnections;
	}
	
	
	private Node[][] createNode(int[] shape) {
		Node[][] nodes = new Node[shape.length][];
		
		for (int i = 0; i < shape.length; i++) {
			nodes[i] = new Node[shape[i]];
		}
		
		
		for (int i = 0; i < nodes[0].length; i++) {
			nodes[0][i] = new InputNode();
		}
		
		for (int i = 1; i < nodes.length - 1; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				nodes[i][j] = new ConnectionNode();
			}
		}
		
		for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
			nodes[nodes.length - 1][i] = new OutputNode();
		}
		return nodes;
	}
	
	
	public double[][][] addValues(int[] shape) {
		double[][][] values = new double[shape.length - 1][][];
		
		for (int i = 0; i < values.length; i++) {
			values[i] = new double[shape[i]][];
			
			for (int j = 0; j < values[i].length; j++) {
				values[i][j] = new double[shape[i + 1]];
				
				for (int k = 0; k < values[i][j].length; k++) {
					values[i][j][k] = Math.random() * 4 - 2;
				}
			}
		}
		
		return values;
	}
	
	public boolean run(double[] inputs, int real) {
		for (int i = 0; i < nodes[0].length; i++) {
			nodes[0][i].setValue(inputs[i]);
		}
		
		for (int i = 0; i < nodes.length - 1; i++) {
			
			for (int j = 0; j < nodes[i + 1].length; j++) {
				
				for (int k = 0; k < nodes[i].length; k++) {
					nodes[i + 1][j].addValue(nodes[i][k].getValue() * getValuesConnections()[i][k][j]);
				}
				
				if (nodes[i + 1][j] instanceof ConnectionNode) {
					nodes[i + 1][j].addValue(((ConnectionNode)nodes[i + 1][j]).getBias());
				}
				else if (nodes[i + 1][j] instanceof OutputNode) {
					nodes[i + 1][j].addValue(((OutputNode)nodes[i + 1][j]).getBias());
				}
				
				nodes[i + 1][j].setValue(1 / (1 + Math.pow(Math.E, nodes[i + 1][j].getValue())));
				/*if (nodes[i + 1][j].getValue() <= nodes[i + 1][j].getThreshold()) {
					nodes[i + 1][j].setValue(0);
				}
				else {
					nodes[i + 1][j].setValue(1);
				}*/
			}
		}
		
		int max = 0;
		for (int i = 1; i < nodes[nodes.length - 1].length; i++) {
			if (nodes[nodes.length - 1][max].getValue() < nodes[nodes.length - 1][i].getValue()) {
				max = i;
			}
		}
		
		if(max != real) {
			System.out.println("False");
			learn(real);
		}
		else {
			System.out.println("True");
		}
		return (max == real);
	}
	
	private void learn (int real) {
		double change;
		double[] expectedValue;
		
		
		for (int i = nodes.length - 1; i > 0; i--) {
			expectedValue = new double[nodes[i].length];
			for (int j = 0; j < nodes[i].length; j++) {
				change = 0;
				for (int k = 0; k < nodes[i - 1].length; k++) {
					change += Math.pow(nodes[i][j].getValue() - expectedValue[j], 2);
				}
				change /= nodes[i].length;
				System.out.println(change + " " + i);
			}
		}
	}
}
