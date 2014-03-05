package com.bylak.network.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 11.09.13
 * Time: 09:19
 * To change this template use File | Settings | File Templates.
 */
public final class ArrayListBuilder<T> {
    private final List<T> list;

    public ArrayListBuilder() {
        this.list = new ArrayList<T>();
    }

    public ArrayListBuilder<T> add(final T element) {
        this.list.add(element);

        return this;
    }

    public List<T> build() {
        return list;
    }
}
