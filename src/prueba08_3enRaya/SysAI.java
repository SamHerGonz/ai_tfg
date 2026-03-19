package prueba08_3enRaya;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class SysAI {
	
	public static Node[][] createNodes(int[] nNodes, OutType[] type, boolean learn) throws Exception {
		if (nNodes.length < 2) {
			throw new Exception("La red neuronal tiene que tener mínimo 2 capas");
		}
		if (type.length != nNodes[nNodes.length - 1]) {
			throw new Exception("No se ha configurado correctamente los tipos de nodos de output");
		}
		
		Node[][] nodes = new Node[nNodes.length][];
		
		for (int i = 0; i < nNodes.length; i++) {
			nodes[i] = new Node[nNodes[i]];
		}
		
		// Create the nodes:
		for (int i = 0; i < nNodes[0]; i++) {
			nodes[0][i] = new InputNode(nNodes[1]);
		}
		for (int i = 1; i < nNodes.length - 1; i++) {
			for (int j = 0; j < nNodes[i]; j++) {
				nodes[i][j] = new ConnectionNode(nNodes[i + 1]);
			}
		}
		for (int i = 0; i < nNodes[nNodes.length - 1]; i++) {
			nodes[nNodes.length - 1][i] = new OutputNode(type[i]);
		}

		// Connect the nodes
		for (int i = 0; i < nodes.length - 1; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				for (int k = 0; k < nNodes[i + 1]; k++) {
					if (nodes[i][j] instanceof InputNode) {
						((InputNode) nodes[i][j]).getConections()[k] = nodes[i + 1][k];
					} 
					else if (nodes[i][j] instanceof ConnectionNode) {
						((ConnectionNode) nodes[i][j]).getConections()[k] = nodes[i + 1][k];
					}
				}
			}
		}
		
		//Add the values
		//Esto es temporal, es en un caso aleatorio (borrar luego)
		if(learn) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(".idea/src/prueba8/text.txt"));
			for (int i = 0; i < nodes.length; i++) {
				for (int j = 0; j < nodes[i].length; j++) {
					if (nodes[i][j] instanceof InputNode) {
						for (int k = 0; k < ((InputNode) nodes[i][j]).getConections().length; k++) {
							((InputNode) nodes[i][j]).getValues()[k] = Math.random() * 2 - 1;
							bw.write(((InputNode) nodes[i][j]).getValues()[k] + "\n");
						}
					}
					else if (nodes[i][j] instanceof ConnectionNode) {
						for (int k = 0; k < ((ConnectionNode) nodes[i][j]).getConections().length; k++) {
							((ConnectionNode) nodes[i][j]).getValues()[k] = Math.random() * 2 - 1;
							bw.write(((ConnectionNode) nodes[i][j]).getValues()[k] + "\n");
						}
					}
				}
			}
			bw.close();
		}
		else {
			BufferedReader br = new BufferedReader(new FileReader(".idea/src/prueba8/text.txt"));
			for (int i = 0; i < nodes.length; i++) {
				for (int j = 0; j < nodes[i].length; j++) {
					if (nodes[i][j] instanceof InputNode) {
						for (int k = 0; k < ((InputNode) nodes[i][j]).getConections().length; k++) {
							((InputNode) nodes[i][j]).getValues()[k] = Double.parseDouble(br.readLine());
						}
					}
					else if (nodes[i][j] instanceof ConnectionNode) {
						for (int k = 0; k < ((ConnectionNode) nodes[i][j]).getConections().length; k++) {
							((ConnectionNode) nodes[i][j]).getValues()[k] = Double.parseDouble(br.readLine());
						}
					}
				}
			}
			br.close();
		}
		return nodes;
	}
	
	/* Esta función solo sirve para cuando se puedan presionar varios botones a la vez. 
	 * También cuándo se puedan presionar con mayor o menor potencia (ej: Joystick)*/
	// EJ: Brawl Stars, Brawlhalla, Smash Bros, Mario Bros, Trackmania...
	public static double[] think_dynamic_game(Node[][] nodes) throws Exception {
		double[] n = new double[nodes[nodes.length - 1].length];
		
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				if (nodes[i][j] instanceof InputNode) {
					((InputNode) nodes[i][j]).sendValues();
				} 
				else if (nodes[i][j] instanceof ConnectionNode) {
					((ConnectionNode) nodes[i][j]).sendValues();
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
}
