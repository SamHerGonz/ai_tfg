package prueba08_3enRaya;

enum OutType {
	normal,
	yesNo,
	noNegative;
}

public class OutputNode extends Node {
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
