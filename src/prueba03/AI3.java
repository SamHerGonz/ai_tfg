package prueba03;

public class AI3 {
	public static Node[] createNodes(int[] nNodes) throws Exception {
		int temp = 0;
		Node[] nodes;
		for (int i = 0; i < nNodes.length; i++) {
			temp+=nNodes[i];
		}
		
		// Create the nodes:
		nodes = new Node[temp];
		temp = 0;
		for (int i = 0; i < nNodes[0]; i++) {
			nodes[temp] = new InputNode(nNodes[1]);
			temp++;
		}
		
		for (int i = 1; i < nNodes.length - 1; i++) {
			for (int j = 0; j < nNodes[i]; j++) {
				nodes[temp] = new ConnectionNode(nNodes[i + 1]);
				temp++;
			}
		}
		for (int i = 0; i < nNodes[nNodes.length - 1]; i++) {
			nodes[temp] = new OutputNode();
			temp++;
		}
		
		// Connect the nodes
		temp = 0;
		for (int i = 0; i < nNodes.length - 1; i++) {
			for (int j = 0; j < nNodes[i]; j++) {
				for (int k = 0; k < nNodes[i + 1]; k++) {
					if (nodes[temp] instanceof InputNode) {
						((InputNode) nodes[temp]).getConections()[k] = nodes[temp + k + nNodes[i] - j];
					} 
					else if (nodes[temp] instanceof ConnectionNode) {
						((ConnectionNode) nodes[temp]).getConections()[k] = nodes[temp + k + nNodes[i] - j];
					}
				}
				temp++;
			}
		}
		return nodes;
	}
	
	public static void main(String[] args) {
		int[] n = {4,7,9,5};
		try {
			Node[] nodes = createNodes(n);
			
			//Esto es temporal, es en un caso aleatorio (borrar luego)
			for (int i = 0; i < nodes.length; i++) {
				if (nodes[i] instanceof InputNode) {
					for (int j = 0; j < ((InputNode) nodes[i]).getConections().length; j++) {
						((InputNode) nodes[i]).getValues()[j] = Math.random() * 2 - 1;
					}
				} 
				else if (nodes[i] instanceof ConnectionNode) {
					for (int j = 0; j < ((ConnectionNode) nodes[i]).getConections().length; j++) {
						((ConnectionNode) nodes[i]).getValues()[j] = Math.random() * 2 - 1;
					}
				}
			}
			
			//Add random inputs (random data) future removal
			for (int i = 0; i < n[0]; i++) {
				nodes[i].setVal(Math.random() * 255);
			}
			
			for (int i = 0; i < nodes.length; i++) {
				if (nodes[i] instanceof InputNode) {
					((InputNode) nodes[i]).sendValues();
				} 
				else if (nodes[i] instanceof ConnectionNode) {
					((ConnectionNode) nodes[i]).sendValues();
				}
			}
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
