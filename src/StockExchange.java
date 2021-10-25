import java.util.HashMap;

public class StockExchange {
    private HashMap<String, Stock> stocks;

    public StockExchange() {
        stocks = new HashMap<>();
    }

    public void listStock(String symbol, String name, double price) {
        stocks.put(symbol, new Stock(symbol, name, price));
    }

    public String getQuote(String symbol) {
        if (stocks.containsKey(symbol)) {
            return stocks.get(symbol).getQuote();
        }
        return "";
    }

    public void placeOrder(TradeOrder order) {
        if (stocks.containsKey(order.getSymbol()))
            stocks.get(order.getSymbol()).placeOrder(order);
        // How should errors be handled when the stock symbol is not found?
    }
}
