import junit.framework.TestCase;

public class StockTest extends TestCase {

    private class TraderStub extends Trader {
        private String name;

        private TraderStub(String name) {
            super(null, name, "");
            this.name = name;
        }

        @Override
        public void receiveMessage(String msg) {
//            System.out.printf("%s: %s%n", name, msg);
        }
    }

    public void testStock() {
        Stock stock1 = new Stock("ASTOCK", "Just Another Stock Company", 5.61);
        Stock stock2 = new Stock("", "", 0.00); // Empty stock edge case
        Stock stock3 = new Stock("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "A Very Long Company Name That Is Most Likely Unnecessary", 1423.12415); // Long stock and high price edge case
        Stock stock4 = new Stock("BSTOCK", "Order Variation Company", 12.15); // Different hi and lo prices and orders

        Trader trader1 = new TraderStub("Trader1");
        Trader trader2 = new TraderStub("Trader2");
        stock4.placeOrder(new TradeOrder(trader1, "BSTOCK", true, false, 10, 13.00));
        stock4.placeOrder(new TradeOrder(trader1, "BSTOCK", true, false, 10, 14.00));
        stock4.placeOrder(new TradeOrder(trader1, "BSTOCK", true, false, 10, 15.00));
        stock4.placeOrder(new TradeOrder(trader2, "BSTOCK", false, false, 10, 14.50));
        stock4.placeOrder(new TradeOrder(trader2, "BSTOCK", false, false, 10, 14.00));
        stock4.placeOrder(new TradeOrder(trader2, "BSTOCK", false, false, 10, 14.00));

        String stock1_quote = "Just Another Stock Company (ASTOCK)\nPrice: 5.61  hi: 5.61  lo: 5.61  vol: 0\nAsk: none  Bid: none";
        String stock2_quote = " ()\nPrice: 0.00  hi: 0.00  lo: 0.00  vol: 0\nAsk: none  Bid: none";
        String stock3_quote = "A Very Long Company Name That Is Most Likely Unnecessary (ABCDEFGHIJKLMNOPQRSTUVWXYZ)\nPrice: 1423.12  hi: 1423.12  lo: 1423.12  vol: 0\nAsk: none  Bid: none";
        String stock4_quote1 = "Order Variation Company (BSTOCK)\nPrice: 14.00  hi: 14.50  lo: 12.15  vol: 20\nAsk: 14.00 size: 10  Bid: 13.00 size: 10";

        assert stock1.getQuote().equals(stock1_quote);
        assert stock2.getQuote().equals(stock2_quote);
        assert stock3.getQuote().equals(stock3_quote);
        assert stock4.getQuote().equals(stock4_quote1);

        stock4.placeOrder(new TradeOrder(trader1, "BSTOCK", true, true, 5, 0.00));
        String stock4_quote2 = "Order Variation Company (BSTOCK)\nPrice: 14.00  hi: 14.50  lo: 12.15  vol: 25\nAsk: 14.00 size: 5  Bid: 13.00 size: 10";

        assert stock4.getQuote().equals(stock4_quote2);

        stock4.placeOrder(new TradeOrder(trader1, "BSTOCK", true, true, 5, 0.00));
        String stock4_quote3 = "Order Variation Company (BSTOCK)\nPrice: 14.00  hi: 14.50  lo: 12.15  vol: 30\nAsk: none  Bid: 13.00 size: 10";

        assert stock4.getQuote().equals(stock4_quote3);

        stock4.placeOrder(new TradeOrder(trader1, "BSTOCK", true, false, 10, 11.25));
        stock4.placeOrder(new TradeOrder(trader2, "BSTOCK", false, true, 5, 0.00));
        String stock4_quote4 = "Order Variation Company (BSTOCK)\nPrice: 13.00  hi: 14.50  lo: 12.15  vol: 35\nAsk: none  Bid: 13.00 size: 5";

        assert stock4.getQuote().equals(stock4_quote4);

        stock4.placeOrder(new TradeOrder(trader2, "BSTOCK", false, true, 20, 0.00));
        String stock4_quote5 = "Order Variation Company (BSTOCK)\nPrice: 11.25  hi: 14.50  lo: 11.25  vol: 50\nAsk: market size: 5  Bid: none";

        assert stock4.getQuote().equals(stock4_quote5);
    }
}