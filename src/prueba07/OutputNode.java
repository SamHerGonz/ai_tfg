package prueba07;

enum Type {
	normal,
	yesNo,
	noNegative;
}

public class OutputNode extends Node {
	private Type type;

	public OutputNode(Type type) {
		super();		
		setType(type);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public void typeVal() {
		switch (type) {
		case Type.yesNo:
			if (super.getVal() <= 0) {
				super.setVal(0);
			}
			else {
				super.setVal(1);
			}
			break;
		case Type.noNegative:
			if (super.getVal() < 0) {
				super.setVal(0);
			}
			break;
		default:
			break;
		}
	}
}
