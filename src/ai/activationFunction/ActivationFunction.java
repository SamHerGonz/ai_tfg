package ai.activationFunction;

import java.io.Serializable;

public abstract class ActivationFunction implements Serializable {
    /**
     * Función de activación de los nodos
     * @param n Valor
     * @return La funcion activada
     */
    public abstract double activateFunction(double n);

    /**
     *  La derivada de la función de activación de los nodos
     * @param n valor
     * @return La derivada de la funcion activada
     */
    public abstract double derActivateFunction(double n);

    /**
     *  La derivada de la función de activación de los nodos
     * @param n valor
     * @return La derivada de la funcion activada
     */
    public abstract double derActivateFunctionNonActivated(double n);
}
