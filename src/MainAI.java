import ai.*;

import java.io.*;
import java.util.*;

public class MainAI {
    public static void main(String[] args) {
        short half = Float.floatToFloat16(3.15F);
        float full = Float.float16ToFloat(half);
        for (int i = 0; i < 100; i++) {
            System.out.println(half);
            half = Float.floatToFloat16(full + 0.01F);
            full = Float.float16ToFloat(half);
        }
        System.out.println(full);
        if (args.length != 2) throw new RuntimeException("Debe de haber 2 parámetros");

        // Con un tamaño de {784,100,100,75,75,50,10} la tasa que mejor va es 0,04
        int[] shape = {784,75,10};
        SupervisedLearningNeuralNetwork ai;

        List<float[]> recordsTrain = new ArrayList<>();
        List<float[]> recordsTest = new ArrayList<>();
        List<float[]> expectedTrain = new ArrayList<>();
        List<float[]> expectedTest = new ArrayList<>();
        Scanner reader = new Scanner(System.in);

        // Leer datos de la base de datos
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            System.out.println("Leyendo " + args[0]);
            readFile(recordsTrain, expectedTrain, br);
        } catch (IOException e) {
            System.out.println("No se ha podido encontrar los casos de prueba. Por favor, verifique que el archivo está ahí y que es el correcto");
        }
        finally {
            System.out.println(args[0] + " leído correctamente");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
            System.out.println("Leyendo " + args[1]);
            readFile(recordsTest, expectedTest, br);
        } catch (IOException e) {
            System.out.println("No se ha podido encontrar los casos de prueba. Por favor, verifique que el archivo está ahí y que es el correcto");
        }
        finally {
            System.out.println(args[1] + " leído correctamente");
        }

        // Run neural network
        System.out.println("Cual quieres que sea la tasa de aprendizaje (recomendado: 0,05)");
        float rate = reader.nextFloat();
        System.out.println("Indique el tamaño del mini-batch: ");
        int sizeMiniBatch = reader.nextInt();
        long time = System.nanoTime();
        for (int i = 0; i < 1; i++) {
            System.out.println("IA N " + i);
            try {
                ai = new SupervisedLearningNeuralNetwork(shape, 1);
                System.out.println(ai);
                removeALotOfWeights(ai,0.6F);
                System.out.println(ai);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                // BufferedWriter bw = new BufferedWriter(new FileWriter("data.txt"));
                rate -= 0.005F;
                time = System.nanoTime();
                int v;
                for (int j = 0; j < 30; j++) {
                    List<float[]> records = moveAllData(recordsTrain,1);
                    ai.train(records, expectedTrain, sizeMiniBatch, rate);
                    v = verify(ai,recordsTest,expectedTest);
                    System.out.println("Vuelta: " + j + "\t" + v);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.println("En total tardó " + ((System.nanoTime() - time) / 1000000000.0) + " segundos");
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("aa" + i + ".obj"));
                oos.writeObject(ai);
                oos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static void readFile(List<float[]> records, List<float[]> real, BufferedReader br) throws IOException {
        br.readLine();
        String line;
        float max = 1;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            float[] valuesfloat = new float[values.length - 1];
            float[] resultValue = new float[10];

            resultValue[Integer.parseInt(values[0])] = 1;
            for (int i = 1; i < valuesfloat.length; i++) {
                valuesfloat[i] = Float.parseFloat(values[i]);
                if (max <= valuesfloat[i]) {
                    max = valuesfloat[i];
                }
            }
            records.add(valuesfloat);
            real.add(resultValue);
        }

        // Convertir los valores de los Inputs a un rango del 0 al 1. El motivo por el que no se calcula directamente en la red neuronal es que, en IAs más complejas, se va a necesitar varios datos de distintos rangos, con lo que
        for (float[] record : records) {
            for (int j = 0; j < record.length; j++) {
                record[j] /= max;
            }
        }
    }

    public static List<float[]> moveAllData(List<float[]> list, int pixels) {
        List<float[]> ret_value = new ArrayList<>();
        for (float[] val : list) {
            ret_value.add(NeuralMath.moveMatrix(val, 28, 28, (int) (Math.random() * (pixels * 2 + 1)) - pixels, (int) (Math.random() * (pixels * 2 + 1)) - pixels));
        }
        return ret_value;
    }

    public static int verify(SupervisedLearningNeuralNetwork ai, List<float[]> recordsTest, List<float[]> expectedTest) throws ExceptionInInitializerError {
        int n = 0;
        for (int i = 0; i < recordsTest.size(); i++) {
            int maxAnswer = NeuralMath.getMaxPosition(ai.getAnswer(recordsTest.get(i)));
            int maxResult = NeuralMath.getMaxPosition(expectedTest.get(i));
            if (maxResult == maxAnswer) {
                n++;
            }
        }
        return n;
    }

    // Una prueba tonta, no te preocupes. Además, no funciona del t0d0 bien, pero me sirve para una pequeña prueba
    public static NeuralNetwork createThinNeuralNetwork(int[] shape, int max) {
        NeuralNetwork ai;
        try {
            ai = new SupervisedLearningNeuralNetwork(shape, max);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (int j = 0; j < ai.getNodes()[0].length; j++) {
            int randomNodeOutput = (int)((ai.getNodes()[1].length) * Math.random());
            for (int k = 0; k < ai.getNodes()[1].length; k++) {
                try {
                    if (ai.getNodes()[0][j] instanceof InputNode) {
                        if (k != randomNodeOutput) {
                            if (((InputNode) ai.getNodes()[0][j]).getIdNodeFrontLayerContains(k)) {
                                ai.removeWeight(0, j, k);
                            }
                        }
                    }
                } catch (Exception _) {}
            }
        }
        return ai;
    }

    private static NeuralNetwork reverseNeuralNetwork(int[] shape, NeuralNetwork ai) throws Exception {
        NeuralNetwork ai2;
        ai2 = new SupervisedLearningNeuralNetwork(shape, 1);
        for (int i = 0; i < ai2.getNodes().length - 1; i++) {
            for (int j = 0; j < ai2.getNodes()[i].length; j++) {
                if (ai2.getNodes()[i][j] instanceof InputNode) {
                    for (int k = 0; k < ((InputNode)ai2.getNodes()[i][j]).getWeightsFrontLayer().length; k++) {
                        ((InputNode)ai2.getNodes()[i][j]).getWeightsFrontLayer()[k] =
                                -1 * ((InputNode)ai.getNodes()[i][j]).getWeightsFrontLayer()[k];
                    }
                }
                else {
                    for (int k = 0; k < ((ConnectionNode)ai2.getNodes()[i][j]).getWeightsFrontLayer().length; k++) {
                        ((ConnectionNode)ai2.getNodes()[i][j]).getWeightsFrontLayer()[k] =
                                -1 * ((ConnectionNode)ai.getNodes()[i][j]).getWeightsFrontLayer()[k];
                    }
                }
            }
        }
        return ai2;
    }

    public static void removeALotOfWeights(NeuralNetwork ai, float percent) {
        for (int i = 0; i < ai.getSizeTotal() - 2; i++) {
            for (int j = 0; j < ai.getNodes()[i].length; j++) {
                if (ai.getNodes()[i][j] instanceof InputNode) {
                    int size = ((InputNode) ai.getNodes()[i][j]).getIdNodeFrontLayer().length;
                    for (int k = 0; k < size; k++) {
                        if (Math.random() < percent) {
                            try {
                                ai.removeWeight(i, j, k);
                            } catch (Exception _) {}
                        }
                    }
                }
                else if (ai.getNodes()[i][j] instanceof ConnectionNode) {
                    int size = ((ConnectionNode) ai.getNodes()[i][j]).getIdNodeFrontLayer().length;
                    for (int k = 0; k < size; k++) {
                        if (Math.random() < percent) {
                            try {
                                ai.removeWeight(i, j, k);
                            } catch (Exception _) {}
                        }
                    }
                }
            }
        }
    }
}