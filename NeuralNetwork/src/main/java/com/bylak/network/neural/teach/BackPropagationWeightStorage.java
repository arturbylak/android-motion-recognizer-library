package com.bylak.network.neural.teach;

import com.bylak.network.layer.Layer;
import com.bylak.network.neural.NeuralNetwork;
import com.bylak.network.neural.Neuron;
import com.bylak.network.util.ArrayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 08.11.13
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public final class BackPropagationWeightStorage {

    private final List<Double[][]> wags;

    private BackPropagationWeightStorage(final List<Double[][]> wags) {
        this.wags = wags;
    }

    public void put(double wag, int layer, int neuronNumber, int inputNumber) {
        Double[][] layerWags = wags.get(layer);
        layerWags[neuronNumber][inputNumber] = wag;
    }

    public double get(int layer, int neuronNumber, int inputNumber) {
        Double[][] layerWags = get(layer);
        return layerWags[neuronNumber][inputNumber];
    }

    @Override
    protected BackPropagationWeightStorage clone() {
        return new BackPropagationWeightStorage(copyWags());
    }

    private List<Double[][]> copyWags(){
        List<Double[][]> copyOfWags = new ArrayList<>();

        for(int i=0; i<wags.size(); i++){
            Double[][] layerWags = wags.get(i);
            Double[][] copyOfLayerWags = ArrayUtil.copy(layerWags);
            copyOfWags.add(copyOfLayerWags);
        }

        return copyOfWags;
    }

    public void read(final NeuralNetwork neuralNetwork) {
        for (int i = 1; i < neuralNetwork.getLayersCount(); i++) {
            Layer currentLayer = neuralNetwork.getLayer(i);
            Layer previousLayer = neuralNetwork.getLayer(i - 1);
            for (int j = 0; j < currentLayer.getNeuronsCount(); j++) {
                Neuron currentNeuron = currentLayer.getNeuron(j);
                for (int k = 0; k < previousLayer.getNeuronsCount(); k++) {
                    double wag = currentNeuron.getWag(k);

                    wags.get(i)[j][k] = wag;
                }
            }
        }
    }

    public Double[][] get(int layer) {
        return wags.get(layer);
    }

    public static class Builder {
        private final List<Double[][]> wags;

        public Builder() {
            this.wags = new ArrayList<>();
        }

        public Builder createFrom(final NeuralNetwork neuralNetwork) {
            int layerCount = neuralNetwork.getLayersCount();
            int inputNeuronsCount = neuralNetwork.getLayer(0).getNeuronsCount();
            this.addLayer(inputNeuronsCount, 1);

            for (int i = 1; i < layerCount; i++) {
                Layer selectedLayer = neuralNetwork.getLayer(i);
                Layer previousLayer = neuralNetwork.getLayer(i - 1);
                int layerNeuronCount = selectedLayer.getNeuronsCount();
                int previousLayerNeuronCount = previousLayer.getNeuronsCount();
                this.addLayer(layerNeuronCount, previousLayerNeuronCount);
            }

            return this;
        }

        public Builder addLayer(int neuronCount, int neuronInputs) {
            Double[][] wags = new Double[neuronCount][neuronInputs];
            wags = ArrayUtil.clear(wags);
            this.wags.add(wags);

            return this;
        }

        public BackPropagationWeightStorage build() {
            return new BackPropagationWeightStorage(this.wags);
        }
    }
}
