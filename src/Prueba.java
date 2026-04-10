import panelDrawNumber.DrawingPanel;
import panelDrawNumber.MouseClick;
import panelDrawNumber.actionButtons.ClearAction;
import panelDrawNumber.actionButtons.SendAction;

import javax.swing.*;
import java.awt.*;

public class Prueba {
    public static final int WIDTHDP = 28;
    public static final int HEIGTHDP = 28;
    public static void main(String[] args) {
        JFrame window = new JFrame("Título");
        DrawingPanel dp = new DrawingPanel(WIDTHDP, HEIGTHDP);
        MouseClick mouse = new MouseClick(window, dp, new Rectangle(0,0,dp.TILESIZE * WIDTHDP,dp.TILESIZE * WIDTHDP), 2, 50);
        JButton clearButton = new JButton("Clear");
        JButton sendButton = new JButton("Send to ai");

        clearButton.addActionListener(new ClearAction(dp));
        sendButton.addActionListener(new SendAction(dp));
        clearButton.setBounds(800,70,200,80);
        sendButton.setBounds(800,300,200,80);


        window.setSize(dp.TILESIZE * WIDTHDP + 300,dp.TILESIZE * WIDTHDP + 100);
        window.setResizable(false);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.add(dp);

        dp.setLayout(null);
        dp.add(clearButton);
        dp.add(sendButton);
        dp.addMouseListener(mouse);
        dp.addMouseMotionListener(mouse);

        dp.startThread();
        /*int[] shape = {3,4,3,1};
        NeuralNetwork ai = new NeuralNetwork(shape, 1);
        int[] shape2 = {2,5,3,4,2};
        NeuralNetwork ai2 = new NeuralNetwork(shape2, 1);
        double[] result = {0,1};
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
            ai2.removeWeight(0,0,0);
            ai2.removeWeight(1,0,2);
            ai2.removeWeight(1,2,1);
            ai2.removeWeight(2,2,3);
            ai.run(data[0][0], 0, 1,data[0][1],0.8, true);
            for (int i = 0; i < 10000; i++) {
                ai2.run(result, 0, 1,result,0.8, true);
            }
            ai2.setLearn(false);
            System.out.println("\n\n\nPrueba final:");
            ai2.run(result, 0, 1,result,0.5, true);
            for (int i = 0; i < 10000; i++) {
                for (int j = 0; j < data.length; j++) {
                    int n = (int)(Math.random() * data.length);
                    ai.run(data[n][0], 0, 1,data[n][1],0.8, true);
                }
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
