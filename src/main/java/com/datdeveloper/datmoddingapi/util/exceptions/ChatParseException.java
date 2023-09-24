package com.datdeveloper.datmoddingapi.util.exceptions;

import java.text.ParseException;

public class ChatParseException extends ParseException {
    private final String formatMessage;

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
