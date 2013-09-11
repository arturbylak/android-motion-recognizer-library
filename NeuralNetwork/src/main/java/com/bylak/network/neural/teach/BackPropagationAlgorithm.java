package com.bylak.network.neural.teach;

import com.bylak.network.layer.Layer;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 11.09.13
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public final class BackPropagationAlgorithm implements NeutralNetworkTeachingAlgorithm {
    private final EpochData epochData;
    private int epochCount;
    private double maxErrorValue;

    public BackPropagationAlgorithm(final EpochData epochData, double maxErrorValue, int epochCount) {
        this.epochData = epochData;
        this.maxErrorValue = maxErrorValue;
        this.epochCount = epochCount;
    }

    @Override
    public void teach(final List<Layer> layers) {
        int teachDataCount = epochData.getSize();
        for (int i = 0; i < epochCount; i++) {
            for (int j = 0; j < teachDataCount; j++) {
                TeachData singleTeachData = getRandomTeachData();

                propagate(singleTeachData, layers);
            }
        }
    }

    private TeachData getRandomTeachData() {
        return epochData.getElement(0);
    }

    private void propagate(final TeachData singleTeachData, final List<Layer> layers) {

    }
}
