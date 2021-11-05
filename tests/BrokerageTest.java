import junit.framework.TestCase;

public class BrokerageTest extends TestCase {
    public void testBrokerageAndTrader() {
        StockExchange exchange = new StockExchange();
        Brokerage brokerage = new Brokerage(exchange);

        assert brokerage.addUser("trader1", "insecurepassword") == -2;
        assert brokerage.addUser("trader2", "securepassword") == -2;
        assert brokerage.addUser("trader3", "") == -2;
        assert brokerage.addUser("averylongname", "testing") == -1;
        assert brokerage.addUser("aaa", "testing") == -1;
        assert brokerage.addUser("trader2", "securepassword") == -2;
        assert brokerage.addUser("trader1", "password") == 0;
        assert brokerage.addUser("trader2", "pass") == 0;
        assert brokerage.addUser("trader3", "word") == 0;
        assert brokerage.addUser("trader2", "word") == -3;

        assert brokerage.login("trader", "") == -1;
        assert brokerage.login("trader1", "") == -2;
        assert brokerage.login("trader1", "password") == 0;
        assert brokerage.login("trader2", "pass") == 0;
        assert brokerage.login("trader3", "word") == 0;
        assert brokerage.login("trader1", "password") == -3;
    }
}