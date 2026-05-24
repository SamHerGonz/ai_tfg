import ai.ConnectionNode;
import ai.InputNode;
import ai.NeuralMath;
import ai.NeuralNetwork;
import java.io.*;
import java.util.*;

public class MainAI {
	public static void main(String[] args) {
        if (args.length != 2) throw new RuntimeException("Debe de haber 2 parámetros");
        int n, n1, n2,n3;
        double d;
        // Tamaño 784,30,10 60 vueltas tiempo: 582,71 segundos (9,71 por vuelta, 70000 iteraciones(10000 + 5000 de MiniBatch 12))
        // Tamaño 784,16,16,10 60 vueltas tiempo: 370,453093 segundos (6,17 por vuelta, 70000 iteraciones(10000 + 5000 de MiniBatch 12))
        // Tamaño 784,16,16,10 con 1ª capa con el mínimo de pesos (784) 60 vueltas tiempo: 195.7935879 segundos (6,17 por vuelta, 70000 iteraciones(10000 + 5000 de MiniBatch 12))
        // Tamaño 784,16,16,10 60 vueltas tiempo: 811,1674603 segundos (13,52 por vuelta, 70000 iteraciones)
        // DefAi tamaño 784,24,24,10 60 vueltas moviendo el panel tiempo:  segundos ( por vuelta, 70000 iteraciones)
        int[] shape = {784,10};
        NeuralNetwork ai;
        try {
            ai = new NeuralNetwork(784,10,2, 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<double[]> recordsTrain = new ArrayList<>();
        List<double[]> recordsTest = new ArrayList<>();
        List<double[]> expectedTrain = new ArrayList<>();
        List<double[]> expectedTest = new ArrayList<>();
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

        do {
            boolean b = false;
            System.out.println("¿Quieres usar una IA ya creada? (s/N)");
            if (reader.nextLine().equalsIgnoreCase("s")) {
                System.out.print("Inserte el nombre de la IA: ");
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(reader.nextLine()));
                    ai = (NeuralNetwork) ois.readObject();
                } catch (Exception e) {
                    System.out.println("No se ha encontrado, creando una red neuronal");
                    b = true;
                }
            } else {
                b = true;
            }
            if (b) {
                System.out.println("¿Quieres crear una IA con profundidad? (s/N)");
                if (reader.nextLine().equalsIgnoreCase("s")) {
                    System.out.println("Inserte el valor máximo de iniciación de los pesos y biases");
                    try {
                        d = reader.nextDouble();
                        System.out.println("Indique el valor en el que las matrices se irán decrementando");
                        n2 = reader.nextInt();
                        reader.nextLine();
                        ai = new NeuralNetwork(784,10,n2, d);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    System.out.println("Inserte el número de capas ocultas que va a tener la red neuronal: ");
                    n = reader.nextInt();
                    shape = new int[n + 2];
                    shape[0] = 784;
                    shape[shape.length - 1] = 10;
                    for (int i = 1; i < shape.length - 1; i++) {
                        System.out.println("Inserte el tamaño de la capa " + i);
                        n = reader.nextInt();
                        shape[i] = n;
                    }
                    System.out.println("Inserte el valor máximo de iniciación de los pesos y biases");
                    try {
                        d = reader.nextDouble();
                        ai = new NeuralNetwork(shape, d);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    reader.nextLine();
                }
            }
            System.out.println("Quieres hacer alguna modificación extra a la red neuronal? (s/N)");
            if (reader.nextLine().equalsIgnoreCase("s")) {
                do {
                    System.out.println("Inserte opción: ");
                    System.out.println("0: Insertar nodo");
                    System.out.println("1: Insertar peso");
                    System.out.println("2: Eliminar peso");
                    System.out.println("3: Ver las conexiones de la red neuronal a fondo");
                    System.out.println("4: Salir");
                    n = reader.nextInt();
                    switch (n) {
                        case 0:
                            System.out.println("¿En qué capa?");
                            do {
                                n = reader.nextInt();
                            } while (n <= 0 || n >= shape.length - 1);
                            System.out.println("¿Quieres que tenga todas las conexiones o hacemos simplemente una aleatoria? (s/N)");
                            reader.nextLine();
                            ai.addNode(n, 1, reader.nextLine().equalsIgnoreCase("s"));
                            n = 0;
                            break;
                        case 1:
                            System.out.println("¿En qué capa?");
                            n = reader.nextInt();
                            System.out.println("¿En qué nodo?");
                            n1 = reader.nextInt();
                            System.out.println("¿A qué nodo de la siguiente capa?");
                            n2 = reader.nextInt();
                            System.out.println("Cual quieres que sea el valor máximo de la conexión");
                            n3 = reader.nextInt();
                            try {
                                ai.addWeight(n, n1, n2, n3);
                            } catch (Exception _) {
                            }
                            break;
                        case 2:
                            System.out.println("¿En qué capa?");
                            n = reader.nextInt();
                            System.out.println("¿En qué nodo?");
                            n1 = reader.nextInt();
                            System.out.println("¿A qué nodo de la siguiente capa?");
                            n2 = reader.nextInt();
                            try {
                                ai.removeWeight(n, n1, n2);
                            } catch (Exception _) {
                            }
                            break;
                        case 3:
                            System.out.println("IMPORTANTE: SE VAN A IMPRIMIR MUCHOS DATOS");
                            do {
                                System.out.println("1: Ver la red neuronal entera (detallado)");
                                System.out.println("2: Ver la red neuronal entera");
                                System.out.println("3: Ver una capa de la red neuronal (detallado)");
                                System.out.println("4: Ver un nodo de la red neuronal (detallado)");
                                System.out.println("5: Salir");
                                n = reader.nextInt();
                                switch (n) {
                                    case 1:
                                        System.out.println(ai.printDetailed());
                                        break;
                                    case 2:
                                        System.out.println(ai);
                                        break;
                                    case 3:
                                        System.out.println("¿Qué capa?");
                                        n = reader.nextInt();
                                        System.out.println(ai.printDetailed(n));
                                        n = 3;
                                        break;
                                    case 4:
                                        System.out.println("¿Qué capa?");
                                        n = reader.nextInt();
                                        System.out.println("¿Qué nodo?");
                                        n2 = reader.nextInt();
                                        System.out.println(ai.printDetailed(n, n2));
                                        break;
                                }
                            } while (n != 5);
                            break;
                    }
                } while (n != 4);
            }
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
                int firstVal = 0;
                switch (n) {
                    case 0:
                        time = System.nanoTime();
                        for (int i = 0; i < iMax; i++) {
                            reader.nextLine();
                            System.out.println("Vuelta " + i);
                            List<double[]> records = moveAllData(recordsTrain, 1);
                            ai.train(records, expectedTrain, rate);
                            int v = ai.verify(recordsTest, expectedTest);
                            System.out.println("Se han acertado " + v + " de " + recordsTest.size());
                            if (firstVal == 0) {
                                firstVal = recordsTest.size() - v;
                            }
                            lr -= lr * 0.001;
                            rate = lr * (10000 - v) / firstVal;
                        }
                        break;
                    case 1:
                        System.out.println("Indique el tamaño del mini-batch: ");
                        int sizeMiniBatch = reader.nextInt();
                        reader.nextLine();
                        time = System.nanoTime();
                        for (int i = 0; i < iMax; i++) {
                            System.out.println("Vuelta " + i);
                            List<double[]> records = moveAllData(recordsTrain, 1);
                            ai.train(records, expectedTrain, sizeMiniBatch, rate);
                            int v = ai.verify(recordsTest, expectedTest);
                            System.out.println("Se han acertado " + v + " de " + recordsTest.size());
                            if (firstVal == 0) {
                                firstVal = recordsTest.size() - v;
                            }
                            lr -= lr * 0.001;
                            rate = lr * (10000 - v) / firstVal;
                        }
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("En total tardó " + ((System.nanoTime() - time) / 1000000000.0) + " segundos");
            System.out.println(ai);
            try {
                System.out.println("Indique el nombre de la nueva red neuronal: ");
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(reader.nextLine() + ".obj"));
                oos.writeObject(ai);
                oos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("¿Quieres salir? s/N");
        } while (!reader.nextLine().equalsIgnoreCase("s"));
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

        // Convertir los valores de los Inputs a un rango del 0 al 1. El motivo por el que no se calcula directamente en la red neuronal es que, en IAs más complejas, se va a necesitar varios datos de distintos rangos, con lo que
        for (double[] record : records) {
            for (int j = 0; j < record.length; j++) {
                record[j] /= max;
            }
        }
    }

    public static List<double[]> moveAllData(List<double[]> list, int pixels) {
        List<double[]> ret_value = new ArrayList<>();
        for (double[] val : list) {
            ret_value.add(NeuralMath.moveMatrix(val, 28, 28, (int) (Math.random() * (pixels * 2 + 1)) - pixels, (int) (Math.random() * (pixels * 2 + 1)) - pixels));
        }
        return ret_value;
    }

    // Una prueba tonta, no te preocupes. Además, no funciona del t0d0 bien, pero me sirve para una pequeña prueba
    public static NeuralNetwork createThinNeuralNetwork(int[] shape, int max) {
        NeuralNetwork ai;
        try {
            ai = new NeuralNetwork(shape, max);
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

    private static NeuralNetwork reverseNeuralNetwork(int[] shape, NeuralNetwork ai) throws Exception {
        NeuralNetwork ai2;
        ai2 = new NeuralNetwork(shape, 1);
        for (int i = 0; i < ai2.getNodes().length - 1; i++) {
            for (int j = 0; j < ai2.getNodes()[i].length; j++) {
                if (ai2.getNodes()[i][j] instanceof InputNode) {
                    for (int k = 0; k < ((InputNode)ai2.getNodes()[i][j]).getWeightsFrontLayer().size(); k++) {
                        ((InputNode)ai2.getNodes()[i][j]).getWeightsFrontLayer().set(k,
                                -1 * ((InputNode)ai.getNodes()[i][j]).getWeightsFrontLayer().get(k));
                    }
                }
                else {
                    for (int k = 0; k < ((ConnectionNode)ai2.getNodes()[i][j]).getWeightsFrontLayer().size(); k++) {
                        ((ConnectionNode)ai2.getNodes()[i][j]).getWeightsFrontLayer().set(k,
                                -1 * ((ConnectionNode)ai.getNodes()[i][j]).getWeightsFrontLayer().get(k));
                    }
                }
            }
        }
        return ai2;
    }
}