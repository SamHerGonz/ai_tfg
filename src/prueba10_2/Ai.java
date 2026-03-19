package prueba10_2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class Ai {
	private Node[][] nodes;

	
	public Ai(int[] nNodes, OutType[] type, boolean exists) throws Exception {
		setNodes(createNodes(nNodes, type, exists));
	}
	
	public Ai(int[] nNodes, boolean exists) throws Exception {
		OutType[] type = new OutType[nNodes.length];
		for (OutType ot : type) {
			ot = OutType.normal;
		}
		
		setNodes(createNodes(nNodes, type, exists));
	}
	
	public Node[][] getNodes() {
		return nodes;
	}

	private void setNodes(Node[][] nodes) {
		this.nodes = nodes;
	}
	
	
	public Node[][] createNodes(int[] nNodes, OutType[] type, boolean exists) throws Exception {
		if (nNodes.length < 2) {
			throw new Exception("La red neuronal tiene que tener mínimo 2 capas");
		}
		if (type.length != nNodes[nNodes.length - 1]) {
			throw new Exception("No se ha configurado correctamente los tipos de nodos de output");
		}
		
		Node[][] nodes = new Node[nNodes.length][];

		if(exists) {
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
							((InputNode) nodes[i][j]).getconectionsFoward()[k] = nodes[i + 1][k];
						} 
						else if (nodes[i][j] instanceof ConnectionNode) {
							((ConnectionNode) nodes[i][j]).getconectionsFoward()[k] = nodes[i + 1][k];
						}
					}
				}
			}
			
			//Add the values
			//Esto es temporal, es en un caso aleatorio (borrar luego)
			for (int i = 0; i < nodes.length; i++) {
				for (int j = 0; j < nodes[i].length; j++) {
					if (nodes[i][j] instanceof InputNode) {
						for (int k = 0; k < ((InputNode) nodes[i][j]).getconectionsFoward().length; k++) {
							((InputNode) nodes[i][j]).getValues()[k] = Math.random() * 2 - 1;
						}
					}
					else if (nodes[i][j] instanceof ConnectionNode) {
						for (int k = 0; k < ((ConnectionNode) nodes[i][j]).getconectionsFoward().length; k++) {
							((ConnectionNode) nodes[i][j]).getValues()[k] = Math.random() * 2 - 1;
						}
					}
				}
			}
			
			// Import into 'datos.dat' the values
			FileOutputStream fos = new FileOutputStream(new File(".idea/src/prueba09/datos.dat"));
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(nodes);
			oos.close();
		}
		else {
			FileInputStream fis = new FileInputStream(new File(".idea/src/prueba09/datos.dat"));
			ObjectInputStream ois = new ObjectInputStream(fis);
			nodes = (Node[][])ois.readObject();
			ois.close();
		}
		
		return nodes;
	}
	
	public double[] think_game() throws Exception {
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
		
		for (int i = 0; i < n.length; i++) {
			System.out.println(n[i]);
		}
		return n;
	}
	
	
	@Override
	public String toString() {
		return "Ai [nodes=" + Arrays.toString(nodes) + "]";
	}
	
	/* Esta función solo sirve para cuando se puedan presionar varios botones a la vez. 
	 * También cuándo se puedan presionar con mayor o menor potencia (ej: Joystick)*/
	// EJ: Brawl Stars, Brawlhalla, Smash Bros, Mario Bros, Trackmania...
	// It doesn't exist, still trying to do it...

}
