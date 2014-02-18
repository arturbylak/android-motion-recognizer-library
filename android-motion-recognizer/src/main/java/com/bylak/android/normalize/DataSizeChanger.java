package com.bylak.android.normalize;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 18.02.14
 * Time: 12:32
 * To change this template use File | Settings | File Templates.
 */
public interface DataSizeChanger {
    double[] changeSize(double[] dataToProcess, int expectedOutputCount);
}
