package com.bylak.android.normalize;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 12.10.13
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */
public class DefaultInputProcessorTest {
    @Test
    public void testProcessInputResize() throws Exception {
        //given
        InputProcessor inputProcessor = new DefaultInputProcessor();
        int expectedOutSize = 4;
        float[] valuesToResize = new float[]{1, 2};
        double[] expected = new double[]{0, 0.5, 1, 1};

        //when
        double[] result = inputProcessor.processInput(expectedOutSize, valuesToResize);

        //then
        for (int i = 0; i < result.length; i++) {
            Assert.assertEquals(expected[i], result[i], 0.01d);
        }
    }

    @Test
    public void testProcessInputNoChangeSize() throws Exception {
        //given
        InputProcessor inputProcessor = new DefaultInputProcessor();
        int expectedOutSize = 2;
        float[] valuesToResize = new float[]{1, 2};
        double[] expected = new double[]{0, 1};

        //when
        double[] result = inputProcessor.processInput(expectedOutSize, valuesToResize);

        //then
        for (int i = 0; i < result.length; i++) {
            Assert.assertEquals(expected[i], result[i], 0.01d);
        }
    }

    @Test
    public void testProcessInputCut() throws Exception {
        //given
        InputProcessor inputProcessor = new DefaultInputProcessor();
        int expectedOutSize = 2;
        float[] valuesToCut = new float[]{0f, 0.5f, 1f, 1f};
        double[] expected = new double[]{0, 1};

        //when
        double[] result = inputProcessor.processInput(expectedOutSize, valuesToCut);

        //then
        for (int i = 0; i < result.length; i++) {
            Assert.assertEquals(expected[i], result[i], 0.01d);
        }
    }
}
