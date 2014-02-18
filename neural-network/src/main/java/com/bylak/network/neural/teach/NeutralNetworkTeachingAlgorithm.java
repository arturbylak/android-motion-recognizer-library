package com.bylak.network.neural.teach;

import com.bylak.network.neural.NeuralNetwork;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 11.09.13
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public interface NeutralNetworkTeachingAlgorithm {
    void teach(final NeuralNetwork neuralNetwork, final EpochData epochData, final TeachConfiguration teachConfiguration);
}
