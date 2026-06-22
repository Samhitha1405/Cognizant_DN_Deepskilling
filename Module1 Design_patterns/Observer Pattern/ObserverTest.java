import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update(String stockName, double price);
}

interface Stock {
    void register(Observer o);
    void deregister(Observer o);
    void notifyObservers();
}

class StockMarket implements Stock {
    private final List<Observer> observers = new ArrayList<>();
    private String stockName;
    private double price;

    public void setPrice(String stockName, double price) {
        System.out.println("\nStock update: " + stockName + " = $" + price);
        this.stockName = stockName;
        this.price     = price;
        notifyObservers();
    }

    public void register(Observer o)   { observers.add(o); }
    public void deregister(Observer o) { observers.remove(o); }
    public void notifyObservers() {
        for (Observer o : observers) o.update(stockName, price);
    }
}

class MobileApp implements Observer {
    private final String userId;
    public MobileApp(String userId) { this.userId = userId; }
    public void update(String s, double p) {
        System.out.println("  [MobileApp:" + userId + "] " + s + " = $" + p);
    }
}

class WebApp implements Observer {
    public void update(String s, double p) {
        System.out.println("  [WebApp] Dashboard updated: " + s + " = $" + p);
    }
}

public class ObserverTest {
    public static void main(String[] args) {
        StockMarket market = new StockMarket();

        Observer alice = new MobileApp("Alice");
        Observer bob   = new MobileApp("Bob");
        Observer web   = new WebApp();

        market.register(alice);
        market.register(bob);
        market.register(web);

        market.setPrice("AAPL", 189.50);

        System.out.println("\nBob unsubscribes...");
        market.deregister(bob);
        market.setPrice("AAPL", 192.00);
    }
}