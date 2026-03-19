package prueba05;

public class AI5 {
	
	public static void main(String[] args) {
		int[] n = {5,3,6,7,4};
		try {
			Node[][] nodes = SysAI.createNodes(n);
			
			//Esto es temporal, es en un caso aleatorio (borrar luego)
			for (int i = 0; i < nodes.length; i++) {
				for (int j = 0; j < nodes[i].length; j++) {
					if (nodes[i][j] instanceof InputNode) {
						for (int k = 0; k < ((InputNode) nodes[i][j]).getConections().length; k++) {
							((InputNode) nodes[i][j]).getValues()[k] = Math.random() * 2 - 1;
						}
					} 
					else if (nodes[i][j] instanceof ConnectionNode) {
						for (int k = 0; k < ((ConnectionNode) nodes[i][j]).getConections().length; k++) {
							((ConnectionNode) nodes[i][j]).getValues()[k] = Math.random() * 2 - 1;
						}
					}
				}
			}
			
			//Add random inputs (random data) future removal
			for (int i = 0; i < nodes[0].length; i++) {
				nodes[0][i].setVal(Math.random() * 255);
			}
						
			System.out.println("El input presionado es: " + SysAI.think_1_output(nodes));
			System.out.println();
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

}
