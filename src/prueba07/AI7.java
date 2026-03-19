package prueba07;

public class AI7 {
	
	public static void main(String[] args) {
		int[] n = {4,9,6,7,5,5};
		double[] data;
		Type[] type = {Type.yesNo,Type.noNegative,Type.normal,Type.yesNo,Type.noNegative};
		Node[][] nodes;
		
		try {			
			nodes = SysAI.createNodes(n, type);
			
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
			
			// Se guarda los outputs en el array data. Después tengo que hacer que el programa interprete el output para que lo envie al juego o programa (Cambia en TODOS los proyectos)
			data = SysAI.think_dynamic_game(nodes);
			for (int i = 0; i < data.length; i++) {
				System.out.println(data[i]);
			}
			
			System.out.println();
		} catch (Exception e) {
			e.getMessage();
		}
	}
}