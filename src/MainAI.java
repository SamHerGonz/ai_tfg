import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainAI {
    // En esta versión voy a intentar hacer que no se guarden los valores que tienen los nodos en los nodos, más bien que los tenga la red neuronal en una matriz
	public static void main(String[] args) {
        int n;
        int[] shape = {784,16,16,10};
		NeuralNetwork ai;
		List<double[]> records = new ArrayList<>();
		List<Integer> real = new ArrayList<>();
        Scanner reader = new Scanner(System.in);

        // Search the ai.obj file, which has a Neural Network. If it doesn't exist, it creates a Neural Network
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

        // I did this 10 times each, in total each way did 100000 numbers. The first method ended doing almost 2000 more numbers correctly than the second one
        // I thought it was just bad luck, since the second one should be better. But I realised it was because of the learning method, which if its horribly wrong, the function doesn't change much the values.
        // Since the first ones are the ones in which it is more wrong, it doesn't change a lot in the first ones, and in the second method, the weights and biases are changed 10 times less than in the first one
        System.out.println("Inserte el método de aprendizaje:");
        System.out.println("0: Aprendizaje normal");
        System.out.println("1: Aprendizaje con mini-batch de 10");
        n = reader.nextInt();
        long time = System.nanoTime();
        try {
            switch (n) {
                case 0:
                    for (int i = 0; i < 1; i++) {
                        System.out.println("Vuelta " + i);
                        for (int j = 0; j < records.size(); j++) {
                            ai.setLearn(true);

                            double[] expectedData = new double[10];
                            expectedData[real.get(j)] = 1;

                            ai.run(records.get(j), 0, 255, expectedData, 0.9, false);
                        }
                    }
                    break;
                case 1:
                    for (int i = 0; i < 1; i++) {
                        System.out.println("Vuelta " + i);
                        for (int j = 0; j < records.size(); j += 10) {
                            double[][] miniBatchData = new double[10][];
                            double[][] miniBatchExpected = new double[10][];
                            for (int k = 0; k < 10; k++) {
                                double[] expectedData = new double[10];
                                expectedData[real.get(j + k)] = 1;

                                miniBatchData[k] = records.get(j + k);
                                miniBatchExpected[k] = expectedData;
                            }
                            ai.setLearn(true);

                            ai.runMiniBatch(miniBatchData, 0, 255, miniBatchExpected, 0.9, false);
                        }
                    }
                    break;
            }
            n = 0;
            ai.setLearn(false);
            for (int i = 0; i < records.size(); i++) {
                double[] expectedData = new double[10];
                expectedData[real.get(i)] = 1;
                double [] dataReceived = ai.run(records.get(i), 0, 255, expectedData, 1, false);

                int max = 0;
                for (int j = 1; j < dataReceived.length; j++) {
                    if (dataReceived[j] > dataReceived[max]) {
                        max = j;
                    }
                }
                if (real.get(i) == max) {
                    n++;
                }
                else {
                    System.out.println("The real data is the index " + i);
                    ai.run(records.get(i), 0, 255, expectedData, 1, true);
                    System.out.println("Expected: " + real.get(i));
                    System.out.println("Value guessed: " + max);
                }
            }
            System.out.println("Se han completado " + n + " de " + records.size());
        } catch (Exception e) {
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

    // Una prueba tonta, no te preocupes. Además, no funciona
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