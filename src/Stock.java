import java.text.DecimalFormat;
import java.util.PriorityQueue;

public class Stock {
    private PriorityQueue<TradeOrder> buyOrders;
    private final String companyName;
    private double hiPrice;
    private double lastPrice;
    private double loPrice;
    static DecimalFormat money = new DecimalFormat("0.00");
    private PriorityQueue<TradeOrder> sellOrders;
    private final String stockSymbol;
    private int volume;

    public Stock(String symbol, String name, double price) {
        this.stockSymbol = symbol;
        this.companyName = name;
        this.loPrice = price;
        this.lastPrice = price;
        this.hiPrice = price;
        volume = 0;
        sellOrders = new PriorityQueue<>(new PriceComparator(true));
        buyOrders = new PriorityQueue<>(new PriceComparator(false));
    }

    protected void executeOrders() {
        while (!sellOrders.isEmpty() && !buyOrders.isEmpty()) {
            if(buyOrders.peek().getShares() == 0) { // Sanity Check
                buyOrders.poll();
                continue;
            }
            assert sellOrders.peek() != null; // Compiler required
            if(sellOrders.peek().getShares() == 0) { // Sanity Check
                sellOrders.poll();
                continue;
            }

            double exchangePrice;
            TradeOrder sell = sellOrders.peek();
            TradeOrder buy = buyOrders.peek();
            if(sell == null || buy == null)
                continue;
            if (sell.isLimit() && buy.isLimit()) {
                if (sell.getPrice() > buy.getPrice())
                    break;
                exchangePrice = sell.getPrice();
            } else if (sell.isLimit() && buy.isMarket()) {
                exchangePrice = sell.getPrice();
            } else if (sell.isMarket() && buy.isLimit()) {
                exchangePrice = buy.getPrice();
            } else {
                exchangePrice = lastPrice;
            }

            int orders = Math.min(sell.getShares(), buy.getShares());

            buy.getTrader().receiveMessage(String.format("You bought: %d %s at %s amt %s", orders, stockSymbol, money.format(exchangePrice), money.format(exchangePrice * orders)));
            sell.getTrader().receiveMessage(String.format("You sold: %d %s at %s amt %s", orders, stockSymbol, money.format(exchangePrice), money.format(exchangePrice * orders)));

            buy.subtractShares(orders);
            sell.subtractShares(orders);

            if(buy.getShares() == 0)
                buyOrders.poll();
            if(sell.getShares() == 0)
                sellOrders.poll();

            volume += orders;
            lastPrice = exchangePrice;
            loPrice = Math.min(loPrice, lastPrice);
            hiPrice = Math.max(hiPrice, lastPrice);
        }
    }

    public String getQuote() {
        String statistics = String.format("%s (%s)\nPrice: %s  hi: %s  lo: %s  vol: %d", companyName, stockSymbol, money.format(lastPrice), money.format(hiPrice), money.format(loPrice), volume);
        TradeOrder sell = sellOrders.peek();
        TradeOrder buy = buyOrders.peek();
        String sellString = "Ask: none";
        String buyString = "Bid: none";
        if (sell != null)
            sellString = String.format("Ask: %s size: %d", money.format(sell.getPrice()), sell.getShares());
        if (buy != null)
            buyString = String.format("Bid: %s size: %d", money.format(buy.getPrice()), buy.getShares());
        String market = String.format("%s  %s", sellString, buyString);
        return String.format("%s\n%s", statistics, market);
    }

    public void placeOrder(TradeOrder order) {
        String msg;
        if (order.isBuy()) {
            buyOrders.add(order);
            if (order.isMarket()) {
                msg = String.format("New order:  Buy %s (%s)\n%d shares at market", stockSymbol, companyName, order.getShares());
            } else {
                msg = String.format("New order:  Buy %s (%s)\n%d shares at $%s", stockSymbol, companyName, order.getShares(), money.format(order.getPrice()));
            }
        } else {
            sellOrders.add(order);
            if (order.isMarket()) {
                msg = String.format("New order:  Sell %s (%s)\n%d shares at market", stockSymbol, companyName, order.getShares());
            } else {
                msg = String.format("New order:  Sell %s (%s)\n%d shares at $%s", stockSymbol, companyName, order.getShares(), money.format(order.getPrice()));
            }
        }
        order.getTrader().receiveMessage(msg);
        executeOrders();
    }
}
