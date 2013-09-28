package com.bylak.android.motion.recognize.listener;

import com.bylak.android.motion.recognize.MotionType;

/**
 * Created with IntelliJ IDEA.
 * User: bylak
 * Date: 28.09.13
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public interface OnRecognizedListener {
    void onRecognize(MotionType typeOfMotion);
}
