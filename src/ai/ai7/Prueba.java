package ai.ai7;

public class Prueba {
    public static void main(String[] args) {
        int[] shape = {3,2};
        NeuralNetwork ai = new NeuralNetwork(shape, 1);
        double[] data = {1,1,1};
        try{
            ai.run(data, 1, 1,0 ,1);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
