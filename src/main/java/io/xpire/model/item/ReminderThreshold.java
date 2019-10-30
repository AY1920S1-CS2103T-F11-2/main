package io.xpire.model.item;

import static java.util.Objects.requireNonNull;

import io.xpire.commons.util.AppUtil;
import io.xpire.commons.util.StringUtil;


/**
 * Represents an XpireItem's reminder threshold.
 * Guarantees: immutable; is valid as declared in {@link #isValidReminderThreshold(String)}.
 */
public class ReminderThreshold {

    public static final String MESSAGE_CONSTRAINTS = "Reminder threshold should be a non-negative integer.";
    public static final String MESSAGE_QUANTITY_EXCEEDED = "Reminder threshold exceeds maximum input of 36500.";
    public static final String DEFAULT_THRESHOLD = "0";

    public static final int MAX_VALUE = 36500;

    private final int reminderThreshold;

    /**
     * Constructs a {@code ReminderThreshold}.
     *
     * @param reminderThreshold A valid reminder threshold.
     */
    public ReminderThreshold(String reminderThreshold) {
        requireNonNull(reminderThreshold);
        AppUtil.checkArgument(isValidReminderThreshold(reminderThreshold), MESSAGE_CONSTRAINTS);
        this.reminderThreshold = Integer.parseInt(reminderThreshold);
    }

    /**
     * Returns true if a given integer is a valid reminder threshold.
     */
    public static boolean isValidReminderThreshold(String test, ExpiryDate ed) {
        long remainingDays = Long.parseLong(ed.getStatus());
        return StringUtil.isNonNegativeInteger(test)
                && (!StringUtil.isExceedingMaxValue(test, MAX_VALUE))
                && Integer.parseInt(test) <= remainingDays;
    }

    /**
     * Returns true if a given integer is a valid reminder threshold.
     */
    public static boolean isValidReminderThreshold(String test) {
        return StringUtil.isNonNegativeInteger(test)
                && (!StringUtil.isExceedingMaxValue(test, MAX_VALUE));
    }

    /**
     * Returns true if a given input string is numeric but exceeds given range.
     */
    public static boolean isNumericButExceedQuantity(String test) {
        return StringUtil.isNumeric(test) && test.length() > 5;
    }

    public int getValue() {
        return this.reminderThreshold;
    }

    public boolean isDefault() {
        return this.reminderThreshold == 0;
    }

    @Override
    public String toString() {
        return "" + this.reminderThreshold;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof ReminderThreshold)) {
            return false;
        } else {
            ReminderThreshold other = (ReminderThreshold) obj;
            return this.reminderThreshold == other.reminderThreshold;
        }
    }

    @Override
    public int hashCode() {
        return this.reminderThreshold;
    }
}
