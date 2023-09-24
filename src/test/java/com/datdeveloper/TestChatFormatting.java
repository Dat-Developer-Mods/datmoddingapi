package com.datdeveloper;

import com.datdeveloper.datmoddingapi.util.DatChatFormatting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class TestChatFormatting {
    /**
     * Check a string without any text replacements
     */
    @Test
    void testNoReplacement() {
        final String chatString = "Test message";
        final Component component = DatChatFormatting.formatChatString(chatString);
        Assertions.assertEquals("empty[siblings=[literal{Test message}]]", component.toString());
    }

    /**
     * Test empty string
     */
    @Test
    void testEmpty() {
        final String chatString = "";
        final Component component = DatChatFormatting.formatChatString(chatString);
        Assertions.assertEquals("empty", component.toString());
    }

    /**
     * Test all formatting replacements
     */
    @Test
    void testFormattingInstruction() {
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

                entry("info", ChatFormatting.GOLD),
                entry("error", ChatFormatting.RED),
                entry("command", ChatFormatting.DARK_PURPLE),
                entry("header", ChatFormatting.DARK_AQUA),

                entry("online", ChatFormatting.GREEN),
                entry("offline", ChatFormatting.RED),
                entry("away", ChatFormatting.GOLD)
        );

        for (final Map.Entry<String, ChatFormatting> formattingString : formatting.entrySet()) {
            final String chatString = "<f " + formattingString.getKey() +">This is a colour replacement";
            final Component component = DatChatFormatting.formatChatString(chatString);

            Assertions.assertEquals("empty[siblings=[literal{" + formattingString.getValue() + "This is a colour replacement}]]", component.toString());
        }
    }

    /**
     * Test Unknown format
     * This should skip the format command
     */
    @Test
    void testUnknownFormat() {
        final String chatString = "<f sillyColour>Unknown format";
        final Component component = DatChatFormatting.formatChatString(chatString);
        Assertions.assertEquals("empty[siblings=[literal{Unknown format}]]", component.toString());
    }

    /**
     * Test reset replacement
     */
    @Test
    void testResetInstruction() {
        final String chatString = "<f red>This has a <r>reset <f reset>replacement";
        final Component component = DatChatFormatting.formatChatString(chatString);
        Assertions.assertEquals("empty[siblings=[literal{§cThis has a §rreset §rreplacement}]]", component.toString());
    }

    /**
     * Test simple variable replacement
     */
    @Test
    void testSingleVariableInstruction() {
        final String chatString = "My replacement variable is <v>";
        final String replacement = "Test";
        final Component component = DatChatFormatting.formatChatString(chatString, replacement);
        Assertions.assertEquals("empty[siblings=[literal{My replacement variable is " + replacement + "}]]", component.toString());
    }

    /**
     * Test simple variable replacement with a non-string
     */
    @Test
    void testNonStringVariableInstruction() {
        final String chatString = "My replacement variable is <v>";
        final int replacement = 52;
        final Component component = DatChatFormatting.formatChatString(chatString, replacement);
        Assertions.assertEquals("empty[siblings=[literal{My replacement variable is " + replacement + "}]]", component.toString());
    }

    /**
     * Test sequential variable replacement
     */
    @Test
    void testSequentialVariableInstructions() {
        final String chatString = "My replacement variable is <v> <v> <v>";
        final String[] replacements = {"Test", "test2", "test3"};
        final Component component = DatChatFormatting.formatChatString(chatString, (Object[]) replacements);
        Assertions.assertEquals("empty[siblings=[literal{My replacement variable is " + replacements[0] + " " + replacements[1] + " " + replacements[2] + "}]]", component.toString());
    }

    /**
     * Test indexed variable replacement
     */
    @Test
    void testIndexedVariableInstructions() {
        final String chatString = "My replacement variable is <v 1> <v 0> <v 2>";
        final String[] replacements = {"Test", "test2", "test3"};
        final Component component = DatChatFormatting.formatChatString(chatString, (Object[]) replacements);
        Assertions.assertEquals("empty[siblings=[literal{My replacement variable is " + replacements[1] + " " + replacements[0] + " " + replacements[2] + "}]]", component.toString());
    }

    /**
     * Test indexed and sequential variable replacement
     */
    @Test
    void testMixedVariableInstructions() {
        final String chatString = "My replacement variable is <v><v 2><v 1><v><v 2><v 0><v>";
        final String[] replacements = {"Test", "test2", "test3"};
        final Component component = DatChatFormatting.formatChatString(chatString, (Object[]) replacements);
        Assertions.assertEquals("empty[siblings=[literal{My replacement variable is " + replacements[0] + replacements[2] + replacements[1] + replacements[1] + replacements[2] + replacements[0] + replacements[2] + "}]]", component.toString());
    }

    /**
     * Test not enough arguments for sequentials
     */
    @Test
    void testSequentialVariableInstructionTooMany() {
        final String chatString = "My replacement variable is <v><v><v><v>";
        final String[] replacements = {"Test", "test2", "test3"};
        final Component component = DatChatFormatting.formatChatString(chatString, (Object[]) replacements);
        Assertions.assertEquals("literal{Failed to parse message, Too many unindexed variable instructions encountered at 36}", component.toString());
    }

    /**
     * Test too great variable index
     */
    @Test
    void testIndexedVariableInstructionTooLarge() {
        final String chatString = "My replacement variable is <v 4>";
        final String[] replacements = {"Test", "test2", "test3"};
        final Component component = DatChatFormatting.formatChatString(chatString, (Object[]) replacements);
        Assertions.assertEquals("literal{Failed to parse message, variable instruction at 27 requested an argument at an index that isn't available: 4}", component.toString());
    }

    /**
     * Test variable index is not a number
     */
    @Test
    void testIndexedVariableInstructionNotNumber() {
        final String chatString = "My replacement variable is <v Bad>";
        final String[] replacements = {"Test", "test2", "test3"};
        final Component component = DatChatFormatting.formatChatString(chatString, (Object[]) replacements);
        Assertions.assertEquals("literal{Failed to parse message, variable instruction at 27 uses bad variable index: Bad}", component.toString());
    }

    /**
     * Test variable instruction with component
     */
    @Test
    void testVariableInstructionComponent() {
        final String chatString = "My replacement variable is <v>";
        final Component componentParam = Component.literal("Test Component").withStyle(ChatFormatting.BLUE, ChatFormatting.BOLD);
        final Component component = DatChatFormatting.formatChatString(chatString, componentParam);
        Assertions.assertEquals("empty[siblings=[literal{My replacement variable is }, " + componentParam + "]]", component.toString());
    }

    /**
     * Test colour formatting and variable combined
     */
    @Test
    void testFormattingVariableInstruction() {
        final String chatString = "<f red>My replacement variable <f bold> is <v>";
        final String[] replacements = {"Test", "test2", "test3"};
        final Component component = DatChatFormatting.formatChatString(chatString, (Object[]) replacements);
        Assertions.assertEquals("empty[siblings=[literal{§cMy replacement variable §l is Test}]]", component.toString());
    }

    /**
     * Test colour is continued after component replacement
     */
    @Test
    void testFormattingComponentVariableInstruction() {
        final String chatString = "<f red>My replacement variable <f bold> is <v> and there's <f gold> some more";
        final Component componentParam = Component.literal("Test Component").withStyle(ChatFormatting.BLUE, ChatFormatting.BOLD);
        final Component component = DatChatFormatting.formatChatString(chatString, componentParam);
        Assertions.assertEquals("literal{Failed to parse message, variable instruction at 27 uses bad variable index: Bad}", component.toString());
    }

    /**
     * Test formatting resets occur properly after component replacement
     */
    @Test
    void testFormattingResetComponentVariableInstruction() {
        final String chatString = "<f red>My replacement variable <f bold> is <f blue> <v> and there's <r> some <v 0> more";
        final Component componentParam = Component.literal("Test Component").withStyle(ChatFormatting.BLUE, ChatFormatting.BOLD);
        final Component component = DatChatFormatting.formatChatString(chatString, componentParam);
        Assertions.assertEquals("empty[siblings=[literal{§cMy replacement variable §l is §9 }, literal{Test Component}[style={color=blue,bold}], literal{§9 and there's §r some }, literal{Test Component}[style={color=blue,bold}], literal{§r more}]]", component.toString());
    }
}
