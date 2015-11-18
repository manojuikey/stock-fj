package com.jpmc.stocks.api;

import java.util.Date;

/**
 * The Interface StocksService.
 */
public interface StocksService {

    /**
     * Calculate the dividend for the given stock and current price.
     *
     * @param stockSymbol
     *            the stock symbol.
     * @param currentPrice
     *            the current price.
     * @return the dividend of the given stock with given price.
     */
    double calculateDividend(String stockSymbol, double currentPrice);

    /**
     * Calculate profit to earning ratio for the given stock and current price.
     *
     * @param stockSymbol
     *            the stock symbol.
     * @param currentPrice
     *            the current price.
     * @return the PE ratio for the given stock with given price.
     */
    double calculatePERatio(String stockSymbol, double currentPrice);

    /**
     * Record stock trade operation.
     *
     * @param operation
     *            the operation {@link StockOperation#BUY} or {@link StockOperation#SELL}.
     * @param stockSymbol
     *            the unique symbol assigned to the stock.
     * @param quantity
     *            the number of stocks traded.
     * @param tradedPrice
     *            the traded price per stock.
     * @param tradedTime
     *            the traded time.
     */
    void recordStockTrade(String stockSymbol, StockOperation operation, int quantity, double tradedPrice, Date tradedTime);

    /**
     * Calculate the trade volume of the stock in the last 15 minutes.
     *
     * @param stockSymbol
     *            the stock symbol.
     * @return the volume of the given stock.
     */
    double calculateVolume(String stockSymbol);

    /**
     * Calculates the GBCE All Share Index using the geometric mean of prices for all stocks.
     *
     * @return the geometric mean of all share index.
     */
    double calculateGBCE();
}
