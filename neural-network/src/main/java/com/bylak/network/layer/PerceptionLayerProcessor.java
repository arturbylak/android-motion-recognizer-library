package com.bylak.network.layer;

import java.util.List;

import org.apache.commons.math.linear.RealMatrix;

import com.bylak.network.neural.Neuron;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 10.09.13
 * Time: 22:45
 * To change this template use File | Settings | File Templates.
 */
public final class PerceptionLayerProcessor implements LayerProcessor {
    @Override
    public void processLayer(final Layer firstLayer, final Layer secondLayer) {
        final RealMatrix currentValues = firstLayer.createNeuronsValuesMatrix();
        final List<Neuron> neurons = secondLayer.getNeurons();

        for (int i = 0; i < secondLayer.getNeuronsCount(); i++) {
            final Neuron neuron = neurons.get(i);
            neuron.simulate(currentValues.transpose());
        }
    }
}
