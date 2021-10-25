import java.util.LinkedList;
import java.util.Queue;

public class Trader implements Comparable<Trader> {
    private final Brokerage brokerage;
    private final String name;
    private final String pswd;
    private TraderWindow myWindow;
    private Queue<String> messages;

    public Trader(Brokerage brokerage, String name, String pswd) {
        this.brokerage = brokerage;
        this.name = name;
        this.pswd = pswd;
        messages = new LinkedList<>();
    }

    @Override
    public int compareTo(Trader o) {
        return name.toLowerCase().compareTo(o.getName().toLowerCase());
    }

    public boolean equals(Object other) {
        if (!(other instanceof Trader))
            throw new ClassCastException();
        return name.equalsIgnoreCase(((Trader) other).getName());
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return pswd;
    }

    public void getQuote(String symbol) {
        brokerage.getQuote(symbol, this);
    }

    public boolean hasMessages() {
        return !messages.isEmpty();
    }

    public void openWindow() {
        myWindow = new TraderWindow(this);
        while(!messages.isEmpty()) {
            myWindow.showMessage(messages.poll());
        }
    }

    public void placeOrder(TradeOrder order) {
        brokerage.placeOrder(order);
    }

    public void receiveMessage(String msg) {
        messages.add(msg);
        if(myWindow != null) {
            while(!messages.isEmpty()) {
                myWindow.showMessage(messages.poll());
            }
        }
    }

    public void quit() {
        brokerage.logout(this);
        myWindow = null;
    }
}
