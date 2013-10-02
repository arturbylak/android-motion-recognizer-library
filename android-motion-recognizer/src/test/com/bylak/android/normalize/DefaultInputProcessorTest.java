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
    public void testProcessInput() throws Exception {

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
}
