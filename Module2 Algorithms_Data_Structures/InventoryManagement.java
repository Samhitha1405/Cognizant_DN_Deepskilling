import java.util.HashMap;
import java.util.Map;

class Product {
    int productId; String productName; int quantity; double price;
    Product(int id, String name, int qty, double price) {
        productId = id; productName = name; quantity = qty; this.price = price;
    }
    public String toString() {
        return String.format("ID: %-4d | Name: %-12s | Qty: %-4d | Price: $%.2f",
                productId, productName, quantity, price);
    }
}

class Inventory {
    private Map<Integer, Product> products = new HashMap<>(); // O(1) avg access

    void addProduct(Product p) { products.put(p.productId, p); System.out.println("Added   -> " + p); }

    void updateProduct(int id, int qty, double price) {
        Product p = products.get(id);
        if (p != null) { p.quantity = qty; p.price = price; System.out.println("Updated -> " + p); }
        else System.out.println("Update failed: Product ID " + id + " not found");
    }

    void deleteProduct(int id) {
        Product removed = products.remove(id);
        System.out.println(removed != null ? "Deleted -> " + removed : "Delete failed: Product ID " + id + " not found");
    }

    void displayAll() {
        System.out.println("\n--- Current Inventory (" + products.size() + " items) ---");
        for (Product p : products.values()) System.out.println(p);
    }
}

public class InventoryManagement {
    public static void main(String[] args) {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(101, "Laptop", 25, 750.00));
        inv.addProduct(new Product(102, "Mouse", 150, 15.50));
        inv.addProduct(new Product(103, "Keyboard", 80, 35.00));
        inv.displayAll();
        inv.updateProduct(102, 200, 14.99);
        inv.deleteProduct(103);
        inv.displayAll();
        inv.updateProduct(999, 10, 10.0);
        inv.deleteProduct(999);
    }
}