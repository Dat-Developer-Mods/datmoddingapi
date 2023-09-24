package com.datdeveloper;

import com.datdeveloper.datmoddingapi.util.DatChatFormatting;
import com.datdeveloper.datmoddingapi.util.DatMessageFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Map;

import static java.util.Map.entry;

class TestChatFormatting {
    /**
     * Check a string without any text replacements
     */
    @Test
    void testNoReplacement() throws ParseException {
        final String chatString = "Test message";
        final Component component = new DatMessageFormatter(chatString).parse();
        Assertions.assertEquals("literal{Test message}", component.toString());
    }

    /**
     * Test empty string
     */
    @Test
    void testEmpty() throws ParseException {
        final String chatString = "";
        final Component component = new DatMessageFormatter(chatString).parse();
        Assertions.assertEquals("empty", component.toString());
    }

    /**
     * Test all formatting replacements
     */
    @Test
    void testFormattingInstruction() throws ParseException {
        final Map<String, ChatFormatting> formatting = Map.ofEntries(
                entry("black", ChatFormatting.BLACK),
                entry("dark_blue", ChatFormatting.DARK_BLUE),
                entry("dark_green", ChatFormatting.DARK_GREEN),
                entry("dark_aqua", ChatFormatting.DARK_AQUA),
                entry("dark_red", ChatFormatting.DARK_RED),
                entry("dark_purple", ChatFormatting.DARK_PURPLE),
                entry("gold", ChatFormatting.GOLD),
                entry("gray", ChatFormatting.GRAY),
                entry("dark_gray", ChatFormatting.DARK_GRAY),
                entry("blue", ChatFormatting.BLUE),
                entry("green", ChatFormatting.GREEN),
                entry("aqua", ChatFormatting.AQUA),
                entry("red", ChatFormatting.RED),
                entry("light_purple", ChatFormatting.LIGHT_PURPLE),
                entry("yellow", ChatFormatting.YELLOW),
                entry("white", ChatFormatting.WHITE),
                entry("obfuscated", ChatFormatting.OBFUSCATED),
                entry("bold", ChatFormatting.BOLD),
                entry("strikethrough", ChatFormatting.STRIKETHROUGH),
                entry("underline", ChatFormatting.UNDERLINE),
                entry("italic", ChatFormatting.ITALIC),
                entry("reset", ChatFormatting.RESET),

                entry("info", DatChatFormatting.TextColour.INFO),
                entry("error", DatChatFormatting.TextColour.ERROR),
                entry("command", DatChatFormatting.TextColour.COMMAND),
                entry("header", DatChatFormatting.TextColour.HEADER),

                entry("online", DatChatFormatting.PlayerColour.ONLINE),
                entry("offline", DatChatFormatting.PlayerColour.OFFLINE),
                entry("away", DatChatFormatting.PlayerColour.AWAY)
        );

        for (final Map.Entry<String, ChatFormatting> formattingString : formatting.entrySet()) {
            final String chatString = "<f " + formattingString.getKey() +">This is a colour replacement";
            final Component component = new DatMessageFormatter(chatString).parse();

            Assertions.assertEquals("literal{" + formattingString.getValue() + "This is a colour replacement}", component.toString());
        }
    }

    /**
     * Test Unknown format
     * This should skip the format command
     */
    @Test
    void testUnknownFormat() throws ParseException {
        final String chatString = "<f sillyColour>Unknown format";
        final Component component = new DatMessageFormatter(chatString).parse();
        Assertions.assertEquals("literal{Unknown format}", component.toString());
    }

    /**
     * Test reset replacement
     */
    @Test
    void testResetInstruction() throws ParseException {
        final String chatString = "<f red>This has a <r>reset <f reset>replacement";
        final Component component = new DatMessageFormatter(chatString).parse();
        Assertions.assertEquals("literal{§cThis has a §rreset §rreplacement}", component.toString());
    }

    /**
     * Test simple variable replacement
     */
    @Test
    void testSingleVariableInstruction() throws ParseException {
        final String chatString = "My replacement variable is <v>";
        final String replacement = "Test";
        final Component component = new DatMessageFormatter(chatString, replacement).parse();
        Assertions.assertEquals("literal{My replacement variable is " + replacement + "}", component.toString());
    }

    /**
     * Test simple variable replacement with a non-string
     */
    @Test
    void testNonStringVariableInstruction() throws ParseException {
        final String chatString = "My replacement variable is <v>";
        final int replacement = 52;
        final Component component = new DatMessageFormatter(chatString, replacement).parse();
        Assertions.assertEquals("literal{My replacement variable is " + replacement + "}", component.toString());
    }

    /**
     * Test sequential variable replacement
     */
    @Test
    void testSequentialVariableInstructions() throws ParseException {
        final String chatString = "My replacement variable is <v> <v> <v>";
        final String[] replacements = {"Test", "test2", "test3"};
        final Component component = new DatMessageFormatter(chatString, (Object[]) replacements).parse();
        Assertions.assertEquals("literal{My replacement variable is " + replacements[0] + " " + replacements[1] + " " + replacements[2] + "}", component.toString());
    }

    /**
     * Test indexed variable replacement
     */
    @Test
    void testIndexedVariableInstructions() throws ParseException {
        final String chatString = "My replacement variable is <v 1> <v 0> <v 2>";
        final String[] replacements = {"Test", "test2", "test3"};
        final Component component = new DatMessageFormatter(chatString, (Object[]) replacements).parse();
        Assertions.assertEquals("literal{My replacement variable is " + replacements[1] + " " + replacements[0] + " " + replacements[2] + "}", component.toString());
    }

    /**
     * Test indexed and sequential variable replacement
     */
    @Test
    void testMixedVariableInstructions() throws ParseException {
        final String chatString = "My replacement variable is <v><v 2><v 1><v><v 2><v 0><v>";
        final String[] replacements = {"Test", "test2", "test3"};
        final Component component = new DatMessageFormatter(chatString, (Object[]) replacements).parse();
        Assertions.assertEquals("literal{My replacement variable is " + replacements[0] + replacements[2] + replacements[1] + replacements[1] + replacements[2] + replacements[0] + replacements[2] + "}", component.toString());
    }

    /**
     * Test not enough arguments for sequentials
     */
    @Test
    void testSequentialVariableInstructionTooMany() {
        final String chatString = "My replacement variable is <v><v><v><v>";
        final String[] replacements = {"Test", "test2", "test3"};
    }

    /**
     * Test too great variable index
     */
    @Test
    void testIndexedVariableInstructionTooLarge() {
        final String chatString = "My replacement variable is <v 4>";
        final String[] replacements = {"Test", "test2", "test3"};
        Assertions.assertThrows(ParseException.class, () -> new DatMessageFormatter(chatString, (Object[]) replacements).parse());
    }

    /**
     * Test variable index is not a number
     */
    @Test
    void testIndexedVariableInstructionNotNumber() {
        final String chatString = "My replacement variable is <v Bad>";
        final String[] replacements = {"Test", "test2", "test3"};
        Assertions.assertThrows(ParseException.class, () -> new DatMessageFormatter(chatString, (Object[]) replacements).parse());
    }

    /**
     * Test variable instruction with component
     */
    @Test
    void testVariableInstructionComponent() throws ParseException {
        final String chatString = "My replacement variable is <v>";
        final Component componentParam = Component.literal("Test Component").withStyle(ChatFormatting.BLUE, ChatFormatting.BOLD);
        final Component component = new DatMessageFormatter(chatString, componentParam).parse();
        Assertions.assertEquals("literal{My replacement variable is }[siblings=[" + componentParam + "]]", component.toString());
    }

    /**
     * Test colour formatting and variable combined
     */
    @Test
    void testFormattingVariableInstruction() throws ParseException {
        final String chatString = "<f red>My replacement variable <f bold> is <v>";
        final String[] replacements = {"Test", "test2", "test3"};
        final Component component = new DatMessageFormatter(chatString, (Object[]) replacements).parse();
        Assertions.assertEquals("literal{§cMy replacement variable §l is Test}", component.toString());
    }

    /**
     * Test colour is continued after component replacement
     */
    @Test
    void testFormattingComponentVariableInstruction() throws ParseException {
        final String chatString = "<f red>My replacement variable <f bold> is <v> and there's <f gold> some more";
        final Component componentParam = Component.literal("Test Component").withStyle(ChatFormatting.BLUE, ChatFormatting.BOLD);
        final Component component = new DatMessageFormatter(chatString, componentParam).parse();
        Assertions.assertEquals("literal{§cMy replacement variable §l is }[siblings=[literal{Test Component}[style={color=blue,bold}], literal{§c§l and there's §6 some more}]]", component.toString());
    }

    /**
     * Test formatting resets occur properly after component replacement
     */
    @Test
    void testFormattingResetComponentVariableInstruction() throws ParseException {
        final String chatString = "<f red>My replacement variable <f bold> is <f blue> <v> and there's <r> some <v 0> more";
        final Component componentParam = Component.literal("Test Component").withStyle(ChatFormatting.BLUE, ChatFormatting.BOLD);
        final Component component = new DatMessageFormatter(chatString, componentParam).parse();
        Assertions.assertEquals("literal{§cMy replacement variable §l is §9 }[siblings=[literal{Test Component}[style={color=blue,bold}], literal{§9 and there's §r some }, literal{Test Component}[style={color=blue,bold}], literal{§r more}]]", component.toString());
    }
}
