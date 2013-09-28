package com.bylak.android.motion.recognize;

import com.bylak.network.neural.NeuralNetwork;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 28.09.13
 * Time: 12:12
 * To change this template use File | Settings | File Templates.
 */
public final class MotionRecognizer {
    private final Map<MotionType, NeuralNetwork> networks;

    private MotionRecognizer(final Map<MotionType, NeuralNetwork> networks){
        this.networks = networks;
    }

    public MotionRecognizer network(final NeuralNetwork networkToAdd, final MotionType motionType){
        this.networks.put(motionType, networkToAdd);

        return this;
    }

    public void remove(final MotionType motionType){
        this.networks.remove(motionType);
    }

    public static class Builder{
        private final Map<MotionType, NeuralNetwork> networks;

        public Builder(){
            this.networks = new HashMap<>();
        }

        public Builder add(final NeuralNetwork networkToAdd, final MotionType motionType){
            this.networks.put(motionType, networkToAdd);

            return this;
        }

        public MotionRecognizer build(){
            return new MotionRecognizer(this.networks);
        }
    }
}
