package io.xpire.logic.commands;

/**
 * Specifies if command causes change in Expiry Date tracker or Replenish List.
 */
public enum CommandType {

    XPIRE, REPLENISH;

    public static final String MESSAGE_INVALID_COMMAND_TYPE = "This command type is not valid";
}
