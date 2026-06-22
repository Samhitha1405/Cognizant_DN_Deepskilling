import java.util.Arrays;
import java.util.Comparator;

class Product {
    int productId; String productName, category;
    Product(int id, String name, String cat) { productId=id; productName=name; category=cat; }
    public String toString() { return "ID:" + productId + " " + productName + " (" + category + ")"; }
}

public class SearchDemo {
    static Product linearSearch(Product[] arr, String name) {           // O(n)
        for (Product p : arr) if (p.productName.equalsIgnoreCase(name)) return p;
        return null;
    }

    static Product binarySearch(Product[] sorted, String name) {        // O(log n), needs sorted input
        int low = 0, high = sorted.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = sorted[mid].productName.compareToIgnoreCase(name);
            if (cmp == 0) return sorted[mid];
            else if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }
        return null;
    }

    public static void main(String[] args) {
        Product[] catalog = {
            new Product(1,"Headphones","Electronics"), new Product(2,"Blender","Kitchen"),
            new Product(3,"Sneakers","Footwear"), new Product(4,"Backpack","Accessories"),
            new Product(5,"Monitor","Electronics")
        };
        System.out.println(linearSearch(catalog, "Sneakers"));

        Product[] sorted = catalog.clone();
        Arrays.sort(sorted, Comparator.comparing(p -> p.productName.toLowerCase()));
        System.out.println(binarySearch(sorted, "Sneakers"));
    }
}