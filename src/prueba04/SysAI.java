package prueba04;

public class SysAI {
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
	
	public static int think(Node[] nodes) {
		int temp1 = nodes.length, temp2;
		int n;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof InputNode) {
				((InputNode) nodes[i]).sendValues();
			} 
			else if (nodes[i] instanceof ConnectionNode) {
				((ConnectionNode) nodes[i]).sendValues();
			}
			else {
				temp1 = i;
				break;
			}
		}
		temp2 = 1;
		n = temp2;
		for (int i = temp1; i < nodes.length; i++) {
			if (nodes[temp1].getVal() < nodes[i].getVal()) {
				n = temp2;
			}
			temp2++;
		}
		return n;
	}
}
