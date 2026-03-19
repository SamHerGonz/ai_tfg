package prueba05;

public class SysAI {
	public static Node[][] createNodes(int[] nNodes) throws Exception {
		if (nNodes.length < 2) {
			throw new Exception("La red neuronal tiene que tener mínimo 2 capas");
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
			nodes[nNodes.length - 1][i] = new OutputNode();
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
		return nodes;
	}
	
	// Esta función solo sirve para cuando sólo se puede hacer una acción
		// EJ: Parchís, 3 en raya, identificar un número...
		public static int think_1_output(Node[][] nodes) {
			int n = 1;
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
			for (int i = 1; i < nodes[nodes.length - 1].length; i++) {
				if (nodes[nodes.length - 1][n - 1].getVal() < nodes[nodes.length - 1][i].getVal()) {
					n = i + 1;
				}
			}
			return n;
		}
	
}
