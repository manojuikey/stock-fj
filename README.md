# Stock Service

JPMC Stock Service is a thread safe application that can be used to performs various stock operations on millions of stock trade recordss stored in memory containers in paraller.
It  uses Fork join pool to distribute the jobs to each containers, in each container the trade records are processed and result is returnd to a the processor and its merged and returned.

![Alt text](images/arch.jpg?raw=true "Architecture")


----

## com.jpmc.stocks.api.StocksService
Interface defines operations on stocks
  - calculateDividend(stock, currentPrice)
  -- Calculate the dividend for the given stock and current price.
  - calculatePERatio(stock, currentPrice)
   -- Calculate profit to earning ratio for the given stock and current price.
  - calculateVolume(stock)
  -- Calculate the trade volume of the stock in the last 15 minutes.
  - calculateGBCE()
   -- Calculates the GBCE All Share Index using the geometric mean of prices for all stocks
  - recordStockTrade(operation, stock, quantity, price, time)
   -- Record stock trade operation.

----

## com.jpmc.stocks.api.StocksOperation
Enum that denotes stock operation type, values are-
- BUY
- SELL

----

## com.jpmc.stocks.api.StockNotFoundException
Runtime exception thrown when invalid stock is queried.

---

# Data Access Layer
mTrade records can increase over time, the stock service app stores the trade records in the in-memory containers in a round robin fashion, so when recordTrade method is called to store the trade records data is evenly distributed in all the available containers.

## com.jpmc.stocks.dao.CacheStoreFactory
Factory class to get the cache store instance.

## com.jpmc.stocks.dao.CacheStore
Cache store interface powered by in-memory storage.

## com.jpmc.stocks.dao.container.ContainerBasedCacheStore
Cache store implementation powered by in-memory containers used to store data in round robin fashion.
 
---

# Fork Join Processor
Fork join processor operates on each containers, it devides the trade records processing jobs to each containers and aggregates the result fetched from each.

## com.jpmc.stocks.dao.container.ForkJoinProcessor
The Trade record search processor engine which finds the trade record using ForkJoinPool utility to search the trade record in all cache containers in parallel.

   - calculateVolume(stock, timeFrom)
    -- Calculate the volume of a given stock by finding all of it's trade records from the given time and computing the mean price.


## How To test/build

$ mvn clean install

## How To generate javadoc, PMD, code coverage etc

$ mvn clean site
