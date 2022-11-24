package com.datdeveloper.datmoddingapi.collections;

/**
 * A simple container for 2 values
 * @param <lh> the type of the left hand
 * @param <rh> the type of the right hand
 */
public class Pair<lh, rh> {
    private lh leftHand;
    private rh rightHand;

    public Pair(final lh leftHand, final rh rightHand) {
        this.leftHand = leftHand;
        this.rightHand = rightHand;
    }

    public lh getLeftHand() {
        return leftHand;
    }

    public rh getRightHand() {
        return rightHand;
    }

    public void setLeftHand(final lh leftHand) {
        this.leftHand = leftHand;
    }

    public void setRightHand(final rh rightHand) {
        this.rightHand = rightHand;
    }
}
