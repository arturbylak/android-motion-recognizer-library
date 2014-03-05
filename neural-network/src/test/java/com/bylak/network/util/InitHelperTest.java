package com.bylak.network.util;

import org.junit.Test;

import com.bylak.network.exception.AggregatedException;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 11.09.13
 * Time: 13:55
 * To change this template use File | Settings | File Templates.
 */
public class InitHelperTest {
    @Test(expected = AggregatedException.class)
    public void testCheckNullValues() throws Exception {
        InitHelper.checkNullValues(null, null);
    }
}
