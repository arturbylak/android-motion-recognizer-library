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
public final class EpochData {
    private final List<TeachData> teachData;

    private EpochData(final List<TeachData> teachData) {
        this.teachData = teachData;
    }

    public List<TeachData> getTeachData() {
        return new ArrayList<TeachData>(teachData);
    }

    public int getSize() {
        return teachData.size();
    }

    public TeachData getElement(int elementIndex) {
        return teachData.get(elementIndex);
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

        public EpochData build() {
            return new EpochData(teachData);
        }
    }
}
