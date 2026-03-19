package ai.ai6;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class InputNode extends Node implements Serializable{
	private ArrayList<Node> frontLayer;
	private ArrayList<Double> valFrontLayer;
    @Serial
    private static final long serialVersionUID = -7693503413333452026L;


    public InputNode() {
		setFrontLayer(new ArrayList<>());
		setValFrontLayer(new ArrayList<>());
	}
	
	
	public ArrayList<Node> getFrontLayer() {
		return frontLayer;
	}
	
	public void setFrontLayer(ArrayList<Node> frontLayer) {
		this.frontLayer = frontLayer;
	}
	
	public ArrayList<Double> getValFrontLayer() {
		return valFrontLayer;
	}
	
	public void setValFrontLayer(ArrayList<Double> valFrontLayer) {
		this.valFrontLayer = valFrontLayer;
	}
	
	// Convierte un número en un rango entre el minRange al MaxRange en un número del 0 al 1
	public void setValueSigmoid(int minRange, int maxRange) {
		setValue(getValue() / (maxRange - minRange) - minRange);
	}
	
	public void addNodeFront(Node node, double max) {
		getFrontLayer().add(node);
		getValFrontLayer().add(Math.random() * (max * 2) - max);
	}


    public void transferAllData(int minRange,int maxRange) {
        setValueSigmoid(minRange, maxRange);
        for (int i = 0; i < getFrontLayer().size(); i++) {
            getFrontLayer().get(i).addValue(getValue() * getValFrontLayer().get(i));
        }
    }

    public void getLearningData(double learningRate) {
        double sigmoidVal = NeuralMath.setSigmoid(getValue());
        for (int i = 0; i < valFrontLayer.size(); i++) {
            double n = valFrontLayer.get(i);
            /*
            Tengo que cambiar esta línea.
            Ya he verificado, y la operación que se debe de hacer aquí es:
            La activación de la antigua neurona * la función de activación derivada del valor del nodo (sin hacer la función de activación) * 2(no entiendo bien por qué) * (el valor(esta vez con la función de activación) - el valor esperado)
            No tengo garantizado que funcione, pero debería. Eso sí, con mi suerte no va a funcionar, pero lo probaré igualmente.
            No tengo muy claro cómo lo voy a hacer, creo que tengo que cambiar el cómo funciona la red neuronal, haciendo que se guarde el valor real en vez de la de activación, y luego la retropropagación haré la función.
            También puedo hacer que se guarde la función de activación y luego, para la retropropagación hago ingeniería inversa. El problema es que eso es complicarme la vida
            */
            valFrontLayer.set(i,
                    valFrontLayer.get(i) +
                            sigmoidVal * NeuralMath.setDerivativeSigmoid(frontLayer.get(i).getValue()) *
                                    2 * (frontLayer.get(i).getExpected() - NeuralMath.setSigmoid(frontLayer.get(i).getValue())) * learningRate);

            addExpected(
                    n * NeuralMath.setDerivativeSigmoid(frontLayer.get(i).getValue()) *
                            2 * (frontLayer.get(i).getExpected() - NeuralMath.setSigmoid(frontLayer.get(i).getValue())) * learningRate);
        }
    }

}