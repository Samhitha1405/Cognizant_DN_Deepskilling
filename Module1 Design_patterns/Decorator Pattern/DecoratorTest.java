interface Notifier {
    void send(String message);
}


class EmailNotifier implements Notifier {
    public void send(String message) {
        System.out.println("Email notification sent: " + message);
    }
}

abstract class NotifierDecorator implements Notifier {
    protected final Notifier wrapped;
    public NotifierDecorator(Notifier n) { this.wrapped = n; }
    public void send(String message)     { wrapped.send(message); }
}

class SMSNotifierDecorator extends NotifierDecorator {
    public SMSNotifierDecorator(Notifier n) { super(n); }
    public void send(String message) {
        super.send(message);
        System.out.println("SMS notification sent: " + message);
    }
}

class SlackNotifierDecorator extends NotifierDecorator {
    public SlackNotifierDecorator(Notifier n) { super(n); }
    public void send(String message) {
        super.send(message);
        System.out.println("Slack notification sent: " + message);
    }
}

public class DecoratorTest {
    public static void main(String[] args) {
        System.out.println("--- Email only ---");
        new EmailNotifier().send("Server is down!");

        System.out.println("\n--- Email + SMS ---");
        new SMSNotifierDecorator(new EmailNotifier()).send("Disk usage at 90%!");

        System.out.println("\n--- Email + SMS + Slack ---");
        new SlackNotifierDecorator(
            new SMSNotifierDecorator(
                new EmailNotifier())).send("Deployment successful!");
    }
}