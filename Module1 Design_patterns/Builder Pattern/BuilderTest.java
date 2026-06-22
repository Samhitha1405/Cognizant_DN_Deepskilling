public class BuilderTest {
    public static void main(String[] args) {
        Computer basicPC = new Computer.Builder("Intel i3", "8GB")
                .storage("512GB HDD")
                .wifi(true)
                .build();

        Computer gamingPC = new Computer.Builder("Intel i9", "32GB")
                .storage("2TB NVMe SSD")
                .gpu("NVIDIA RTX 4090")
                .bluetooth(true)
                .wifi(true)
                .build();

        System.out.println("Basic PC:\n" + basicPC);
        System.out.println("\nGaming PC:\n" + gamingPC);
    }
}