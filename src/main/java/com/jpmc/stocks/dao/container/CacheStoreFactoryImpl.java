package com.jpmc.stocks.dao.container;

import com.jpmc.stocks.dao.CacheStore;
import com.jpmc.stocks.dao.CacheStoreFactory;

public class CacheStoreFactoryImpl implements CacheStoreFactory {

    @Override
    public CacheStore getCacheStore() {
        return ContainerBasedCacheStore.INSTANCE;
    }
}
