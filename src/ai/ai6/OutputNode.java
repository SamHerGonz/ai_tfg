package ai.ai6;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class OutputNode extends Node implements Serializable {
	private ArrayList<Node> backLayer;
	private double bias;
    @Serial
    private static final long serialVersionUID = -4041293869028036396L;

	public OutputNode(double max) {
		setBackLayer(new ArrayList<>());
		setBias(Math.random() * (max * 2) - max);
	}


	public ArrayList<Node> getBackLayer() {
		return backLayer;
	}

	public void setBackLayer(ArrayList<Node> backLayer) {
		this.backLayer = backLayer;
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}


	public void addNodeBack(Node node) {
		getBackLayer().add(node);
	}

    public void transferExpectedLearning(double learningRate) {

        double sigmoidVal = NeuralMath.setSigmoid(getValue());
        for (int i = 0; i < getBackLayer().size(); i++) {
            Node n = getBackLayer().get(i);
            // Lo siento por crear esta abominación de código
            if (n instanceof ConnectionNode) {
                /*
                Tengo que cambiar esta línea.
                Ya he verificado, y la operación que se debe de hacer aquí es:
                El peso a editar * la función de activación derivada del valor del nodo (sin hacer la función de activación) * 2(no entiendo bien por qué) * (el valor(esta vez con la función de activación) - el valor esperado)
                No tengo garantizado que funcione, pero debería. Eso sí, con mi suerte no va a funcionar, pero lo probaré igualmente.
                No tengo muy claro cómo lo voy a hacer, creo que tengo que cambiar el cómo funciona la red neuronal, haciendo que se guarde el valor real en vez de la de activación, y luego la retropropagación haré la función.
                También puedo hacer que se guarde la función de activación y luego, para la retropropagación hago ingeniería inversa. El problema es que eso es complicarme la vida
                */
                n.addExpected(
                        ((ConnectionNode)n).getValFrontLayer().get(((ConnectionNode)n).getFrontLayer().indexOf(this)) *
                                NeuralMath.setDerivativeSigmoid(getValue()) *
                                2 * (sigmoidVal - getExpected()) * learningRate);
            }
        }
    }
}