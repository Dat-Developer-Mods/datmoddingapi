package com.datdeveloper.datmoddingapi.util;

import com.datdeveloper.datmoddingapi.util.exceptions.ChatParseException;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for formatting
 */
public class DatMessageFormatter {
    /**
     * The variables used for variable replacement instructions
     */
    private final Object[] args;

    /**
     * The next argument to be used when using sequential variable replacement
     */
    private int nextArg = 0;

    /**
     * The current index of the format string being processed
     */
    private int cursor = 0;

    /**
     * The string that is parsed
     */
    private final String formatString;

    /**
     * A list of formats that are currently applying at the location of the cursor
     */
    private final List<ChatFormatting> formatContext = new ArrayList<>();

    /**
     * The buffer containing the characters that will be added to the resulting {@link Component}
     */
    private StringBuilder buffer = new StringBuilder();

    /**
     * The component being built that will be returned
     */
    private MutableComponent component = null;

    /**
     * Parameters that can be passed to the formatting instruction
     */
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

        FormattingOperations(final ChatFormatting formatting, final boolean resets) {
            this.formatting = formatting;
            this.resets = resets;
        }
    }

    public DatMessageFormatter(final String formatString, final Object... args) {
        this.formatString = formatString;
        this.args = args;
    }

    /**
     * Parse the given formatString using the arguments
     * @return The processed message
     * @throws ChatParseException Thrown when an exception is encountered whilst parsing the string
     */
    public Component parse() throws ChatParseException {
        while (cursor < formatString.length()) {
            final char nextChar = formatString.charAt(cursor);
            if (nextChar == '<') {
                handleInstruction();
            } else {
                buffer.append(nextChar);
            }

            ++cursor;
        }

        if (!buffer.isEmpty()) appendComponent(Component.literal(buffer.toString()));

        return component == null ? Component.empty() : component;
    }

    /**
     * Handle an instruction that specifies a special behaviour in the output
     */
    private void handleInstruction() throws ChatParseException {
        // Hold instruction pointer in case of error
        final int instructionPointer = cursor;
        final char instruction = formatString.charAt(++cursor);

        // Move on from instruction
        ++cursor;

        final List<String> instructionArgs;

        try {
            instructionArgs = consumeArgs();
        } catch (final IndexOutOfBoundsException e) {
            throw new ChatParseException("Failed to parse arguments for instruction", formatString, instructionPointer);
        }

        switch (instruction) {
            case 'f' -> handleFormattingInstruction(instructionPointer, instructionArgs);
            case 'v' -> handleVariableInstruction(instructionPointer, instructionArgs);
            case 'r' -> handleResetInstruction(instructionPointer, instructionArgs);
            case '<' -> handleGtInstruction(instructionPointer, instructionArgs);
            case '>' -> {
                // Discard empty instruction
            }
            default -> throw new ChatParseException("Unknown instruction character: " + instruction, formatString, instructionPointer);
        }
    }

    /* ========================================= */
    /* Instructions                              */
    /* ========================================= */

    /**
     * An instruction to insert a formatting directive into the message
     * <br>
     * &lt;f format...&gt;, where "format..." is a space separated list of formats to apply
     *
     * @param instructionPointer The index of the opening < of the instruction
     * @param instructionArgs    A list of formats to add to the message
     * @throws ChatParseException Thrown when no arguments are provided
     */
    private void handleFormattingInstruction(final int instructionPointer, final List<String> instructionArgs) throws ChatParseException {
        if (instructionArgs.isEmpty()) {
            throw generateWrongNumberOfArgumentsException(instructionPointer, ">1", 0);
        }

        for (final String arg : instructionArgs) {
            try {
                final FormattingOperations formatting = FormattingOperations.valueOf(arg.toUpperCase());
                handleFormatChange(formatting);
            } catch (final IllegalArgumentException e) {
                // Skip
            }
        }
    }

    /**
     * An instruction to insert a passed variable into the message
     * <br>
     * The instruction can take one of two forms:
     * <br>
     * &lt;v index&gt;, where "index" is the index of the argument in the list of arguments provided to the formatter.
     * The space is optional
     * <br>
     * &lt;v&gt;, where each usage counts to a running total, of which is used as the index
     *
     * @param instructionPointer The index of the opening < of the instruction
     * @param instructionArgs    An empty list, or a single integer that defines the index of the argument array to insert
     * @throws ChatParseException Thrown when there are too many arguments, or if the requested variable cannot be accessed
     */
    private void handleVariableInstruction(final int instructionPointer, final List<String> instructionArgs) throws ChatParseException {
        final Object arg;

        // Get the argument
        if (instructionArgs.isEmpty()) {
            // Sequence case
            try {
                arg = args[nextArg++];
            } catch (final IndexOutOfBoundsException e) {
                throw new ChatParseException("Too many sequential variable accesses", formatString, instructionPointer);
            }
        } else if (instructionArgs.size() == 1) {
            try {
                arg = args[Integer.parseInt(instructionArgs.get(0))];
            } catch (final IndexOutOfBoundsException e) {
                throw new ChatParseException("Requested variable that wasn't in the args array", formatString, instructionPointer);
            } catch (final NumberFormatException e) {
                throw new ChatParseException("Bad argument index: " + instructionArgs.get(0), formatString, instructionPointer);
            }
        } else {
            throw generateWrongNumberOfArgumentsException(instructionPointer, "0 or 1", instructionArgs.size());
        }

        if (arg instanceof final Component newComponent) {
            // Components have special treatment so they are rendered.
            appendComponent(Component.literal(buffer.toString()));
            appendComponent(newComponent);
            buffer = new StringBuilder();
            formatContext.forEach(buffer::append);
        } else {
            buffer.append(arg.toString());
        }
    }

    /**
     * An instruction to reset the style of future text in the message
     * <br>
     * &lt;r&gt;
     * <br>
     * Equivalent to "&lt;f reset&gt;"
     *
     * @param instructionPointer The index of the opening < of the instruction
     * @param instructionArgs    An empty list
     * @throws ChatParseException Thrown when any arguments are provided
     */
    private void handleResetInstruction(final int instructionPointer, final List<String> instructionArgs) throws ChatParseException {
        /* Reset style instruction
         * <r>
         * Equivalent to "<f reset>"
         */

        if (!instructionArgs.isEmpty()) {
            throw generateWrongNumberOfArgumentsException(instructionPointer, "0", instructionArgs.size());
        }

        formatContext.clear();
        formatContext.add(FormattingOperations.RESET.formatting);
        buffer.append(FormattingOperations.RESET.formatting);
    }

    /**
     * An instruction to insert the special character "<"
     * <br>
     * &lt;r&gt;
     *
     * @param instructionPointer The index of the opening < of the instruction
     * @param instructionArgs    An empty list
     * @throws ChatParseException Thrown when any arguments are given
     */
    private void handleGtInstruction(final int instructionPointer, final List<String> instructionArgs) throws ChatParseException {
        if (!instructionArgs.isEmpty()) {
            throw generateWrongNumberOfArgumentsException(instructionPointer, "0", instructionArgs.size());
        }

        buffer.append('<');
    }

    /* ========================================= */
    /* Util                                      */
    /* ========================================= */

    /**
     * Build an exception explaining that too many arguments were provided
     *
     * @param instructionPointer The pointer to the beginning of the instruction that caused the error
     * @param expected           The expected number of arguments
     * @param actual             The actual number of arguments
     */
    private ChatParseException generateWrongNumberOfArgumentsException(final int instructionPointer,
                                                                   final String expected,
                                                                   final int actual) {
        return new ChatParseException("Instruction expected " + expected + " argument(s), received "
                + actual, formatString, instructionPointer);
    }

    /**
     * Append a new component onto the return Component
     * <br>
     * If no component has been appended yet, then the passed component becomes the root
     *
     * @param newComponent The component to append
     */
    private void appendComponent(final Component newComponent) {
        if (component == null) {
            if (newComponent instanceof final MutableComponent mutableComponent) {
                component = mutableComponent;
            } else {
                component = newComponent.copy();
            }
        } else {
            component.append(newComponent);
        }
    }

    /**
     * Consume all the whitespace
     * <br>
     * The cursor should be on the next non-space character
     */
    private void consumeWhitespace() {
        while (formatString.charAt(cursor) == ' ') {
            ++cursor;
        }
    }

    /**
     * Consume arguments and return a list of them
     * <br>
     * The cursor should be on the closing character (>) of the instruction
     *
     * @return A list of the arguments consumed
     * @throws IndexOutOfBoundsException Thrown when an instruction is not closed and it blows the formatString
     */
    private List<String> consumeArgs() throws IndexOutOfBoundsException {
        final List<String> instructionArgs = new ArrayList<>();
        while (formatString.charAt(cursor) != '>') {
            consumeWhitespace();
            final int start = cursor;
            while (formatString.charAt(cursor) != ' ' && formatString.charAt(cursor) != '>') {
                ++cursor;
            }

            instructionArgs.add(formatString.substring(start, cursor));
        }

        return instructionArgs;
    }

    /**
     * Handle a change in formatting
     * <br>
     * This ensures that the new format is stored in the context list, allowing continuation of formatting after a
     * component is inserted
     *
     * @param formatting The format operation to perform
     */
    private void handleFormatChange(final FormattingOperations formatting) {
        if (formatting.resets) formatContext.clear();
        formatContext.add(formatting.formatting);
        buffer.append(formatting.formatting);
    }
}
