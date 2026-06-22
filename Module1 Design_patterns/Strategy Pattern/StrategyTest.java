interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    private final String cardNumber;
    public CreditCardPayment(String cardNumber) { this.cardNumber = cardNumber; }
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Credit Card ending in "
                           + cardNumber.substring(cardNumber.length() - 4));
    }
}

class PayPalPayment implements PaymentStrategy {
    private final String email;
    public PayPalPayment(String email) { this.email = email; }
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " via PayPal: " + email);
    }
}

class UPIPayment implements PaymentStrategy {
    private final String upiId;
    public UPIPayment(String upiId) { this.upiId = upiId; }
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " via UPI: " + upiId);
    }
}

class PaymentContext {
    private PaymentStrategy strategy;
    public void setStrategy(PaymentStrategy s) { this.strategy = s; }
    public void executePayment(double amount)   { strategy.pay(amount); }
}

public class StrategyTest {
    public static void main(String[] args) {
        PaymentContext ctx = new PaymentContext();

        ctx.setStrategy(new CreditCardPayment("1234-5678-9012-3456"));
        ctx.executePayment(150.00);

        ctx.setStrategy(new PayPalPayment("user@example.com"));
        ctx.executePayment(75.50);

        ctx.setStrategy(new UPIPayment("user@upi"));
        ctx.executePayment(200.00);
    }
}