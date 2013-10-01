package com.bylak.android.normalize;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 30.09.13
 * Time: 13:15
 * To change this template use File | Settings | File Templates.
 */
public interface InputProcessor {
    double[] processInput(int expectedOutputCount, float[] dataToProcess);
}
