package com.jpmc.stocks.dao;

import java.util.Collection;
import java.util.Date;

import com.jpmc.stocks.dao.container.Container;
import com.jpmc.stocks.entity.Stock;
import com.jpmc.stocks.entity.TradeRecord;

/**
 * The Interface CacheStore.
 */
public interface CacheStore {

    /**
     * Stores a trade record, container based implementation can utilize round robin load balancing strategy to store the trade records evenly spread
     * across multiple containers.
     *
     * @param tradeRecord
     *            the trade record
     */
    void storeTrade(final TradeRecord tradeRecord);

    /**
     * Store stock.
     *
     * @param stock
     *            the stock
     */
    void storeStock(final Stock stock);

    /**
     * Gets the stock by id.
     *
     * @param stockSymbol
     *            the stock symbol
     * @return the stock by id
     */
    Stock getStockById(final String stockSymbol);

    /**
     * Gets the all the listed stocks.
     *
     * @return the listed stocks
     */
    Collection<Stock> getListedStocks();

    /**
     * Gets the container by index.
     *
     * @param index
     *            the index
     * @return the container by index
     */
    Container getContainerByIndex(int index);

    /**
     * Calculate volume.
     *
     * @param stock
     *            the stock
     * @param startingDate
     *            the starting date
     * @return the double
     */
    double calculateVolume(String stock, Date startingDate);
}
