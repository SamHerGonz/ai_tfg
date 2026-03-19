package ai.ai2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainAI {

	public static void main(String[] args) {
		List<double[]> records = new ArrayList<>();
		List<Integer> real = new ArrayList<Integer>(); 
		try (BufferedReader br = new BufferedReader(new FileReader(".idea/src/ai/ai1/data/mnist_test.csv"))) {
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
		}
		
		//I want to create the AI that can identify numbers, but I want to add the "no number" output, so I'll add it later
		int[] aiSize = {784,16,16,10};
		NeuralNetwork ai1 = new NeuralNetwork(aiSize);

		for (int i = 0; i < 1; i++) {
			ai1.run(records.get(i), real.get(i));
		}
	}

}
