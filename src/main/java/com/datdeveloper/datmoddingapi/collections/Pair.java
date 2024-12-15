package com.datdeveloper.datmoddingapi.collections;

/**
 * A simple container for 2 values
 * @param <L> the type of the left hand
 * @param <R> the type of the right hand
 * @deprecated Use {@link org.apache.commons.lang3.tuple.Pair} instead
 */
@Deprecated(since = "1.9.0")
public class Pair<L, R> {
    private L leftHand;
    private R rightHand;

    /**
     * @param leftHand The left hand value of the pair
     * @param rightHand The right hand value of the pair
     */
    public Pair(final L leftHand, final R rightHand) {
        this.leftHand = leftHand;
        this.rightHand = rightHand;
    }

    public L getLeftHand() {
        return leftHand;
    }

    public R getRightHand() {
        return rightHand;
    }

    public void setLeftHand(final L leftHand) {
        this.leftHand = leftHand;
    }

    public void setRightHand(final R rightHand) {
        this.rightHand = rightHand;
    }
}
