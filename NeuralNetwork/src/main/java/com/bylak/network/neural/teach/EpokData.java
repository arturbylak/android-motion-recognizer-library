package com.bylak.network.neural.teach;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Artur.Bylak
 * Date: 11.09.13
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
public final class EpokData {
    private final List<TeachData> teachData;

    public EpokData(final List<TeachData> teachData) {
        this.teachData = teachData;
    }

    public List<TeachData> getTeachData() {
        return teachData;
    }

    public static class Builder {
        private final List<TeachData> teachData;

        public Builder() {
            this.teachData = new ArrayList<TeachData>();
        }

        public Builder add(final TeachData teachData) {
            this.teachData.add(teachData);

            return this;
        }

        public EpokData build() {
            return new EpokData(teachData);
        }
    }
}
