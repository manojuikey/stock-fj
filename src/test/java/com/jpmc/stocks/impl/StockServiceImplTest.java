package com.jpmc.stocks.impl;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jpmc.stocks.api.*;
import com.jpmc.stocks.dao.CacheStoreFactory;
import com.jpmc.stocks.entity.Stock;
import com.jpmc.stocks.entity.StockType;

public class StockServiceImplTest {

    @BeforeClass
    public static void setup() {
        final Stock TEA = new Stock("TEA", StockType.COMMON);
        TEA.setLastDividend(0);
        TEA.setParValue(100);

        final Stock POP = new Stock("POP", StockType.COMMON);
        POP.setLastDividend(8);
        POP.setParValue(100);

        final Stock ALE = new Stock("ALE", StockType.COMMON);
        ALE.setLastDividend(23);
        ALE.setParValue(60);

        final Stock GIN = new Stock("GIN", StockType.PREFERED);
        GIN.setLastDividend(8);
        GIN.setFixedDividend(2);
        GIN.setParValue(100);

        final Stock JOE = new Stock("JOE", StockType.COMMON);
        JOE.setLastDividend(13);
        JOE.setParValue(250);

        CacheStoreFactory.INSTANCE.getCacheStore().storeStock(TEA);
        CacheStoreFactory.INSTANCE.getCacheStore().storeStock(POP);
        CacheStoreFactory.INSTANCE.getCacheStore().storeStock(ALE);
        CacheStoreFactory.INSTANCE.getCacheStore().storeStock(GIN);
        CacheStoreFactory.INSTANCE.getCacheStore().storeStock(JOE);
    }

    public void test_factory_instances() {
        assertTrue(CacheStoreFactory.INSTANCE.getCacheStore() != null);
        assertSame(CacheStoreFactory.INSTANCE.getCacheStore(), CacheStoreFactory.INSTANCE.getCacheStore());
    }

    @Test(expected = StockNotFoundException.class)
    public void add_invalid_trade() {
        final StocksService impl = new StocksServiceImpl();
        impl.recordStockTrade("NOT_LISTED_STOCK", StockOperation.BUY, 20, 10, Calendar.getInstance().getTime());
    }

    @Test(expected = StockNotFoundException.class)
    public void add_null_stock() {
        final StocksService impl = new StocksServiceImpl();
        impl.recordStockTrade(null, StockOperation.BUY, 20, 10, Calendar.getInstance().getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_invalid_stock_quantity() {
        final StocksService impl = new StocksServiceImpl();
        impl.recordStockTrade("TEA", StockOperation.BUY, -10, 20, Calendar.getInstance().getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_null_operation() {
        final StocksService impl = new StocksServiceImpl();
        impl.recordStockTrade("TEA", null, 10, 20, Calendar.getInstance().getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void calculateDividend_invalid_currentPrice() {
        final StocksService impl = new StocksServiceImpl();
        impl.calculateDividend("GIN", 0d);
    }

    @Test(expected = StockNotFoundException.class)
    public void calculateDividend_invalid_stock() {
        final StocksService impl = new StocksServiceImpl();
        impl.calculateDividend("JUNK_STOCK", 50.0d);
    }

    @Test(expected = StockNotFoundException.class)
    public void calculatePERatio_invalid_stock() {
        final StocksService impl = new StocksServiceImpl();
        impl.calculatePERatio("JUNK_STOCK", 50.0d);

    }

    @Test(expected = IllegalArgumentException.class)
    public void calculatePERatio_invalid_currentPrice() {
        final StocksService impl = new StocksServiceImpl();
        impl.calculatePERatio("GIN", 0d);
    }

    @Test(expected = StockNotFoundException.class)
    public void calculateVolume_null_stock() {
        final StocksService impl = new StocksServiceImpl();
        impl.calculateVolume(null);
    }

    @Test(expected = StockNotFoundException.class)
    public void calculateVolume_invalid_stock() {
        final StocksService impl = new StocksServiceImpl();
        impl.calculateVolume("JUNK_STOCK");
    }

    @Test
    public void test_calculateDividend() {
        final StocksService impl = new StocksServiceImpl();
        assertTrue(0d == impl.calculateDividend("TEA", 15d));
        assertTrue(8.0d == impl.calculateDividend("GIN", 25d));
        assertTrue(1d == impl.calculateDividend("ALE", 23d));
        assertTrue(2.0d == impl.calculateDividend("POP", 4d));
        assertTrue(0.5d == impl.calculateDividend("JOE", 26d));
    }

    @Test
    public void test_calculatePERatio() {
        final StocksService impl = new StocksServiceImpl();
        assertTrue(0d == impl.calculatePERatio("TEA", 15d));
        assertTrue(0.32d == impl.calculatePERatio("GIN", 25d));
        assertTrue(1 / 23d == impl.calculatePERatio("ALE", 23d));
        assertTrue(0.5d == impl.calculatePERatio("POP", 4d));
        assertTrue(1 / 52d == impl.calculatePERatio("JOE", 26d));
    }

    @Test
    public void test_GBCE_volume() {
        final StocksService impl = new StocksServiceImpl();

        impl.recordStockTrade("TEA", StockOperation.BUY, 20, 10, Calendar.getInstance().getTime());
        impl.recordStockTrade("TEA", StockOperation.BUY, 30, 11, Calendar.getInstance().getTime());
        impl.recordStockTrade("GIN", StockOperation.SELL, 40, 12, Calendar.getInstance().getTime());
        impl.recordStockTrade("ALE", StockOperation.BUY, 40, 12, Calendar.getInstance().getTime());
        impl.recordStockTrade("TEA", StockOperation.SELL, 12, 10, Calendar.getInstance().getTime());
        impl.recordStockTrade("ALE", StockOperation.BUY, 50, 20, Calendar.getInstance().getTime());
        impl.recordStockTrade("POP", StockOperation.SELL, 100, 15, Calendar.getInstance().getTime());

        assertTrue(650d == impl.calculateVolume("TEA"));
        assertTrue(480d == impl.calculateVolume("GIN"));
        assertTrue(1480d == impl.calculateVolume("ALE"));
        assertTrue(1500d == impl.calculateVolume("POP"));
        assertTrue(480d == impl.calculateVolume("GIN"));

        assertTrue(912.2773495221646d == impl.calculateGBCE());

        // add another old record

        final Date date = new Date();
        final long now = date.getTime();
        final long twentyMinsAgo = now - (20 * 60 * 1000);
        final Date twentyMinsAgoDate = new Date(twentyMinsAgo);

        impl.recordStockTrade("TEA", StockOperation.BUY, 12, 10, twentyMinsAgoDate);
        impl.recordStockTrade("ALE", StockOperation.BUY, 100, 20, twentyMinsAgoDate);
        impl.recordStockTrade("POP", StockOperation.SELL, 150, 15, twentyMinsAgoDate);
        impl.recordStockTrade("TEA", StockOperation.SELL, 40, 25, twentyMinsAgoDate);

        // volume should be intact as 20 mins old records are
        assertTrue(650d == impl.calculateVolume("TEA"));
        assertTrue(480d == impl.calculateVolume("GIN"));
        assertTrue(1480d == impl.calculateVolume("ALE"));
        assertTrue(1500d == impl.calculateVolume("POP"));
        assertTrue(480d == impl.calculateVolume("GIN"));

        // however GBCE all share price index should get updated now
        assertTrue(1824.7621071997667d == impl.calculateGBCE());

    }
}
