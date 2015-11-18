package com.jpmc.stocks.dao.container;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import com.jpmc.stocks.dao.CacheStore;
import com.jpmc.stocks.entity.Stock;
import com.jpmc.stocks.entity.TradeRecord;

/**
 * The scalable ContainerBasedCacheStore.
 */
public class ContainerBasedCacheStore implements CacheStore {

    /** The containers. */
    private final List<Container> CONTAINERS = new ArrayList<Container>();

    /** The num of containers. */
    private final int numOfContainers = 4;

    /** The pointer. */
    private final AtomicInteger pointer = new AtomicInteger(0);

    /** The stocks. */
    private final Map<String, Stock> STOCKS = new HashMap<>();

    /** The Constant INSTANCE. */
    static final ContainerBasedCacheStore INSTANCE = new ContainerBasedCacheStore();

    /** The fork join processor. */
    private final ForkJoinProcessor forkJoinProcessor;

    /**
     * Instantiates a new container based cache store.
     */
    private ContainerBasedCacheStore() {
        for (int index = 0; index < numOfContainers; index++) {
            final Container container = new Container();
            CONTAINERS.add(container);
        }
        forkJoinProcessor = new ForkJoinProcessor(numOfContainers);
    }

    /**
     * Find all the trade records executed after the <code>startingDate</code> for the given Stock.
     *
     * @param stock
     *            the stock
     * @param startingDate
     *            the starting date
     * @return the double
     */
    @Override
    public double calculateVolume(final String stock, final Date startingDate) {
        return forkJoinProcessor.calculateVolume(stock, startingDate);
    }

    /**
     * Store trade.
     *
     * @param tradeRecord
     *            the trade record
     */
    @Override
    public void storeTrade(final TradeRecord tradeRecord) {
        CONTAINERS.get(getNextIndex()).storeTrade(tradeRecord);
    }

    /**
     * Store stock.
     *
     * @param stock
     *            the stock
     */
    @Override
    public void storeStock(final Stock stock) {
        STOCKS.put(stock.getSymbol(), stock);

    }

    /**
     * Gets the stock by id.
     *
     * @param stockSymbol
     *            the stock symbol
     * @return the stock by id
     */
    @Override
    public Stock getStockById(final String stockSymbol) {
        return STOCKS.get(stockSymbol);

    }

    /**
     * Gets the container by index.
     *
     * @param index
     *            the index
     * @return the container by index
     */
    @Override
    public Container getContainerByIndex(final int index) {
        return CONTAINERS.get(index);
    }

    /**
     * Gets the next index.
     *
     * @return the next index
     */
    private int getNextIndex() {
        if (pointer.get() >= CONTAINERS.size()) {
            pointer.set(0);
        }
        final int currentIndex = pointer.get();
        pointer.incrementAndGet();
        return currentIndex;
    }

    /**
     * Gets the listed stocks.
     *
     * @return the listed stocks
     */
    @Override
    public Collection<Stock> getListedStocks() {
        return STOCKS.values();
    }

}
