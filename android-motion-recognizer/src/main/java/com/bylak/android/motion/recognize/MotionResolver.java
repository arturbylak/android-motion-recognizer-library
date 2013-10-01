package com.bylak.android.motion.recognize;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 01.10.13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public interface MotionResolver {
    MotionType resolve(final Map<MotionType, Double[]> simulationOutput);
}
