package com.bylak.network.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 11.09.13
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
public final class AggregatedException extends Exception {
    private final List<Exception> exceptions;

    public AggregatedException(final List<Exception> exceptions) {
        this.exceptions = exceptions;
    }

    public List<Exception> getExceptions() {
        return new ArrayList<Exception>(exceptions);
    }

    public static final class Builder {
        private final List<Exception> exceptions;

        public Builder() {
            this.exceptions = new ArrayList<Exception>();
        }

        public Builder add(final Exception ex) {
            this.exceptions.add(ex);
            return this;
        }

        public AggregatedException build() {
            return new AggregatedException(this.exceptions);
        }
    }
}
