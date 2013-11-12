package com.bylak.network.util;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 12.11.13
 * Time: 14:04
 * To change this template use File | Settings | File Templates.
 */
public final class ArrayUtil {

    private ArrayUtil(){};

    public static Double[][] clear(final Double[][] array){
        for(int i=0; i<array.length; i++){
            for(int j=0; j<array[i].length; j++){
                array[i][j] = 0.0d;
            }
        }

        return array;
    }

    public static Double[][] copy(final Double[][] arrayToCopy){
        Double[][] copyOfArray = new Double[arrayToCopy.length][];

        for(int i=0; i<arrayToCopy.length; i++){
            int arraySize = arrayToCopy[i].length;
            copyOfArray[i] = new Double[arraySize];

            for(int j=0; j < arraySize; j++){
                double doubleValue = arrayToCopy[i][j];
                copyOfArray[i][j] = Double.valueOf(doubleValue);
            }
        }

        return copyOfArray;
    }
}
