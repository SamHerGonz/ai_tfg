import ai.ConnectionNode;
import ai.InputNode;
import ai.NeuralMath;
import ai.NeuralNetwork;
import java.io.*;
import java.util.*;

public class MainAI {
    public static int minRange = 0;
    public static int maxRange = 255;
    public static boolean aBoolean = false;
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
        List<double[]> recordsTrain = new ArrayList<>();
        List<double[]> recordsTest = new ArrayList<>();
        List<double[]> expectedTrain = new ArrayList<>();
        List<double[]> expectedTest = new ArrayList<>();
        Scanner reader = new Scanner(System.in);

        // Search the 'ai.obj' file (if there isn't a 3rd argument, if not the 3rd), which has a Neural Network. If it doesn't exist, it creates a Neural Network
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(args.length == 3 ? args[2] : "src/data/ai.obj"))){
            ai = (NeuralNetwork)ois.readObject();
            ai.mutate();
        } catch (Exception e) {
            try {
                System.out.println("No se ha encontrado ninguna IA con el nombre indicado, creando nueva: ");
                // ai = createThinNeuralNetwork(shape);
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

        // Leer datos de la base de datos
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            readFile(recordsTrain, expectedTrain, br);
        } catch (IOException e) {
            System.out.println("No se ha podido encontrar los casos de prueba. Por favor, verifique que el archivo está ahí y que es el correcto");
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

        // Run neural network

        // I did this 10 times each, in total each way did 100000 numbers. The first method ended doing almost 2000 more numbers correctly than the second one
        // I thought it was just bad luck, since the second one should be better. But I realised it was because of the learning method, which, if its horribly wrong, the function doesn't change much the values.
        // Since the first ones are the ones in which it is more wrong, it doesn't change a lot in the first ones, and in the second method, the weights and biases are changed 10 times less than in the first one
        System.out.println("Inserte el método de aprendizaje:");
        System.out.println("0: Aprendizaje normal");
        System.out.println("1: Aprendizaje con mini-batch de tamaño customizado");
        n = reader.nextInt();
        System.out.println("Cual quieres que sea la tasa de aprendizaje (recomendado: 0,3 )");
        double lr = reader.nextDouble();
        double rate = lr;
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
                        ai.train(recordsTrain, expectedTrain, minRange, maxRange, rate);
                        int v = ai.verify(minRange,maxRange, recordsTest, expectedTest);
                        System.out.println("Se han completado " + v + " de " + recordsTest.size());
                        lr -= lr * 0.001;
                        rate = lr * (10000 - v) / 1000;
                    }
                    break;
                case 1:
                    System.out.println("Indique el tamaño del mini-batch: ");
                    int sizeMiniBatch = reader.nextInt();
                    reader.nextLine();
                    time = System.nanoTime();
                    for (int i = 0; i < iMax; i++) {
                        System.out.println("Vuelta " + i);
                        ai.train(recordsTrain, expectedTrain, minRange, maxRange, sizeMiniBatch, rate);
                        int v = ai.verify(minRange,maxRange, recordsTest, expectedTest);
                        System.out.println("Se han completado " + v + " de " + recordsTest.size());
                        lr -= lr * 0.001;
                        rate = lr * (10000 - v) / 1000;
                    }
                    break;
                default:
                    time = System.nanoTime();
                    ai.verify(minRange,maxRange, recordsTest, expectedTest);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Took " + ((System.nanoTime() - time) / 1000000000.0) + " seconds");
        System.out.println(ai);
        try {
            aBoolean = true;
            ai.verify(minRange,maxRange, recordsTest, expectedTest);
            for (int i = 0; i < ai.getNodes()[0].length; i++) {
                for (int j = 0; j < ai.getNodes()[1].length; j++) {
                    try {
                        ai.addWeight(0, i, j, 0);
                    } catch (Exception _) {}
                }
            }
            ai.verify(minRange,maxRange, recordsTest, expectedTest);
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

    private static void readFile(List<double[]> records, List<double[]> real, BufferedReader br) throws IOException {
        br.readLine();
        String line;
        double max = 1;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            double[] valuesDouble = new double[values.length - 1];
            double[] resultValue = new double[10];

            resultValue[Integer.parseInt(values[0])] = 1;
            for (int i = 1; i < valuesDouble.length; i++) {
                valuesDouble[i] = Double.parseDouble(values[i]);
                if (max <= valuesDouble[i]) {
                    max = valuesDouble[i];
                }
            }
            records.add(valuesDouble);
            real.add(resultValue);
        }
        maxRange = (int) max;
    }

    // Una prueba tonta, no te preocupes. Además, no funciona del t0d0 bien, pero me sirve para una pequeña prueba
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
}