package prueba04;

public class AI4 {
	
	public static void main(String[] args) {
		int[] n = {2,2,2,2};
		try {
			Node[] nodes = SysAI.createNodes(n);
			
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
						
			System.out.println(SysAI.think(nodes));
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
