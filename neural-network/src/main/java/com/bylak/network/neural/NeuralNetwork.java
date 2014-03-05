package com.bylak.network.neural;

import java.util.ArrayList;
import java.util.List;

import com.bylak.network.layer.DefaultLayerExecutor;
import com.bylak.network.layer.Layer;
import com.bylak.network.layer.LayerExecutor;
import com.bylak.network.neural.teach.EpochData;
import com.bylak.network.neural.teach.NeutralNetworkTeachingAlgorithm;
import com.bylak.network.neural.teach.OneOutputBackPropagationAlgorithm;
import com.bylak.network.neural.teach.TeachConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 22.08.13
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public final class NeuralNetwork {
    private final List<Layer> layers;
    private double[] output;
    private final LayerExecutor layerExecutor;
    private final NeutralNetworkTeachingAlgorithm teachingAlgorithm;

    public NeuralNetwork() {
        this.layers = new ArrayList<Layer>();
        this.layerExecutor = new DefaultLayerExecutor();
        this.teachingAlgorithm = new OneOutputBackPropagationAlgorithm();
    }

    public void addLayer(final Layer layer) {
        this.layers.add(layer);
    }

    public void simulate() {
        layerExecutor.execute(layers);

        output = layers.get(layers.size() - 1).createNeuronsValues();
    }

    public double[] getOutput() {
        return this.output;
    }

    public void teach(final EpochData epochData, final TeachConfiguration teachConfiguration) {
        teachingAlgorithm.teach(this, epochData, teachConfiguration);
    }

    public int getLayersCount() {
        return this.layers.size();
    }

    public Layer getLayer(final int index) {
        return this.layers.get(index);
    }

    public void setInputs(final double[] values) {
        final Layer inputLayer = getLayer(0);
        for (int i = 0; i < getNeuronCountWithoutBias(); i++) {
            final Neuron inputNeuron = inputLayer.getNeuron(i);
            final double input = values[i];
            inputNeuron.setValue(input);
        }
    }

    private int getNeuronCountWithoutBias() {
        final Layer inputLayer = getLayer(0);
        return inputLayer.getNeuronsCount() - 1;
    }

    public int getInputCount() {
        final Layer inputLayer = getLayer(0);

        return inputLayer.getNeuronsCount();
    }
}
