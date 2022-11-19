package com.datdeveloper.datmoddingapi.util;

import net.minecraft.ChatFormatting;

public class DatChatFormatting {
    /**
     * Colours for chat messages
     */
    public static final class TextColour {
        public static final ChatFormatting INFO = ChatFormatting.GOLD;
        public static final ChatFormatting ERROR = ChatFormatting.RED;
        public static final ChatFormatting COMMAND = ChatFormatting.DARK_PURPLE;
        public static final ChatFormatting HEADER = ChatFormatting.DARK_AQUA;
    }

    /**
     * Colours for player states
     */
    public static final class PlayerColour {
        public static final ChatFormatting ONLINE = ChatFormatting.GREEN;
        public static final ChatFormatting OFFLINE = ChatFormatting.RED;
        public static final ChatFormatting AWAY = ChatFormatting.GOLD;
    }
}
