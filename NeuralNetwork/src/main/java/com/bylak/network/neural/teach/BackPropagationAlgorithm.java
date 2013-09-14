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

    @Override
    public void teach(final List<Layer> layers, final EpochData epochData, final TeachConfiguration teachConfiguration) {
        int teachDataCount = epochData.getSize();
        int epochCount = teachConfiguration.getEpochCount();

        for (int i = 0; i < epochCount; i++) {
            for (int j = 0; j < teachDataCount; j++) {
                TeachData singleTeachData = epochData.getElement(0);

                propagate(singleTeachData, layers);
            }
        }
    }

    private void propagate(final TeachData singleTeachData, final List<Layer> layers) {

    }
}
