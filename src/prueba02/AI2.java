package prueba02;

public class AI2 {
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
		int temp;
		int[] n = {2,2,2,2};
		try {
			Node[] no = createNodes(n);
			//Esto es temporal, es en un caso aleatorio (borrar luego)
			for (int i = 0; i < no.length; i++) {
				if (no[i] instanceof InputNode) {
					for (int j = 0; j < ((InputNode) no[i]).getConections().length; j++) {
						((InputNode) no[i]).getValues()[j] = Math.random() * 2 - 1;
					}
				} 
				else if (no[i] instanceof ConnectionNode) {
					for (int j = 0; j < ((ConnectionNode) no[i]).getConections().length; j++) {
						((ConnectionNode) no[i]).getValues()[j] = Math.random() * 2 - 1;
					}
				}
			}
			for (int i = 0; i < n[0]; i++) {
				no[i].setVal(Math.random() * 255);
			}
			temp = 0;
			for (int i = 0; i < n.length; i++) {
				for (int j = 0; j < n[i]; j++) {
					if (no[temp] instanceof InputNode) {
						for (int k = 0; k < ((InputNode) no[temp]).getConections().length; k++) {
							((InputNode) no[temp]).getConections()[k].setVal(((InputNode) no[temp]).getConections()[k].getVal() + no[temp].getVal() * ((InputNode) no[temp]).getValues()[k]);
						}
					} 
					else if (no[temp] instanceof ConnectionNode) {
						for (int k = 0; k < ((ConnectionNode) no[temp]).getConections().length; k++) {
							((ConnectionNode) no[temp]).getConections()[k].setVal(((ConnectionNode) no[temp]).getConections()[k].getVal() + no[temp].getVal() * ((ConnectionNode) no[temp]).getValues()[k]);
						}
					}
					temp++;
				}
			}
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
