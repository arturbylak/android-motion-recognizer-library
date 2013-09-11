package com.bylak.network.layer;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 10.09.13
 * Time: 22:42
 * To change this template use File | Settings | File Templates.
 */
public interface LayerProcessor {
    void processLayer(final Layer firstLayer, final Layer secondLayer);
}
