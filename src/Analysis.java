/*import ai.ConnectionNode;
import ai.InputNode;
import ai.NeuralNetwork;

import java.io.*;
import java.util.*;

public class Analysis {
    public static void main(String[] args) {
        int iMax = 2;
        Integer [] temp;
        int[] shape = {784,16,16,10};
        NeuralNetwork ai;
        List<double[]> recordsTrain = new ArrayList<>();
        List<double[]> recordsTest = new ArrayList<>();
        List<Integer> expectedTrain = new ArrayList<>();
        List<Integer> expectedTest = new ArrayList<>();
        Scanner reader = new Scanner(System.in);
        ObjectOutputStream oos;
        BufferedWriter bw;
        long time = System.nanoTime();

        System.out.println("Leyendo datos");
        // Read data of the database
        try (BufferedReader br = new BufferedReader(new FileReader("src/data/mnist_train.csv"))) {
            readFile(recordsTrain, expectedTrain, br);
        } catch (IOException e) {
            System.out.println("No se ha podido encontrar los casos de prueba. Por favor, verifique que el archivo está ahí y que es el correcto");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try (BufferedReader br = new BufferedReader(new FileReader("src/data/mnist_test.csv"))) {
            readFile(recordsTest, expectedTest, br);
        } catch (IOException e) {
            System.out.println("No se ha podido encontrar los casos de prueba. Por favor, verifique que el archivo está ahí y que es el correcto");
        }
        temp = new Integer[recordsTrain.size()];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = i;
        }
        List<Integer> index_records = Arrays.asList(temp);

        // Run neural network
        // I did this 10 times each, in total each way did 100000 numbers. The first method ended doing almost 2000 more numbers correctly than the second one
        // I thought it was just bad luck, since the second one should be better. But I realised it was because of the learning method, which, if its horribly wrong, the function doesn't change much the values.
        // Since the first ones are the ones in which it is more wrong, it doesn't change a lot in the first ones, and in the second method, the weights and biases are changed 10 times less than in the first one
        try {
            bw = new BufferedWriter(new FileWriter("data.txt"));
            for (int lap = 0; lap < 5; lap++) {
                double rate = 0;
                for (int i = 0; i < 10; i++) {
                    ai = new NeuralNetwork(shape, 1);
                    rate += 0.1;
                    oos = new ObjectOutputStream(new FileOutputStream("src/data/ai" + (rate * 10) + "_" + lap));
                    bw.write("IA método individual, learningRate=" + rate);
                    for (int j = 0; j < iMax; j++) {
                        System.out.println("Vuelta " + j);
                        ai.setLearn(true);
                        for (int k = 0; k < recordsTrain.size(); k++) {

                            double[] expectedData = new double[10];
                            expectedData[expectedTrain.get(k)] = 1;

                            ai.run(recordsTrain.get(k), 0, 255, expectedData, rate, false);
                        }

                        bw.write(verify(ai, recordsTest, expectedTest) + "\n");
                    }
                    oos.writeObject(ai);
                }
                for (int rate_count = 0; rate_count < 10; rate_count++) {
                    for (int MBSize = 0; MBSize < 10; MBSize++) {
                        oos = new ObjectOutputStream(new FileOutputStream("src/data/aiMB" + MBSize + "-" + (rate * 10)+ "_" + lap));
                        int sizeMiniBatch = 10 + MBSize;
                        ai = new NeuralNetwork(shape, 1);
                        for (int i = 0; i < iMax; i++) {
                            ai.setLearn(true);
                            System.out.println("Vuelta " + i);
                            // 0.002 segundos
                            Collections.shuffle(index_records);
                            // Este bucle 8.3 segundos (con el modelo normal)
                            // 3.1 segundos con el modelo con pocas conexiones
                            for (int j = 0; j < recordsTrain.size(); j += sizeMiniBatch) {
                                double[][] miniBatchData;
                                double[][] miniBatchExpected;
                                if (j + sizeMiniBatch > recordsTrain.size()) {
                                    miniBatchData = new double[recordsTrain.size() - j][];
                                    miniBatchExpected = new double[recordsTrain.size() - j][];
                                } else {
                                    miniBatchData = new double[sizeMiniBatch][];
                                    miniBatchExpected = new double[sizeMiniBatch][];
                                }
                                for (int k = 0; k < miniBatchData.length; k++) {
                                    double[] expectedData = new double[10];
                                    expectedData[expectedTrain.get(index_records.get(j + k))] = 1;

                                    miniBatchData[k] = recordsTrain.get(index_records.get(j + k));
                                    miniBatchExpected[k] = expectedData;
                                }
                                ai.runMiniBatch(miniBatchData, 0, 255, miniBatchExpected, rate, false);
                            }
                            String n = verify(ai, recordsTest, expectedTest);
                            bw.write(n + "\n");
                        }

                    }
                }
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Took " + ((System.nanoTime() - time) / 1000000000.0) + " seconds");
    }

    private static String verify(NeuralNetwork ai, List<double[]> recordsTest, List<Integer> expectedTest) throws Exception {
        int n;
        ai.setLearn(false);
        n = 0;
        for (int i = 0; i < recordsTest.size(); i++) {
            int max = ai.getAnswer(recordsTest.get(i),0,255);
            if (expectedTest.get(i) == max) {
                n++;
            }/*
            else {
                System.out.println("The realTrain data is the index " + i);
                ai.run(recordsTest.get(i), 0, 255, expectedData, 1, true);
                System.out.println("Expected: " + expectedTest.get(i));
                System.out.println("Value guessed: " + max);
            }
        }
        System.out.println("Se han completado " + n + " de " + recordsTest.size());
        return String.valueOf(n);
    }

    private static void readFile(List<double[]> records, List<Integer> real, BufferedReader br) throws IOException {
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
    }

    // Una prueba tonta, no te preocupes. Además, no funciona, pero me sirve para una pequeña prueba
    public static NeuralNetwork createThinNeuralNetwork(int[] shape) {
        NeuralNetwork ai = new NeuralNetwork(shape, 1);
        for (int i = 0; i < ai.getNodes().length - 1; i++) {
            for (int j = 0; j < ai.getNodes()[i].length; j++) {
                int randomNodeOutput = (int)((ai.getNodes()[i + 1].length) * Math.random());
                for (int k = 0; k < ai.getNodes()[i + 1].length; k++) {
                    try {
                        if (ai.getNodes()[i][j] instanceof InputNode) {
                            if (k != randomNodeOutput) {
                                if (((InputNode) ai.getNodes()[i][j]).getIdNodeFrontLayer().contains(k)) {
                                    ai.removeWeight(i, j, k);
                                }
                            }
                        } else if (ai.getNodes()[i][j] instanceof ConnectionNode) {
                            if (k != randomNodeOutput) {
                                if (((ConnectionNode) ai.getNodes()[i][j]).getIdNodeFrontLayer().contains(k)) {
                                    ai.removeWeight(i, j, k);
                                }
                            }
                        }
                    } catch (Exception _) {}
                }
            }
        }
        return ai;
    }

    // Todo: Fix
    public static void moveDrawing(double[] record, int x, int y) throws Exception {
        if (record.length != 784) {
            throw new Exception("");
        }

        // Move in the x coordinates
        if (x != 0) {
            if (x < 0) {
                for (int i = 0; i > x; i--) {
                    boolean b = false;
                    for (int j = 0; j < 28; j++) {
                        if (record[j * 28] != 0) {
                            b = true;
                            break;
                        }
                    }
                    if (!b) {
                        double n;
                        for (int j = 0; j < record.length - 1; j++) {
                            n = record[j + 1];
                            record[j] = n;
                        }
                        record[record.length - 1] = 0;
                    }
                    else {
                        break;
                    }
                }
            }
            else {
                for (int i = 0; i < x; i++) {
                    boolean b = false;
                    for (int j = 0; j < 28; j++) {
                        if (record[j * 28] != 0) {
                            b = true;
                            break;
                        }
                    }
                    if (!b) {
                        double n;
                        for (int j = record.length - 1; j > 0; j--) {
                            n = record[j - 1];
                            record[j] = n;
                        }
                        record[record.length - 1] = 0;
                    }
                    else {
                        break;
                    }
                }
            }
        }
    }
}*/
