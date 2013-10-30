package com.bylak.network.neural;

import com.bylak.network.layer.DefaultLayerExecutor;
import com.bylak.network.layer.Layer;
import com.bylak.network.layer.LayerExecutor;
import com.bylak.network.neural.teach.OneOutputBackPropagationAlgorithm;
import com.bylak.network.neural.teach.EpochData;
import com.bylak.network.neural.teach.NeutralNetworkTeachingAlgorithm;
import com.bylak.network.neural.teach.TeachConfiguration;

import java.util.ArrayList;
import java.util.List;

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
    private LayerExecutor layerExecutor;
    private NeutralNetworkTeachingAlgorithm teachingAlgorithm;

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

    public Layer getLayer(int index) {
        return this.layers.get(index);
    }

    public void setInputs(double[] values) {
        Layer inputLayer = getLayer(0);
        for (int i = 0; i < getNeuronCountWithoutBias(); i++) {
            Neuron inputNeuron = inputLayer.getNeuron(i);
            double input = values[i];
            inputNeuron.setValue(input);
        }
    }

    private int getNeuronCountWithoutBias(){
        Layer inputLayer = getLayer(0);
        return inputLayer.getNeuronsCount() - 1;
    }

    public int getInputCount() {
        Layer inputLayer = getLayer(0);

        return inputLayer.getNeuronsCount();
    }
}
