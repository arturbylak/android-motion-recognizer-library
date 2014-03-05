package com.bylak.network.neural.teach;

import java.util.ArrayList;
import java.util.List;

import com.bylak.network.layer.Layer;
import com.bylak.network.neural.NeuralNetwork;
import com.bylak.network.neural.Neuron;
import com.bylak.network.util.ArrayUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 08.11.13
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public final class BackPropagationWeightStorage implements Cloneable {

    private final List<Double[][]> wags;

    private BackPropagationWeightStorage(final List<Double[][]> wags) {
        this.wags = wags;
    }

    public void put(final double wag, final int layer, final int neuronNumber, final int inputNumber) {
        final Double[][] layerWags = wags.get(layer);
        layerWags[neuronNumber][inputNumber] = wag;
    }

    public double get(final int layer, final int neuronNumber, final int inputNumber) {
        final Double[][] layerWags = get(layer);
        return layerWags[neuronNumber][inputNumber];
    }

    @Override
    protected BackPropagationWeightStorage clone() {
        return new BackPropagationWeightStorage(copyWags());
    }

    private List<Double[][]> copyWags() {
        final List<Double[][]> copyOfWags = new ArrayList<>();

        for (int i = 0; i < wags.size(); i++) {
            final Double[][] layerWags = wags.get(i);
            final Double[][] copyOfLayerWags = ArrayUtil.copy(layerWags);
            copyOfWags.add(copyOfLayerWags);
        }

        return copyOfWags;
    }

    public void read(final NeuralNetwork neuralNetwork) {
        for (int i = 1; i < neuralNetwork.getLayersCount(); i++) {
            final Layer currentLayer = neuralNetwork.getLayer(i);
            final Layer previousLayer = neuralNetwork.getLayer(i - 1);
            for (int j = 0; j < currentLayer.getNeuronsCount(); j++) {
                final Neuron currentNeuron = currentLayer.getNeuron(j);
                for (int k = 0; k < previousLayer.getNeuronsCount(); k++) {
                    final double wag = currentNeuron.getWag(k);

                    wags.get(i)[j][k] = wag;
                }
            }
        }
    }

    public Double[][] get(final int layer) {
        return wags.get(layer);
    }

    public static class Builder {
        private final List<Double[][]> wags;

        public Builder() {
            this.wags = new ArrayList<>();
        }

        public Builder createFrom(final NeuralNetwork neuralNetwork) {
            final int layerCount = neuralNetwork.getLayersCount();
            final int inputNeuronsCount = neuralNetwork.getLayer(0).getNeuronsCount();
            this.addLayer(inputNeuronsCount, 1);

            for (int i = 1; i < layerCount; i++) {
                final Layer selectedLayer = neuralNetwork.getLayer(i);
                final Layer previousLayer = neuralNetwork.getLayer(i - 1);
                final int layerNeuronCount = selectedLayer.getNeuronsCount();
                final int previousLayerNeuronCount = previousLayer.getNeuronsCount();
                this.addLayer(layerNeuronCount, previousLayerNeuronCount);
            }

            return this;
        }

        public Builder addLayer(final int neuronCount, final int neuronInputs) {
            Double[][] localWags = new Double[neuronCount][neuronInputs];
            localWags = ArrayUtil.clear(localWags);
            this.wags.add(localWags);

            return this;
        }

        public BackPropagationWeightStorage build() {
            return new BackPropagationWeightStorage(this.wags);
        }
    }
}
