package ai.ai7;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainAI {

	public static void main(String[] args) {
        int[] shape = {784,16,16,10};
		NeuralNetwork ai;
		List<double[]> records = new ArrayList<>();
		List<Integer> real = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/ai/ai7/data/ai.obj"))){
            ai = (NeuralNetwork)ois.readObject();
        } catch (Exception e) {
            ai = new NeuralNetwork(shape, 1);
        }
        try (BufferedReader br = new BufferedReader(new FileReader("src/ai/ai7/data/mnist_test.csv"))) {
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
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < 100; j++) {
                    ai.setLearn(true);
                    System.out.println(j + ": " + real.get(j));
                    ai.run(records.get(j), 0, 255, real.get(j), 0.1);
                    ai.setLearn(false);
                    System.out.println("After: ");
                    ai.run(records.get(j), 0, 255, real.get(j), 1);

                }
            }
		}
		catch(Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/ai/ai7/data/ai.obj"));
            oos.writeObject(ai);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
}