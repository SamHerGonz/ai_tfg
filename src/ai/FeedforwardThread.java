package ai;

import java.util.concurrent.Callable;

public class FeedforwardThread implements Callable<double[]> {
    private final Node node;
    private final double nodeValue;
    private final int sizeNextLayer;

    public FeedforwardThread(Node node, double nodeValue, int sizeNextLayer) {
        this.node = node;
        this.nodeValue = nodeValue;
        this.sizeNextLayer = sizeNextLayer;
    }

    @Override
    public double[] call() {
        double[] retValue;
        if (node instanceof InputNode) {
            retValue = ((InputNode)node).transferAllData(nodeValue, sizeNextLayer);
        }
        else {
            retValue = ((ConnectionNode)node).transferAllData(nodeValue, sizeNextLayer);
        }
        return retValue;
    }
}
