package com.jpmc.stocks.entity;

public final class Stock {

    private final String symbol;
    private final StockType stockType;
    private double lastDividend;
    private double fixedDividend;
    private double parValue;

    public Stock(final String symbol, final StockType stockType) {
        this.symbol = symbol;
        this.stockType = stockType;
    }

    /**
     * @return the lastDividend
     */
    public double getLastDividend() {
        return lastDividend;
    }

    /**
     * @param lastDividend
     *            the lastDividend to set
     */
    public void setLastDividend(final int lastDividend) {
        this.lastDividend = lastDividend;
    }

    /**
     * @return the fixedDividend
     */
    public double getFixedDividend() {
        return fixedDividend;
    }

    /**
     * @param fixedDividend
     *            the fixedDividend to set
     */
    public void setFixedDividend(final float fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    /**
     * @return the parValue
     */
    public double getParValue() {
        return parValue;
    }

    /**
     * @param parValue
     *            the parValue to set
     */
    public void setParValue(final int parValue) {
        this.parValue = parValue;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @return the stockType
     */
    public StockType getStockType() {
        return stockType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((stockType == null) ? 0 : stockType.hashCode());
        result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Stock other = (Stock) obj;
        if (stockType != other.stockType) {
            return false;
        }
        if (symbol == null) {
            if (other.symbol != null) {
                return false;
            }
        } else if (!symbol.equals(other.symbol)) {
            return false;
        }
        return true;
    }

}
