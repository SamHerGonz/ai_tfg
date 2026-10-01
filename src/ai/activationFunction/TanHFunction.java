package ai.activationFunction;

public class TanHFunction extends ActivationFunction {
    @Override
    public double activateFunction(double n) {
        double active = Math.exp(n);
        active = (active - (1 / active)) / ((1 / active) + active);
        if (Double.isNaN(active)) {
            if (n < 0) {
                active = -1;
            }
            else {
                active = 1;
            }
        }
        return active;
    }

    @Override
    public double derActivateFunction(double n) {
        return 1 - Math.pow(n,2);
    }

    @Override
    public double derActivateFunctionNonActivated(double n) {
        return 1 - Math.pow(Math.exp(n),2);
    }
}
