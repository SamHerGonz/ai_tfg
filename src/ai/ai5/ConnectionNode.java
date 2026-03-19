package ai.ai5;

import java.io.Serializable;
import java.util.ArrayList;

public class ConnectionNode extends Node implements Serializable {
	private ArrayList<Node> frontLayer;
	private ArrayList<Double> valFrontLayer;
	private ArrayList<Node> backLayer;
	private double bias;

	
	public ConnectionNode(double max) {
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
	
	
	public void addNodeFront(Node node, double max) {
		getFrontLayer().add(node);
		getValFrontLayer().add(Math.random() * (max * 2) - max);
	}
	
	public void addNodeBack(Node node) {
		getBackLayer().add(node);
	}
	
	public void transferAllData() {
        addValue(bias);
        setValueSigmoid();
        for (int i = 0; i < getFrontLayer().size(); i++) {
			getFrontLayer().get(i).addValue(getValue() * getValFrontLayer().get(i));
		}
	}

    public void transferLearningData(double lerningRate) {
        for (int i = 0; i < getBackLayer().size(); i++) {
            Node n = getBackLayer().get(i);

            // Lo siento por crear esta abominación de código

            // Este código busca en el frontLayer de un nodo del array backLayer en qué posición está él mismo,
            // para luego irse a esa posición en el valFrontLayer de ese nodo que estamos usando en el backLayer,
            // para que el valor sea él mismo + el verdadero valor esperado puesto en los nodos *
            // un valor para señalar cuánta importancia queremos que le dé a ese aprendizaje
            if (n instanceof ConnectionNode) {
                n.addExpected(getExpected() * ((ConnectionNode)n).getValFrontLayer().indexOf(this));

                ((ConnectionNode)n).getValFrontLayer().set(
                        ((ConnectionNode)n).getFrontLayer().indexOf(this),
                        ((ConnectionNode)n).getValFrontLayer().get(((ConnectionNode)n).getFrontLayer().indexOf(this)) *
                                (getExpected() - getValue()) * lerningRate);
            }
            else if (n instanceof InputNode) {
                n.addExpected(getExpected() * ((InputNode)n).getValFrontLayer().indexOf(this));

                ((InputNode)n).getValFrontLayer().set(
                        ((InputNode)n).getFrontLayer().indexOf(this),
                        ((InputNode)n).getValFrontLayer().get(((InputNode)n).getFrontLayer().indexOf(this)) *
                                (getExpected() - getValue()) * lerningRate);
            }

            /*if (getBackLayer().get(i) instanceof ConnectionNode) {
                ((ConnectionNode)n).getValFrontLayer().set(
                        ((ConnectionNode)n).getFrontLayer().indexOf(this),
                        ((ConnectionNode)n).getValFrontLayer().get(
                                ((ConnectionNode)n).getFrontLayer().indexOf(this)) +
                                (getExpected() - getValue()) * lerningRate);
            }
            else if (getBackLayer().get(i) instanceof InputNode) {
                ((InputNode)n).getValFrontLayer().set(
                        ((InputNode)n).getFrontLayer().indexOf(this),
                        ((InputNode)n).getValFrontLayer().get(
                                ((InputNode)n).getFrontLayer().indexOf(this)) +
                                (getExpected() - getValue()) * lerningRate);
            }*/
        }
        setBias((getExpected() - getValue()) * lerningRate * NeuralMath.setDerivativeSigmoid(getBias()));
    }
	
	public void setValueSigmoid() {
        addValue(bias);
        setValue(NeuralMath.setSigmoid(getValue()));
	}

    public void setValueDerivativeSigmoid() {
        setValue(NeuralMath.setSigmoid(getValue()) * (1 - (NeuralMath.setSigmoid(getValue()))));
    }
}
