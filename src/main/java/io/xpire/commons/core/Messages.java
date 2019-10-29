package io.xpire.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_ITEM_DISPLAYED_INDEX = "The item index provided is invalid.";
    public static final String MESSAGE_ITEMS_LISTED_OVERVIEW = "%d items listed!";
    public static final String MESSAGE_INVALID_REMINDER_THRESHOLD = "%s is not a valid reminder threshold.";
    public static final String MESSAGE_INVALID_TAGS = "Tags are not in the item specified!";
    public static final String MESSAGE_UNKNOWN_DELETE_MODE = "Unknown Delete mode.";
    public static final String MESSAGE_SUGGESTIONS = " Did you mean %s?";
    public static final String MESSAGE_REMINDER_THRESHOLD_EXCEEDED =
            "The item has only %s day(s) left before expiring. \nReminder will start from today!";
    public static final String MESSAGE_THRESHOLD_ITEM_EXPIRED = "Cannot set reminder for expired item!";
    public static final String MESSAGE_MAXIMUM_INPUT_LENGTH_EXCEEDED = "Maximum length of input exceeded!";
}
