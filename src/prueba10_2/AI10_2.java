package prueba10_2;

import java.util.Scanner;

public class AI10_2 {
	
	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		int[] n = {3,4,5,3};
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
		OutType[] outType = {OutType.normal, OutType.normal, OutType.normal};
		boolean opcion1;
		String sTemp;
		Ai ai1;
		
		try {
			System.out.println("¿Quieres que se cambien los valores? s/N");
			sTemp = reader.nextLine();
			opcion1 = sTemp.equalsIgnoreCase("s");
			
			ai1 = new Ai(n, outType, opcion1);
			//Add random inputs (random data) future removal
			for (int i = 0; i < ai1.getNodes()[0].length; i++) {
				ai1.getNodes()[0][i].setVal(1);
			}
			
			// Se guarda los outputs en el array data. Después tengo que hacer que el programa interprete el output para que lo envie al juego o programa (Cambia en TODOS los proyectos)
			data = ai1.think_game();
			
			System.out.println("Before:");
			for (int i = 0; i < ai1.getNodes().length; i++) {
				for (int j = 0 ; j < ai1.getNodes()[i].length; j++) {
					if (ai1.getNodes()[i][j] instanceof InputNode) {
						for (int k = 0; k < ((InputNode) ai1.getNodes()[i][j]).getValues().length; k++) {
							System.out.print(((InputNode) ai1.getNodes()[i][j]).getValues()[k] + " ");
						}
						System.out.println();
					}
					else if (ai1.getNodes()[i][j] instanceof ConnectionNode) {
						for (int k = 0; k < ((ConnectionNode) ai1.getNodes()[i][j]).getValues().length; k++) {
							System.out.print(((ConnectionNode) ai1.getNodes()[i][j]).getValues()[k] + " ");
						}
						System.out.println();
					}
					else {
						break;
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