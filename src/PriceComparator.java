import java.util.Comparator;

public class PriceComparator implements Comparator<TradeOrder> {
    private final boolean asc;

    public PriceComparator() {
        asc = true;
    }

    public PriceComparator(boolean asc) {
        this.asc = asc;
    }

    @Override
    public int compare(TradeOrder order1, TradeOrder order2) {
        if (order1.isMarket() && order2.isMarket())
            return 0;
        if (order1.isMarket() && order2.isLimit())
            return -1;
        if (order1.isLimit() && order2.isMarket())
            return 1;
        int cents1 = (int) (order1.getPrice() * 100);
        int cents2 = (int) (order2.getPrice() * 100);
        return asc ? cents1 - cents2 : cents2 - cents1;
    }
}
