package prueba10;

public class ConnectionNode extends Node {
	
	private static final long serialVersionUID = 6055705019397095014L;
	private double[] values;
	private int capa;
	
	public ConnectionNode() {
		setValues(new double[0]);
	}
	
	public ConnectionNode(int n, int capa) throws Exception {
		setValues(new double[n]);
		setCapa(capa);
	}
	
	public ConnectionNode(double[] values, int capa) throws Exception {
		setValues(values);
		setCapa(capa);
	}
	
	
	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	public int getCapa() {
		return capa;
	}

	public void setCapa(int capa) {
		this.capa = capa;
	}
	
	
	public void sendValues() {
		for (int i = 0; i < values.length; i++) {
			SysAI.nodes[capa + 1][i].addVal(getVal() * values[i]);
		}
	}
	
	public void learnNodes(double[] data) {
		if (SysAI.nodes[capa - 1][0] instanceof InputNode) {
			for (int i = 0; i < SysAI.nodes[capa - 1].length; i++) {
				for (int j = 0; j < ((InputNode)SysAI.nodes[capa - 1][i]).getValues().length; j++) {
					((InputNode) SysAI.nodes[capa - 1][i]).getValues()[j] += data[j];
				}
			}
		}
		else {
			for (int i = 0; i < SysAI.nodes[capa - 1].length; i++) {
				for (int j = 0; j < ((ConnectionNode)SysAI.nodes[capa - 1][i]).getValues().length; j++) {
					((ConnectionNode) SysAI.nodes[capa - 1][i]).getValues()[j] += data[j];
				}
			}
		}
		
	}
}
