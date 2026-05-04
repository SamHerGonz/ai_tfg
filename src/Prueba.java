import ai.InputNode;
import ai.NeuralNetwork;
import ai.OutputNode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Prueba {
    private static void verify(NeuralNetwork ai, List<double[]> recordsTest, List<Integer> expectedTest) throws Exception {
        int n;
        ai.setLearn(false);
        n = 0;
        for (int i = 0; i < recordsTest.size(); i++) {
            int max = ai.getAnswer(recordsTest.get(i),0,255,false);
            if (expectedTest.get(i) == max) {
                n++;
            }/*
            else {
                System.out.println("The realTrain data is the index " + i);
                double[] expectedData = new double[10];
                expectedData[expectedTest.get(i)] = 1;
                ai.run(recordsTest.get(i), 0, 255, expectedData, 1, true);
                System.out.println("Expected: " + expectedTest.get(i));
                System.out.println("Value guessed: " + max);
            }*/
        }
        System.out.println("Se han completado " + n + " de " + recordsTest.size());
    }

    public static void main(String[] args) {
        try{
            BufferedReader br1 = new BufferedReader(new FileReader("out/artifacts/AI_jar/mnist_test.csv"));
            br1.readLine();
            String s;
            String s2;
            int i = 0;
            while ((s = br1.readLine()) != null) {
                i++;
                BufferedReader br2 = new BufferedReader(new FileReader("out/artifacts/AI_jar/mnist_train.csv"));
                while ((s2 = br2.readLine()) != null) {
                    if (s.equals(s2)) {
                        System.out.println(s);
                    }
                }
                br2.close();
                if (i % 1000 == 0) System.out.println("Vuelta " + i);
            }
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
