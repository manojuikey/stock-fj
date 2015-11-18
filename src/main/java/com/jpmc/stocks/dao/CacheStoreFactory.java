package com.jpmc.stocks.dao;

import com.jpmc.stocks.dao.container.CacheStoreFactoryImpl;

public interface CacheStoreFactory {

    public static final CacheStoreFactory INSTANCE = new CacheStoreFactoryImpl();

    public CacheStore getCacheStore();
}
