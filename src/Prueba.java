import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Prueba {
    public static void main(String[] args) {
        try{
            /*int[] shape3 = {2,3,2};
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/aiPrueba.obj"));

            NeuralNetwork ai3 = new NeuralNetwork(shape3,1);
            ai3.removeWeight(0,0,2);
            ai3.removeWeight(1,1,1);
            ai3.removeWeight(1,2,0);

            ((InputNode)ai3.getNodes()[0][0]).getWeightsFrontLayer().set(0,0.05);
            ((InputNode)ai3.getNodes()[0][0]).getWeightsFrontLayer().set(1,-0.1);
            ((InputNode)ai3.getNodes()[0][1]).getWeightsFrontLayer().set(0,0.15);
            ((InputNode)ai3.getNodes()[0][1]).getWeightsFrontLayer().set(1,-0.2);
            ((InputNode)ai3.getNodes()[0][1]).getWeightsFrontLayer().set(2,0.3);

            ((ConnectionNode)ai3.getNodes()[1][0]).getWeightsFrontLayer().set(0,-0.25);
            ((ConnectionNode)ai3.getNodes()[1][0]).getWeightsFrontLayer().set(1,-0.3);
            ((ConnectionNode)ai3.getNodes()[1][1]).getWeightsFrontLayer().set(0,0.35);
            ((ConnectionNode)ai3.getNodes()[1][2]).getWeightsFrontLayer().set(0,0.4);

            ((ConnectionNode)ai3.getNodes()[1][0]).setBias(0.3);
            ((ConnectionNode)ai3.getNodes()[1][1]).setBias(0.5);
            ((ConnectionNode)ai3.getNodes()[1][2]).setBias(-0.45);
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
    }
}
