import com.sun.source.tree.NewArrayTree;

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
            /*for (int i = 0; i < ai.getNodes()[1].length; i++) {
                try {
                    ai.removeWeight(0,0,i);
                } catch (Exception ex) {
                    System.out.println("Error " + ex);
                }
            }
            for (int i = 0; i < ai.getNodes()[1].length; i++) {
                try {
                    ai.removeWeight(1,i,0);
                } catch (Exception ex) {
                    System.out.println("Error " + ex);
                }
            }
            try {
                ai.removeWeight(1,0,0);
            } catch (Exception ex) {
                System.out.println("Error " + ex);
            }*/
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
            for (int i = 0; i < 10; i++) {
                System.out.println("Vuelta " + i);
                for (int j = 0; j < records.size() - 1000; j++) {
                    ai.setLearn(true);

                    double[] expectedData = new double[10];
                    expectedData[real.get(j)] = 1;

                    ai.run(records.get(j), 0, 255, expectedData, 0.9, false);
                }
            }
            ai.setLearn(false);
            int n = 0;
            for (int i = 9001; i < records.size(); i++) {
                double[] expectedData = new double[10];
                expectedData[real.get(i)] = 1;
                System.out.println("The real data is the index " + i);
                double [] dataReceived = ai.run(records.get(i), 0, 255, expectedData, 1, true);

                int max = 0;
                for (int j = 1; j < dataReceived.length; j++) {
                    if (dataReceived[j] > dataReceived[max]) {
                        max = j;
                    }
                }
                System.out.println("Expected: " + real.get(i));
                System.out.println("Value guessed: " + max);
                if (real.get(i) == max) {
                    n++;
                }
            }
            System.out.println("Se han completado " + n + " de " + 1000);
        }
		catch(Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
        System.out.println("Took " + (double)((System.nanoTime() - time) / 1000000000) + " seconds");

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/ai.obj"));
            oos.writeObject(ai);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

    public static NeuralNetwork createThinNeuralNetwork(int[] shape) {
        NeuralNetwork ai = new NeuralNetwork(shape, 1);
        for (int i = 0; i < ai.getNodes().length - 1; i++) {
            for (int j = 0; j < ai.getNodes()[i].length; j++) {
                int randomNodeOutput = (int)((ai.getNodes()[i + 1].length) * Math.random());
                for (int k = 0; k < ai.getNodes()[i + 1].length; k++) {
                    try {
                        if (ai.getNodes()[i][j] instanceof InputNode) {
                            if (k != randomNodeOutput) {
                                ai.removeWeight(i, j, k);
                            }
                        } else if (ai.getNodes()[i][j] instanceof ConnectionNode) {
                            if (k != randomNodeOutput) {
                                ai.removeWeight(i, j, k);
                            }
                        }
                    } catch (Exception e1) {
                        randomNodeOutput = k;
                        k = 0;
                    }
                }
            }
        }
        return ai;
    }
}