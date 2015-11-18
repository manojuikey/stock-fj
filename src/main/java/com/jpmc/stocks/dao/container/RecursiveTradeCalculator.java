package com.jpmc.stocks.dao.container;

import java.util.Date;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jpmc.stocks.dao.CacheStoreFactory;
import com.jpmc.stocks.entity.TradeRecord;

/**
 * Search trade record task. Search the trade record in all the container in parallel using devide and conquer strategy.
 */
public class RecursiveTradeCalculator extends RecursiveTask<Double> {

    /** The logger. */
    private static Logger LOGGER = Logger.getLogger(RecursiveTradeCalculator.class.getName());

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 235756576876861L;

    /** The startingDate criteria to search record from */
    final Date startingDate;

    /** The low. */
    final int low;

    /** The high. */
    final int high;

    private final String stockSymbol;

    /**
     * Instantiates a new find query task.
     *
     * @param startingDate
     *            the minimum net value search criteria.
     * @param low
     *            lower index of the container to delegate the task
     * @param high
     *            the higher index of the container to delegate the task
     *
     */
    public RecursiveTradeCalculator(final String stockSymbol, final Date startingDate, final int low, final int high) {
        this.startingDate = startingDate;
        this.low = low;
        this.high = high;
        this.stockSymbol = stockSymbol;
    }

    @Override
    public Double compute() {
        if (high - low <= 1) {
            LOGGER.log(Level.INFO, "Running compute job on container- " + low);
            final List<TradeRecord> records = CacheStoreFactory.INSTANCE.getCacheStore().getContainerByIndex(low).getRecords(stockSymbol,
                    startingDate);
            return calculateVolume(records);
        } else {
            final int mid = low + (high - low) / 2;
            final RecursiveTradeCalculator leftSubTree = new RecursiveTradeCalculator(stockSymbol, startingDate, low, mid);
            final RecursiveTradeCalculator rightSubTree = new RecursiveTradeCalculator(stockSymbol, startingDate, mid, high);
            leftSubTree.fork();
            final double rightTreeResult = rightSubTree.compute();
            final double leftTreeResult = leftSubTree.join();

            return leftTreeResult + rightTreeResult;
        }
    }

    private double calculateVolume(final List<TradeRecord> records) {
        if (records == null || records.isEmpty()) {
            return 0.0d;
        }
        double totalPrice = 0.0;
        double totalQuantity = 0;
        for (final TradeRecord record : records) {
            final double tradePrice = record.getTradedPrice() * record.getQuantity();
            totalPrice = totalPrice + tradePrice;
            totalQuantity = totalQuantity + record.getQuantity();
        }
        return (totalPrice * totalQuantity) / totalQuantity;
    }
}
