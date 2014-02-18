package com.bylak.network.layer;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 10.09.13
 * Time: 22:59
 * To change this template use File | Settings | File Templates.
 */
public interface LayerExecutor {
    void execute(final List<Layer> layers);
}
