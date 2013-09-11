package com.bylak.network.util;

import com.bylak.network.exception.AggregatedException;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
