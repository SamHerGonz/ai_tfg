package ai.ai8;

public class Prueba {
    public static void main(String[] args) {
        int[] shape = {3,3,1};
        NeuralNetwork ai = new NeuralNetwork(shape, 1);
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
            for (int i = 0; i < 100000; i++) {
                for (int j = 0; j < data.length; j++) {
                    int n = (int)(Math.random() * data.length);
                    ai.run(data[n][0], 0, 1,data[n][1],0.8, true);
                }
            }
            ai.setLearn(false);
            System.out.println("\n\n\nPruebas finales:");
            for (int i = 0; i < data.length; i++) {
                ai.run(data[i][0], 0, 1,data[i][1],0.5, true);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        double[][] a1 = new double[2][3];
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
