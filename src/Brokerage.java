import java.util.TreeMap;
import java.util.TreeSet;

public class Brokerage implements Login {
    private final TreeMap<String, Trader> registeredTraders;
    private final TreeSet<Trader> activeTraders;
    private final StockExchange exchange;

    public Brokerage(StockExchange exchange) {
        registeredTraders = new TreeMap<>();
        activeTraders = new TreeSet<>();
        this.exchange = exchange;
    }

    @Override
    public int addUser(String name, String password) {
        if (name.length() > 10 || name.length() < 4)
            return -1;
        if (password.length() > 10 || password.length() < 2)
            return -2;
        if (registeredTraders.containsKey(name))
            return -3;
        registeredTraders.put(name, new Trader(this, name, password));
        return 0;
    }

    @Override
    public int login(String name, String password) {
        if (!registeredTraders.containsKey(name))
            return -1;
        if (!registeredTraders.get(name).getPassword().equals(password))
            return -2;
        Trader trader = registeredTraders.get(name);
        if (activeTraders.contains(trader))
            return -3;
        activeTraders.add(trader);
        if (!trader.hasMessages()) {
            trader.receiveMessage("Welcome to SafeTrade!");
        }
        trader.openWindow();
        return 0;
    }

    public void logout(Trader trader) {
        activeTraders.remove(trader);
    }

    public void getQuote(String symbol, Trader trader) {
        trader.receiveMessage(exchange.getQuote(symbol));
    }

    public void placeOrder(TradeOrder order) {
        exchange.placeOrder(order);
    }
}
