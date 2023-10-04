package com.datdeveloper.datmoddingapi.command.util;

import com.datdeveloper.datmoddingapi.util.DatChatFormatting;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * A class to split the results of a command into pages
 * Commands that implement this should take an integer as the last argument that is passed to the {@link #sendPage(int, CommandSource)} method
 * @param <T> The type of the item being paged
 */
public class Pager<T> {
    /**
     * The command used to create the pager
     */
    final String command;

    /**
     * The heading of the pager
     */
    final String headerText;

    /**
     * The amount of elements per page
     */
    final int elementsPerPage;

    /**
     * The elements
     */
    final Collection<? extends T> elements;

    /**
     * A function that converts the elements into chat components
     */
    final ElementTransformer<T> transformer;

    /**
     * Construct a pager with the default amount of items per page (10)
     * @param command The command used
     * @param headerText The heading of the pager
     * @param elements A list of the elements being paged
     * @param transformer The function that converts the elements into a chat component
     */
    public Pager(final String command, @Nullable final String headerText, final Collection<? extends T> elements, final ElementTransformer<T> transformer) {
        this(command, headerText, 10, elements, transformer);
    }

    /**
     * Construct a pager
     * @param command The command used
     * @param headerText The heading of the pager
     * @param elementsPerPage The amount of elements per page
     * @param elements A list of the elements being paged
     * @param transformer The function that converts the elements into a chat component
     */
    public Pager(@NotNull final String command, @Nullable final String headerText, final int elementsPerPage, @NotNull final Collection<? extends T> elements, @NotNull final ElementTransformer<T> transformer) {
        this.command = command;
        this.headerText = headerText;
        this.elementsPerPage = elementsPerPage;
        this.elements = elements;
        this.transformer = transformer;
    }

    /**
     * Get the total number of pages that this pager has
     * @return The number of pages this pager has
     */
    private int getTotalPageCount() {
        return (int) Math.ceil((float) elements.size() / (float) elementsPerPage);
    }

    /**
     * Get the header for the pager
     * @return The header for the pager
     */
    protected Component getHeader() {
        String header = headerText;
        @SuppressWarnings("ConstantConditions")
        int headerLength = 2 + headerText.length();
        if (headerText.length() % 2 == 1) {
            header += " ";
            headerLength += 1;
        }

        final int footerLength = 37 + (String.valueOf(getTotalPageCount()).length() * 2);
        final int paddingLength = (footerLength - headerLength) / 2;
        final String pad;
        if (paddingLength > 2) {
            pad = "=".repeat(paddingLength);
        } else {
            pad = "";
        }

        return Component.literal(
                DatChatFormatting.TextColour.INFO + pad + "["
                        + DatChatFormatting.TextColour.HEADER + header
                        + DatChatFormatting.TextColour.INFO + "]" + pad
        );
    }

    /**
     * Get the body of the page
     * <br>
     * This performs the transformations on the snippet of the elements, formatted as a list
     * @param page The page the body is for
     * @return the body
     */
    protected Component getBody(final int page) {
        final List<Component> components = elements.stream()
                .skip((long) (page - 1) * elementsPerPage)
                .limit(elementsPerPage)
                .map(transformer::transform)
                .toList();

        return ComponentUtils.formatList(components, Component.literal("\n"));
    }

    /**
     * Get the footer for the page
     * <br>
     * This will generate the navigation buttons, accounting for which buttons are enabled
     * @param page The page the footer is for
     * @return The footer
     */
    protected Component getFooter(final int page) {
        final int totalPages = getTotalPageCount();
        final int prevPage = page - 1;
        final int nextPage = page + 1;
        // First and Previous buttons
        final MutableComponent firstButton = Component.literal(" «");
        final MutableComponent prevButton = Component.literal(" < ");
        if (page == 1) {
            firstButton.withStyle(ChatFormatting.DARK_GRAY);
            prevButton.withStyle(ChatFormatting.DARK_GRAY);
        } else {
            firstButton.withStyle(Style.EMPTY
                    .withHoverEvent(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, Component.literal(DatChatFormatting.TextColour.INFO + "First Page")
                    ))
                    .withClickEvent(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            command + " 1"
                    ))
                    .applyFormats(DatChatFormatting.TextColour.COMMAND)
            );
            prevButton.withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        Component.literal(DatChatFormatting.TextColour.INFO + "Previous Page")
                ))
                .withClickEvent(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        command + " " + prevPage
                ))
                .applyFormats(DatChatFormatting.TextColour.COMMAND)
            );
        }


        // Current page and total pages text
        String pageString = String.valueOf(page);
        final String totalPagesString = String.valueOf(totalPages);
        {
            final int lengthDiff = totalPagesString.length() - pageString.length();
            pageString = " ".repeat(lengthDiff) + pageString;
        }
        final MutableComponent pageText = Component.literal("(%s/%s)".formatted(pageString, totalPagesString))
                .withStyle(DatChatFormatting.TextColour.HEADER);

        // Next and Last Buttons
        final MutableComponent nextButton = Component.literal(" > ");
        final MutableComponent lastButton = Component.literal("» ");
        if (page == totalPages) {
            nextButton.withStyle(ChatFormatting.DARK_GRAY);
            lastButton.withStyle(ChatFormatting.DARK_GRAY);
        } else {
            nextButton.withStyle(Style.EMPTY
                    .withHoverEvent(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            Component.literal(DatChatFormatting.TextColour.INFO + "Next Page")
                    ))
                    .withClickEvent(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            command + " " + nextPage
                    ))
                    .applyFormats(DatChatFormatting.TextColour.COMMAND)
            );
            lastButton.withStyle(Style.EMPTY
                    .withHoverEvent(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            Component.literal(DatChatFormatting.TextColour.INFO + "Last Page")
                    ))
                    .withClickEvent(new ClickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            command + " " + totalPages
                    ))
                    .applyFormats(DatChatFormatting.TextColour.COMMAND)
            );
        }

        return Component.literal(DatChatFormatting.TextColour.INFO + "============[")
                .append(firstButton).append(prevButton)
                .append(pageText)
                .append(nextButton).append(lastButton)
                .append(DatChatFormatting.TextColour.INFO + "]============");
    }

    /**
     * Render the page of the pager
     * <br>
     * This produces a full component ready to display to the user, with the header, body, and footer, rendered and
     * combined
     * @param page The page to render
     * @return The fully rendered page of the pager
     */
    public Component renderPage(final int page) {
        final MutableComponent component = MutableComponent.create(ComponentContents.EMPTY);
        if (headerText != null) {
            component.append(getHeader())
                     .append("\n");
        }
        component.append(getBody(page))
                 .append("\n");

        component.append(getFooter(page));

        return component;
    }

    /**
     * Send the given page to the given {@link CommandSource}
     * @param page The page to show to the CommandSource
     * @param source The command source to send the page to
     */
    public void sendPage(final int page, final CommandSource source) {
        if (page > getTotalPageCount()) {
            source.sendSystemMessage(Component.literal(DatChatFormatting.TextColour.ERROR + "There aren't that many pages"));
            return;
        }

        source.sendSystemMessage(renderPage(page));
    }

    /**
     * A Functional interface to transform the given PagedElement into a Chat Component
     * @param <T> The class to transform into a {@link Component}
     */
    @FunctionalInterface
    public interface ElementTransformer<T> {
        /**
         * @param element The element
         *
         * @return The element represented as a component
         */
        Component transform(final T element);
    }
}
