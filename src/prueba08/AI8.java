package prueba08;

import java.util.Scanner;

// Voy a hacer ahora una prueba para ver cómo puede funcionar con el juego 3 en raya

public class AI8 {
	
	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		int[] n = {9,15,13,11,9};
		double[] data;
		OutType[] outType = {OutType.normal,OutType.normal,OutType.normal,OutType.normal,OutType.normal,OutType.normal,OutType.normal,OutType.normal,OutType.normal};
		Node[][] nodes;
		boolean opcion1;
		String s;
		

		try {
			System.out.println("¿Quieres que se cambien los valores? S/N");
			s = reader.nextLine();
			opcion1 = s.equalsIgnoreCase("s");
			
			nodes = SysAI.createNodes(n, outType, opcion1);
						
			//Add random inputs (random data) future removal
			for (int i = 0; i < nodes[0].length; i++) {
				nodes[0][i].setVal(Math.floor(Math.random() * 3));
				System.out.println(nodes[0][i].getVal());
			}
			System.out.println("\n");
			
			// Se guarda los outputs en el array data. Después tengo que hacer que el programa interprete el output para que lo envie al juego o programa (Cambia en TODOS los proyectos)
			data = SysAI.think_dynamic_game(nodes);
			for (int i = 0; i < data.length; i++) {
				System.out.println(data[i]);
			}
			
		} catch (Exception e) {
			e.getMessage();
		}
		reader.close();
	}
}