import ai.ConnectionNode;
import ai.InputNode;
import ai.NeuralNetwork;
import java.io.*;
import java.util.*;

public class MainAI {
    // En esta versión voy a intentar hacer que no se guarden los valores que tienen los nodos en los nodos, más bien que los tenga la red neuronal en una matriz
	public static void main(String[] args) {
        if (!(args.length == 2 || args.length == 3)) {
            throw new RuntimeException("Error de sintaxis: los parámetros deben ser: datos de entrenamiento\tdatos de verificación\t[Red neuronal a leer]");
        }
        int n;
        Integer [] temp;
        // Tamaño 784,30,10 60 vueltas tiempo: 582,71 segundos (9,71 por vuelta, 70000 iteraciones(10000 + 5000 de MiniBatch 12))
        // Tamaño 784,16,16,10 60 vueltas tiempo: 370,453093 segundos (6,17 por vuelta, 70000 iteraciones(10000 + 5000 de MiniBatch 12))
        // Tamaño 784,16,16,10 con 1ª capa con el mínimo de pesos (784) 60 vueltas tiempo: 195.7935879 segundos (6,17 por vuelta, 70000 iteraciones(10000 + 5000 de MiniBatch 12))
        // Tamaño 784,16,16,10 60 vueltas tiempo: 811,1674603 segundos (13,52 por vuelta, 70000 iteraciones)
        // DefAi tamaño 784,24,24,10 60 vueltas moviendo el panel tiempo:  segundos ( por vuelta, 70000 iteraciones)
        int[] shape = {784,30,10};
		NeuralNetwork ai;
        List<float[]> recordsTrain = new ArrayList<>();
        List<float[]> recordsTest = new ArrayList<>();
        List<Integer> expectedTrain = new ArrayList<>();
        List<Integer> expectedTest = new ArrayList<>();
        Scanner reader = new Scanner(System.in);

        // Search the 'ai.obj' file (if there isn't a 3rd argument, if not the 3rd), which has a Neural Network. If it doesn't exist, it creates a Neural Network
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(args.length == 3 ? args[2] : "src/data/ai.obj"))){
            ai = (NeuralNetwork)ois.readObject();
            ai.mutate();
        } catch (Exception e) {
            try {
                System.out.println("No se ha encontrado ninguna IA con el nombre indicado, creando nueva: ");
                ai = new NeuralNetwork(shape,1);
                System.out.println(ai);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            // ai = createThinNeuralNetwork(shape);
            /*for (int i = 0; i < ai.getNodes()[1].length; i++) {
                try {
                    ai.removeWeight(0,0,i);
                } catch (Exception ex) {
                    System.out.println("Error " + ex);
                }
            }
            for (int i = 0; i < ai.getNodes()[1].length; i++) {
                try {
                    ai.addWeight(0,0,i,1);
                } catch (Exception ex) {
                    System.out.println("Error " + ex);
                }
            }
            /*for (int i = 0; i < ai.getNodes()[1].length; i++) {
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
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            readFile(recordsTrain, expectedTrain, br);
        } catch (IOException e) {
            System.out.println("No se ha podido encontrar los casos de prueba. Por favor, verifique que el archivo está ahí y que es el correcto");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
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
        System.out.println("Inserte el método de aprendizaje:");
        System.out.println("0: Aprendizaje normal");
        System.out.println("1: Aprendizaje con mini-batch de tamaño customizado");
        n = reader.nextInt();
        System.out.println("Cual quieres que sea la tasa de aprendizaje (recomendado: 0,3 )");
        float lr = reader.nextFloat();
        float rate = lr;
        System.out.println("¿Cuántas iteraciones quieres que haya?");
        int iMax = reader.nextInt();
        long time = System.nanoTime();
        try {
            // BufferedWriter bw = new BufferedWriter(new FileWriter("data.txt"));
            switch (n) {
                case 0:
                    time = System.nanoTime();
                    for (int i = 0; i < iMax; i++) {
                        System.out.println("Vuelta " + i);
                        ai.setLearn(true);
                        for (int j = 0; j < recordsTrain.size(); j++) {

                            float[] expectedData = new float[10];
                            expectedData[expectedTrain.get(j)] = 1;

                                ai.run(recordsTrain.get(j), 0, 255, expectedData, rate, false);

                        }
                        verify(ai, recordsTest, expectedTest);
                        int v = verify(ai, recordsTest, expectedTest);
                        lr -= (float) (lr * 0.001);
                        rate = lr * (10000 - v) / 1000;
                    }
                    break;
                case 1:
                    System.out.println("Indique el tamaño del mini-batch: ");
                    int sizeMiniBatch = reader.nextInt();
                    reader.nextLine();
                    time = System.nanoTime();
                    for (int i = 0; i < iMax; i++) {
                        ai.setLearn(true);
                        System.out.println("Vuelta " + i);
                        // 0.002 segundos
                        Collections.shuffle(index_records);
                        // Este bucle 8.3 segundos (con el modelo normal)
                        // 3.1 segundos con el modelo con pocas conexiones
                        for (int j = 0; j < recordsTrain.size(); j += sizeMiniBatch) {
                            float[][] miniBatchData;
                            float[][] miniBatchExpected;
                            if (j + sizeMiniBatch > recordsTrain.size()) {
                                miniBatchData = new float[recordsTrain.size() - j][];
                                miniBatchExpected = new float[recordsTrain.size() - j][];
                            }
                            else {
                                miniBatchData = new float[sizeMiniBatch][];
                                miniBatchExpected = new float[sizeMiniBatch][];
                            }
                            for (int k = 0; k < miniBatchData.length; k++) {
                                float[] expectedData = new float[10];
                                expectedData[expectedTrain.get(index_records.get(j + k))] = 1;

                                miniBatchData[k] = recordsTrain.get(index_records.get(j + k));

                                miniBatchExpected[k] = expectedData;
                            }
                            ai.runMiniBatch(miniBatchData, 0, 255, miniBatchExpected, rate, false);
                        }
                        int v = verify(ai, recordsTest, expectedTest);
                        lr -= (float) (lr * 0.001);
                        rate = lr * (10000 - v) / 1000;
                        System.out.println("Tasa de aprendizaje: " + rate);
                    }
                    break;
                default:
                    time = System.nanoTime();
                    verify(ai, recordsTest, expectedTest);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Took " + ((System.nanoTime() - time) / 1000000000.0) + " seconds");
        System.out.println(ai);
        try {
            verify(ai, recordsTest, expectedTest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println("Indique el nombre de la nueva red neuronal: ");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(reader.nextLine() + ".obj"));
            oos.writeObject(ai);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

    private static int verify(NeuralNetwork ai, List<float[]> recordsTest, List<Integer> expectedTest) throws Exception {
        int n;
        ai.setLearn(false);
        n = 0;
        for (int i = 0; i < recordsTest.size(); i++) {
            int max = ai.getAnswer(recordsTest.get(i),0,255, false);
            if (expectedTest.get(i) == max) {
                n++;
            }
            /*else {
                System.out.println("The realTrain data is the index " + i);
                double[] expectedData = new double[10];
                expectedData[expectedTest.get(i)] = 1;
                ai.run(recordsTest.get(i), 0, 255, expectedData, 1, true);
                System.out.println("Expected: " + expectedTest.get(i));
                System.out.println("Value guessed: " + max);
            }*/
        }
        System.out.println("Se han completado " + n + " de " + recordsTest.size());
        return n;
    }

    private static void readFile(List<float[]> records, List<Integer> real, BufferedReader br) throws IOException {
        br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            float[] valuesDouble = new float[784];

            real.add(Integer.parseInt(values[0]));

            for (int i = 1; i < valuesDouble.length; i++) {
                valuesDouble[i] = Float.parseFloat(values[i]);
            }
            records.add(valuesDouble);
        }
    }

    // Una prueba tonta, no te preocupes. Además, no funciona, pero me sirve para una pequeña prueba
    public static NeuralNetwork createThinNeuralNetwork(int[] shape) {
        NeuralNetwork ai;
        try {
            ai = new NeuralNetwork(shape, 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (int j = 0; j < ai.getNodes()[0].length; j++) {
            int randomNodeOutput = (int)((ai.getNodes()[1].length) * Math.random());
            for (int k = 0; k < ai.getNodes()[1].length; k++) {
                try {
                    if (ai.getNodes()[0][j] instanceof InputNode) {
                        if (k != randomNodeOutput) {
                            if (((InputNode) ai.getNodes()[0][j]).getIdNodeFrontLayer().contains(k)) {
                                ai.removeWeight(0, j, k);
                            }
                        }
                    } else if (ai.getNodes()[0][j] instanceof ConnectionNode) {
                        if (k != randomNodeOutput) {
                            if (((ConnectionNode) ai.getNodes()[0][j]).getIdNodeFrontLayer().contains(k)) {
                                ai.removeWeight(0, j, k);
                            }
                        }
                    }
                } catch (Exception _) {}
            }
        }
        return ai;
    }

    public static double[] moveDrawing(double[] record, int x, int y) throws Exception {
        if (record.length != 784) {
            throw new Exception("");
        }
        int movePosX = 0;
        int movePosY = 0;
        int movePos;
        boolean b = false;
        if (x < 0) {
            for (int i = 0; i < 28; i++) {
                for (int j = 0; j < 28; j++) {
                    if (record[i * 28 + j] != 0) {
                        b = true;
                        break;
                    }
                }
                if (b) {
                    break;
                }
                movePosX--;
            }
            movePosX = Math.max(x, movePosX);
        }
        else {
            for (int i = 27; i > 0; i--) {
                for (int j = 0; j < 28; j++) {
                    if (record[j * 28 + i] != 0) {
                        b = true;
                        break;
                    }
                }
                if (b) {
                    break;
                }
                movePosX++;
            }
            movePosX = Math.min(x, movePosX);
        }

        b = false;

        if (y < 0) {
            for (int i = 0; i < 28; i++) {
                for (int j = 0; j < 28; j++) {
                    if (record[i * 28 + j] != 0) {
                        b = true;
                        break;
                    }
                }
                if (b) {
                    break;
                }
                movePosY--;
            }
            movePosY = Math.max(y, movePosY);
        }
        else {
            for (int i = 27; i > 0; i--) {
                for (int j = 0; j < 28; j++) {
                    if (record[i * 28 + j] != 0) {
                        b = true;
                        break;
                    }
                }
                if (b) {
                    break;
                }
                movePosY++;
            }
            movePosY = Math.min(y, movePosY);
        }

        movePos = movePosX + movePosY * 28;
        double[] ret_value = new double[record.length];
        if (movePos >= 0) {
            if (record.length - movePos >= 0)
                System.arraycopy(record, 0, ret_value, movePos, record.length - movePos);
        }
        else {
            if (record.length + movePos >= 0)
                System.arraycopy(record, -movePos, ret_value, 0, record.length + movePos);
        }
        return ret_value;
    }
}