package prueba10;

enum OutType {
	normal,
	yesNo,
	noNegative;
}

public class OutputNode extends Node {
	
	private static final long serialVersionUID = -134070040103608329L;
	private OutType type;
	
	
	public OutputNode(OutType type) {
		super();		
		setType(type);
	}
	
	
	public OutType getType() {
		return type;
	}

	public void setType(OutType type) {
		this.type = type;
	}
	
	
	public void learnNodes(double[] data) {
		for (int i = 0; i < SysAI.nodes[SysAI.nodes.length - 2].length; i++) {
			for (int j = 0; j < ((ConnectionNode)SysAI.nodes[SysAI.nodes.length - 2][i]).getValues().length; j++)
			((ConnectionNode) SysAI.nodes[SysAI.nodes.length - 2][i]).getValues()[j] += data[j];
		}
	}
	
	public void typeVal() {
		switch (type) {
		case OutType.yesNo:
			if (super.getVal() <= 0) {
				super.setVal(0);
			}
			else {
				super.setVal(1);
			}
			break;
		case OutType.noNegative:
			if (super.getVal() < 0) {
				super.setVal(0);
			}
			break;
		default:
			break;
		}
	}
}
