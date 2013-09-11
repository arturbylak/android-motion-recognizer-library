package com.bylak.network.layer;

import com.bylak.network.neural.Neuron;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 22.08.13
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */
public final class Layer {
    private List<Neuron> neurons;
    private LayerProcessor layerProcessor;

    public Layer(final List<Neuron> neurons) {
        this.neurons = neurons;
        this.layerProcessor = new PerceptionLayerProcessor();
    }

    public void calculateNeuronsValues(final Layer layer) {
        layerProcessor.processLayer(layer, this);
    }

    public RealMatrix createNeuronsValuesMatrix() {
        double[] neuronsValues = createNeuronsValues();

        return MatrixUtils.createColumnRealMatrix(neuronsValues);
    }

    public double[] createNeuronsValues() {
        int currentNeuronsCount = getNeuronsCount();
        double[] neuronsValues = new double[currentNeuronsCount];

        for (int i = 0; i < currentNeuronsCount; i++) {
            neuronsValues[i] = neurons.get(i).getValue();
        }

        return neuronsValues;
    }

    public int getNeuronsCount() {
        return neurons.size();
    }

    public List<Neuron> getNeurons() {
        return this.neurons;
    }
}
