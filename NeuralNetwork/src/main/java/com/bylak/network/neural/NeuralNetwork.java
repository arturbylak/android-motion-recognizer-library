package com.bylak.network.neural;

import com.bylak.network.layer.DefaultLayerExecutor;
import com.bylak.network.layer.Layer;
import com.bylak.network.layer.LayerExecutor;
import com.bylak.network.neural.teach.BackPropagationAlgorithm;
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
    private double[] output;
    private LayerExecutor layerExecutor;
    private NeutralNetworkTeachingAlgorithm teachingAlgorithm;
    private final List<Layer> layers;

    public NeuralNetwork() {
        this.layers = new ArrayList<Layer>();
        this.layerExecutor = new DefaultLayerExecutor();
        this.teachingAlgorithm = new BackPropagationAlgorithm();
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
        teachingAlgorithm.teach(layers, epochData, teachConfiguration);
    }
}
