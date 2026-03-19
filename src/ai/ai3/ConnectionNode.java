package ai.ai3;

import java.io.Serializable;
import java.util.ArrayList;

public class ConnectionNode extends Node implements Serializable {
	private ArrayList<Node> frontLayer;
	private ArrayList<Double> valFrontLayer;
	private ArrayList<Node> backLayer;
	private double bias;

	
	public ConnectionNode(int max) {
		setFrontLayer(new ArrayList<Node>());
		setValFrontLayer(new ArrayList<Double>());
		setBackLayer(new ArrayList<Node>());
        setBias(Math.random() * (max * 2) - max);
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
	
	
	public void addNodeFront(Node node, int max) {
		getFrontLayer().add(node);
		getValFrontLayer().add(Math.random() * (max * 2) - max);
	}
	
	public void addNodeBack(Node node) {
		getBackLayer().add(node);
	}
	
	public void transferAllData() {
        addValue(NeuralMath.setSigmoid(bias));
        setValue(getValue() / (getBackLayer().size() + 1));
        for (int i = 0; i < getFrontLayer().size(); i++) {
			getFrontLayer().get(i).addValue(NeuralMath.setSigmoid(getValue() * getValFrontLayer().get(i)));
		}
	}

    public void transferLearningData(double lerningRate) {
        for (int i = 0; i < getBackLayer().size(); i++) {
            Node n = getBackLayer().get(i);
            n.addExpected(getExpected());
            // Lo siento por crear esta abominación de código

            // No he verificado si funciona, aunque debería. Eso se lo dejo a mi yo del futuro (quiero mantener hoy mi sanidad intacta)

            // Este código busca en el frontLayer de un nodo del array backLayer en qué posición está él mismo,
            // para luego irse a esa posición en el valFrontLayer de ese nodo que estamos usando en el backLayer,
            // para que el valor sea él mismo + el verdadero valor esperado puesto en los nodos *
            // un valor para señalar cuánta importancia queremos que le dé a ese aprendizaje
            if (getBackLayer().get(i) instanceof ConnectionNode) {
                ((ConnectionNode)n).getValFrontLayer().set(
                        ((ConnectionNode)n).getFrontLayer().indexOf(this),
                        ((ConnectionNode)n).getValFrontLayer().get(
                                ((ConnectionNode)n).getFrontLayer().indexOf(this)) +
                                (getExpected() - getValue() - 0.5) * lerningRate);
            }
            else if (getBackLayer().get(i) instanceof InputNode) {
                ((InputNode)n).getValFrontLayer().set(
                        ((InputNode)n).getFrontLayer().indexOf(this),
                        ((InputNode)n).getValFrontLayer().get(
                                ((InputNode)n).getFrontLayer().indexOf(this)) +
                                (getExpected() - getValue() - 0.5) * lerningRate);
            }
        }
        setBias(getBias() + (getExpected() - getValue()) * lerningRate);
    }
	
	public void setValueSigmoid() {
		setValue(1 / (1 + Math.exp(- getValue())));
	}
}
