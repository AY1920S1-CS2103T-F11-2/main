package io.xpire.model.item;

import static java.util.Objects.requireNonNull;

import io.xpire.commons.util.AppUtil;
import io.xpire.commons.util.StringUtil;
import io.xpire.logic.parser.exceptions.ParseException;

/**
 * Represents the quantity of an xpireItem.
 * Users are only allowed to key in positive integers.
 * Internally, there can be quantity of value 0.
 * Guarantees: immutable and valid.
 */
public class Quantity {

    public static final String DEFAULT_QUANTITY = "1";
    public static final String MESSAGE_CONSTRAINTS =
            "Quantity added should be a positive integer and should not be blank";
    public static final String MESSAGE_QUANTITY_LIMIT = "Quantity will exceed maximum allowed quantity of 100000";

    public static final int MAX_VALUE = 100000;
    private static final String INTERNAL_MESSAGE_CONSTRAINTS =
            "Quantity added should be a non-negative integer and should not be blank";
    private int quantity;


    /**
     * Constructs a {@code Quantity}.
     *
     * @param quantity A valid input quantity, i.e. positive integer not exceeding maximum allowed limit.
     */
    public Quantity(String quantity) {
        String trimmedQuantity = quantity.trim();
        requireNonNull(trimmedQuantity);
        AppUtil.checkArgument(isPositiveIntegerQuantity(trimmedQuantity), MESSAGE_CONSTRAINTS);
        AppUtil.checkArgument(isAcceptedRange(trimmedQuantity), MESSAGE_QUANTITY_LIMIT);
        this.quantity = Integer.parseInt(trimmedQuantity);
    }

    /**
     * Constructs a {@code Quantity}.
     *
     * @param quantity A valid quantity, i.e. a non-negative integer.
     */
    public Quantity(String quantity, boolean internalCall) {
        requireNonNull(quantity);
        String trimmedQuantity = quantity.trim();
        if (internalCall) {
            AppUtil.checkArgument(isValidQuantity(trimmedQuantity), INTERNAL_MESSAGE_CONSTRAINTS);
            this.quantity = Integer.parseInt(trimmedQuantity);
        }
    }

    private Quantity(int quantity) throws ParseException {
        if (quantity < 0) {
            throw new ParseException(MESSAGE_CONSTRAINTS);
        }
        this.quantity = quantity;
    }


    /**
     * Returns true if a given input string is a valid quantity.
     */
    public static boolean isValidQuantity(String test) {
        return StringUtil.isNonNegativeInteger(test) && Integer.parseInt(test) <= MAX_VALUE;
    }

    /**
     * Returns true if a given input string lies below the maximum value.
     */
    public static boolean isAcceptedRange(String test) {
        return Integer.parseInt(test) <= MAX_VALUE;
    }


    /**
     * Returns true if a given input string is a valid integer.
     */
    public static boolean isPositiveIntegerQuantity(String test) {
        return StringUtil.isNonZeroUnsignedInteger(test);
    }

    /**
     * Returns true if a given input string is numeric but exceeds given range.
     */
    public static boolean isNumericButExceedQuantity(String test) {
        return StringUtil.isNumeric(test) && test.length() > 6;
    }

    /**
     * Returns true if a given input string is a valid input.
     */

    /**
     * Returns true if quantity is zero.
     */
    public static boolean quantityIsZero(Quantity quantity) {
        return quantity.quantity == 0;
    }

    public boolean isLessThan(Quantity deductAmount) {
        return this.quantity < deductAmount.quantity;
    }

    /**
     * Reduces quantity by specified amount.
     *
     * @param deductAmount Amount to be deducted.
     * @return Quantity to be deducted.
     * @throws ParseException if new quantity is negative.
     */
    public Quantity deductQuantity(Quantity deductAmount) throws ParseException {
        Quantity newQuantity;
        newQuantity = new Quantity(this.quantity - deductAmount.quantity);
        return newQuantity;
    }

    /**
     * Reduces quantity by specified amount.
     *
     * @param increaseAmount quantity to be increased.
     * @return new Quantity of item.
     * @throws ParseException if new quantity is not within valid range.
     */
    public Quantity increaseQuantity(Quantity increaseAmount) throws ParseException {
        Quantity newQuantity;
        if ((increaseAmount.quantity + this.quantity) >= MAX_VALUE) {
            throw new ParseException(MESSAGE_QUANTITY_LIMIT);
        }
        newQuantity = new Quantity(this.quantity + increaseAmount.quantity);
        return newQuantity;
    }

    @Override
    public String toString() {
        return "" + this.quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Quantity)) {
            return false;
        } else {
            Quantity other = (Quantity) obj;
            return this.quantity == other.quantity;
        }
    }

    @Override
    public int hashCode() {
        return this.quantity;
    }
}
