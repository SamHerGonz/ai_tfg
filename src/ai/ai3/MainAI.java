package ai.ai3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainAI {

	public static void main(String[] args) {
        int[] shape = {784,16,16,10};
		NeuralNetwork ai;
		List<double[]> records = new ArrayList<>();
		List<Integer> real = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/ai/ai3/data/ai.obj"))){
            ai = (NeuralNetwork)ois.readObject();
        } catch (Exception e) {
            ai = new NeuralNetwork(shape, 3);
        }
        try (BufferedReader br = new BufferedReader(new FileReader("src/ai/ai3/data/mnist_test.csv"))) {
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				double[] valuesDouble = new double[784];
				
				real.add(Integer.parseInt(values[0]));
				
				for (int i = 1; i < valuesDouble.length; i++) {
					valuesDouble[i] = Double.parseDouble(values[i]);
				}
				records.add(valuesDouble);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
            // Desde aquí se puede ver el problema: POR ALGÚN MOTIVO DA COMPLETAMENTE IGUAL EL VALOR QUE LE PASES (el número a analizar) NO CAMBIA EL VALOR
            for (int i = 0; i < 1000; i++) {
                System.out.println(i + ": " + real.get(i));
                ai.run(records.get(i), 0, 255, true, real.get(i), 1);
                System.out.println("After: ");
                ai.run(records.get(i), 0, 255, false, real.get(i), 0.1);
            }
		}
		catch(Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/ai/ai3/data/ai.obj"));
            oos.writeObject(ai);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
}