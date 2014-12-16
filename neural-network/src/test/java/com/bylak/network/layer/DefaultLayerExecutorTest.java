package com.bylak.network.layer;

import com.bylak.network.util.ArrayListBuilder;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 11.09.13
 * Time: 09:04
 * To change this template use File | Settings | File Templates.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Layer.class)
public class DefaultLayerExecutorTest {
    @Test
    public void testExecute() throws Exception {
        // given
        final LayerExecutor layerExecutor = new DefaultLayerExecutor();

        final Layer inputLayer = PowerMockito.mock(Layer.class);
        final Layer hiddenLayer = PowerMockito.mock(Layer.class);
        final Layer outputLayer = PowerMockito.mock(Layer.class);

        final List<Layer> neuronLayers = new ArrayListBuilder<Layer>()
                .add(inputLayer)
                .add(hiddenLayer)
                .add(outputLayer)
                .build();

        // when
        layerExecutor.execute(neuronLayers);

        // then
        Mockito.verify(hiddenLayer).processLayer(inputLayer);
        Mockito.verify(outputLayer).processLayer(hiddenLayer);
    }
}