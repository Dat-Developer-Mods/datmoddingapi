package com.demmodders.datmoddingapi.util;

public class Maths {
    // Clamp

    /**
     * Ensures the given value is between the given max and min
     * @param Value The value to clamp
     * @param Min The minimum the value is allowed to be
     * @param Max The maximum the value is allowed to be
     * @return the clamped value
     */
    public static int clamp(int Value, int Min, int Max) {
        if (Value < Min) return Min;
        else return Math.min(Value, Max);
    }

    /**
     * Ensures the given value is between the given max and min
     * @param Value The value to clamp
     * @param Min The minimum the value is allowed to be
     * @param Max The maximum the value is allowed to be
     * @return the clamped value
     */
    public static float clamp(float Value, float Min, float Max) {
        if (Value < Min) return Min;
        else return Math.min(Value, Max);
    }

    /**
     * Ensures the given value is between the given max and min
     * @param Value The value to clamp
     * @param Min The minimum the value is allowed to be
     * @param Max The maximum the value is allowed to be
     * @return the clamped value
     */
    public static double clamp(double Value, double Min, double Max) {
        if (Value < Min) return Min;
        else return Math.min(Value, Max);
    }
}
