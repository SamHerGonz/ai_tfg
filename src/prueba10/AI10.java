package prueba10;

import java.util.Scanner;

public class AI10 {

	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		int[] n = {4,5,5,3};
		double[] data, newData = new double[n[n.length - 1]];
		/* The outType for different games:
		 * 1. Brawlhalla: 
		 * 	1, 2. the joystick (1 for x coordinates and 1 for y coordinates, with circle limitations)
		 * 	3. Normal attack
		 * 	4. Special attack
		 * 	5. Jump
		 * 	6. Dash
		 * OutType[] outType = {OutType.normal,OutType.normal,OutType.yesNo,OutType.yesNo,OutType.yesNo,OutType.yesNo};
		 * 
		 */
		OutType[] outType = {OutType.normal,OutType.normal,OutType.normal};
		boolean opcion1;
		String sTemp;
		

		try {
			System.out.println("¿Quieres que se cambien los valores? s/N");
			sTemp = reader.nextLine();
			opcion1 = sTemp.equalsIgnoreCase("s");
			
			SysAI.nodes = SysAI.createNodes(n, outType, opcion1);
			//Add random inputs (random data) future removal
			for (int i = 0; i < SysAI.nodes[0].length; i++) {
				SysAI.nodes[0][i].setVal(1);
			}
			
			
			// Se guarda los outputs en el array data. Después tengo que hacer que el programa interprete el output para que lo envie al juego o programa (Cambia en TODOS los proyectos)
			data = SysAI.think_game(SysAI.nodes);
			
			for (double d : data) {
				System.out.println(d);
			}
			
			System.out.println("Before:");
			for (int i = 0; i < SysAI.nodes.length; i++) {
				for (int j = 0 ; j < SysAI.nodes[i].length; j++) {
					if (SysAI.nodes[i][j] instanceof InputNode) {
						for (int k = 0; k < ((InputNode) SysAI.nodes[i][j]).getValues().length; k++) {
							System.out.print(((InputNode)SysAI.nodes[i][j]).getValues()[k] + " ");
						}
						System.out.println();
					}
					else if (SysAI.nodes[i][j] instanceof ConnectionNode) {
						for (int k = 0; k < ((ConnectionNode) SysAI.nodes[i][j]).getValues().length; k++) {
							System.out.print(((ConnectionNode)SysAI.nodes[i][j]).getValues()[k] + " ");
						}
						System.out.println();
					}
					else {
						break;
					}
				}
			}
			for (int i = 0; i < newData.length; i++) {
				newData[i] = Math.random() / 100;
			}
			SysAI.addLearningValues(SysAI.nodes, newData);
			
			System.out.println("\nAfter:");
			for (int i = 0; i < SysAI.nodes.length; i++) {
				for (int j = 0 ; j < SysAI.nodes[i].length; j++) {
					if (SysAI.nodes[i][j] instanceof InputNode) {
						for (int k = 0; k < ((InputNode) SysAI.nodes[i][j]).getValues().length; k++) {
							System.out.print(((InputNode)SysAI.nodes[i][j]).getValues()[k] + " ");
						}
						System.out.println();
					}
					else if (SysAI.nodes[i][j] instanceof ConnectionNode) {
						for (int k = 0; k < ((ConnectionNode) SysAI.nodes[i][j]).getValues().length; k++) {
							System.out.print(((ConnectionNode)SysAI.nodes[i][j]).getValues()[k] + " ");
						}
						System.out.println();
					}
				}
			}
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		reader.close();
	}
}