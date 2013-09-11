package com.bylak.network.util;

import com.bylak.network.exception.AggregatedException;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 11.09.13
 * Time: 12:23
 * To change this template use File | Settings | File Templates.
 */
public final class InitHelper {
    private static final String ERROR_MESSAGE = "Argument %s can not be null";

    private InitHelper() {
    }

    public static void checkNullValues(Object... objects) throws AggregatedException {
        AggregatedException.Builder exceptionBuilder = new AggregatedException.Builder();

        for (Object object : objects) {
            if (object == null) {
                String message = String.format(ERROR_MESSAGE, "test");
                exceptionBuilder.add(new IllegalArgumentException(message));
            }
        }

        throw exceptionBuilder.build();
    }
}
