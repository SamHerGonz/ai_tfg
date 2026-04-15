import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Prueba {
    public static void main(String[] args) {
        int[] shape = {3,4,3,1};
        NeuralNetwork ai = new NeuralNetwork(shape, 1);
        int[] shape2 = {2,5,3,4,2};
        NeuralNetwork ai2 = new NeuralNetwork(shape2, 1);
        ai.setLearn(true);
        double[][][] data = {
                {{0,0,0},{1}},
                {{0,0,1},{0}},
                {{0,1,0},{0}},
                {{0,1,1},{1}},
                {{1,0,0},{0}},
                {{1,0,1},{1}},
                {{1,1,0},{1}},
                {{1,1,1},{0}},
        };
        try{
            /*int[] shape3 = {2,2,2};
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/aiPrueba.obj"));

            NeuralNetwork ai3 = new NeuralNetwork(shape3,1);
            ((InputNode)ai3.getNodes()[0][0]).getWeightsFrontLayer().set(0,0.05);
            ((InputNode)ai3.getNodes()[0][0]).getWeightsFrontLayer().set(1,-0.1);
            ((InputNode)ai3.getNodes()[0][1]).getWeightsFrontLayer().set(0,0.15);
            ((InputNode)ai3.getNodes()[0][1]).getWeightsFrontLayer().set(1,-0.2);
            ((ConnectionNode)ai3.getNodes()[1][0]).getWeightsFrontLayer().set(0,-0.25);
            ((ConnectionNode)ai3.getNodes()[1][0]).getWeightsFrontLayer().set(1,-0.3);
            ((ConnectionNode)ai3.getNodes()[1][1]).getWeightsFrontLayer().set(0,0.35);
            ((ConnectionNode)ai3.getNodes()[1][1]).getWeightsFrontLayer().set(1,0.4);
            ((ConnectionNode)ai3.getNodes()[1][0]).setBias(0.3);
            ((ConnectionNode)ai3.getNodes()[1][1]).setBias(0.5);
            ((OutputNode)ai3.getNodes()[2][0]).setBias(0.7);
            ((OutputNode)ai3.getNodes()[2][1]).setBias(0.45);
            oos.writeObject(ai3);*/
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/data/aiPrueba.obj"));
            NeuralNetwork ai3 = (NeuralNetwork) ois.readObject();

            for (int i = 0; i < 20000; i++) {
                double[] inputData = {Math.random(),Math.random()};
                double[] outputData = {1 - inputData[0],1 - inputData[1]};
                System.out.println("Input data: " + inputData[0] + " " + inputData[1]);
                ai3.run(inputData,0,1,outputData,0.5,true);
            }
            double[] inputData = {0.01,0.99};
            double[] outputData = {0.99,0.01};
            ai3.run(inputData,0,1,outputData,1,true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
            /*

            ai2.removeWeight(0,1,0);
            ai2.removeWeight(1,0,2);
            ai2.removeWeight(1,2,1);
            ai2.removeWeight(2,2,3);
            ai.run(data[7][0], 0, 1,data[7][1],0.8, true);
            for (int i = 0; i < 10000; i++) {
                ai2.run(inputData, 0, 1,outputData,0.8, true);
            }
            ai2.setLearn(false);
            System.out.println("\n\n\nPrueba final:");
            ai2.run(inputData, 0, 1,outputData,0.5, true);
            for (int i = 0; i < 10000; i++) {
                int n = (int)(Math.random() * data.length);
                ai.run(data[n][0], 0, 1,data[n][1],0.8, true);
            }
            ai.setLearn(false);
            System.out.println("\n\n\nPruebas finales:");
            for (double[][] datum : data) {
                ai.run(datum[0], 0, 1, datum[1], 0.5, true);
                System.out.println("Resultado esperado: " + datum[1][0]);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        /*double[][] a1 = new double[2][3];
        double[][] a2 = new double[3][2];

        double count = 1;
        for (int i = 0; i < a1.length; i++) {
            for (int j = 0; j < a1[0].length; j++) {
                a1[i][j] = count;
                count++;
            }
        }

        count = 7;
        for (int i = 0; i < a2.length; i++) {
            for (int j = 0; j < a2[0].length; j++) {
                a2[i][j] = count;
                count++;
            }
        }

        a1 = (NeuralMath.multMatrix(a1, a2));
        for (double[] d : a1) {
            for (double d2: d) {
                System.out.println(d2);
            }
        }

        a1 = (NeuralMath.multMatrix(2.1, a1));
        for (double[] d : a1) {
            for (double d2: d) {
                System.out.println(d2);
            }
        }
        /*BigDecimal bd1 = new BigDecimal("1.00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001");
        BigDecimal bd2 = new BigDecimal(1.000000000000001);
        System.out.println(bd1);*/
    }
}
