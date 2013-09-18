package com.bylak.network.layer;

import com.bylak.network.neural.Neuron;
import org.apache.commons.math.linear.RealMatrix;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 10.09.13
 * Time: 22:45
 * To change this template use File | Settings | File Templates.
 */
public final class PerceptionLayerProcessor implements LayerProcessor {
    @Override
    public void processLayer(Layer firstLayer, Layer secondLayer) {
        RealMatrix currentValues = firstLayer.createNeuronsValuesMatrix();
        List<Neuron> neurons = secondLayer.getNeurons();

        for (int i = 0; i < secondLayer.getNeuronsCount(); i++) {
            Neuron neuron = neurons.get(i);
            neuron.simulate(currentValues.transpose());
        }
    }
}
