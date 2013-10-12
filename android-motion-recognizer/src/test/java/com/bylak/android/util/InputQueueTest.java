package com.bylak.android.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 12.10.13
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */
public class InputQueueTest {
    @Test
    public void testAdd(){
        //given
        InputQueue inputQueue = new InputQueue(10);
        float[] data = new float[] {1f, 2f};

        //when
        inputQueue.add(data);
        float[] result = inputQueue.getAllData();

        //then
        for(int i=0; i<data.length; i++){
            Assert.assertEquals(data[i], result[i], 0.01f);
        }
    }

    @Test
    public void testRemoveFirst(){
        //given
        InputQueue inputQueue = new InputQueue(2);
        float[] data = new float[] {1f, 2f};

        //when
        inputQueue.add(data);
        inputQueue.add(data);
        inputQueue.removeFirst();
        float[] result = inputQueue.getAllData();

        //then
        for(int i=0; i<data.length; i++){
            Assert.assertEquals(data[i], result[i], 0.01f);
        }
    }
}
