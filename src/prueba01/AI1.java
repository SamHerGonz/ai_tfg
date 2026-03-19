package prueba01;

public class AI1 {
	public static Node[] createNodes(int[] nNodes) throws Exception {
		int n = 0;
		Node[] nodes;
		for (int i = 0; i < nNodes.length; i++) {
			n+=nNodes[i];
		}
		
		// Create the nodes:
		nodes = new Node[n];
		n = 0;
		for (int i = 0; i < nNodes.length - 1; i++) {
			for (int j = 0; j < nNodes[i]; j++) {
				nodes[n] = new Node(nNodes[i + 1]);
				n++;
			}
		}
		for (int i = 0; i < nNodes[nNodes.length - 1]; i++) {
			nodes[n] = new Node(0);
			n++;
		}
		
		// Connect the nodes
		n = 0;
		for (int i = 0; i < nNodes.length - 1; i++) {
			for (int j = 0; j < nNodes[i]; j++) {
				for (int k = 0; k < nNodes[i + 1]; k++) {
					nodes[n].getConections()[k] = nodes[n + k + nNodes[i] - j];
				}
				n++;
			}
		}
		return nodes;
	}
	public static void main(String[] args) {
		int[] n = {810,100,100,9};
		try {
			Node[] no = createNodes(n);
			for (int i = 0; i < no.length; i++) {
				for (int j = 0; j < no[i].getValues().length; j++) {
					no[i].getValues()[j] = Math.random();
				}
			}
			for (int i = 0; i < no.length; i++) {
				System.out.println(no[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
