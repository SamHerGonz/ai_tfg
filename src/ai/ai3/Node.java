package ai.ai3;

import java.io.Serializable;

public abstract class Node implements Serializable {
	private double value;
    private double expected;
    private static final long serialVersionUID = -8207463873783980213L;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void addValue(double value) {
        setValue(getValue() + value);
    }


    public double getExpected() {
        return expected;
    }

    public void setExpected(double expected) {
        this.expected = expected;
    }

    public void addExpected(double expected) {
        setExpected(getExpected() + expected);
    }

    public void setExpectedSigmoid() {
        setExpected(1 / (1 + Math.pow(Math.E, - getExpected())));
    }
}
