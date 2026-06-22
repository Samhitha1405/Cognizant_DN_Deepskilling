import java.util.Arrays;

class Order {
    int orderId;
    String customerName;
    double totalPrice;

    public Order(int orderId, String customerName, double totalPrice) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return String.format("Order #%-3d | %-8s | $%.2f", orderId, customerName, totalPrice);
    }
}

public class SortDemo {

    // O(n^2) - simple, but slow on large lists
    static void bubbleSort(Order[] orders) {
        int n = orders.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (orders[j].totalPrice > orders[j + 1].totalPrice) {
                    Order temp = orders[j];
                    orders[j] = orders[j + 1];
                    orders[j + 1] = temp;
                }
            }
        }
    }

    // O(n log n) average - divide and conquer
    static void quickSort(Order[] orders, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(orders, low, high);
            quickSort(orders, low, pivotIndex - 1);
            quickSort(orders, pivotIndex + 1, high);
        }
    }

    static int partition(Order[] orders, int low, int high) {
        double pivot = orders[high].totalPrice;
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (orders[j].totalPrice <= pivot) {
                i++;
                Order temp = orders[i];
                orders[i] = orders[j];
                orders[j] = temp;
            }
        }
        Order temp = orders[i + 1];
        orders[i + 1] = orders[high];
        orders[high] = temp;
        return i + 1;
    }

    static void print(Order[] orders) {
        for (Order o : orders) System.out.println("  " + o);
    }

    public static void main(String[] args) {
        Order[] original = {
                new Order(1, "Alice", 250.75),
                new Order(2, "Bob", 89.99),
                new Order(3, "Carla", 430.00),
                new Order(4, "Dan", 12.50),
                new Order(5, "Eve", 199.25)
        };

        System.out.println("Original orders:");
        print(original);

        Order[] bubbleArr = original.clone();
        long start = System.nanoTime();
        bubbleSort(bubbleArr);
        long end = System.nanoTime();
        System.out.println("\nAfter Bubble Sort (by totalPrice ascending):");
        print(bubbleArr);
        System.out.println("Bubble Sort time: " + (end - start) + " ns");

        Order[] quickArr = original.clone();
        start = System.nanoTime();
        quickSort(quickArr, 0, quickArr.length - 1);
        end = System.nanoTime();
        System.out.println("\nAfter Quick Sort (by totalPrice ascending):");
        print(quickArr);
        System.out.println("Quick Sort time: " + (end - start) + " ns");
    }
}