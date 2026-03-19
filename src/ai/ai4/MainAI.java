package ai.ai4;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.NumberFormatException;

public class MainAI {

	public static void main(String[] args) {
		int[] shape = {784,16,16,10};
		NeuralNetwork ai = new NeuralNetwork(shape);
		List<double[]> records = new ArrayList<>();
		List<Integer> real = new ArrayList<Integer>();
		
		try (BufferedReader br = new BufferedReader(new FileReader("src/ai/ai3/data/mnist_test.csv"))) {
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				double[] valuesDouble = new double[784];
				
				real.add((int) Integer.parseInt(values[0]));
				
				for (int i = 1; i < valuesDouble.length; i++) {
					valuesDouble[i] = Double.parseDouble(values[i]);
				}
				records.add(valuesDouble);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
        try {
			ai.run(records.get(0), 0, 255, false);
            System.out.println(real.get(0));

		}
		catch(Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
	}
	
}