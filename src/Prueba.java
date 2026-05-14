import ai.InputNode;
import ai.NeuralMath;
import ai.NeuralNetwork;
import ai.OutputNode;

import java.io.*;
import java.util.*;

public class Prueba {
    public static void verifyDuplicate(String[] s1, String[] s2) {
        for (String s : s1) {
            for (String string : s2) {
                if (s.equals(string)) {
                    System.out.println("Duplicado");
                }
            }
        }
    }

    public static void main(String[] args) {
        try{
            // Ejemplo valor inverso
            int[] shape = {2,2,2};
            NeuralNetwork ai1 = new NeuralNetwork(shape, 1);
            double[] temp = {1,1};
            long time = System.nanoTime();
            // A partir de la iteración 2000 (más o menos) empieza a aprender correctamente, eso cuando el rango de iniciación de los pesos y biases es 1.
            for (int i = 0; i < 100000; i++) {
                double[] data = new double[2];
                for (int j = 0; j < data.length; j++) {
                    data[j] = Math.random();
                }
                double[] answer = NeuralMath.subtractArrays(temp,data);
                ai1.run(data,answer,10,false);
                if (i % 20000 == 0) {
                    System.out.println("Vuelta: " + i);
                }
            }

            for (int i = 0; i < 10; i++) {
                double[] data = new double[2];
                for (int j = 0; j < data.length; j++) {
                    data[j] = Math.random();
                }
                System.out.println("Input: " + Arrays.toString(data));
                double marginError = 0;
                double[] answer = ai1.getAnswer(data);
                double[] expectedAnswer = NeuralMath.subtractArrays(temp,data);
                System.out.println("Output: " + Arrays.toString(answer));

                for (int j = 0; j < answer.length; j++) {
                    marginError += Math.pow(expectedAnswer[j] - answer[j], 2) / 2;
                }

                System.out.println("Error de margen: " + marginError);

            }
            System.out.println("Tiempo: " + ((System.nanoTime() - time) / 1000000000.0) + "\n\n\n");
            System.out.println(ai1);
            System.out.println(ai1.printDetailed());
            // Ejemplo XOR de 3 inputs
            List<double[]> dataAnswer = new ArrayList<>();
            double[][] dataUnparsed = {
                    {0,0,0},{0,0,1},
                    {0,1,0},
                    {1,0,0},{1,0,1},
                    {1,1,0},{1,1,1}
            };
            List<double[]> data2 = new ArrayList<>(Arrays.asList(dataUnparsed));
            double[] answer = {0,1,1,0,1,0,0,1};
            for (int i = 0; i < answer.length; i++) {
                dataAnswer.add(new double[1]);
                dataAnswer.get(i)[0] = answer[i];
            }

            shape = new int[]{3, 3, 1};
            NeuralNetwork ai2 = new NeuralNetwork(shape, 1);

            data2.add(new double[]{0,1,1});

            for (int i = 0; i < 100000; i++) {
                ai2.train(data2,dataAnswer,0.2);
            }

            for (int i = 0; i < data2.size(); i++) {
                System.out.println("Input: " + Arrays.toString(data2.get(i)));
                System.out.println("Output: " + Arrays.toString(ai2.getAnswer(data2.get(i))) + "\tEsperado: " + Arrays.toString(dataAnswer.get(i)));
            }
            System.out.println(ai2);
            System.out.println(ai2.printDetailed());

            dataUnparsed = new double[16][4];
            int mult = 1;
            for (int i = 0; i < dataUnparsed[0].length; i++) {
                mult *= 2;
                for (int j = 0; j < dataUnparsed.length; j+= mult) {
                    for (int k = 0; k < mult / 2; k++) {
                        dataUnparsed[j + k][i] = 1;
                    }
                }
            }

            List<double[]> data3 = Arrays.asList(dataUnparsed);
            double[] answer3 = new double[data3.size()];
            for (int i = 0; i < data3.size(); i++) {
                if ((data3.get(i)[0] == 1 || data3.get(i)[1] == 1) && !(data3.get(i)[2] == 1 && data3.get(i)[3] == 1)) {
                    answer3[i] = 1;
                }
            }
            for (int i = 0; i < answer.length; i++) {
                dataAnswer.add(new double[1]);
                dataAnswer.get(i)[0] = answer3[i];
            }

            // Ejemplo operación de 4 inputs
            shape = new int[]{4,4,4,1};

            NeuralNetwork ai3 = new NeuralNetwork(shape,1);

            for (int i = 0; i < 100000; i++) {
                ai3.train(data3,dataAnswer,0.2);
            }

            for (int i = 0; i < data3.size(); i++) {
                System.out.println("Input: " + Arrays.toString(data3.get(i)));
                System.out.println("Output: " + Arrays.toString(ai3.getAnswer(data3.get(i))) + "\tEsperado: " + Arrays.toString(dataAnswer.get(i)));
            }
            // Ejemplo de prueba en el que se puede ver que comprende qué es cada input, porque descubre que el número deseado es el primer input
            /*shape = new int[]{10,5,1};
            NeuralNetwork ai = new NeuralNetwork(shape, 1);

            for (int i = 0; i < 10000; i++) {
                double[] data = new double[10];
                double[] answer = new double[1];
                for (int j = 0; j < data.length; j++) {
                    data[j] = Math.random();
                }
                answer[0] = data[0];
                ai.run(data,answer,0.3,false);
            }
            for (int i = 0; i < 100; i++) {
                double[] data = new double[10];
                for (int j = 0; j < data.length; j++) {
                    data[j] = Math.random();
                }
                System.out.println(Arrays.toString(data));
                System.out.println(Arrays.toString(ai.getAnswer(data)));
            }*/
            /*BufferedReader br1 = new BufferedReader(new FileReader("out/artifacts/AI_jar/mnist_test.csv"));
            br1.readLine();
            String[] lineas1 = new String[10000];
            for (int i = 0; i <lineas1.length ; i++) {
                lineas1[i] = br1.readLine();
                if (lineas1[i] == null) {
                    break;
                }
            }
            br1.close();

            br1 = new BufferedReader(new FileReader("out/artifacts/AI_jar/mnist_train.csv"));
            br1.readLine();
            String[] lineas2 = new String[60000];
            for (int i = 0; i <lineas2.length ; i++) {
                lineas2[i] = br1.readLine();
                if (lineas2[i] == null) {
                    break;
                }
            }
            br1.close();

            br1 = new BufferedReader(new FileReader("out/artifacts/AI_jar/test_data.txt"));
            br1.readLine();
            String[] lineas3 = new String[10000];
            for (int i = 0; i <lineas3.length ; i++) {
                lineas3[i] = br1.readLine();
                if (lineas3[i] == null) {
                    break;
                }
            }
            br1.close();

            br1 = new BufferedReader(new FileReader("out/artifacts/AI_jar/training_data.txt"));
            br1.readLine();
            String[] lineas4 = new String[10000];
            for (int i = 0; i <lineas4.length ; i++) {
                lineas4[i] = br1.readLine();
                if (lineas4[i] == null) {
                    break;
                }
            }
            br1.close();

            verifyDuplicate(lineas1, lineas2);
            System.out.println("No hay duplicidad entre 1 y 2");
            verifyDuplicate(lineas1, lineas3);
            System.out.println("No hay duplicidad entre 1 y 3");
            verifyDuplicate(lineas1, lineas4);
            System.out.println("No hay duplicidad entre 1 y 4");
            verifyDuplicate(lineas2, lineas3);
            System.out.println("No hay duplicidad entre 2 y 3");
            verifyDuplicate(lineas2, lineas4);
            System.out.println("No hay duplicidad entre 2 y 4");
            verifyDuplicate(lineas3, lineas4);
            System.out.println("No hay duplicidad entre 3 y 4");
            /*List<double[]> recordsTest = new ArrayList<>();
            List<Integer> expectedTest = new ArrayList<>();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/data/aiMBSize10.obj"));
            try (BufferedReader br = new BufferedReader(new FileReader("src/data/mnist_test.csv"))) {
                br.readLine();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    double[] valuesDouble = new double[784];

                    expectedTest.add(Integer.parseInt(values[0]));

                    for (int i = 1; i < valuesDouble.length; i++) {
                        valuesDouble[i] = Double.parseDouble(values[i]);
                    }
                    recordsTest.add(valuesDouble);
                }
            } catch (IOException e) {
                System.out.println("No se ha podido encontrar los casos de prueba. Por favor, verifique que el archivo está ahí y que es el correcto");
            }

            NeuralNetwork ai = (NeuralNetwork)ois.readObject();
            verify(ai,recordsTest,expectedTest);
            /*Scanner reader = new Scanner(System.in);
            double n = reader.nextDouble();
            NeuralNetwork ai;
            long t = System.nanoTime();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/data/ai.obj"))) {
                ai = (NeuralNetwork) ois.readObject();
                System.out.println(ai);
            }
            catch (Exception _) {

            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/collections/ai.obj"))) {
                ai = (NeuralNetwork) ois.readObject();
                System.out.println(ai);
            }
            catch (Exception _) {

            }
            /*int[] shape3 = {2,3,2};
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/aiPrueba.obj"));

            ai.NeuralNetwork ai3 = new ai.NeuralNetwork(shape3,1);
            ai3.removeWeight(0,0,2);
            ai3.removeWeight(1,1,1);
            ai3.removeWeight(1,2,0);

            ((ai.InputNode)ai3.getNodes()[0][0]).getWeightsFrontLayer().set(0,0.05);
            ((ai.InputNode)ai3.getNodes()[0][0]).getWeightsFrontLayer().set(1,-0.1);
            ((ai.InputNode)ai3.getNodes()[0][1]).getWeightsFrontLayer().set(0,0.15);
            ((ai.InputNode)ai3.getNodes()[0][1]).getWeightsFrontLayer().set(1,-0.2);
            ((ai.InputNode)ai3.getNodes()[0][1]).getWeightsFrontLayer().set(2,0.3);

            ((ai.ConnectionNode)ai3.getNodes()[1][0]).getWeightsFrontLayer().set(0,-0.25);
            ((ai.ConnectionNode)ai3.getNodes()[1][0]).getWeightsFrontLayer().set(1,-0.3);
            ((ai.ConnectionNode)ai3.getNodes()[1][1]).getWeightsFrontLayer().set(0,0.35);
            ((ai.ConnectionNode)ai3.getNodes()[1][2]).getWeightsFrontLayer().set(0,0.4);

            ((ai.ConnectionNode)ai3.getNodes()[1][0]).setBias(0.3);
            ((ai.ConnectionNode)ai3.getNodes()[1][1]).setBias(0.5);
            ((ai.ConnectionNode)ai3.getNodes()[1][2]).setBias(-0.45);
            ((ai.OutputNode)ai3.getNodes()[2][0]).setBias(0.7);
            ((ai.OutputNode)ai3.getNodes()[2][1]).setBias(0.45);
            oos.writeObject(ai3);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/data/aiPrueba.obj"));
            ai.NeuralNetwork ai3 = (ai.NeuralNetwork) ois.readObject();

            for (int i = 0; i < 20000; i++) {
                double[] inputData = {Math.random(),Math.random()};
                double[] outputData = {1 - inputData[0],1 - inputData[1]};
                System.out.println("Input data: " + inputData[0] + " " + inputData[1]);
                ai3.run(inputData,0,1,outputData,0.5,true);
            }
            double[] inputData = {0.01,0.99};
            double[] outputData = {0.99,0.01};
            ai3.run(inputData,0,1,outputData,1,true);*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
