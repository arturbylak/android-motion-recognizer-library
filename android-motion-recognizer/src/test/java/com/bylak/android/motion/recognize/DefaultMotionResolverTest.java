package com.bylak.android.motion.recognize;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 12.10.13
 * Time: 17:24
 * To change this template use File | Settings | File Templates.
 */
public class DefaultMotionResolverTest {
    private final Map<MotionType, Double[]> simulationOutput;

    public DefaultMotionResolverTest() {
        simulationOutput = new HashMap<>();
        simulationOutput.put(new MotionType(1, "no name"), new Double[] { 0.76d });
        simulationOutput.put(new MotionType(2, "no name"), new Double[] { 0.75d });
        simulationOutput.put(new MotionType(2, "no name"), new Double[] { 0.74d });
    }

    @Test
    public void testResolve() throws Exception {
        // given
        final double threshold = 0.75d;
        final MotionResolver resolver = new DefaultMotionResolver(threshold);
        final int expectedId = 1;

        // when
        final MotionType actual = resolver.resolve(simulationOutput);

        // then
        Assert.assertTrue(expectedId == actual.getId());
    }

    @Test
    public void testResolveNoMatched() throws Exception {
        // given
        final double threshold = 0.99d;
        final MotionResolver resolver = new DefaultMotionResolver(threshold);
        // when
        final MotionType actual = resolver.resolve(simulationOutput);

        // then
        Assert.assertTrue(MotionType.UNKNOWN == actual.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResolveNull() {
        // given
        final double threshold = 0.99d;
        final MotionResolver resolver = new DefaultMotionResolver(threshold);

        resolver.resolve(null);

    }
}
