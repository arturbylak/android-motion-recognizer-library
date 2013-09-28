package com.bylak.android.motion.recognize;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 28.09.13
 * Time: 12:23
 * To change this template use File | Settings | File Templates.
 */
public final class MotionType {
    private final int id;
    private final String name;

    public MotionType(int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
