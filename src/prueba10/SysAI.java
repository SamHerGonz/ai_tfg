package prueba10;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SysAI {
	public static Node[][] nodes;
	
	public static void saveData() throws IOException {
		FileOutputStream fos = new FileOutputStream(new File("src/prueba10/datos.dat"));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(nodes);
		oos.close();
	}
	
	public static Node[][] createNodes(int[] nNodes, OutType[] type, boolean learn) throws Exception {
		// Exceptions
		if (nNodes.length < 2) {
			throw new Exception("La red neuronal tiene que tener mínimo 2 capas");
		}
		if (type.length != nNodes[nNodes.length - 1]) {
			throw new Exception("No se ha configurado correctamente los tipos de nodos de output");
		}
		
		Node[][] nodes = new Node[nNodes.length][];

		if(learn) {
			for (int i = 0; i < nNodes.length; i++) {
				nodes[i] = new Node[nNodes[i]];
			}
			
			// Create the nodes:
			for (int i = 0; i < nNodes[0]; i++) {
				nodes[0][i] = new InputNode(nNodes[1]);
			}
			for (int i = 1; i < nNodes.length - 1; i++) {
				for (int j = 0; j < nNodes[i]; j++) {
					nodes[i][j] = new ConnectionNode(nNodes[i + 1], i);
				}
			}
			for (int i = 0; i < nNodes[nNodes.length - 1]; i++) {
				nodes[nNodes.length - 1][i] = new OutputNode(type[i]);
			}
			
			// Add the values of the connections
			// This is a random case. It's necessary for the initialization.
			for (int i = 0; i < nodes.length; i++) {
				for (int j = 0; j < nodes[i].length; j++) {
					if (nodes[i][j] instanceof InputNode) {
						for (int k = 0; k < ((InputNode) nodes[i][j]).getValues().length; k++) {
							((InputNode) nodes[i][j]).getValues()[k] = Math.random() * 2 - 1;
						}
					}
					else if (nodes[i][j] instanceof ConnectionNode) {
						for (int k = 0; k < ((ConnectionNode) nodes[i][j]).getValues().length; k++) {
							((ConnectionNode) nodes[i][j]).getValues()[k] = Math.random() * 2 - 1;
						}
					}
				}
			}
			
			// Import into 'datos.dat' the values
			saveData();
		}
		else {
			FileInputStream fis = new FileInputStream(new File(".idea/src/prueba10/datos.dat"));
			ObjectInputStream ois = new ObjectInputStream(fis);
			nodes = (Node[][])ois.readObject();
			ois.close();
		}
		return nodes;
	}
	
	public static double[] think_game(Node[][] nodes) throws Exception {
		double[] n = new double[nodes[nodes.length - 1].length];
		
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				if (nodes[i][j] instanceof InputNode) {
					((InputNode)nodes[i][j]).sendValues();
				} 
				else if (nodes[i][j] instanceof ConnectionNode) {
					((ConnectionNode)nodes[i][j]).sendValues();
				}
				else {
					break;
				}
			}
		}
		
		for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
			if (nodes[nodes.length - 1][i] instanceof OutputNode) {
				((OutputNode) nodes[nodes.length - 1][i]).typeVal();
			}
			else {
				throw new Exception("Error en el output");
			}
		}
		
		for (int i = 0; i < nodes[nodes.length - 1].length; i++) {
			n[i] = nodes[nodes.length - 1][i].getVal();
		}
		
		return n;
	}
	
	/* Esta función solo sirve para cuando se puedan presionar varios botones a la vez. 
	 * También cuándo se puedan presionar con mayor o menor potencia (ej: Joystick)*/
	// EJ: Brawl Stars, Brawlhalla, Smash Bros, Mario Bros, Trackmania...
	// It doesn't exist, still trying to do it...
	
	//Function for learning:
	//The first parameter is for the nodes
	//The second parameter is for saying the AI if he did it correctly or not, and if not the accuracy (in a double value)
	public static void addLearningValues(Node[][] nodes, double[] learningData) throws Exception {
		double[] learn = learningData;
		if (nodes[nodes.length - 1].length != learningData.length) {
			throw new Exception("Los parametros no son correctos");
		}
		
		for (int i = nodes.length - 1; i > 0; i--) {
			for (int j = 0; j < nodes[i].length; j++) {
				//I have to make a function for learning in the InputNode class
				if (nodes[i][j] instanceof OutputNode) {
					((OutputNode) nodes[i][j]).learnNodes(learn);
				}
				else if (nodes[i][j] instanceof ConnectionNode) {
					((ConnectionNode) nodes[i][j]).learnNodes(learn);
				}
				else {
					break;
				}
			}
			learn = new double[nodes[i - 1].length];
			for (int j = 0; j < learn.length; j++) {
				learn[j] = Math.random() / 100;
			}
		}
		saveData();
		
		// Now I just need to add the learning data and change a little bit the 'learnNodes' function
	}
}
