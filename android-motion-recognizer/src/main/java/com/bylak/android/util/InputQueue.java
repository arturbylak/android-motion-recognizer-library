package com.bylak.android.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 04.10.13
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public final class InputQueue {
    private final List<Float> queue;
    private final int packageSize;

    public InputQueue(final int packageSize) {
        this.queue = new LinkedList<Float>();
        this.packageSize = packageSize;
    }

    public int size() {
        return this.queue.size();
    }

    private void add(final float value) {
        this.queue.add(value);
    }

    public void add(final float[] values) {
        for (final float value : values) {
            this.add(value);
        }
    }

    public void removeFirst() {
        for (int i = 0; i < packageSize; i++) {
            this.queue.remove(0);
        }
    }

    public float[] getAllData() {
        final float[] data = new float[size()];

        for (int i = 0; i < size(); i++) {
            data[i] = queue.get(i);
        }

        return data;
    }
}
