package com.datdeveloper.datmoddingapi.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.text.ParseException;

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

    /**
     * Format the given chat string into a chat component using objects passed as arguments
     * <br>
     * A shortcut function for {@link DatMessageFormatter}
     * <br>
     * When an error is encountered in the {@link DatMessageFormatter} Then it is caught and dumped in the chat message.
     * {@link DatMessageFormatter} should be used directly if you desire handling the error yourself.
     * @see DatMessageFormatter
     * @param formatString The string that is processed
     * @param args The arguments to use to format the string
     * @return A chat component containing the formatted text
     */
    public static Component formatChatString(final String formatString, final Object... args) {
        try {
            return new DatMessageFormatter(formatString, args).parse();
        } catch (final ParseException e) {
            return Component.literal(e.getMessage());
        }
    }
}

