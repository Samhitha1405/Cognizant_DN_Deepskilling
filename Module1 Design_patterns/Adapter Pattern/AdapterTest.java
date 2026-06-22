interface PaymentProcessor {
    void processPayment(double amount);
}

class PayPalGateway {
    public void makePayment(double amount) {
        System.out.println("PayPal: Processing payment of $" + amount);
    }
}

class StripeGateway {
    public void chargeCard(double amountInCents) {
        System.out.println("Stripe: Charging " + amountInCents + " cents");
    }
}

class RazorpayGateway {
    public void initiateTransaction(String currency, double amount) {
        System.out.println("Razorpay: Initiating " + currency + " transaction of " + amount);
    }
}

class PayPalAdapter implements PaymentProcessor {
    private final PayPalGateway paypal;
    public PayPalAdapter(PayPalGateway p) { this.paypal = p; }
    public void processPayment(double amount) { paypal.makePayment(amount); }
}

class StripeAdapter implements PaymentProcessor {
    private final StripeGateway stripe;
    public StripeAdapter(StripeGateway s) { this.stripe = s; }
    public void processPayment(double amount) { stripe.chargeCard(amount * 100); }
}

class RazorpayAdapter implements PaymentProcessor {
    private final RazorpayGateway razorpay;
    public RazorpayAdapter(RazorpayGateway r) { this.razorpay = r; }
    public void processPayment(double amount) { razorpay.initiateTransaction("INR", amount * 83); }
}

public class AdapterTest {
    public static void main(String[] args) {
        PaymentProcessor[] processors = {
            new PayPalAdapter(new PayPalGateway()),
            new StripeAdapter(new StripeGateway()),
            new RazorpayAdapter(new RazorpayGateway())
        };

        for (PaymentProcessor p : processors) {
            p.processPayment(100.00);
        }
    }
}