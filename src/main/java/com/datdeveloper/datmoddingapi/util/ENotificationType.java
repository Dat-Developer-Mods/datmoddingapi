package com.datdeveloper.datmoddingapi.util;

/**
 * An enum representing different ways to notify a player
 */
public enum ENotificationType {
    /** A message in the middle of the player's screen */
    TITLE,

    /** A message above the player's hotbar */
    ACTIONBAR,

    /** A message in chat (Only visible to the player) */
    CHAT,

    /** No notification will be sent */
    DISABLED
}
