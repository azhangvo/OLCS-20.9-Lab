import junit.framework.TestCase;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.logging.ConsoleHandler;

public class PriceComparatorTest extends TestCase {

    private class TraderStub extends Trader {
        private String name;

        private TraderStub(String name) {
            super(null, name, "");
            this.name = name;
        }

        @Override
        public void receiveMessage(String msg) {
            System.out.printf("%s: %s%n", name, msg);
        }
    }

    public void testCompare() {
        Trader trader1 = new TraderStub("asdf");

        PriorityQueue<TradeOrder> buyOrders = new PriorityQueue<>(new PriceComparator(false));
        buyOrders.add(new TradeOrder(trader1, "ABC", true, false, 10, 4.21));
        buyOrders.add(new TradeOrder(trader1, "ABC", true, false, 10, 2.13));
        buyOrders.add(new TradeOrder(trader1, "ABC", true, false, 10, 20.31));
        buyOrders.add(new TradeOrder(trader1, "ABC", true, false, 10, -2.14)); // Normally, negative price would not need to be tested, however, TraderWindow has implementation flaws that allow this to occur.
        buyOrders.add(new TradeOrder(trader1, "ABC", true, true, 10, 0));
        buyOrders.add(new TradeOrder(trader1, "ABC", true, true, 10, 0)); // Market orders have a price of zero when entered

        assert buyOrders.size() == 6;
        assert Objects.requireNonNull(buyOrders.poll()).isMarket();
        assert Objects.requireNonNull(buyOrders.poll()).isMarket();
        assert Objects.requireNonNull(buyOrders.poll()).getPrice() == 20.31;
        assert Objects.requireNonNull(buyOrders.poll()).getPrice() == 4.21;
        assert Objects.requireNonNull(buyOrders.poll()).getPrice() == 2.13;
        assert Objects.requireNonNull(buyOrders.poll()).getPrice() == -2.14;

        PriorityQueue<TradeOrder> sellOrders = new PriorityQueue<>(new PriceComparator(true));
        sellOrders.add(new TradeOrder(trader1, "EFG", false, false, 10, 4.21));
        sellOrders.add(new TradeOrder(trader1, "EFG", false, false, 10, 2.13));
        sellOrders.add(new TradeOrder(trader1, "EFG", false, false, 10, 20.31));
        sellOrders.add(new TradeOrder(trader1, "EFG", false, false, 10, -2.14)); // Normally, negative price would not need to be tested, however, TraderWindow has implementation flaws that allow this to occur.
        sellOrders.add(new TradeOrder(trader1, "EFG", false, true, 10, 0));
        sellOrders.add(new TradeOrder(trader1, "EFG", false, true, 10, 0)); // Market orders have a price of zero when entered

        assert sellOrders.size() == 6;
        assert Objects.requireNonNull(sellOrders.poll()).isMarket();
        assert Objects.requireNonNull(sellOrders.poll()).isMarket();
        assert Objects.requireNonNull(sellOrders.poll()).getPrice() == -2.14;
        assert Objects.requireNonNull(sellOrders.poll()).getPrice() == 2.13;
        assert Objects.requireNonNull(sellOrders.poll()).getPrice() == 4.21;
        assert Objects.requireNonNull(sellOrders.poll()).getPrice() == 20.31;
    }
}