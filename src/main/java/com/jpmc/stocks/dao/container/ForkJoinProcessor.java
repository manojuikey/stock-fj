package com.jpmc.stocks.dao.container;

import java.util.Date;
import java.util.concurrent.ForkJoinPool;

/**
 * The Trade record search processor engine which finds the trade record using {@link ForkJoinPool} utility to search the trade record in all cache
 * containers in parallel.
 */
public class ForkJoinProcessor {

    /** The pool. */
    private ForkJoinPool pool;
    private final int numOfContainers;

    /**
     * @param numOfContainers
     */
    public ForkJoinProcessor(final int numOfContainers) {
        this.numOfContainers = numOfContainers;
        pool = new ForkJoinPool(numOfContainers);
    }

    public Double calculateVolume(final String stock, final Date from) {
        return pool.invoke(new RecursiveTradeCalculator(stock, from, 0, numOfContainers));
    }
}