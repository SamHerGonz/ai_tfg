package ai.activationFunction;

public class SigmoidFunction extends ActivationFunction{
    /**
     * Función de activación de los nodos
     * @param n Valor
     * @return La funcion activada
     */
    @Override
    public double activateFunction(double n) {
        return 1 / (1 + Math.exp(- n));
    }

    /**
     *  La derivada de la función de activación de los nodos
     * @param n valor
     * @return La derivada de la funcion activada
     */
    @Override
    public double derActivateFunction(double n) {
        return n * (1 - n);
    }

    /**
     *  La derivada de la función de activación de los nodos
     * @param n valor
     * @return La derivada de la funcion activada
     */
    @Override
    public double derActivateFunctionNonActivated(double n) {
        double active = activateFunction(n);
        return active * (1 - active);
    }

}
