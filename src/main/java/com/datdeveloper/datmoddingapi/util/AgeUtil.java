package com.datdeveloper.datmoddingapi.util;

/**
 * A utility class to help display time scales
 */
public class AgeUtil {
    private AgeUtil() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * Get a friendly string representing the difference in time
     * <br>
     * For example, if the timestamp represents 3500 seconds, it would return "58 minutes"
     * Supports seconds, minutes, hours, days, months, and years
     * @param deltaTime The time period in milliseconds
     * @return A string representing the difference in time
     */
    public static String getFriendlyDifference(final long deltaTime) {
        final long delta = Math.abs(deltaTime) / 1000;

        final String period;
        if (delta < 60) {
            period = "Second";
        } else if (delta < 3_600) {
            period = (delta / 60) + "Minute";
        } else if (delta < 86_400) {
            period = (delta / 3_600) + "Hour";
        } else if (delta < 2_592_000) {
            period = (delta / 86_400) + "Day";
        } else if (delta < 31_104_000) {
            period = (delta / 2_592_000) + "Month";
        } else {
            period = (delta / 31_104_000) + "Year";
        }

        // Add plural if delta isn't 1
        return delta + " " + period + (delta == 1 ? "" : "s");
    }

    /**
     * Get a friendly string representing the difference between now and the given timestamp
     * <br>
     * For example, if the timestamp represents 3500 seconds in the future, it would return "In 58 minutes", equally, if
     * the timestamp represents 80000 seconds in the past, it would return "22 hours ago"
     * Supports seconds, minutes, hours, days, months, and years
     * @param timeStamp The timestamp the message should be relative to
     * @return A string representing the difference in time
     */
    public static String getFriendlyRelativeTime(final long timeStamp) {
        final long delta = timeStamp - System.currentTimeMillis();

        final String differenceString = getFriendlyDifference(delta);

        if (delta > 0) return "In " + differenceString;
        else return differenceString + " ago";
    }
}
