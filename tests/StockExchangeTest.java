import junit.framework.TestCase;

public class StockExchangeTest extends TestCase {

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

    public void testListStock() {
        StockExchange exchange = new StockExchange();

        exchange.listStock("APPL", "APPLE", 1.23);
        exchange.listStock("BNNA", "BANANA", 4.56);
        exchange.listStock("CNBY", "CRANBERRY", 7.89);
    }

    public void testGetQuote() {
        StockExchange exchange = new StockExchange();

        exchange.listStock("APPL", "APPLE", 1.23);
        exchange.listStock("BNNA", "BANANA", 4.56);
        exchange.listStock("CNBY", "CRANBERRY", 7.89);

        String appl_stock = "APPLE (APPL)\nPrice: 1.23  hi: 1.23  lo: 1.23  vol: 0\nAsk: none  Bid: none";
        String bnna_stock = "BANANA (BNNA)\nPrice: 4.56  hi: 4.56  lo: 4.56  vol: 0\nAsk: none  Bid: none";
        String cnby_stock = "CRANBERRY (CNBY)\nPrice: 7.89  hi: 7.89  lo: 7.89  vol: 0\nAsk: none  Bid: none";

        assert exchange.getQuote("APPL").equals(appl_stock);
        assert exchange.getQuote("BNNA").equals(bnna_stock);
        assert exchange.getQuote("CNBY").equals(cnby_stock);
        assert exchange.getQuote("").equals("");
        assert exchange.getQuote("FOO").equals("");
    }

    public void testPlaceOrder() {
        StockExchange exchange = new StockExchange();
        Trader trader = new TraderStub("trader");

        exchange.listStock("APPL", "APPLE", 1.23);
        exchange.listStock("BNNA", "BANANA", 4.56);
        exchange.listStock("CNBY", "CRANBERRY", 7.89);

        exchange.placeOrder(new TradeOrder(trader, "APPL", true, false, 10, 2.00));
        exchange.placeOrder(new TradeOrder(trader, "BNNA", false, false, 10, 4.241));
        exchange.placeOrder(new TradeOrder(trader, "CNBY", true, true, 50, 1.31));
    }
}