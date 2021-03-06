package com.bylak.network.layer;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 10.09.13
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */
public final class DefaultLayerExecutor implements LayerExecutor {
    @Override
    public void execute(final List<Layer> layers) {
        for (int i = 0; i < layers.size() - 1; i++) {
            final Layer layer = layers.get(i);
            final Layer nextLayer = layers.get(i + 1);
            nextLayer.processLayer(layer);
        }
    }
}
