import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainAI {
    // En esta versión voy a intentar hacer que no se guarden los valores que tienen los nodos en los nodos, más bien que los tenga la red neuronal en una matriz
	public static void main(String[] args) {
        // Create Neural Network
        int[] shape = {784,16,16,10};
		NeuralNetwork ai;
		List<double[]> records = new ArrayList<>();
		List<Integer> real = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/data/ai.obj"))){
            ai = (NeuralNetwork)ois.readObject();
        } catch (Exception e) {
            ai = new NeuralNetwork(shape, 1);
        }

        // Read data of the database
        try (BufferedReader br = new BufferedReader(new FileReader("src/data/mnist_test.csv"))) {
            br.readLine();
            String line;
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
            System.out.println("No se ha podido encontrar los casos de prueba. Por favor, verifique que el archivo está ahí y que es el correcto");
		}

        // Run neural network
        long time = System.nanoTime();
        try {
            for (int i = 0; i < 100; i++) {
                System.out.println("Vuelta " + i);
                for (int j = 0; j < records.size(); j++) {
                    ai.setLearn(true);

                    double[] expectedData = new double[10];
                    expectedData[real.get(j)] = 1;

                    ai.run(records.get(j), 0, 255, expectedData, 0.9, false);
                }
            }
            ai.setLearn(false);
            for (int i = 0; i < 10; i++) {
                int data = (int) (Math.random() * records.size());
                double[] expectedData = new double[10];
                expectedData[real.get(data)] = 1;
                System.out.println("The real data is the index " + data);
                double [] dataReceived = ai.run(records.get(data), 0, 255, expectedData, 1, true);

                int max = 0;
                for (int j = 1; j < dataReceived.length; j++) {
                    if (dataReceived[j] > dataReceived[max]) {
                        max = j;
                    }
                }
                System.out.println("Expected: " + real.get(data));
                System.out.println("Value guessed: " + max);
            }
		}
		catch(Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
        System.out.println("Took " + (double)((System.nanoTime() - time) / 1000000000) + " seconds");

        /*try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/ai/ai8/data/ai.obj"));
            oos.writeObject(ai);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
	}
}