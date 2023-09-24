package com.datdeveloper.datmoddingapi.util;

import com.ibm.icu.impl.number.DecimalFormatProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;

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

    private enum FormattingOperations {
        BLACK(ChatFormatting.BLACK, true),
        DARK_BLUE(ChatFormatting.DARK_BLUE, true),
        DARK_GREEN(ChatFormatting.DARK_GREEN, true),
        DARK_AQUA(ChatFormatting.DARK_AQUA, true),
        DARK_RED(ChatFormatting.DARK_RED, true),
        DARK_PURPLE(ChatFormatting.DARK_PURPLE, true),
        GOLD(ChatFormatting.GOLD, true),
        GRAY(ChatFormatting.GRAY, true),
        DARK_GRAY(ChatFormatting.DARK_GRAY, true),
        BLUE(ChatFormatting.BLUE, true),
        GREEN(ChatFormatting.GREEN, true),
        AQUA(ChatFormatting.AQUA, true),
        RED(ChatFormatting.RED, true),
        LIGHT_PURPLE(ChatFormatting.LIGHT_PURPLE, true),
        YELLOW(ChatFormatting.YELLOW, true),
        WHITE(ChatFormatting.WHITE, true),
        OBFUSCATED(ChatFormatting.OBFUSCATED, false),
        BOLD(ChatFormatting.BOLD, false),
        STRIKETHROUGH(ChatFormatting.STRIKETHROUGH, false),
        UNDERLINE(ChatFormatting.UNDERLINE, false),
        ITALIC(ChatFormatting.ITALIC, false),
        RESET(ChatFormatting.RESET, true),

        INFO(ChatFormatting.GOLD, true),
        ERROR(ChatFormatting.RED, true),
        COMMAND(ChatFormatting.DARK_PURPLE, true),
        HEADER(ChatFormatting.DARK_AQUA, true),

        ONLINE(ChatFormatting.GREEN, true),
        OFFLINE(ChatFormatting.RED, true),
        AWAY(ChatFormatting.GOLD, true);

        final ChatFormatting formatting;
        final boolean resets;
        private FormattingOperations(final ChatFormatting formatting, final boolean resets) {
            this.formatting = formatting;
            this.resets = resets;
        }
    }

    /**
     * Format the given chat string into a chat component using objects passed as arguments
     * <br>
     * The formatString can have commands to dictate how to format the formatString is formatted:
     * <br>
     * %f ->
     * TODO: Finish
     * @param formatString The string that is processed
     * @param args The arguments to use to format the string
     * @return A chat component containing the formatted text
     */
    public static Component formatChatString(final String formatString, final Object... args) {
        final MutableComponent component = Component.empty();

        int cursor = 0;
        int nextArg = 0;
        StringBuilder buffer = new StringBuilder();
        final List<ChatFormatting> formatContext = new ArrayList<>();

        while (cursor < formatString.length()) {
            final char nextChar = formatString.charAt(cursor);
            if (nextChar == '<') {
                // Hold instruction pointer in case of error
                final int instructionPointer = cursor;
                try {
                    switch (formatString.charAt(++cursor)) {
                        case 'f' -> {
                            /* Formatting instruction
                             * <f format>, where "format" is a formatting code
                             */

                            // Consume space
                            if (formatString.charAt(++cursor) != ' ') {
                                return Component.literal("Failed to parse message, Formatting instruction at "
                                        + instructionPointer + " is not formatted correctly");
                            }

                            // Get whole formatting string
                            final int start = ++cursor;
                            do {
                                ++cursor;
                            } while (formatString.charAt(cursor) != '>');

                            try {
                                final FormattingOperations formatting = FormattingOperations.valueOf(formatString.substring(start, cursor).toUpperCase());
                                if (formatting.resets) formatContext.clear();
                                formatContext.add(formatting.formatting);
                                buffer.append(formatting.formatting);
                            } catch (final IllegalArgumentException e) {
                                // Skip
                            }
                        }
                        case 'v' -> {
                            /* Variable instruction
                             * Can one of:
                             * <v index>, where "index" is the index of the argument in the list of arguments provided
                             * to the formatter. The space is optional
                             * <v>, where each usage counts to a running total, of which is used as the index
                             */
                            // Consume all spaces
                            // Do while is used to always move on from the instruction
                            do {
                                ++cursor;
                            } while (formatString.charAt(cursor) != ' ');

                            final Object arg;
                            if (formatString.charAt(cursor) != '>') {
                                // Indexed variable access
                                final int start = ++cursor;

                                do {
                                    ++cursor;
                                } while (formatString.charAt(cursor) != '>');

                                final String indexString = formatString.substring(start, cursor);
                                try {
                                    arg = args[Integer.parseInt(indexString)];
                                } catch (final NumberFormatException e) {
                                    return Component.literal("Failed to parse message, variable instruction at "
                                            + instructionPointer + " uses bad variable index: "
                                            + indexString);
                                } catch (final IndexOutOfBoundsException e) {
                                    return Component.literal("Failed to parse message, variable instruction at "
                                            + instructionPointer + " requested an argument at an index that isn't " +
                                            "available: " + indexString);
                                }

                            } else {
                                try {
                                    arg = args[nextArg++];
                                } catch (final IndexOutOfBoundsException e) {
                                    return Component.literal("Failed to parse message, Too many unindexed variable" +
                                            " instructions encountered at " + instructionPointer);
                                }
                            }

                            if (arg instanceof final Component newComponent) {
                                // Components have special treatment so they are rendered.
                                component.append(Component.literal(buffer.toString()))
                                        .append(newComponent);
                                buffer = new StringBuilder();
                                formatContext.forEach(buffer::append);
                            } else {
                                buffer.append(arg.toString());
                            }
                        }
                        case 'r' -> {
                            /* Reset style instruction
                             * <r>
                             * Equivalent to "<f reset>"
                             */

                            if (formatString.charAt(++cursor) != '>') {
                                return Component.literal("Failed to parse message, reset instruction at "
                                        + instructionPointer + " is not formatted correctly");
                            }

                            formatContext.clear();
                            formatContext.add(FormattingOperations.RESET.formatting);
                            buffer.append(FormattingOperations.RESET.formatting);
                        }
                        case '<' -> {
                            /* '<' instruction
                             * <<>
                             * A method to use the '<' character
                             */

                            if (formatString.charAt(++cursor) != '>') {
                                return Component.literal("Failed to parse message, '<' instruction at "
                                        + instructionPointer + " is not formatted correctly");
                            }

                            buffer.append('<');
                        }
                        case '>' -> {
                            // Discard empty instruction
                        }
                        default -> {
                            return Component.literal("Failed to parse message, unexpected instruction character"
                                    + " at position " + cursor);
                        }
                    }
                } catch (final IndexOutOfBoundsException e) {
                    return Component.literal("Failed to parse message, an instruction at " + instructionPointer
                            + " was not closed");
                }
            } else {
                buffer.append(nextChar);
            }

            ++cursor;
        }

        if (!buffer.isEmpty()) component.append(Component.literal(buffer.toString()));

        return component;
    }
}
