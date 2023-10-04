package com.datdeveloper.datmoddingapi.util.exceptions;

import java.text.ParseException;

/**
 * An exception that occurs when failing to parse a chat message
 */
public class ChatParseException extends ParseException {
    private final String formatMessage;

    /**
     * @param message       A comment on the error
     * @param formatMessage The message that failed to parse
     * @param errorOffset   The offset in characters from the beginning of the format message when the parse error
     *                      occurred
     */
    public ChatParseException(final String message, final String formatMessage, final int errorOffset) {
        super(message, errorOffset);
        this.formatMessage = formatMessage;
    }

    @Override
    public String getMessage() {
        return "Failed to parse message at position" + getErrorOffset() + ": \n" + super.getMessage();
    }

    public String getFormatMessage() {
        return formatMessage;
    }
}
