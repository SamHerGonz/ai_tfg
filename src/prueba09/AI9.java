package prueba09;

import java.util.Scanner;

public class AI9 {
	
	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		int[] n = {9,15,13,11,9};
		double[] data;
		/* The outType for different games:
		 * 1. Brawlhalla: 
		 * 	1, 2. the joystick (1 for x coordinates and 1 for y coordinates)
		 * 	3. Normal attack
		 * 	4. Special attack
		 * 	5. Jump
		 * 	6. Dash
		 * OutType[] outType = {OutType.normal,OutType.normal,OutType.yesNo,OutType.yesNo,OutType.yesNo,OutType.yesNo};
		 * 
		 */
		OutType[] outType = {OutType.normal,OutType.normal,OutType.normal,OutType.normal,OutType.normal,OutType.normal,OutType.normal,OutType.normal,OutType.normal};
		Node[][] nodes;
		boolean opcion1;
		String sTemp;
		

		try {
			System.out.println("¿Quieres que se cambien los valores? s/N");
			sTemp = reader.nextLine();
			opcion1 = sTemp.equalsIgnoreCase("s");
			
			nodes = SysAI.createNodes(n, outType, opcion1);
			//Add random inputs (random data) future removal
			for (int i = 0; i < nodes[0].length; i++) {
				nodes[0][i].setVal(0.1);
			}
			
			// Se guarda los outputs en el array data. Después tengo que hacer que el programa interprete el output para que lo envie al juego o programa (Cambia en TODOS los proyectos)
			data = SysAI.think_game(nodes);
			
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		reader.close();
	}
}