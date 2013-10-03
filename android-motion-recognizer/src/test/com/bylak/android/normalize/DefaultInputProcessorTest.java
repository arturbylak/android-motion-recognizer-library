package com.bylak.android.normalize;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 02.10.13
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public class DefaultInputProcessorTest {
    @Test
    public void testProcessInput() {

        //given
        InputProcessor inputProcessor = new DefaultInputProcessor();
        double[] expected = new double[]{0.0, 0.5, 1};

        //when
        double[] result = inputProcessor.processInput(3, new float[]{1, 2, 3});

        //then
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], result[i], 0.001d);
        }
    }

    @Test
    public void testReduce() {

        //given
        InputProcessor inputProcessor = new DefaultInputProcessor();
        double[] expected = new double[]{0.0, 1};
        double[] expected2 = new double[]{0.0, 0.2, 0.4, 0.6};

        //when
        double[] result = inputProcessor.processInput(2, new float[]{1, 2, 3});
        double[] result2 = inputProcessor.processInput(4, new float[]{1, 2, 3, 4, 5, 6});

        //then
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], result[i], 0.001d);
        }

        for (int i = 0; i < result2.length; i++) {
            Assert.assertEquals(expected2[i], result2[i], 0.001d);
        }
    }

    @Test
    public void testResize() {

        //given
        InputProcessor inputProcessor = new DefaultInputProcessor();
        double[] expected = new double[]{0.0, 0.25, 0.5, 0.75, 1, 1};

        //when
        double[] result = inputProcessor.processInput(6, new float[]{0.0f, 0.5f, 1f});

        //then
        for (int i = 0; i < result.length; i++) {
            Assert.assertEquals(expected[i], result[i], 0.001d);
        }
    }


}
