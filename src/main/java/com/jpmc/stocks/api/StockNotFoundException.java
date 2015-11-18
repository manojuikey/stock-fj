package com.jpmc.stocks.api;

/**
 * The Class StockNotFoundException thrown when invalid stock is queried.
 */
public class StockNotFoundException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1543453535L;

    /**
     * Instantiates a new stock not found exception.
     *
     * @param message
     *            the message
     */
    public StockNotFoundException(final String message) {
        super(message);
    }

}
