package com.datdeveloper.datmoddingapi.util;

import net.minecraft.ChatFormatting;

/**
 * A utility class containing standardised colours
 */
public class DatChatFormatting {
    private DatChatFormatting() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Colours for chat messages
     */
    public static final class TextColour {
        /** Content that intends to inform the user */
        public static final ChatFormatting INFO = ChatFormatting.GOLD;

        /** Content that describes an error */
        public static final ChatFormatting ERROR = ChatFormatting.RED;

        /** Text that is a command */
        public static final ChatFormatting COMMAND = ChatFormatting.DARK_PURPLE;

        /** Content is the header of a group of text */
        public static final ChatFormatting HEADER = ChatFormatting.DARK_AQUA;

        private TextColour() {
        }
    }

    /**
     * Colours for player states
     */
    public static final class PlayerColour {
        /** The player is online */
        public static final ChatFormatting ONLINE = ChatFormatting.GREEN;

        /** The player is offline */
        public static final ChatFormatting OFFLINE = ChatFormatting.RED;

        /** The player is online but not currently active */
        public static final ChatFormatting AWAY = ChatFormatting.GOLD;

        private PlayerColour() {
        }
    }
}

