package com.jpmc.stocks.entity;

import java.util.Date;

import com.jpmc.stocks.api.StockOperation;

/**
 * The Immutable Class for TradeRecord.
 */
public final class TradeRecord {

    /** The traded stock. */
    private final String tradedStock;

    /** The traded time. */
    private final Date tradedTime;

    /** The traded price. */
    private final double tradedPrice;

    /** The quantity. */
    private final int quantity;

    /** The trade type. */
    private final StockOperation tradeType;

    /**
     * Instantiates a new trade record.
     *
     * @param tradedStock
     *            the traded stock
     * @param quantity
     *            the quantity
     * @param tradedPrice
     *            the price of single stock traded in this transaction.
     * @param tradeType
     *            the trade type
     * @param tradedTime
     *            the traded time
     */
    public TradeRecord(final String tradedStock, final int quantity, final double tradedPrice, final StockOperation tradeType,
                       final Date tradedTime) {
        this.tradedStock = tradedStock;
        this.quantity = quantity;
        this.tradedTime = tradedTime;
        this.tradeType = tradeType;
        this.tradedPrice = tradedPrice;
    }

    /**
     * Gets the traded stock.
     *
     * @return the tradedStock
     */
    public String getTradedStock() {
        return tradedStock;
    }

    /**
     * Gets the traded price.
     *
     * @return the tradedPrice
     */
    public double getTradedPrice() {
        return tradedPrice;
    }

    /**
     * Gets the traded time.
     *
     * @return the tradedTime
     */
    public Date getTradedTime() {
        return tradedTime;
    }

    /**
     * Gets the quantity.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the trade type.
     *
     * @return the tradeType
     */
    public StockOperation getTradeType() {
        return tradeType;
    }

}
