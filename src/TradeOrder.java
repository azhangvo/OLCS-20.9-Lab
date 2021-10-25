public class TradeOrder {
    private final Trader trader;
    private final String symbol;
    private final boolean buyOrder;
    private final boolean marketOrder;
    private int numShares;
    private final double price;

    public TradeOrder(Trader trader, String symbol, boolean buyOrder, boolean marketOrder, int numShares, double price) {
        this.trader = trader;
        this.symbol = symbol;
        this.buyOrder = buyOrder;
        this.marketOrder = marketOrder;
        this.numShares = numShares;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public int getShares() {
        return numShares;
    }

    public String getSymbol() {
        return symbol;
    }

    public Trader getTrader() {
        return trader;
    }

    public boolean isBuy() {
        return buyOrder;
    }

    public boolean isLimit() {
        return !marketOrder;
    }

    public boolean isMarket() {
        return marketOrder;
    }

    public boolean isSell() {
        return !buyOrder;
    }

    public void subtractShares(int shares) {
        if (shares > numShares) {
            throw new IllegalArgumentException();
        }
        numShares -= shares;
    }

    @Override
    public String toString() {
        return "TradeOrder{" +
                "trader=" + trader +
                ", symbol='" + symbol + '\'' +
                ", buyOrder=" + buyOrder +
                ", marketOrder=" + marketOrder +
                ", numShares=" + numShares +
                ", price=" + price +
                '}';
    }
}
