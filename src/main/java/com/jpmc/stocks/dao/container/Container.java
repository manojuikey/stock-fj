package com.jpmc.stocks.dao.container;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

import com.jpmc.stocks.entity.TradeRecord;

public class Container {
    private final static Logger LOGGER = Logger.getLogger(Container.class.getCanonicalName());

    private final ConcurrentMap<String, List<TradeRecord>> tradeRecords = new ConcurrentHashMap<>();
    private final String id;

    public Container() {
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Find all the trade records executed after the <code>startingDate</code> for the given Stock
     *
     * @param startingDate
     */
    public List<TradeRecord> getRecords(final String stock, final Date startingDate) {
        if (stock == null) {
            LOGGER.severe("Null stock passed");
            throw new IllegalArgumentException("Null stock passed");
        }
        if (!tradeRecords.containsKey(stock)) {
            /** No trading record found for the passed stock in this container **/
            LOGGER.info("No trading record found for the passed stock in this container -" + id);
            return Collections.emptyList();
        }

        LOGGER.info("searching all trading record for the stock " + stock + ", starting date- " + startingDate);

        final List<TradeRecord> allTradeRecordsForStock = tradeRecords.get(stock);
        if (startingDate == null) {
            /** find all record for the given trade **/
            return allTradeRecordsForStock;
        } else {
            final List<TradeRecord> filteredTradeRecordsForStock = new ArrayList<>();
            for (final TradeRecord record : allTradeRecordsForStock) {
                if (record.getTradedTime().after(startingDate)) {
                    filteredTradeRecordsForStock.add(record);
                }
            }
            return filteredTradeRecordsForStock;
        }

    }

    public void storeTrade(final TradeRecord tradeRecord) {
        List<TradeRecord> stockRecords = tradeRecords.get(tradeRecord.getTradedStock());
        if (stockRecords == null) {
            stockRecords = new ArrayList<TradeRecord>();
            final List<TradeRecord> previousValue = tradeRecords.putIfAbsent(tradeRecord.getTradedStock(), stockRecords);
            if (previousValue != null) {
                /** Map is populated by another thread with the given stock entry **/
                stockRecords = previousValue;
            }
        }
        stockRecords.add(tradeRecord);
    }
}
