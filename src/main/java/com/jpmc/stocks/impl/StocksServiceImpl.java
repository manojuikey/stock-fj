package com.jpmc.stocks.impl;

import java.util.*;
import java.util.logging.Logger;

import com.jpmc.stocks.api.*;
import com.jpmc.stocks.dao.CacheStore;
import com.jpmc.stocks.dao.CacheStoreFactory;
import com.jpmc.stocks.entity.*;

/**
 * The Class StocksServiceImpl.
 */
public class StocksServiceImpl implements StocksService {

    private final static Logger LOGGER = Logger.getLogger(StocksServiceImpl.class.getCanonicalName());
    /** The cache store. */
    private final CacheStore cacheStore;

    /** The Constant FIFTEEN_MILLIS. */
    private static final long FIFTEEN_MILLIS = 15 * 60 * 1000;

    /**
     * Instantiates a new stocks service impl.
     */
    public StocksServiceImpl() {

        this.cacheStore = CacheStoreFactory.INSTANCE.getCacheStore();
    }

    @Override
    public double calculateDividend(final String stockSymbol, final double currentPrice) {
        if (stockSymbol == null) {
            LOGGER.severe("Stock symbol can not be null");
            throw new IllegalArgumentException("Stock symbol can not be null");
        }
        if (currentPrice == 0d) {
            LOGGER.severe("Current price can not be 0.0 for this operation");
            throw new IllegalArgumentException("Current price can not be 0.0 for this operation");
        }
        final Stock stock = cacheStore.getStockById(stockSymbol);
        if (stock == null) {
            LOGGER.severe("Could not find the stock with the symbol " + stockSymbol);
            throw new StockNotFoundException("Could not find the stock with the symbol " + stockSymbol);
        }
        final StockType stockType = stock.getStockType();
        switch (stockType) {
            case COMMON:
                return stock.getLastDividend() / currentPrice;
            case PREFERED:
                return (stock.getFixedDividend() * stock.getParValue()) / currentPrice;
        }
        return 0;
    }

    @Override
    public double calculatePERatio(final String stockSymbol, final double currentPrice) {
        return calculateDividend(stockSymbol, currentPrice) / currentPrice;
    }

    @Override
    public void recordStockTrade(final String stockSymbol, final StockOperation operation, final int quantity, final double tradedPrice,
                                 final Date tradedTime) {
        if (quantity <= 0) {
            LOGGER.severe("The quantity should be more than 0 for this operation");
            throw new IllegalArgumentException("The quantity can not be 0 for this operation");
        }

        if (stockSymbol == null || cacheStore.getStockById(stockSymbol) == null) {
            LOGGER.severe("Could not find the stock with the symbol " + stockSymbol);
            throw new StockNotFoundException("Could not find the stock with the symbol " + stockSymbol);
        }

        if (operation == null) {
            LOGGER.severe("The stock operation argument can not be null");
            throw new IllegalArgumentException("The stock operation argument can not be null " + stockSymbol);
        }

        final TradeRecord tradeRecord = new TradeRecord(stockSymbol, quantity, tradedPrice, operation, tradedTime);
        cacheStore.storeTrade(tradeRecord);
    }

    @Override
    public double calculateVolume(final String stockSymbol) {
        if (stockSymbol == null || cacheStore.getStockById(stockSymbol) == null) {
            LOGGER.severe("Could not find the stock with the symbol " + stockSymbol);
            throw new StockNotFoundException("Could not find the stock with the symbol " + stockSymbol);
        }
        final Date currentDate = new Date();
        final long fifteenMinsAgo = currentDate.getTime() - FIFTEEN_MILLIS;

        final Date startindTradeDate = new Date(fifteenMinsAgo);

        return cacheStore.calculateVolume(stockSymbol, startindTradeDate);
    }

    @Override
    public double calculateGBCE() {
        final Collection<Stock> stocks = cacheStore.getListedStocks();
        final ArrayList<Double> stockPrices = new ArrayList<Double>();
        for (final Stock stock : stocks) {
            final double stockPrice = cacheStore.calculateVolume(stock.getSymbol(), null);
            if (stockPrice > 0) {
                stockPrices.add(stockPrice);
            }
        }
        double geometricMean = 0.0;
        double product = 1.0;

        if (stockPrices.size() > 0) {
            for (final double price : stockPrices) {
                product = product * price;
            }
            geometricMean = Math.pow(product, 1.0 / stockPrices.size());
        }
        return geometricMean;
    }

}
