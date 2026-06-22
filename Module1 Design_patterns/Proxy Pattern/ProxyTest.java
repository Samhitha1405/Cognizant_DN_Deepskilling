interface Image {
    void display();
}

class RealImage implements Image {
    private final String filename;
    public RealImage(String filename) {
        this.filename = filename;
        System.out.println("Loading image from remote server: " + filename);
    }
    public void display() { System.out.println("Displaying: " + filename); }
}

class ProxyImage implements Image {
    private final String filename;
    private RealImage realImage;

    public ProxyImage(String filename) { this.filename = filename; }

    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename); 
        }
        realImage.display(); 
    }
}


public class ProxyTest {
    public static void main(String[] args) {
        Image image = new ProxyImage("photo.jpg");

        System.out.println("First call:");
        image.display();

        System.out.println("\nSecond call (no reload):");
        image.display();
    }
}