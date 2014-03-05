package com.bylak.network.layer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;

import com.bylak.network.neural.Neuron;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 22.08.13
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */
public final class Layer {
    private final List<Neuron> neurons;
    private final LayerProcessor layerProcessor;

    private Layer(final List<Neuron> neurons) {
        this.neurons = neurons;
        this.layerProcessor = new PerceptionLayerProcessor();
    }

    public void processLayer(final Layer layer) {
        layerProcessor.processLayer(layer, this);
    }

    public RealMatrix createNeuronsValuesMatrix() {
        final double[] neuronsValues = createNeuronsValues();

        return MatrixUtils.createColumnRealMatrix(neuronsValues);
    }

    public double[] createNeuronsValues() {
        final int currentNeuronsCount = getNeuronsCount();
        final double[] neuronsValues = new double[currentNeuronsCount];

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

    public Neuron getNeuron(final int index) {
        return this.neurons.get(index);
    }

    public static class Builder {
        private final List<Neuron> neurons;

        public Builder() {
            this.neurons = new ArrayList<Neuron>();
        }

        public Builder addNeuron(final Neuron neuron) {
            this.neurons.add(neuron);

            return this;
        }

        public Layer build() {
            return new Layer(neurons);
        }
    }
}
